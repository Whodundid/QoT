package envision.game.world.mapEditor;

import java.io.File;
import java.util.Comparator;

import envision.game.entity.Entity;
import envision.game.screens.GameScreen;
import envision.game.sounds.SoundEngine;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import envision.game.world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import envision.game.world.mapEditor.editorParts.sidePanel.SidePanelType;
import envision.game.world.mapEditor.editorParts.toolBox.EditorToolBox;
import envision.game.world.mapEditor.editorParts.topHeader.EditorScreenTopHeader;
import envision.game.world.mapEditor.editorParts.util.EditorItem;
import envision.game.world.mapEditor.editorTools.EditorToolType;
import envision.game.world.mapEditor.editorTools.ToolHandler;
import envision.game.world.worldTiles.WorldTile;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.renderEngine.textureSystem.GameTexture;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import game.QoT;

public class MapEditorScreen extends GameScreen {

	protected File mapFile;
	protected GameWorld world;
	
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
	
	public MapEditorScreen(File mapFileIn) {
		mapFile = mapFileIn;
		setDefaultDims();
	}
	
	public MapEditorScreen(GameWorld worldIn) {
		world = worldIn;
		setDefaultDims();
	}
	
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
		
		if (world == null || !world.isFileLoaded()) drawStringC("Failed to load!", midX, midY);
		else {
			tileDrawWidth = (world.getTileWidth() * world.getZoom()); //pixel width of each tile
			tileDrawHeight = (world.getTileHeight() * world.getZoom()); //pixel height of each tile
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
					//COMPLETELY HASHED TOGETHER :)
					EditorItem primary = settings.getPrimaryPalette();
					if (primary == null) break;
					GameTexture tex = primary.getTexture();
					double tW = primary.getGameObject().width;
					double tH = primary.getGameObject().height;
					double tDrawW = tW * world.getZoom();
					double tDrawH = tH * world.getZoom();
					drawTexture(tex, Mouse.getMx() - tW / 2, Mouse.getMy() - tH, tDrawW, tDrawH, 0x99ffffff);
					//drawTexture(tex, mX, mY, tDrawW, tDrawH);
					drawHoveredTileBox((int) x, (int) y, (int) w, (int) h);
					break;
				}
				default:
					drawHoveredTileBox((int) x, (int) y, (int) w, (int) h);
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
	
	/**
	 * Draws a box around the currently hovered tile.
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	private void drawHoveredTileBox(int x, int y, int w, int h) {
		worldXPos = NumberUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumberUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		
		double xPos = (x + ((Mouse.getMx() - x) / w) * w) - 1; //not sure why need to sub 1 here..
		double yPos = (y + ((Mouse.getMy() - y) / h) * h);
		
		//drawString("POS: " + xPos + " : " + yPos, 150, 150);
		
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
	
	@Override
	public void mouseScrolled(int change) {
		double c = Math.signum(change);
		double z = 1.0;
		
		if (Keyboard.isCtrlDown()) {
			if (c > 0 && world.getZoom() == 0.25) 	z = 0.05;		//if at 0.25 and zooming out -- 0.05x
			else if (world.getZoom() < 1.0) 		z = c * 0.1;	//if less than 1 zoom by 0.1x
			else if (c > 0) 						z = 0.25;		//if greater than 1 zoom by 0.25x
			else if (world.getZoom() == 1.0) 		z = c * 0.1;	//if at 1.0 and zooming in -- 0.1x
			else 									z = c * 0.25;	//otherwise always zoom by 0.25x
			
			z = NumberUtil.round(world.getZoom() + z, 2);
			world.setZoom(z);
			
			updateDrawDist();
		}
		else if (Keyboard.isShiftDown()) {
			midDrawX -= c * 2;
			midDrawX = (midDrawX < 0) ? 0 : (midDrawX > (world.getWidth() - 1)) ? world.getWidth() - 1 : midDrawX;
		}
		else if (Keyboard.isAltDown()) {
			midDrawY -= c * 2;
			midDrawY = (midDrawY < 0) ? 0 : (midDrawY > (world.getHeight()) - 1) ? world.getHeight() - 1 : midDrawY;
		}
	}
	
	private void updateDrawDist() {
		if (world != null) {
			double w = world.getTileWidth() * world.getZoom();
			double h = world.getTileHeight() * world.getZoom();
			
			double dw = ((QoT.getWidth() - sidePanel.width) / w) / 2.0;
			double dh = ((QoT.getHeight() - topHeader.height) / h) / 2.0;
			
			drawDistX = (int) Math.ceil(dw);
			drawDistY = (int) Math.ceil(dh);
			
			updateMap = true;
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
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override
	public void onScreenClosed() {
		SoundEngine.stopAll();
	}
	
	@Override
	public void onGameTick(long ticks) {
		updateMovement();
		if (hasFocus()) {
			toolHandler.handleToolUpdate();
		}
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
			if (Keyboard.isSDown() || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) { midDrawY++; midDrawY = (midDrawY > (world.getHeight() - 1)) ? world.getHeight() - 1 : midDrawY; }
			if (Keyboard.isDDown() || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) { midDrawX++; midDrawX = (midDrawX > (world.getWidth() - 1)) ? world.getWidth() - 1 : midDrawX; }
			
			timeSinceKey = System.currentTimeMillis();
		}
	}
	
	public void loadWorld() {
		boolean center = world == null || !firstPress;
		
		if (mapFile != null) {
			double oldZoom = 1;
			if (world != null) oldZoom = world.getZoom();
			world = new GameWorld(mapFile);
			world.spawnEntities();
			world.setZoom(oldZoom);
		}
		else if (world != null) {
			double oldZoom = world.getZoom();
			world.getHighlightedRegions().clear();
			mapFile = world.getWorldFile();
			world.spawnEntities();
			world.setZoom(oldZoom);
		}
		
		if (center) {
			midDrawX = world.getWidth() / 2;
			midDrawY = world.getHeight() / 2;
			drawDistX = (int) (world.getWidth() / getZoom());
			drawDistY = (int) (world.getHeight() / getZoom());
		}
		
		loading = false;
	}
	
	public void saveWorld() {
		if (world != null) {
			if (world.saveWorldToFile()) {
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
	
	/** Draws the world tiles of the map. */
	private void drawMap(double x, double y, double w, double h) {
		//only update values if needed
		//if (updateMap || midDrawX != oldWorldX || midDrawY != oldWorldY) {
			left = NumberUtil.clamp(midDrawX - drawDistX, 0, world.getWidth() - 1);
			top = NumberUtil.clamp(midDrawY - drawDistY, 0, world.getHeight() - 1);
			right = NumberUtil.clamp(midDrawX + drawDistX, left, world.getWidth() - 1);
			bot = NumberUtil.clamp(midDrawY + drawDistY, top, world.getHeight() - 1);
			dw = right - left; //draw width
			dh = bot - top; //draw height
			updateMap = false;
		//}
		
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				WorldTile t = world.getWorldData()[i][j];
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
				
				t.renderTile(world, dX, dY, w, h, 0xffffffff, mouseOver);
			}
		}
	}
	
	private void renderEntities(double x, double y, double w, double h) {
		var entities = world.getEntitiesInWorld();
		entities.sort(Comparator.comparingInt(e -> e.endY));
		
		for (int i = 0; i < entities.size(); i++) {
			Entity ent = entities.get(i);
			GameTexture tex = ent.getTexture();
			if (tex == null) continue;
			
			//transform the world coordinates of the entity to screen x/y coordinates
			double drawX = (ent.worldX * w) + x;
			double drawY = (ent.worldY * h) + y;
			
			//translate to the middle drawn world tile
			drawX += (drawDistX - midDrawX) * w;
			drawY += (drawDistY - midDrawY) * h;
			
			//calculate the entity's draw width and height based off of actual dims and zoom
			double drawW = ent.width * world.getZoom();
			double drawH = ent.height * world.getZoom();
			
			//draw the entity on top of the tile it's on (elevated if it's a wall)
			if (ent.worldX >= 0 && ent.worldX < world.getWidth() &&
				ent.worldY >= 0 && ent.worldY < world.getHeight())
			{
				WorldTile tileUnderEntity = world.getTileAt(ent.worldX, ent.worldY);
				if (tileUnderEntity != null && tileUnderEntity.isWall()) {
					var wallHeight = tileUnderEntity.getWallHeight() * h;
					drawY -= wallHeight;
				}
			}
			
			//render the entity
			ent.renderObject(drawX, drawY, drawW, drawH);
			
			if (settings.drawEntityHitBoxes) {
				drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
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
		double drawPosEndX = x + (drawDistX - midDrawX + world.getWidth()) * w;
		double drawPosEndY = y + (drawDistY - midDrawY + world.getHeight()) * h;
		
		//shift over in terms of midDrawX/Y offset in relation to map render distance
		if (midDrawX < drawDistX) drawPosX += (drawDistX - midDrawX) * w;
		if (midDrawY < drawDistY) drawPosY += (drawDistY - midDrawY) * h;
		
		drawHRect(drawPosX, drawPosY, drawPosEndX, drawPosEndY, 1, EColors.red);
	}
	
	private void drawRegions(double x, double y, double w, double h) {
		world.getHighlightedRegions().forEach(r -> drawRegion(r, x, y, w, h));
		world.getRegionData().forEach(r -> drawRegion(r, x, y, w, h));
	}
	
	private void drawRegion(Region r, double x, double y, double w, double h) {
		double z = world.getZoom();
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
			boolean inMap = worldXPos >= 0 && worldXPos < world.getWidth() && worldYPos >= 0 && worldYPos < world.getHeight();
			
			return mouseOver && inMap && EDimension.of(x, y, x + w + (drawDistX * 2 * w), y + h + (drawDistY * 2 * h)).contains(mXIn, mYIn);
		}
		return false;
	}
	
	//---------------------------------
	//         Public Getters
	//---------------------------------
	
	public int getXPos() { return midDrawX; }
	public int getYPos() { return midDrawY; }
	public int getViewX() { return drawDistX; }
	public int getViewY() { return drawDistY; }
	public int getWorldMX() { return worldXPos; }
	public int getWorldMY() { return worldYPos; }
	
	public double getZoom() { return (world != null) ? world.getZoom() : 1; }
	
	public GameWorld getWorld() { return world; }
	public boolean isMouseInMap() { return mouseInMap; }
	
	public WorldTile setTileAt(int x, int y, WorldTile in) {
		world.setTileAt(x, y, in);
		hasBeenModified = true;
		hasSaved = false;
		return in;
	}
	
	public WorldTile setTileAtMouse(WorldTile in) {
		if (mouseInMap) {
			world.setTileAt(worldXPos, worldYPos, in);
			hasBeenModified = true;
			hasSaved = false;
		}
		return in;
	}
	
	public WorldTile getTileHoveringOver() {
		if (world != null) {
			return world.getTileAt(worldXPos, worldYPos);
		}
		return null;
	}
	
	public boolean shouldDrawMouse() { return drawingMousePos; }
	
	public MapEditorSettings getSettings() { return settings; }
	public EditorScreenTopHeader getTopHeader() { return topHeader; }
	public EditorToolBox getToolBox() { return toolBox; }
	public EditorSidePanel getSidePanel() { return sidePanel; }
	
	//---------------------------------
	//         Public Setters
	//---------------------------------
	
	public MapEditorScreen setXPos(int pos) { return this; }
	public MapEditorScreen setYPos(int pos) { return this; }
	
	public MapEditorScreen setViewX(int dist) { drawDistX = NumberUtil.clamp(dist, 0, dist); return this; }
	public MapEditorScreen setViewY(int dist) { drawDistY = NumberUtil.clamp(dist, 0, dist); return this; }
	
}
