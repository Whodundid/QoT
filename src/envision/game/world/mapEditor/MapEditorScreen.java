package envision.game.world.mapEditor;

import java.io.File;
import java.util.Collection;
import java.util.Comparator;

import envision.events.GameEvent;
import envision.game.GameObject;
import envision.game.entity.Entity;
import envision.game.screens.GameScreen;
import envision.game.sounds.SoundEngine;
import envision.game.world.EntitySpawn;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import envision.game.world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.mapEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.mapEditor.editorParts.toolBox.EditorToolBox;
import envision.game.world.mapEditor.editorParts.topHeader.EditorScreenTopHeader;
import envision.game.world.mapEditor.editorParts.util.EditorObject;
import envision.game.world.mapEditor.editorTools.EditorToolType;
import envision.game.world.mapEditor.editorTools.ToolHandler;
import envision.game.world.worldTiles.WorldTile;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.renderEngine.textureSystem.GameTexture;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.Grid;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import game.QoT;

public class MapEditorScreen extends GameScreen {

	//----------------
	// Fields : World
	//----------------
	
	protected File mapFile;
	
	/** The actual game world (used to load from and save data to) */
	protected GameWorld actualWorld;
	
	/** The world as it exists in the map editor */
	private GameWorld editorWorld;
	
	//-----------------
	// Fields : Editor
	//-----------------
	
	MapEditorSettings settings;
	ToolHandler toolHandler;
	
	EditorToolBox toolBox;
	EditorScreenTopHeader topHeader;
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
	public int oldWorldX, oldWorldY;
	public boolean updateMap = false;
	
	private boolean recentlySaved = false;
	private long saveStringTimeout = 0l;
	private String saveString = "Saved!";
	private EColors saveStringColor = EColors.lgreen;
	private boolean loading = false;
	private boolean hasBeenModified = false;
	private boolean hasSaved = false;
	
	private final EArrayList<EditorObject> selectedObjects = new EArrayList<>();
	
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
		
		if (actualWorld == null || !actualWorld.isFileLoaded()) drawStringC("Failed to load!", midX, midY);
		else {
			tileDrawWidth = (actualWorld.getTileWidth() * actualWorld.getZoom()); //pixel width of each tile
			tileDrawHeight = (actualWorld.getTileHeight() * actualWorld.getZoom()); //pixel height of each tile
			drawAreaMidX = (QoT.getWidth() - sidePanel.width) / 2; //the middle x of the map draw area
			drawAreaMidY = topHeader.endY + (QoT.getHeight() - topHeader.height) / 2; //the middle y of the map draw area
			mapDrawStartX = (drawAreaMidX - (drawDistX * tileDrawWidth) - (tileDrawWidth / 2)); //the left most x coordinate for map drawing
			mapDrawStartY = (drawAreaMidY - (drawDistY * tileDrawHeight) - (tileDrawHeight / 2)); //the top most y coordinate for map drawing
			
			//converting to shorthand to reduce footprint
			x = mapDrawStartX;
			y = mapDrawStartY;
			w = tileDrawWidth;
			h = tileDrawHeight;
			
			//scissor(x, y, deX, deY + 1);
			//endScissor();
			
			drawMap(x, y, w, h);
			if (settings.drawEntities) renderEntities(x, y, w, h);
			if (settings.drawRegions) drawRegions(x, y, w, h);
			if (settings.drawCenterPositionBox) drawCenterPositionBox(x, y, w, h);
			
			mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
			if (drawingMousePos = mouseInMap) {
				switch (sidePanel.getCurrentPanelType()) {
				case ASSET:
				{
					if (getCurrentTool() != EditorToolType.PLACE) break;
					
					//COMPLETELY HASHED TOGETHER :)
					EditorObject primary = settings.getPrimaryPalette();
					if (primary == null) break;
					GameTexture tex = primary.getTexture();
					double tW = primary.getGameObject().width;
					double tH = primary.getGameObject().height;
					double tDrawW = tW * actualWorld.getZoom();
					double tDrawH = tH * actualWorld.getZoom();
					double tmx = primary.getGameObject().collisionBox.midX * actualWorld.getZoom();
					double tmy = primary.getGameObject().collisionBox.midY * actualWorld.getZoom();
					
					drawTexture(tex, mX - tmx, mY - tmy, tDrawW, tDrawH, 0x99ffffff);
					//drawTexture(tex, mX, mY, tDrawW, tDrawH, 0x99ffffff);
					//drawHRect(mX, mY, mX + tDrawW, mY + tDrawH, 1, EColors.blue);
					//drawTexture(tex, mX, mY, tDrawW, tDrawH);
					drawHoveredTileBox((int) x, (int) y, (int) w, (int) h);
					break;
				}
				default:
					drawHoveredTileBox(x, y, w, h);
				}
				if (firstPress && getTopParent().getModifyingObject() == null) {
					//toolHandler.handleToolUpdate(getCurTileTool());
				}
			}
			
			//displays the world tile (world) coordinates directly at the middle of the screen
			drawString("Mid: [" + midDrawX + "," + midDrawY + "]", 5, QoT.getHeight() - FontRenderer.FONT_HEIGHT * 2);
			//displays the number of tiles that the renderer with draw out from the mid in x and y directions
			drawString("Dist: [" + drawDistX + "," + drawDistY + "]", 5, QoT.getHeight() - FontRenderer.FONT_HEIGHT);
			
			if (settings.drawMapBorders) drawMapBorders(x, y, w, h);
			if (recentlySaved) updateSaveString(drawAreaMidX, drawAreaMidY);
			
			oldWorldX = midDrawX;
			oldWorldY = midDrawY;
			
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		double c = Math.signum(change);
		double z = 1.0;
		
		if (Keyboard.isCtrlDown()) {
			if (c > 0 && actualWorld.getZoom() == 0.25) 	z = 0.05;		//if at 0.25 and zooming out -- 0.05x
			else if (actualWorld.getZoom() < 1.0) 		z = c * 0.1;	//if less than 1 zoom by 0.1x
			else if (c > 0) 						z = 0.25;		//if greater than 1 zoom by 0.25x
			else if (actualWorld.getZoom() == 1.0) 		z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
			else 									z = c * 0.25;	//otherwise always zoom by 0.25x
			
			z = NumberUtil.round(actualWorld.getZoom() + z, 2);
			actualWorld.setZoom(z);
			
			updateDrawDist();
		}
		else if (Keyboard.isShiftDown()) {
			midDrawX -= c * 2;
			midDrawX = (midDrawX < 0) ? 0 : (midDrawX > (actualWorld.getWidth() - 1)) ? actualWorld.getWidth() - 1 : midDrawX;
		}
		else if (Keyboard.isAltDown()) {
			midDrawY -= c * 2;
			midDrawY = (midDrawY < 0) ? 0 : (midDrawY > (actualWorld.getHeight()) - 1) ? actualWorld.getHeight() - 1 : midDrawY;
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (drawingMousePos = mouseInMap && getTopParent().getModifyingObject() == null) {
			toolHandler.handleToolPress(button);
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		if (!firstPress && button == 0) {
			firstPress = true;
		}
		
		if (drawingMousePos = mouseInMap && getTopParent().getModifyingObject() == null) {
			toolHandler.handleToolRelease(button);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (Keyboard.isCtrlS(keyCode)) saveWorld();
		if (Keyboard.isCtrlR(keyCode)) {
			loadWorld();
			saveString = "Reloaded!";
			saveStringColor = EColors.lgreen;
			recentlySaved = true;
			saveStringTimeout = System.currentTimeMillis();
		}
		
		if (keyCode == Keyboard.KEY_ESC) {
			if (!hasSaved && hasBeenModified) {
				
			}
			
			if (!screenHistory.isEmpty()) closeScreen();
		}
	}
	
	@Override public void actionPerformed(IActionObject object, Object... args) {}
	
	@Override
	public void onGameTick(long ticks) {
		updateMovement();
		if (hasFocus()) {
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
		if (QoT.getTopRenderer().hasFocus()) { return; }
		if (System.currentTimeMillis() - timeSinceKey < 37) { return; }
		
		if (!Keyboard.isCtrlDown()) {
			if (Keyboard.isWDown() || Keyboard.isKeyDown(Keyboard.KEY_UP)) { midDrawY--; midDrawY = (midDrawY < 0) ? 0 : midDrawY; }
			if (Keyboard.isADown() || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) { midDrawX--; midDrawX = (midDrawX < 0) ? 0 : midDrawX; }
			if (Keyboard.isSDown() || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) { midDrawY++; midDrawY = (midDrawY > (actualWorld.getHeight() - 1)) ? actualWorld.getHeight() - 1 : midDrawY; }
			if (Keyboard.isDDown() || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) { midDrawX++; midDrawX = (midDrawX > (actualWorld.getWidth() - 1)) ? actualWorld.getWidth() - 1 : midDrawX; }
			
			timeSinceKey = System.currentTimeMillis();
		}
	}
	
	/** Draws the world tiles of the map. */
	private void drawMap(double x, double y, double w, double h) {
		//only update values if needed
		//if (updateMap || midDrawX != oldWorldX || midDrawY != oldWorldY) {
			left = NumberUtil.clamp(midDrawX - drawDistX, 0, actualWorld.getWidth() - 1);
			top = NumberUtil.clamp(midDrawY - drawDistY, 0, actualWorld.getHeight() - 1);
			right = NumberUtil.clamp(midDrawX + drawDistX, left, actualWorld.getWidth() - 1);
			bot = NumberUtil.clamp(midDrawY + drawDistY, top, actualWorld.getHeight() - 1);
			dw = right - left; //draw width
			dh = bot - top; //draw height
			updateMap = false;
		//}
		
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				//WorldTile t = world.getWorldData()[i][j];
				WorldTile t = editorWorld.get(i, j).getTile();
				if (t == null) continue;
				if (!t.hasTexture()) continue;
				
				double drawPosX = x;
				double drawPosY = y;
				
				if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
				if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
				
				double dX = drawPosX + (ix * w);
				double dY = drawPosY + (jy * h);
				
				//System.out.println(dX + " : " + dY);
				//System.out.println(drawPosX + " : " + drawPosY);
				boolean mouseOver = false;
				if (settings.drawWallBox) mouseOver = Mouse.getMx() >= dX &&
													  Mouse.getMx() < (dX + w) &&
													  Mouse.getMy() >= dY &&
													  Mouse.getMy() < (dY + h);
				
				t.renderTile(actualWorld, dX, dY, w, h, 0xffffffff, mouseOver);
			}
		}
	}
	
	private void renderEntities(double x, double y, double w, double h) {
		EArrayList<EntitySpawn> spawns = actualWorld.getEntitySpawns();
		EArrayList<Entity> entities = new EArrayList<>();
		for (var s : spawns) entities.add(s.getEntity(actualWorld));
		entities.sort(Comparator.comparingInt(e -> e.endY));
		
		for (int i = 0; i < entities.size(); i++) {
			Entity ent = entities.get(i);
			GameTexture tex = ent.getTexture();
			if (tex == null) continue;
			
			//transform the world coordinates of the entity to screen x/y coordinates
			//then translate to the middle drawn world tile
			double drawX = x + w * (ent.worldX + drawDistX - midDrawX);
			double drawY = y + h * (ent.worldY + drawDistY - midDrawY);
			
			//calculate the entity's draw width and height based off of actual dims and zoom
			double drawW = ent.width * actualWorld.getZoom();
			double drawH = ent.height * actualWorld.getZoom();
			
			//draw the entity on top of the tile it's on (elevated if it's a wall)
			if (ent.worldX >= 0 && ent.worldX < actualWorld.getWidth() &&
				ent.worldY >= 0 && ent.worldY < actualWorld.getHeight())
			{
				WorldTile tileUnderEntity = actualWorld.getTileAt(ent.worldX, ent.worldY);
				if (tileUnderEntity != null && tileUnderEntity.isWall()) {
					var wallHeight = tileUnderEntity.getWallHeight() * h;
					drawY -= wallHeight;
				}
			}
			
			//render the entity
			//ent.renderObject(drawX, drawY, drawW, drawH);
			drawTexture(tex, drawX, drawY, drawW, drawH);
			
			if (settings.drawEntityHitBoxes) {
				drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
			}
		}
	}
	
	//------------
	// Load World
	//------------
	
	public void loadWorld() {
		boolean center = actualWorld == null || !firstPress;
		
		if (mapFile != null) {
			double oldZoom = 1;
			if (actualWorld != null) oldZoom = actualWorld.getZoom();
			actualWorld = new GameWorld(mapFile);
			actualWorld.setZoom(oldZoom);
		}
		else if (actualWorld != null) {
			double oldZoom = actualWorld.getZoom();
			mapFile = actualWorld.getWorldFile();
			actualWorld.setZoom(oldZoom);
		}
		
		//create editor world
		editorWorld = new GameWorld(actualWorld);
		
		//wrap world into editor objects
		for (int i = 0; i < actualWorld.getHeight(); i++) {
			for (int j = 0; j < actualWorld.getWidth(); j++) {
 				EditorObject obj = EditorObject.of(actualWorld.getTileAt(i, j));
 				editorWorld.setTileAt(i, j, obj);
 			}
		}
		
		//wrap entities in world into editor objects
		entitiesInWorld = new EArrayList<>(actualWorld.getEntitySpawns().size());
		for (var spawn : actualWorld.getEntitySpawns()) {
			var ent = spawn.getEntity(actualWorld);
			EditorObject obj = EditorObject.of(ent);
			entitiesInWorld.add(obj);
		}
		
		if (center) {
			midDrawX = actualWorld.getWidth() / 2;
			midDrawY = actualWorld.getHeight() / 2;
			drawDistX = (int) (actualWorld.getWidth() / getZoom());
			drawDistY = (int) (actualWorld.getHeight() / getZoom());
		}
		
		loading = false;
	}
	
	//------------
	// Save World
	//------------
	
	public void saveWorld() {
		if (actualWorld != null) {
			
			//copy editor world data to world
			for (int i = 0; i < actualWorld.getHeight(); i++) {
				for (int j = 0; j < actualWorld.getWidth(); j++) {
					actualWorld.setTileAt(i, j, editorWorld.get(i, j).getTile());
				}
			}
			
			//copy editor entity data to world
			actualWorld.getEntitySpawns().clear();
			for (EditorObject entity : entitiesInWorld) {
				GameObject obj = entity.getGameObject();
				if (obj instanceof Entity e) {
					actualWorld.addEntitySpawn(e);
				}
			}
			
			if (actualWorld.saveWorldToFile()) {
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
		double drawPosEndX = x + (drawDistX - midDrawX + actualWorld.getWidth()) * w;
		double drawPosEndY = y + (drawDistY - midDrawY + actualWorld.getHeight()) * h;
		
		//shift over in terms of midDrawX/Y offset in relation to map render distance
		if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
		if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
		
		drawHRect(drawPosX, drawPosY, drawPosEndX, drawPosEndY, 1, EColors.red);
	}
	
	private void drawRegions(double x, double y, double w, double h) {
		actualWorld.getRegionData().forEach(r -> drawRegion(r, x, y, w, h));
	}
	
	private void drawRegion(Region r, double x, double y, double w, double h) {
		double z = actualWorld.getZoom();
		double sX = (r.startX * z);
		double sY = (r.startY * z);
		double rw = (r.width * z);
		double rh = (r.height * z);
		double drawPosX = x + sX - ((midDrawX - drawDistX) * w);
		double drawPosY = y + sY - ((midDrawY - drawDistY) * h);
		double drawWidth = drawPosX + rw;
		double drawHeight = drawPosY + rh;
		double lineWidth = NumberUtil.clamp((3 * z), 1, 4);
		drawHRect(drawPosX, drawPosY, drawWidth, drawHeight, lineWidth, r.getColor());
	}
	
	private boolean checkMousePos(double x, double y, double w, double h, double mXIn, double mYIn) {
		double xCheck = mXIn - x - ((drawDistX - midDrawX) * w);
		double yCheck = mYIn - y - ((drawDistY - midDrawY) * h);
		
		if (xCheck >= 0 && yCheck >= 0) {
			worldXPos = (int) (xCheck / w);
			worldYPos = (int) (yCheck / h);
			boolean inMap = worldXPos >= 0 && worldXPos < actualWorld.getWidth() && worldYPos >= 0 && worldYPos < actualWorld.getHeight();
			
			return mouseOver && inMap && EDimension.of(x, y, x + w + (drawDistX * 2 * w), y + h + (drawDistY * 2 * h)).contains(mXIn, mYIn);
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
		worldXPos = NumberUtil.clamp(worldXPos, 0, actualWorld.getWidth() - 1);
		worldYPos = NumberUtil.clamp(worldYPos, 0, actualWorld.getHeight() - 1);
		
		double xPos = x + w * (worldXPos + drawDistX - midDrawX);
		double yPos = y + h * (worldYPos + drawDistY - midDrawY);
		
		if (!settings.drawWallBox) drawHRect(xPos, yPos, xPos + w, yPos + h, 1, EColors.chalk);
	}
	
	private void updateSaveString(double drawAreaMidX, double drawAreaMidY) {
		double sw = FontRenderer.getStringWidth(saveString);
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
		if (actualWorld != null) {
			double w = actualWorld.getTileWidth() * actualWorld.getZoom();
			double h = actualWorld.getTileHeight() * actualWorld.getZoom();
			
			double dw = ((QoT.getWidth() - sidePanel.width) / w) / 2.0;
			double dh = ((QoT.getHeight() - topHeader.height) / h) / 2.0;
			
			//System.out.println(dw + " : " + dh);
			
			drawDistX = (int) Math.round(dw);
			drawDistY = (int) Math.ceil(dh);
			
			updateMap = true;
		}
	}
	
	//---------------------------------
	//         Public Getters
	//---------------------------------
	
	public EDimension getMapDrawDims() {
		double sx = 0.0;
		double sy = this.topHeader.endY;
		double ex = this.sidePanel.startX;
		double ey = endY;
		return new EDimension(sx, sy, ex, ey);
	}
	
	public int getXPos() { return midDrawX; }
	public int getYPos() { return midDrawY; }
	public int getViewX() { return drawDistX; }
	public int getViewY() { return drawDistY; }
	public int getWorldMX() { return worldXPos; }
	public int getWorldMY() { return worldYPos; }
	
	public double getZoom() { return (actualWorld != null) ? actualWorld.getZoom() : 1; }
	
	public GameWorld getWorld() { return actualWorld; }
	public boolean isMouseInMap() { return mouseInMap; }
	
	public WorldTile setTileAt(int x, int y, WorldTile in) {
		editorWorld.set(EditorObject.of(in), x, y);
		//world.setTileAt(x, y, in);
		hasBeenModified = true;
		hasSaved = false;
		return in;
	}
	
	public WorldTile setTileAtMouse(WorldTile in) {
		if (mouseInMap) {
			editorWorld.set(EditorObject.of(in), worldXPos, worldYPos);
			//world.setTileAt(worldXPos, worldYPos, in);
			hasBeenModified = true;
			hasSaved = false;
		}
		return in;
	}
	
	public WorldTile getTileHoveringOver() {
		if (actualWorld != null) {
			return editorWorld.get(worldXPos, worldYPos).getTile();
		}
		return null;
	}
	
	public boolean shouldDrawMouse() { return drawingMousePos; }
	
	public MapEditorSettings getSettings() { return settings; }
	public EditorScreenTopHeader getTopHeader() { return topHeader; }
	public EditorToolBox getToolBox() { return toolBox; }
	public EditorSidePanel getSidePanel() { return sidePanel; }
	public EditorToolType getCurrentTool() { return settings.getCurrentTool(); }
	public SidePanelType getCurrentSidePanel() { return sidePanel.getCurrentPanelType(); }
	
	
	//---------------------------------
	//         Public Setters
	//---------------------------------
	
	public MapEditorScreen setXPos(int pos) { return this; }
	public MapEditorScreen setYPos(int pos) { return this; }
	
	public MapEditorScreen setViewX(int dist) { drawDistX = NumberUtil.clamp(dist, 0, dist); return this; }
	public MapEditorScreen setViewY(int dist) { drawDistY = NumberUtil.clamp(dist, 0, dist); return this; }
	
	
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
	
	public EArrayList<EditorObject> getSelectedObjects() { return selectedObjects; }
	
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
