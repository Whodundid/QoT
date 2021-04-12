package gameScreens.mapEditor.editorScreen;

import assets.sounds.Songs;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.input.Keyboard;
import envisionEngine.input.Mouse;
import gameScreens.mapEditor.editorScreen.botHeader.EditorScreenBotHeader;
import gameScreens.mapEditor.editorScreen.tileTools.EditorTool;
import gameScreens.mapEditor.editorScreen.tileTools.EditorToolList;
import gameScreens.mapEditor.editorScreen.topHeader.EditorScreenTopHeader;
import gameScreens.mapEditor.editorScreen.util.EditorItem;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.mapSystem.worldTiles.WorldTiles;
import gameSystems.screenSystem.GameScreen;
import java.io.File;
import mathUtil.NumberUtil;
import org.lwjgl.glfw.GLFW;
import renderUtil.EColors;
import storageUtil.EDimension;

public class MapEditorScreen extends GameScreen {

	File mapFile;
	GameWorld world;
	
	EditorHotbar hotbar;
	EditorToolList tileTools;
	EditorScreenTopHeader topHeader;
	EditorScreenBotHeader botHeader;
	EditorMiniMap miniMap;
	ToolHandler toolHandler;
	
	EditorTool curTool = EditorTool.PENCIL;
	
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
		loadWorld();
		firstPress = !Mouse.isButtonDown(0);
	}
	
	@Override
	public void initObjects() {
		topHeader = new EditorScreenTopHeader(this);
		botHeader = new EditorScreenBotHeader(this);
		miniMap = new EditorMiniMap(this);
		tileTools = new EditorToolList(this);
		hotbar = new EditorHotbar(this);
		toolHandler = new ToolHandler(this);
		
		int i = 0;
		for (WorldTile t : WorldTiles.getTiles()) {
			if (t.hasTexture()) {
				hotbar.setItemAtPos(t, i);
				i++;
			}
		}
		
		addObject(topHeader);
		addObject(miniMap);
		addObject(botHeader);
		addObject(hotbar);
		addObject(tileTools);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		//System.out.println(EColors.yellow.intVal);
		
		drawRect(EColors.vdgray);
		if (hasFocus()) { updateMovement(); }
		mouseOver = isMouseOver();
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else {
			
			int w = (int) (world.getTileWidth() * world.getZoom());
			int h = (int) (world.getTileHeight() * world.getZoom());
			int x = (int) (midX - (distX * w) - (w / 2));
			int y = (int) (midY - (distY * h) - (h / 2));
			
			int deX = x + w + (distX * 2 * w);
			int deY = y + h + (distY * 2 * h);
			
			scissor(x, y, deX, deY + 1);
			{
				drawMap(x, y, w, h);
				if (drawRegions) { drawRegions(x, y, w, h); }
				if (drawPosBox) { drawPosBox(x, y, w, h); }
				
				mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
				if (drawingMousePos = mouseInMap) {
					drawMouseCoords(x, y, w, h);
					if (firstPress && getTopParent().getModifyingObject() == null) {
						toolHandler.handleToolUpdate(getCurTileTool());
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
			world.setZoom(world.getZoom() + NumberUtil.round(Math.signum(change) * 0.05, 2));
			world.setZoom(NumberUtil.clamp(world.getZoom(), 0.15, 5));
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
			hotbar.scrollHotbar(change);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		super.mousePressed(mXIn, mYIn, button);
		if (drawingMousePos = mouseInMap && getTopParent().getModifyingObject() == null) {
			toolHandler.handleToolPress(getCurTileTool(), button);
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		if (!firstPress && button == 0) {
			firstPress = true;
		}
		
		if (drawingMousePos = mouseInMap && getTopParent().getModifyingObject() == null) {
			toolHandler.handleToolRelease(getCurTileTool(), button);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		hotbar.keyPressed(typedChar, keyCode);
		
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
		if (System.currentTimeMillis() - timeSinceKey >= 37) {
			
			if (!Keyboard.isCtrlDown()) {
				if (Keyboard.isWDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) { yPos--; yPos = (yPos < 0) ? 0 : yPos; }
				if (Keyboard.isADown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) { xPos--; xPos = (xPos < 0) ? 0 : xPos; }
				if (Keyboard.isSDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) { yPos++; yPos = (yPos > (world.getHeight() - 1)) ? world.getHeight() - 1 : yPos; }
				if (Keyboard.isDDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { xPos++; xPos = (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos; }
				
				timeSinceKey = System.currentTimeMillis();
			}
			
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
	
	private void drawMap(int x, int y, int w, int h) {
		WorldTile[][] tiles = world.getTilesAroundPoint(xPos, yPos, distX, distY);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				WorldTile t = tiles[i][j];
				if (t != null) {
					
					int drawPosX = x;
					int drawPosY = y;
					
					if (xPos < distX) { drawPosX += (distX - xPos) * w; }
					if (yPos < distY) { drawPosY += (distY - yPos) * h; }
					
					if (t.hasTexture()) {
						drawTexture(drawPosX + (i * w), drawPosY + (j * h), w, h, t.getTexture());
					}
				}
			}
		}
	}
	
	private void drawPosBox(int x, int y, int w, int h) {
		int sX = x + (distX * w);
		int sY = y + (distY * h);
		int eX = sX + w;
		int eY = sY + h;
		drawHRect(sX, sY, eX, eY, 2, EColors.red);
	}
	
	private void drawViewBox(int x, int y, int w, int h) {
		int dsX = x;
		int dsY = y;
		int deX = x + w + (distX * 2 * w);
		int deY = y + h + (distY * 2 * h);
		drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
	}
	
	private void drawRegions(int x, int y, int w, int h) {
		world.getHighlightedRegions().forEach(r -> drawRegion(r, x, y, w, h));
		world.getRegionData().forEach(r -> drawRegion(r, x, y, w, h));
	}
	
	private void drawRegion(Region r, int x, int y, int w, int h) {
		double z = world.getZoom();
		int sX = (int) (r.startX * z);
		int sY = (int) (r.startY * z);
		int rw = (int) (r.width * z);
		int rh = (int) (r.height * z);
		int drawPosX = x + sX - ((xPos - distX) * w);
		int drawPosY = y + sY - ((yPos - distY) * h);
		int drawWidth = drawPosX + rw;
		int drawHeight = drawPosY + rh;
		int lineWidth = NumberUtil.clamp((int) (3 * z), 1, 4);
		drawHRect(drawPosX, drawPosY, drawWidth, drawHeight, lineWidth, r.getColor());
	}
	
	private boolean checkMousePos(int x, int y, int w, int h, int mXIn, int mYIn) {
		int tW = (int) (FontRenderer.getInstance().getStringWidth(world.getName()) / 2);
		drawRect(midX - tW - 8, 7, midX + tW + 8, 43, EColors.black);
		drawRect(midX - tW - 7, 8, midX + tW + 7, 42, EColors.dgray);
		
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
		worldXPos = (int) worldXPos;
		worldYPos = (int) worldYPos;
		worldXPos = NumberUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumberUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		
		double xPos = x + ((mX - x) / w) * w;
		double yPos = y + ((mY - y) / h) * h;
		
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
	
	public EditorItem getCurItem() { return hotbar.getCurrent(); }
	
	public double getZoom() { return (world != null) ? world.getZoom() : 1; }
	
	public GameWorld getWorld() { return world; }
	
	public void setCurrentTool(EditorTool toolIn) {
		curTool = toolIn;
		topHeader.updateCurTool(curTool);
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
	
	public boolean shouldDrawMouse() { return drawingMousePos; }
	
	public EditorScreenTopHeader getTopHeader() { return topHeader; }
	public EditorScreenBotHeader getBotHeader() { return botHeader; }
	public EditorHotbar getHotbar() { return hotbar; }
	public EditorToolList getTileToolList() { return tileTools; }
	public EditorMiniMap getMiniMap() { return miniMap; }
	
	//---------------------------------
	//         Public Setters
	//---------------------------------
	
	public MapEditorScreen setXPos(int pos) { return this; }
	public MapEditorScreen setYPos(int pos) { return this; }
	
	public MapEditorScreen setViewX(int dist) { distX = NumberUtil.clamp(dist, 0, dist); return this; }
	public MapEditorScreen setViewY(int dist) { distY = NumberUtil.clamp(dist, 0, dist); return this; }
	
	public EditorTool getCurTileTool() { return curTool; }
	
}
