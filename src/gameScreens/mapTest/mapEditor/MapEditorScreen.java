package gameScreens.mapTest.mapEditor;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.mapSystem.worldTiles.WorldTiles;
import input.Keyboard;
import input.Mouse;
import java.io.File;
import org.lwjgl.glfw.GLFW;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.storageUtil.EDimension;

public class MapEditorScreen extends GameScreen {

	File mapFile;
	GameWorld world;
	WindowButton reload, save, back;
	WindowButton distUpX, distDownX;
	WindowButton distUpY, distDownY;
	EditorTileHotbar hotbar;
	
	boolean firstPress = false;
	int xPos, yPos;
	int distX = 20;
	int distY = 15;
	boolean mouseInMap = false;
	double worldXPos, worldYPos;
	int oldWorldX, oldWorldY;
	double zoomVal = 0.55;
	boolean mouseOver = false;
	
	String timeOutText;
	long timeStart;
	long timeOut = 2000;
	
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
		loadWorld();
	}
	
	@Override
	public void initObjects() {
		hotbar = new EditorTileHotbar(this);
		
		distDownY = new WindowButton(this, endX - 42, endY - 45, 40, 40, "-");
		distUpY = new WindowButton(this, distDownY.startX - 42, endY - 45, 40, 40, "+");
		
		distDownX = new WindowButton(this, distUpY.startX - 47, endY - 45, 40, 40, "-");
		distUpX = new WindowButton(this, distDownX.startX - 42, endY - 45, 40, 40, "+");
		
		reload = new WindowButton(this, 10, 10, 140, 40, "Reload");
		save = new WindowButton(this, reload.endX + 5, 10, 140, 40, "Save");
		back = new WindowButton(this, 5, endY - 45, 140, 40, "Back");
		
		int i = 0;
		for (WorldTile t : WorldTiles.getTiles()) {
			if (t.hasTexture()) {
				hotbar.setItemAtPos(t, i);
				i++;
			}
		}
		
		addObject(distUpX, distDownX);
		addObject(distUpY, distDownY);
		addObject(reload, save, back);
		addObject(hotbar);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		updateTimeOutText();
		updateMovement();
		mouseOver = isMouseOver(mXIn, mYIn);
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else {
			
			int w = (int) (world.getTileWidth() * zoomVal);
			int h = (int) (world.getTileHeight() * zoomVal);
			int x = (int) (midX - (distX * w) - (w / 2));
			int y = (int) (midY - (distY * h) - (h / 2));
			
			drawMap(x, y, w, h);
			drawPosBox(x, y, w, h);
			mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
			if (mouseInMap && mouseOver) {
				drawMouseCoords(x, y, w, h);
				changeTiles();
			}
			drawViewBox(x, y, w, h);
		}
		
		drawString("Pos: " + xPos + " " + yPos, reload.startX + 10, reload.endY + 20);
		drawString("Dist: " + distX + " " + distY, reload.startX + 10, reload.endY + 100);
		drawString("Zoom: " + NumUtil.roundD2(zoomVal), reload.startX + 10, reload.endY + 140);
	}
	
	private void updateMovement() {
		if (System.currentTimeMillis() - this.timeSinceKey >= 37) {
			
			if (Keyboard.isWDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) { yPos--; yPos = (yPos < 0) ? 0 : yPos; }
			if (Keyboard.isADown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) { xPos--; xPos = (xPos < 0) ? 0 : xPos; }
			if (Keyboard.isSDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) { yPos++; yPos = (yPos > (world.getHeight() - 1)) ? world.getHeight() - 1 : yPos; }
			if (Keyboard.isDDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { xPos++; xPos = (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos; }
			
			timeSinceKey = System.currentTimeMillis();
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isCtrlDown()) {
			zoomVal += NumUtil.round(Math.signum(change) * 0.05, 2);
			zoomVal = NumUtil.clamp(zoomVal, 0.15, 5);
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
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		//if (!firstPress && button == 0) { firstPress = true; }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!hotbar.hasFocus()) { hotbar.keyPressed(typedChar, keyCode); }
		hotbar.requestFocus();
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == distUpX) { distX++; }
		if (object == distDownX) { distX--; distX = (distX < 0) ? 0 : distX; }
		
		if (object == distUpY) { distY++; }
		if (object == distDownY) { distY--; distY = (distY < 0) ? 0 : distY; }
		
		if (object == reload) { loadWorld(); }
		if (object == save) { saveWorld(); }
		
		if (object == back) { closeScreen(); }
	}
	
	@Override public void onScreenClosed() {}
	
	//------------------------
	// Private Editor Methods
	//------------------------
	
	private void loadWorld() {
		if (mapFile != null) {
			world = new GameWorld(mapFile);
		}
		else if (world != null) {
			mapFile = world.getWorldFile();
		}
		
		xPos = world.getWidth() / 2;
		yPos = world.getHeight() / 2;
	}
	
	private void saveWorld() {
		if (world != null) {
			if (world.saveWorldToFile()) {
				timeOutText = "Saved!";
			}
			else {
				timeOutText = "Failed";
			}
			timeStart = System.currentTimeMillis();
		}
	}
	
	private void updateTimeOutText() {
		if (timeOutText != null) {
			drawString(timeOutText, save.endX + 40, save.startY + 10);
			
			if (System.currentTimeMillis() - timeStart >= timeOut) {
				timeOutText = null;
			}
		}
	}
	
	private void drawMap(int x, int y, int w, int h) {
		//if (xPos < distX) { xPos = distX; }
		//if (yPos < distY) { yPos = distY; }
		
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
	
	private boolean checkMousePos(int x, int y, int w, int h, int mXIn, int mYIn) {
		int tW = (int) (FontRenderer.getInstance().getStringWidth(world.getName()) / 2);
		drawRect(midX - tW - 8, 7, midX + tW + 8, 43, EColors.black);
		drawRect(midX - tW - 7, 8, midX + tW + 7, 42, EColors.dgray);
		drawStringC(world.getName(), midX, 15);
		
		drawString("Dims: " + world.getWidth() + " " + world.getHeight(), reload.startX + 10, reload.endY + 60);
		
		worldXPos = (double) (mXIn - x - ((distX - xPos) * w)) / w;
		worldYPos = (double) (mYIn - y - ((distY - yPos) * h)) / h;
		boolean inMap = worldXPos >= 0 && worldXPos < world.getWidth() && worldYPos > 0 && worldYPos < world.getHeight();
		
		return mouseOver && inMap && EDimension.of(x, y, x + w + (distX * 2 * w), y + h + (distY * 2 * h)).contains(mXIn, mYIn);
	}
	
	private void drawMouseCoords(int x, int y, int w, int h) {
		worldXPos = (int) worldXPos;
		worldYPos = (int) worldYPos;
		worldXPos = NumUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		drawString("World: " + worldXPos + " " + worldYPos, reload.startX + 10, reload.endY + 180);
		
		double xPos = x + ((mX - x) / w) * w;
		double yPos = y + ((mY - y) / h) * h;
		
		drawHRect(xPos, yPos, xPos + w, yPos + h, 1, EColors.vdgray);
	}
	
	private void changeTiles() {
		//if (firstPress) {
			if (Mouse.isButtonDown(0)) { world.setTileAt((int) worldXPos, (int) worldYPos, hotbar.getCurrent()); }
			else if (Mouse.isButtonDown(1)) { world.setTileAt((int) worldXPos, (int) worldYPos, null); }
		//}
	}
	
}
