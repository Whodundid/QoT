package envision.game.world.worldEditor;

import static envision.engine.inputHandlers.Keyboard.*;

import java.io.File;
import java.util.Collection;

import envision.Envision;
import envision.engine.events.GameEvent;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.screens.GameScreen;
import envision.engine.windows.windowObjects.utilityObjects.WindowDialogueBox;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.game.component.ComponentType;
import envision.game.effects.sounds.SoundEngine;
import envision.game.entities.Entity;
import envision.game.entities.EntityRenderer;
import envision.game.util.InsertionSort;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import envision.game.world.worldEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.worldEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.worldEditor.editorParts.sidePanel.toolPanels.regionTool.RegionSidePanel;
import envision.game.world.worldEditor.editorParts.toolBox.EditorToolBox;
import envision.game.world.worldEditor.editorParts.topHeader.EditorScreenBotHeader;
import envision.game.world.worldEditor.editorParts.topHeader.EditorScreenTopHeader;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorTools.EditorToolType;
import envision.game.world.worldEditor.editorTools.ToolHandler;
import envision.game.world.worldTiles.WorldTile;
import envision.game.world.worldTiles.WorldTileRenderer;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import qot.assets.textures.editor.EditorTextures;

public class MapEditorScreen extends GameScreen {

	//----------------
	// Fields : World
	//----------------
	
	protected File mapFile;
	
	/** The actual game world (used to load from and save data to) */
	protected GameWorld actualWorld;
	
	/** The world as it exists in the map editor */
	private EditorWorld editorWorld;
	
	//-----------------
	// Fields : Editor
	//-----------------
	
	MapEditorSettings settings;
	ToolHandler toolHandler;
	
	EditorToolBox toolBox;
	EditorScreenTopHeader topHeader;
	EditorScreenBotHeader botHeader;
	EditorSidePanel sidePanel;
	
	public boolean firstPress = false;
	public boolean mouseInMap = false;
	public boolean mouseOver = false;
	public boolean drawingMousePos = false;
	
	/** the world coordinates at the center of the screen. */
	public int midDrawX, midDrawY;
	/** the width and height of the world that the renderer will draw out from the mid. */
	public int drawDistX = 20, drawDistY = 15;
	/** the world coordinates under the mouse. */
	public int worldXPos, worldYPos;
	/** The world pixel coordinates under the mouse. */
	public int worldPixelX, worldPixelY;
	public int oldWorldX, oldWorldY;
	public boolean updateMap = false;
	
	private boolean recentlySaved = false;
	private long saveStringTimeout = 0l;
	private String saveString = "Saved!";
	private EColors saveStringColor = EColors.lgreen;
	private boolean loading = false;
	private boolean hasBeenModified = false;
	private boolean hasSaved = false;
	private boolean failed = false;
	private Exception failedException;
	private WindowDialogueBox exitSaverBox = null;
	private boolean cancelExitClick = false;
	
	private final EList<EditorObject> selectedObjects = new EArrayList<>();
	
	//---------------------
	int left;
	int top;
	int right;
	int bot;
	/** draw width */
	int dw;
	/** draw height */
	int dh;
	
	public double tileDrawWidth; //pixel width of each tile
	public double tileDrawHeight; //pixel height of each tile
	public double drawAreaMidX; //the middle x of the map draw area
	public double drawAreaMidY; //the middle y of the map draw area
	public double mapDrawStartX; //the left most x coordinate for map drawing
	public double mapDrawStartY; //the top most y coordinate for map drawing
	
	public double x = mapDrawStartX;
	public double y = mapDrawStartY;
	public double w = tileDrawWidth;
	public double h = tileDrawHeight;
	//----------------------
	
	public long timeSinceKey = 0l;
	
	//--------------
	// Constructors
	//--------------
	
	public MapEditorScreen(File mapFileIn) {
		mapFile = mapFileIn;
		setDefaultDims();
	}
	
	public MapEditorScreen(GameWorld worldIn) {
		actualWorld = worldIn;
		setDefaultDims();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void initScreen() {
		settings = new MapEditorSettings(this);
		toolHandler = new ToolHandler(this);
		SoundEngine.stopAll();
		firstPress = !Mouse.isButtonDown(0);
		
		loading = true;
		Runnable loader = () -> loadWorld();
		new Thread(loader).start();
		
		settings.currentTool = EditorToolType.PENCIL;
	}
	
	@Override
	public void initChildren() {
		addObject(topHeader = new EditorScreenTopHeader(this));
		addObject(toolBox = new EditorToolBox(this));
		addObject(sidePanel = new EditorSidePanel(this));
		addObject(botHeader = new EditorScreenBotHeader(this));
		
		sidePanel.setCurrentPanel(SidePanelType.TERRAIN);
		updateDrawDist();
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.vdgray);
		mouseOver = isMouseOver();
		
		if (loading) {
			drawStringC("Loading...!", midX, midY);
			return;
		}
		
		if (failed) {
			loading = false;
			drawStringC("Failed to load!", drawAreaMidX, drawAreaMidY);
			
			if (failedException != null) {
				drawString(failedException, 100, drawAreaMidY + 40, EColors.lred);
				
				var st = failedException.getStackTrace();
				for (int i = 0; i < st.length && i < 10; i++) {
					drawString(failedException.getStackTrace()[i], 100, drawAreaMidY + 60 + (i * 20), EColors.lred);					
				}
			}
		}
		else if (actualWorld == null || !actualWorld.isFileLoaded()) {
			drawStringC("Failed to load!", midX, midY);
			failed = true;
		}
		else {
			try {
				drawEditor(mXIn, mYIn);
			}
			catch (Exception e) {
				e.printStackTrace();
				failed = true;
				failedException = e;
			}
		}
		
		if (exitSaverBox != null) {
			if (containsObject(exitSaverBox)) drawRect(EColors.vdgray.opacity(80));
			else exitSaverBox = null;
		}
	}
	
	private void drawEditor(int mXIn, int mYIn) {
		tileDrawWidth = (editorWorld.getTileWidth() * actualWorld.getCameraZoom()); //pixel width of each tile
		tileDrawHeight = (editorWorld.getTileHeight() * actualWorld.getCameraZoom()); //pixel height of each tile
		drawAreaMidX = (Envision.getWidth() - sidePanel.width) / 2; //the middle x of the map draw area
		drawAreaMidY = topHeader.endY + (Envision.getHeight() - topHeader.height) / 2; //the middle y of the map draw area
		mapDrawStartX = (drawAreaMidX - (drawDistX * tileDrawWidth) - (tileDrawWidth / 2)); //the left most x coordinate for map drawing
		mapDrawStartY = (drawAreaMidY - (drawDistY * tileDrawHeight) - (tileDrawHeight / 2)); //the top most y coordinate for map drawing
		
		//converting to shorthand to reduce footprint
		x = mapDrawStartX;
		y = mapDrawStartY;
		w = tileDrawWidth;
		h = tileDrawHeight;
		
		drawMap(x, y, w, h);
		if (settings.drawTileGrid) drawTileGrid();
		if (settings.drawEntities) renderEntities(x, y, w, h);
		if (settings.drawRegions) drawRegions(x, y, w, h);
		if (settings.drawCenterPositionBox) drawCenterPositionBox(x, y, w, h);
		
		mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
		drawingMousePos = mouseInMap;
		if (exitSaverBox == null) {
			if (mouseInMap) {
				if (getCurrentSidePanel() == SidePanelType.TERRAIN) drawHoveredTileBox(x, y, w, h);
				toolHandler.drawCurrentTool(x, y, w, h);
			}
		}
		
		final var fh = FontRenderer.FONT_HEIGHT; // font height
		final var bot = (botHeader != null) ? botHeader.startY : 0;
		
		
		//displays the world tile (world) coordinates directly at the middle of the screen
		drawString("Px: [" + worldPixelX + "," + worldPixelY + "]", 5, bot - fh * 3);
		//displays the world tile (world) coordinates directly at the middle of the screen
		drawString("Mid: [" + midDrawX + "," + midDrawY + "]", 5, bot - fh * 2);
		//displays the number of tiles that the renderer with draw out from the mid in x and y directions
		drawString("Dist: [" + drawDistX + "," + drawDistY + "]", 5, bot - fh);
		
		if (settings.drawMapBorders) drawMapBorders(x, y, w, h);
		if (recentlySaved) updateSaveString(drawAreaMidX, drawAreaMidY);
	
		drawPlayerSpawn(x, y, w, h);
		
		drawSelectedEntities(x, y, w, h);
		
		oldWorldX = midDrawX;
		oldWorldY = midDrawY;
	}
	
	@Override
	public void mouseScrolled(int change) {
		double c = Math.signum(change);
		double z = 1.0;
		double zoom = actualWorld.getCameraZoom();
		
		if (exitSaverBox != null) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (Keyboard.isCtrlDown()) {
			if (c > 0 && zoom == 0.25)     z = 0.05;     // if at 0.25 and zooming out -- 0.05x
			else if (zoom < 1.0)           z = c * 0.05; // if less than 1 zoom by 0.05x
			else if (zoom < 2.0)           z = c * 0.1;  // if less than 2 zoom by 0.1x
			else if (c > 0 && zoom == 1.0) z = c * 0.05; // if at 1.0 and zooming in -- 0.05x
			else if (c < 0 && zoom == 2.0) z = c * 0.1;  // if at 2.0 and zooming in -- 0.1x
			else                           z = c * 0.25; // otherwise always zoom by 0.25x
			
			z = ENumUtil.round(actualWorld.getCameraZoom() + z, 2);
			actualWorld.setCameraZoom(z);
			
			updateDrawDist();
		}
		else if (Keyboard.isShiftDown()) {
			midDrawX -= c * 2;
			midDrawX = (midDrawX < 0) ? 0 : (midDrawX > (editorWorld.getWidth() - 1)) ? editorWorld.getWidth() - 1 : midDrawX;
		}
		else if (Keyboard.isAltDown()) {
			midDrawY -= c * 2;
			midDrawY = (midDrawY < 0) ? 0 : (midDrawY > (editorWorld.getHeight()) - 1) ? editorWorld.getHeight() - 1 : midDrawY;
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		
		if (exitSaverBox != null && !exitSaverBox.isMouseInside()) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (getTopParent().getModifyingObject() == null) {
			if (mouseInMap || settings.getCurrentTool() == EditorToolType.SELECTOR) {
				toolHandler.handleToolPress(button);
			}
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		if (exitSaverBox != null && !exitSaverBox.isMouseInside()) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		else {
			cancelExitClick = false;
		}
		
		if (!firstPress && button == 0) {
			firstPress = true;
		}
		
		if (mouseInMap && getTopParent().getModifyingObject() == null) {
			toolHandler.handleToolRelease(button);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (exitSaverBox != null) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (mouseOver) {
			if (Keyboard.isCtrlS(keyCode)) saveWorld();
			if (Keyboard.isCtrlR(keyCode)) {
				loadWorld();
				saveString = "Reloaded!";
				saveStringColor = EColors.lgreen;
				recentlySaved = true;
				saveStringTimeout = System.currentTimeMillis();
			}
			
			// select everything
			if (Keyboard.isCtrlA(keyCode)) {
				selectedObjects.addAll(editorWorld.getEditorEntities());
			}
			
			// deselect everything
			if (Keyboard.isCtrlDown() && Keyboard.isKeyDown(Keyboard.KEY_D)) {
				selectedObjects.clear();
			}
			
			if (keyCode == Keyboard.KEY_ESC) {
				boolean isAsset = getSidePanel().getCurrentPanelType() == SidePanelType.ASSET;
				
				if (isAsset && getSettings().getCurrentTool() == EditorToolType.PLACE) {
					getSettings().setCurrentTool(EditorToolType.SELECTOR);
				}
				else if (!hasSaved && hasBeenModified) {
					if (exitSaverBox == null || !containsObject(exitSaverBox)) {
						exitSaverBox = new WindowDialogueBox(this, WindowDialogueBox.DialogBoxTypes.YES_NO);
						exitSaverBox.setTitle("World not saved!");
						exitSaverBox.setMessage("Exit without saving?");
						exitSaverBox.setMinimizable(false);
						exitSaverBox.setActionReceiver(this);
						setFocusLockObject(exitSaverBox);
						displayWindow(exitSaverBox);
						cancelExitClick = true;
					}
				}
				else {
					if (!screenHistory.isEmpty()) closeScreen();
				}
			}
			
			if (keyCode == Keyboard.KEY_DELETE) {
				for (var e : selectedObjects) {
					editorWorld.getEditorEntities().remove(e);
				}
				selectedObjects.clear();
			}
		}
		else if (keyCode == Keyboard.KEY_DELETE) {
			for (var e : selectedObjects) {
				editorWorld.getEditorEntities().remove(e);
			}
			selectedObjects.clear();
		}
		else super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == exitSaverBox) {
			if (args[0] instanceof String action) {
				if (action.equals("y")) closeScreen();
				else exitSaverBox.close();
			}
		}
	}
	
	@Override
	public void onGameTick(float partialTicks) {
		updateMovement();
		if (hasFocus() && exitSaverBox == null && !cancelExitClick) {
			toolHandler.handleToolUpdate();
		}
	}
	
	@Override
	public void onEvent(GameEvent e) {
		System.out.println("GAME EVENT: " + e);
	}
	
	@Override
	public void onScreenClosed() {
		SoundEngine.stopAll();
	}
	
	//------------------------
	// Private Editor Methods
	//------------------------
	
	private void updateMovement() {
		if (Envision.getTopScreen().hasFocus()) return;
		if (System.currentTimeMillis() - timeSinceKey < 37) return;
		
		if (exitSaverBox != null && Keyboard.isAnyKeyDown(KEY_W, KEY_UP, KEY_A, KEY_LEFT, KEY_S, KEY_DOWN, KEY_D, KEY_RIGHT)) {
			exitSaverBox.drawFocusLockBorder();
			return;
		}
		
		if (!Keyboard.isCtrlDown()) {
			if (Keyboard.isWDown() || Keyboard.isKeyDown(Keyboard.KEY_UP)) { midDrawY--; midDrawY = (midDrawY < 0) ? 0 : midDrawY; }
			if (Keyboard.isADown() || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) { midDrawX--; midDrawX = (midDrawX < 0) ? 0 : midDrawX; }
			if (Keyboard.isSDown() || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) { midDrawY++; midDrawY = (midDrawY > (editorWorld.getHeight() - 1)) ? editorWorld.getHeight() - 1 : midDrawY; }
			if (Keyboard.isDDown() || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) { midDrawX++; midDrawX = (midDrawX > (editorWorld.getWidth() - 1)) ? editorWorld.getWidth() - 1 : midDrawX; }
			
			timeSinceKey = System.currentTimeMillis();
		}
	}
	
	/** Draws the world tiles of the map. */
	private void drawMap(double x, double y, double w, double h) {
		//only update values if needed
		//if (updateMap || midDrawX != oldWorldX || midDrawY != oldWorldY) {
			left = ENumUtil.clamp(midDrawX - drawDistX, 0, editorWorld.getWidth() - 1);
			top = ENumUtil.clamp(midDrawY - drawDistY, 0, editorWorld.getHeight() - 1);
			right = ENumUtil.clamp(midDrawX + drawDistX, left, editorWorld.getWidth() - 1);
			bot = ENumUtil.clamp(midDrawY + drawDistY, top, editorWorld.getHeight() - 1);
			dw = right - left; //draw width
			dh = bot - top; //draw height
			updateMap = false;
		//}
			
			//System.out.println(left + " : " + right);
		
		for (int i = top, iy = 0; i <= bot; i++, iy++) {
			for (int j = left, jx = 0; j <= right; j++, jx++) {
				//WorldTile t = world.getWorldData()[i][j];
				WorldTile t = editorWorld.getTileAt(j, i);
				if (t == null) continue;
				if (!t.hasTexture()) continue;
				
				double drawPosX = x;
				double drawPosY = y;
				
				if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
				if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
				
				double dX = drawPosX + (jx * w);
				double dY = drawPosY + (iy * h);
				
				//System.out.println(dX + " : " + dY);
				//System.out.println(drawPosX + " : " + drawPosY);
				boolean mouseOver = false;
				if (settings.drawWallBox) mouseOver = Mouse.getMx() >= dX &&
													  Mouse.getMx() < (dX + w) &&
													  Mouse.getMy() >= dY &&
													  Mouse.getMy() < (dY + h);
													  
				//t.draw(editorWorld, actualWorld.getCameraZoom(), midDrawX, midDrawY, drawDistX, drawDistY);
				WorldTileRenderer renderer = t.getComponent(ComponentType.RENDERING);
				renderer.drawTile(editorWorld, dX, dY, w, h, 0xffffffff, mouseOver);
			}
		}
	}
	
	private void drawTileGrid() {
		for (int i = top, iy = 0; i <= bot; i++, iy++) {
			double drawPosX = x;
			double drawPosY = y;
			
			if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
			if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
			
			double dSX = drawPosX + ((drawPosX <= 0) ? -drawPosX: (left * w));
			double dEX = drawPosX + w * (1 + right - left); //((right + 1) * w) - (left * w)
			double dY = drawPosY + (iy * h);
			
			//drawString((int) drawPosX + " : " + (int) x + " : " + (int) dSX + " : " + (int) (left * w));
			
			drawRect(dSX, dY, dEX, dY + 1, EColors.vdgray);
		}
		
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			double drawPosX = x;
			double drawPosY = y;
			
			if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
			if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
			
			double dX = drawPosX + (ix * w);
			double dSY = drawPosY + ((drawPosY <= 0) ? -drawPosY: (top * w));
			double dEY = drawPosY + h * (1 + bot - top);
			
			//drawString((int) drawPosX + " : " + (int) x + " : " + (int) dSX + " : " + (int) (left * w));
			
			drawRect(dX, dSY, dX + 1, dEY, EColors.vdgray);
		}
	}
	
	private void renderEntities(double x, double y, double w, double h) {
		int size = editorWorld.getEditorEntities().size();
		InsertionSort.sort(editorWorld.getEditorEntities());
		
		for (int i = 0; i < size; i++) {
			EditorObject object = editorWorld.getEditorEntities().get(i);
			Entity ent = object.getEntity();
			GameTexture tex = ent.getTexture();
			if (tex == null) continue;
			
			final double zoom = actualWorld.getCameraZoom();
			final int wtw = actualWorld.getTileWidth(); // world tile width
			final int wth = actualWorld.getTileHeight(); // world tile height
			
			//transform the world coordinates of the entity to screen x/y coordinates
			//then translate to the middle drawn world tile
			double drawX = x + w * (ent.worldX + drawDistX - midDrawX) + ent.startX % wtw;
			double drawY = y + h * (ent.worldY + drawDistY - midDrawY) + ent.startY % wth;
			
			//calculate the entity's draw width and height based off of actual dims and zoom
			double drawW = ent.width * zoom;
			double drawH = ent.height * zoom;
			
			//draw the entity on top of the tile it's on (elevated if it's a wall)
//			if (ent.worldX >= 0 && ent.worldX < editorWorld.getWidth() &&
//				ent.worldY >= 0 && ent.worldY < editorWorld.getHeight())
//			{
//				WorldTile tileUnderEntity = editorWorld.getTileAt(ent.worldX, ent.worldY);
//				if (tileUnderEntity != null && tileUnderEntity.isWall()) {
//					var wallHeight = tileUnderEntity.getWallHeight() * h;
//					drawY -= wallHeight;
//				}
//			}
			
			//render the entity
			//ent.renderObject(drawX, drawY, drawW, drawH);
			//ent.draw(editorWorld, actualWorld.getCameraZoom(), midDrawX, midDrawY, drawDistX, drawDistY);
			EntityRenderer renderer = ent.getComponent(ComponentType.RENDERING);
			renderer.drawEntity(editorWorld, drawX, drawY, drawW, drawH, 0xffffffff, mouseOver);
			//drawTexture(tex, drawX, drawY, drawW, drawH);
			
			if (settings.drawEntityHitBoxes) {
				double colSX = drawX + (ent.collisionBox.startX * zoom);
				double colSY = drawY + (ent.collisionBox.startY * zoom);
				double colEX = colSX + (ent.collisionBox.width * zoom);
				double colEY = colSY + (ent.collisionBox.height * zoom);
				
				drawHRect(colSX - 1, colSY, colEX, colEY - 1, 1, EColors.yellow);
			}
			
			if (settings.drawEntityOutlines) {
				drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
			}
		}
	}
	
	private void drawSelectedEntities(double x, double y, double w, double h) {
		for (var e : selectedObjects) {
			var object = e.getGameObject();
			
			//transform the world coordinates of the entity to screen x/y coordinates
			//then translate to the middle drawn world tile
			double drawX = x + w * (object.worldX + drawDistX - midDrawX);
			double drawY = y + h * (object.worldY + drawDistY - midDrawY);
			
			//calculate the entity's draw width and height based off of actual dims and zoom
			double drawW = object.width * actualWorld.getCameraZoom();
			double drawH = object.height * actualWorld.getCameraZoom();
			
			drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.green);
		}
	}
	
	private void drawPlayerSpawn(double x, double y, double w, double h) {
		var spawn = this.getEditorWorld().getPlayerSpawn();
		int spawnX = spawn.getX(); // spawn world position x
		int spawnY = spawn.getY(); // spawn world position y
		
		double drawX = x + w * (spawnX + drawDistX - midDrawX);
		double drawY = y + h * (spawnY + drawDistY - midDrawY);
		
		double drawW = spawn.width * actualWorld.getCameraZoom();
		double drawH = spawn.height * actualWorld.getCameraZoom();
		
		drawTexture(EditorTextures.player_spawn, drawX, drawY, drawW, drawH);
	}
	
	//------------
	// Load World
	//------------
	
	public void loadWorld() {
		boolean center = actualWorld == null || !firstPress;
		
		if (mapFile != null) {
			double oldZoom = 1;
			if (actualWorld != null) oldZoom = actualWorld.getCameraZoom();
			if (mapFile.isDirectory()) {
				String mapName = mapFile.getName().replace(".twld", "");
				String mapDir = mapFile.getParent();
				mapFile = new File(mapDir, mapName + "/" + mapName + ".twld");
			}
			actualWorld = new GameWorld(mapFile);
			actualWorld.getCamera().setEdgeLocked(false);
			actualWorld.getCamera().setMinZoom(0.25);
			actualWorld.getCamera().setMaxZoom(10);
			actualWorld.setCameraZoom(oldZoom);
		}
		else if (actualWorld != null) {
			double oldZoom = actualWorld.getCameraZoom();
			mapFile = actualWorld.getWorldFile();
			actualWorld.setCameraZoom(oldZoom);
		}
		
		//create editor world
		editorWorld = new EditorWorld(actualWorld);
		
		if (center) {
			midDrawX = actualWorld.getWidth() / 2;
			midDrawY = actualWorld.getHeight() / 2;
			drawDistX = (int) (actualWorld.getWidth() / getCameraZoom());
			drawDistY = (int) (actualWorld.getHeight() / getCameraZoom());
		}
		
		loading = false;
	}
	
	//------------
	// Save World
	//------------
	
	public void saveWorld() {
		if (editorWorld == null) return;
		
		if (editorWorld.saveWorldToFile()) {
			saveString = "Saved!";
			saveStringColor = EColors.lgreen;
			recentlySaved = true;
			hasSaved = true;
			hasBeenModified = false;
			saveStringTimeout = System.currentTimeMillis();
		}
		else {
			saveString = "Failed to Save!";
			saveStringColor = EColors.lred;
			recentlySaved = true;
			saveStringTimeout = System.currentTimeMillis();
		}
	}
	
	/** Highlights the tile at the center of the draw area. */
	private void drawCenterPositionBox(double x, double y, double w, double h) {
		double sX = x + (drawDistX * w);
		double sY = y + (drawDistY * h);
		double eX = sX + w;
		double eY = sY + h;
		drawHRect(sX, sY, eX, eY, 2, EColors.red);
	}
	
	/** Draws a border outlining the edge of the map's bounds. */
	private void drawMapBorders(double x, double y, double w, double h) {
		double drawPosX = x; //left most x coord of map
		double drawPosY = y; //top most y coord of map
		double drawPosEndX = x + (drawDistX - midDrawX + editorWorld.getWidth()) * w;
		double drawPosEndY = y + (drawDistY - midDrawY + editorWorld.getHeight()) * h;
		
		//shift over in terms of midDrawX/Y offset in relation to map render distance
		if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
		if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
		
		drawHRect(drawPosX, drawPosY, drawPosEndX, drawPosEndY, 1, EColors.red);
	}
	
	private void drawRegions(double x, double y, double w, double h) {
		editorWorld.getRegionData().forEach(r -> drawRegion(r, x, y, w, h));
	}
	
	private void drawRegion(Region r, double x, double y, double w, double h) {
		double z = actualWorld.getCameraZoom();
		double sX = (r.startX * z);
		double sY = (r.startY * z);
		double rw = (r.width * z);
		double rh = (r.height * z);
		double drawPosX = x + sX - ((midDrawX - drawDistX) * w);
		double drawPosY = y + sY - ((midDrawY - drawDistY) * h);
		double drawWidth = drawPosX + rw;
		double drawHeight = drawPosY + rh;
		double lineWidth = ENumUtil.clamp((3 * z), 1, 4);
		drawHRect(drawPosX, drawPosY, drawWidth, drawHeight, lineWidth, r.getColor());
	}
	
	private boolean checkMousePos(double x, double y, double w, double h, double mXIn, double mYIn) {
		//The X world pixel under the mouse scaled by the render zoom
		int xWorldPixel = (int) Math.floor(mXIn - x - ((drawDistX - midDrawX) * w));
		//The Y world pixel under the mouse scaled by the render zoom
		int yWorldPixel = (int) Math.floor(mYIn - y - ((drawDistY - midDrawY) * h));
		
		//The X world pixel under the mouse with zoom scaling removed
		int xWorldPixelNoZoom = (int) (xWorldPixel / actualWorld.getCameraZoom());
		//The Y world pixel under the mouse with zoom scaling removed
		int yWorldPixelNoZoom = (int) (yWorldPixel / actualWorld.getCameraZoom());
		
		//The X coordinate world tile under the mouse
		double xMouseTile = xWorldPixel / w;
		//The Y coordinate world tile under the mouse
		double yMouseTile = yWorldPixel / h;
		
		//update in-map values
		if (xMouseTile >= 0 && yMouseTile >= 0) {
			worldXPos = (int) xMouseTile;
			worldYPos = (int) yMouseTile;
			worldPixelX = xWorldPixelNoZoom;
			worldPixelY = yWorldPixelNoZoom;
			boolean inMap = worldXPos >= 0 &&
							worldYPos >= 0 &&
							worldXPos < editorWorld.getWidth() &&
							worldYPos < editorWorld.getHeight();
			
			return mouseOver && inMap && Dimension_d.of(x, y, x + w + (drawDistX * 2 * w), y + h + (drawDistY * 2 * h)).contains(mXIn, mYIn);
		}
		return false;
	}
	
	/**
	 * Draws a box around the currently hovered tile.
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private void drawHoveredTileBox(double x, double y, double w, double h) {
		worldXPos = ENumUtil.clamp(worldXPos, 0, editorWorld.getWidth() - 1);
		worldYPos = ENumUtil.clamp(worldYPos, 0, editorWorld.getHeight() - 1);
		
		double xPos = x + w * (worldXPos + drawDistX - midDrawX);
		double yPos = y + h * (worldYPos + drawDistY - midDrawY);
		
		if (!settings.drawWallBox) drawHRect(xPos, yPos, xPos + w, yPos + h, 1, EColors.chalk);
	}
	
	private void updateSaveString(double drawAreaMidX, double drawAreaMidY) {
		double sw = FontRenderer.strWidth(saveString);
		double hsw = (sw / 2);
		double sbsx = drawAreaMidX - hsw - 12.5;
		double sbsy = drawAreaMidY - (FontRenderer.FONT_HEIGHT / 2) - 12.5;
		double sbex = drawAreaMidX + hsw + 12.5;
		double sbey = drawAreaMidY + (FontRenderer.FONT_HEIGHT / 2) + 12.5;
		drawRect(sbsx, sbsy, sbex, sbey,  EColors.black);
		drawRect(sbsx + 2, sbsy + 2, sbex - 2, sbey - 2,  EColors.dgray);
		drawStringC(saveString, drawAreaMidX, drawAreaMidY - (FontRenderer.FONT_HEIGHT / 2), saveStringColor);
		if (System.currentTimeMillis() - saveStringTimeout > 600) recentlySaved = false;
	}
	
	private void updateDrawDist() {
		if (editorWorld == null) return;
		
		double w = editorWorld.getTileWidth() * actualWorld.getCameraZoom();
		double h = editorWorld.getTileHeight() * actualWorld.getCameraZoom();
		
		double dw = ((Envision.getWidth() - sidePanel.width) / w) / 2.0;
		double dh = ((Envision.getHeight() - topHeader.height) / h) / 2.0;
		
		//System.out.println(dw + " : " + dh);
		
		drawDistX = (int) Math.round(dw);
		drawDistY = (int) Math.ceil(dh);
		
		updateMap = true;
	}
	
	//---------------------------------
	//         Public Getters
	//---------------------------------
	
	public Dimension_d getMapDrawDims() {
		double sx = 0.0;
		double sy = this.topHeader.endY;
		double ex = this.sidePanel.startX;
		double ey = endY;
		return new Dimension_d(sx, sy, ex, ey);
	}
	
	public int getXPos() { return midDrawX; }
	public int getYPos() { return midDrawY; }
	public int getViewX() { return drawDistX; }
	public int getViewY() { return drawDistY; }
	public int getWorldMX() { return worldXPos; }
	public int getWorldMY() { return worldYPos; }
	
	public double getCameraZoom() { return (actualWorld != null) ? actualWorld.getCameraZoom() : 1; }
	
	public GameWorld getActualWorld() { return actualWorld; }
	public EditorWorld getEditorWorld() { return editorWorld; }
	public boolean isMouseInMap() { return mouseInMap; }
	
	public WorldTile setTileAt(int x, int y, WorldTile in) {
		editorWorld.setTileAt(in, x, y);
		//world.setTileAt(x, y, in);
		hasBeenModified = true;
		hasSaved = false;
		return in;
	}
	
	public WorldTile setTileAtMouse(WorldTile in) {
		if (mouseInMap) {
			editorWorld.setTileAt(in, worldXPos, worldYPos);
			//world.setTileAt(worldXPos, worldYPos, in);
			hasBeenModified = true;
			hasSaved = false;
		}
		return in;
	}
	
	public WorldTile getTileHoveringOver() {
		if (editorWorld == null) return null;
		return editorWorld.getTileAt(worldXPos, worldYPos);
	}
	
	public Point2i getHoverTileCoords() { return new Point2i(worldXPos, worldYPos); }
	public Point2i getHoverPixelCoords() { return new Point2i(worldPixelX, worldPixelY); }
	
	public boolean shouldDrawMouse() { return drawingMousePos; }
	
	public MapEditorSettings getSettings() { return settings; }
	public EditorScreenTopHeader getTopHeader() { return topHeader; }
	public EditorScreenBotHeader getBotHeader() { return botHeader; }
	public EditorToolBox getToolBox() { return toolBox; }
	public EditorSidePanel getSidePanel() { return sidePanel; }
	public EditorToolType getCurrentTool() { return settings.getCurrentTool(); }
	public SidePanelType getCurrentSidePanel() { return sidePanel.getCurrentPanelType(); }
	
	public boolean compareSidePanel(SidePanelType type) { return getCurrentSidePanel().equals(type); }
	
	public void updateRegionPanel() {
		if (getSidePanel().getCurrentPanel() instanceof RegionSidePanel regionPanel) {
			regionPanel.loadRegions();
		}
	}
	
	//---------------------------------
	//         Public Setters
	//---------------------------------
	
	public MapEditorScreen setXPos(int pos) { return this; }
	public MapEditorScreen setYPos(int pos) { return this; }
	
	public MapEditorScreen setViewX(int dist) { drawDistX = ENumUtil.clamp(dist, 0, dist); return this; }
	public MapEditorScreen setViewY(int dist) { drawDistY = ENumUtil.clamp(dist, 0, dist); return this; }
	
	
	//------------------
	// Selected Objects
	//------------------
	
	public void clearSelected() {
		var it = selectedObjects.iterator();
		while (it.hasNext()) {
			var o = it.next();
			o.setSelected(false);
			it.remove();
		}
	}
	
	public EList<EditorObject> getSelectedObjects() { return selectedObjects; }
	
	public void addToSelected(EditorObject... objs) { addAllToSelected(objs); }
	public void addToSelected(Collection<EditorObject> objs) { addAllToSelected(objs); }
	public void removeFromSelected(EditorObject... objs) { removeAllFromSelected(objs); }
	public void removeFromSelected(Collection<EditorObject> objs) { removeAllFromSelected(objs); }
	
	public void setSelectedObjects(EditorObject... objs) {
		clearSelected();
		addAllToSelected(objs);
	}
	
	public void setSelectedObjects(Collection<EditorObject> objs) {
		clearSelected();
		addAllToSelected(objs);
	}
	
	private void addAllToSelected(EditorObject... objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(true);
			selectedObjects.add(o);
		}
	}
	
	private void addAllToSelected(Collection<EditorObject> objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(true);
			selectedObjects.add(o);
		}
	}
	
	private void removeAllFromSelected(EditorObject... objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(false);
			selectedObjects.remove(o);
		}
	}
	
	private void removeAllFromSelected(Collection<EditorObject> objs) {
		for (var o : objs) {
			if (o == null) continue;
			o.setSelected(false);
			selectedObjects.remove(o);
		}
	}
	
}
