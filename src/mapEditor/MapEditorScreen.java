package mapEditor;

import assets.sounds.Songs;
import assets.worldTiles.WorldTile;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.screenSystem.GameScreen;
import input.Keyboard;
import input.Mouse;
import java.io.File;
import main.Game;
import mapEditor.editorParts.sidePanel.EditorSidePanel;
import mapEditor.editorParts.sidePanel.SidePanel;
import mapEditor.editorParts.sidePanel.SidePanelType;
import mapEditor.editorParts.toolBox.EditorToolBox;
import mapEditor.editorParts.topHeader.EditorScreenTopHeader;
import mapEditor.editorTools.EditorToolType;
import mathUtil.NumberUtil;
import renderUtil.EColors;
import storageUtil.EDimension;
import windowLib.windowTypes.interfaces.IActionObject;

public class MapEditorScreen extends GameScreen {

	File mapFile;
	GameWorld world;
	
	EditorToolBox toolBox;
	EditorScreenTopHeader topHeader;
	EditorSidePanel sidePanel;
	
	SidePanel currentPanel = null, lastPanel = null;
	EditorToolType curTool = EditorToolType.SELECTOR;
	
	boolean drawPosBox = true;
	boolean drawViewBox = true;
	boolean drawEntities = true;
	boolean drawRegions = true;
	
	boolean firstPress = false;
	int xPos, yPos;
	int distX = 20;
	int distY = 15;
	
	int worldXPos, worldYPos;
	boolean mouseInMap = false;
	boolean mouseOver = false;
	boolean drawingMousePos = false;
	
	long timeSinceKey = 0l;
	
	public MapEditorScreen(File mapFileIn) {
		super();
		mapFile = mapFileIn;
	}
	
	public MapEditorScreen(GameWorld worldIn) {
		super();
		world = worldIn;
	}
	
	@Override
	public void initScreen() {
		Songs.stopAllMusic();
		firstPress = !Mouse.isButtonDown(0);
		loadWorld();
	}
	
	@Override
	public void initObjects() {
		addObject(topHeader = new EditorScreenTopHeader(this));
		addObject(toolBox = new EditorToolBox(this));
		addObject(sidePanel = new EditorSidePanel(this));
		
		setCurrentPanel(SidePanelType.TERRAIN);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		drawRect(EColors.vdgray);
		if (hasFocus()) { updateMovement(); }
		mouseOver = isMouseOver();
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else {
			
			double w = (world.getTileWidth() * world.getZoom());
			double h = (world.getTileHeight() * world.getZoom());
			double x = (midX - (distX * w) - (w / 2));
			double y = (midY - (distY * h) - (h / 2));
			
			double deX = (x + w + (distX * 2 * w));
			double deY = (y + h + (distY * 2 * h));
			
			scissor(x, y, deX, deY + 1);
			{
				drawMap(x, y, w, h);
				if (drawRegions) { drawRegions(x, y, w, h); }
				if (drawPosBox) { drawPosBox(x, y, w, h); }
				
				mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
				if (drawingMousePos = mouseInMap) {
					drawMouseCoords((int) x, (int) y, (int) w, (int) h);
					if (firstPress && getTopParent().getModifyingObject() == null) {
						//toolHandler.handleToolUpdate(getCurTileTool());
					}
				}
				
				if (drawViewBox) { drawViewBox(x, y, w, h); }
			}
			endScissor();
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isCtrlDown()) {
			world.setZoom(world.getZoom() + NumberUtil.round(Math.signum(change) * 0.25, 2));
			world.setZoom(NumberUtil.clamp(world.getZoom(), 0.25, 5));
		}
		else if (Keyboard.isShiftDown()) {
			xPos -= Math.signum(change) * 2;
			xPos = (xPos < 0) ? 0 : (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos;
		}
		else if (Keyboard.isAltDown()) {
			yPos -= Math.signum(change) * 2;
			yPos = (yPos < 0) ? 0 : (yPos > (world.getHeight()) - 1) ? world.getHeight() - 1 : yPos;
		}
		else {
			//hotbar.scrollHotbar(change);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (drawingMousePos = mouseInMap && getTopParent().getModifyingObject() == null) {
			//toolHandler.handleToolPress(getCurTileTool(), button);
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		if (!firstPress && button == 0) {
			firstPress = true;
		}
		
		if (drawingMousePos = mouseInMap && getTopParent().getModifyingObject() == null) {
			//toolHandler.handleToolRelease(getCurTileTool(), button);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		//hotbar.keyPressed(typedChar, keyCode);
		
		if (Keyboard.isCtrlS(keyCode)) { saveWorld(); System.out.println("saved"); }
		if (Keyboard.isCtrlR(keyCode)) { loadWorld(); System.out.println("reloaded"); }
		
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	@Override public void onScreenClosed() { Songs.stopAllMusic(); }
	
	//------------------------
	// Private Editor Methods
	//------------------------
	
	private void updateMovement() {
		if (Game.getTopRenderer().hasFocus()) { return; }
		if (System.currentTimeMillis() - timeSinceKey < 37) { return; }
		
		if (!Keyboard.isCtrlDown()) {
			if (Keyboard.isWDown() || Keyboard.isKeyDown(Keyboard.KEY_UP)) { yPos--; yPos = (yPos < 0) ? 0 : yPos; }
			if (Keyboard.isADown() || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) { xPos--; xPos = (xPos < 0) ? 0 : xPos; }
			if (Keyboard.isSDown() || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) { yPos++; yPos = (yPos > (world.getHeight() - 1)) ? world.getHeight() - 1 : yPos; }
			if (Keyboard.isDDown() || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) { xPos++; xPos = (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos; }
			
			timeSinceKey = System.currentTimeMillis();
		}
	}
	
	public void loadWorld() {
		boolean center = world == null || !firstPress;
		
		if (mapFile != null) {
			double oldZoom = 1;
			if (world != null) { oldZoom = world.getZoom(); }
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
			xPos = world.getWidth() / 2;
			yPos = world.getHeight() / 2;
			distX = (int) (world.getWidth() / getZoom());
			distY =(int) (world.getHeight() / getZoom());
		}
	}
	
	public void saveWorld() {
		if (world != null) {
			if (world.saveWorldToFile()) {
				
			}
			else {
				
			}
		}
	}
	
	private void drawMap(double x, double y, double w, double h) {
		WorldTile[][] tiles = world.getTilesAroundPoint(xPos, yPos, distX, distY);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				WorldTile t = tiles[i][j];
				if (t != null) {
					
					double drawPosX = x;
					double drawPosY = y;
					
					if (xPos < distX) { drawPosX += (distX - xPos) * w; }
					if (yPos < distY) { drawPosY += (distY - yPos) * h; }
					
					if (t.hasTexture()) {
						drawTexture(drawPosX + (i * w), drawPosY + (j * h), w, h, t.getTexture());
					}
				}
			}
		}
	}
	
	private void drawPosBox(double x, double y, double w, double h) {
		double sX = x + (distX * w);
		double sY = y + (distY * h);
		double eX = sX + w;
		double eY = sY + h;
		//drawHRect(sX, sY, eX, eY, 2, EColors.red);
	}
	
	private void drawViewBox(double x, double y, double w, double h) {
		double dsX = x;
		double dsY = y;
		double deX = x + w + (distX * 2 * w);
		double deY = y + h + (distY * 2 * h);
		//drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
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
		double drawPosX = x + sX - ((xPos - distX) * w);
		double drawPosY = y + sY - ((yPos - distY) * h);
		double drawWidth = drawPosX + rw;
		double drawHeight = drawPosY + rh;
		double lineWidth = NumberUtil.clamp((3 * z), 1, 4);
		drawHRect(drawPosX, drawPosY, drawWidth, drawHeight, lineWidth, r.getColor());
	}
	
	private boolean checkMousePos(double x, double y, double w, double h, double mXIn, double mYIn) {
		int tW = (int) (FontRenderer.getInstance().getStringWidth(world.getName()) / 2);
		
		double xCheck = mXIn - x - ((distX - xPos) * w);
		double yCheck = mYIn - y - ((distY - yPos) * h);
		
		if (xCheck >= 0 && yCheck >= 0) {
			worldXPos = (int) (xCheck / w);
			worldYPos = (int) (yCheck / h);
			boolean inMap = worldXPos >= 0 && worldXPos < world.getWidth() && worldYPos >= 0 && worldYPos < world.getHeight();
			
			return mouseOver && inMap && EDimension.of(x, y, x + w + (distX * 2 * w), y + h + (distY * 2 * h)).contains(mXIn, mYIn);
		}
		return false;
	}
	
	private void drawMouseCoords(int x, int y, int w, int h) {
		worldXPos = NumberUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumberUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		
		double xPos = (x + ((mX - x) / w) * w);
		double yPos = (y + ((mY - y) / h) * h);
		
		//drawString("POS: " + xPos + " : " + yPos, 150, 150);
		
		drawHRect(xPos, yPos, xPos + w, yPos + h, 1, EColors.vdgray);
	}
	
	//---------------------------------
	//         Public Getters
	//---------------------------------
	
	public int getXPos() { return xPos; }
	public int getYPos() { return yPos; }
	public int getViewX() { return distX; }
	public int getViewY() { return distY; }
	public int getWorldMX() { return worldXPos; }
	public int getWorldMY() { return worldYPos; }
	
	public double getZoom() { return (world != null) ? world.getZoom() : 1; }
	
	public GameWorld getWorld() { return world; }
	
	public void setCurrentTool(EditorToolType toolIn) {
		curTool = toolIn;
	}
	
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
	
	public EditorToolType getCurTileTool() { return curTool; }
	public boolean shouldDrawMouse() { return drawingMousePos; }
	
	public EditorScreenTopHeader getTopHeader() { return topHeader; }
	public EditorToolBox getToolBox() { return toolBox; }
	public EditorSidePanel getSidePanel() { return sidePanel; }
	public SidePanel getCurrentPanel() { return currentPanel; }
	public SidePanel getLastPanel() { return lastPanel; }
	
	//---------------------------------
	//         Public Setters
	//---------------------------------
	
	public MapEditorScreen setXPos(int pos) { return this; }
	public MapEditorScreen setYPos(int pos) { return this; }
	
	public MapEditorScreen setViewX(int dist) { distX = NumberUtil.clamp(dist, 0, dist); return this; }
	public MapEditorScreen setViewY(int dist) { distY = NumberUtil.clamp(dist, 0, dist); return this; }
	
	public MapEditorScreen setCurrentPanel(SidePanelType type) { sidePanel.setActivePanel(type); topHeader.updateTool(); return this; }
	public MapEditorScreen setSidePanel(SidePanel toolIn) { currentPanel = toolIn; return this; }
	public MapEditorScreen setLastPanel(SidePanel toolIn) { lastPanel = toolIn; return this; }
	
}
