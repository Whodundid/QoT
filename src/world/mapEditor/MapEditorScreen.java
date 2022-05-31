package world.mapEditor;

import engine.input.Keyboard;
import engine.input.Mouse;
import engine.screens.screenUtil.GameScreen;
import engine.soundEngine.SoundEngine;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import eutil.colors.EColors;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import main.QoT;
import world.GameWorld;
import world.Region;
import world.mapEditor.editorParts.sidePanel.EditorSidePanel;
import world.mapEditor.editorParts.sidePanel.SidePanelType;
import world.mapEditor.editorParts.toolBox.EditorToolBox;
import world.mapEditor.editorParts.topHeader.EditorScreenTopHeader;
import world.mapEditor.editorTools.EditorToolType;
import world.mapEditor.editorTools.ToolHandler;
import world.resources.WorldTile;

import java.io.File;

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
	
	public int midDrawX, midDrawY; //the world coordinates at the center of the screen
	public int drawDistX = 20;
	public int drawDistY = 15;
	public int worldXPos, worldYPos; //the world coordinates under the mouse
	public int oldWorldX, oldWorldY;
	public boolean updateMap = false;
	
	private boolean loading = false;
	
	//---------------------
	int left;
	int top;
	int right;
	int bot;
	int dw; //draw width
	int dh; //draw height
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
		
		settings.currentTool = EditorToolType.SELECTOR;
	}
	
	@Override
	public void initObjects() {
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
		
		if (world == null || !world.isFileLoaded()) { drawStringC("Failed to load!", midX, midY); }
		else {
			double tileDrawWidth = (world.getTileWidth() * world.getZoom()); //pixel width of each tile
			double tileDrawHeight = (world.getTileHeight() * world.getZoom()); //pixel height of each tile
			double drawAreaMidX = (QoT.getWidth() - sidePanel.width) / 2; //the middle x of the map draw area
			double drawAreaMidY = topHeader.endY + (QoT.getHeight() - topHeader.height) / 2; //the middle y of the map draw area
			double mapDrawStartX = (drawAreaMidX - (drawDistX * tileDrawWidth) - (tileDrawWidth / 2)); //the left most x coordinate for map drawing
			double mapDrawStartY = (drawAreaMidY - (drawDistY * tileDrawHeight) - (tileDrawHeight / 2)); //the top most y coordinate for map drawing
			
			//converting to shorthand to reduce footprint
			double w = tileDrawWidth;
			double h = tileDrawHeight;
			double x = mapDrawStartX;
			double y = mapDrawStartY;
			
			//scissor(x, y, deX, deY + 1);
			//endScissor();
			
			drawMap(x, y, w, h);
			if (settings.drawRegions) { drawRegions(x, y, w, h); }
			if (settings.drawCenterPositionBox) { drawCenterPositionBox(x, y, w, h); }
			
			mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
			if (drawingMousePos = mouseInMap) {
				drawMouseCoords((int) x, (int) y, (int) w, (int) h);
				if (firstPress && getTopParent().getModifyingObject() == null) {
					//toolHandler.handleToolUpdate(getCurTileTool());
				}
			}
			
			if (settings.drawMapBorders) { drawMapBorders(x, y, w, h); }
			
			oldWorldX = midDrawX;
			oldWorldY = midDrawY;
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isCtrlDown()) {
			world.setZoom(world.getZoom() + NumberUtil.round(Math.signum(change) * 0.25, 2));
			world.setZoom(NumberUtil.clamp(world.getZoom(), 0.25, 5));
			updateDrawDist();
		}
		else if (Keyboard.isShiftDown()) {
			midDrawX -= Math.signum(change) * 2;
			midDrawX = (midDrawX < 0) ? 0 : (midDrawX > (world.getWidth() - 1)) ? world.getWidth() - 1 : midDrawX;
		}
		else if (Keyboard.isAltDown()) {
			midDrawY -= Math.signum(change) * 2;
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
		if (Keyboard.isCtrlS(keyCode)) { saveWorld(); System.out.println("saved"); }
		if (Keyboard.isCtrlR(keyCode)) { loadWorld(); System.out.println("reloaded"); }
		
		super.keyPressed(typedChar, keyCode);
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
			world.setZoom(oldZoom);
		}
		else if (world != null) {
			double oldZoom = world.getZoom();
			world.getHighlightedRegions().clear();
			mapFile = world.getWorldFile();
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
				
			}
			else {
				
			}
		}
	}
	
	/** Draws the world tiles of the map. */
	private void drawMap(double x, double y, double w, double h) {
		//only update values if needed
		if (updateMap || midDrawX != oldWorldX || midDrawY != oldWorldY) {
			left = NumberUtil.clamp(midDrawX - drawDistX, 0, world.getWidth() - 1);
			top = NumberUtil.clamp(midDrawY - drawDistY, 0, world.getHeight() - 1);
			right = NumberUtil.clamp(midDrawX + drawDistX, left, world.getWidth() - 1);
			bot = NumberUtil.clamp(midDrawY + drawDistY, top, world.getHeight() - 1);
			dw = right - left; //draw width
			dh = bot - top; //draw height
			updateMap = false;
		}
		
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				WorldTile t = world.getWorldData()[i][j];
				
				if (t != null) {
					
					double drawPosX = x;
					double drawPosY = y;
					
					if (midDrawX < drawDistX) { drawPosX += (drawDistX - midDrawX) * w; }
					if (midDrawY < drawDistY) { drawPosY += (drawDistY - midDrawY) * h; }
					
					if (t.hasTexture()) {
						drawTexture(t.getTexture(), drawPosX + (ix * w), drawPosY + (jy * h), w, h);
					}
				}
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
		if (midDrawX < drawDistX) { drawPosX += (drawDistX - midDrawX) * w; }
		if (midDrawY < drawDistY) { drawPosY += (drawDistY - midDrawY) * h; }
		
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
		//int tW = (int) (FontRenderer.getStringWidth(world.getName()) / 2);
		
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
	
	private void drawMouseCoords(int x, int y, int w, int h) {
		worldXPos = NumberUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumberUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		
		double xPos = (x + ((mX - x) / w) * w) - 1; //not sure why need to sub 1 here..
		double yPos = (y + ((mY - y) / h) * h);
		
		//drawString("POS: " + xPos + " : " + yPos, 150, 150);
		
		drawHRect(xPos, yPos, xPos + w, yPos + h, 1, EColors.chalk);
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
		return in;
	}
	
	public WorldTile setTileAtMouse(WorldTile in) {
		if (mouseInMap) {
			world.setTileAt(worldXPos, worldYPos, in);
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
