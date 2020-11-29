package gameScreens.mapTest;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IActionObject;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.worldTiles.WorldTile;
import input.Keyboard;
import input.Mouse;
import java.io.File;
import java.util.Stack;
import main.Game;
import org.lwjgl.glfw.GLFW;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.storageUtil.EDimension;

public class MapEditorScreen extends GameScreen {

	File mapFile;
	GameWorld world;
	WindowButton up, left, down, right;
	WindowButton reload, save, back;
	WindowButton distUpX, distDownX;
	WindowButton distUpY, distDownY;
	TileList tileSelector;
	
	boolean firstPress = false;
	int xPos, yPos;
	int distX = 15;
	int distY = 10;
	boolean mouseInMap = false;
	double worldXPos, worldYPos;
	int oldWorldX, oldWorldY;
	double zoomVal = 1.25;
	boolean mouseOver = false;
	
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
		left = new WindowButton(this, midX - 60, endY - 45, 40, 40, "<");
		down = new WindowButton(this, left.endX + 5, left.startY, 40, 40, "\\/");
		up = new WindowButton(this, down.startX, down.startY - 45, 40, 40, "^");
		right = new WindowButton(this, down.endX + 5, left.startY, 40, 40, ">");
		
		distUpX = new WindowButton(this, right.endX + 100, left.startY, 40, 40, "+");
		distDownX = new WindowButton(this, distUpX.endX + 5, left.startY, 40, 40, "-");
		
		distUpY = new WindowButton(this, distDownX.endX + 10, left.startY, 40, 40, "+");
		distDownY = new WindowButton(this, distUpY.endX + 5, left.startY, 40, 40, "-");
		
		reload = new WindowButton(this, 10, 10, 140, 40, "Reload");
		save = new WindowButton(this, reload.endX + 5, 10, 140, 40, "Save");
		back = new WindowButton(this, 10, endY - 45, 140, 40, "Back");
		
		tileSelector = new TileList(this, 0, midY - 150);
		
		addObject(up, left, down, right);
		addObject(distUpX, distDownX);
		addObject(distUpY, distDownY);
		addObject(reload, save, back);
		addObject(tileSelector);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		mouseOver = isMouseOver(mXIn, mYIn);
		checkArrowPress();
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else {
			
			int w = (int) (world.getTileWidth() * zoomVal);
			int h = (int) (world.getTileHeight() * zoomVal);
			int x = (int) (midX - (distX * w) - (w / 2));
			int y = (int) (midY - (distY * h) - (h / 2));
			
			drawMap(x, y, w, h);
			drawPosBox(x, y, w, h);
			drawViewBox(x, y, w, h);
			mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
			if (mouseInMap && mouseOver) {
				drawMouseCoords();
				changeTiles();
			}
		}
		
		drawString("Pos: " + xPos + " " + yPos, reload.startX + 10, reload.endY + 20);
		drawString("Dist: " + distX + " " + distY, reload.startX + 10, reload.endY + 100);
		drawString("Zoom: " + NumUtil.roundD2(zoomVal), reload.startX + 10, reload.endY + 140);
	}
	
	@Override
	public void mouseScrolled(int change) {
		if (Keyboard.isCtrlDown()) {
			zoomVal += NumUtil.round(Math.signum(change) * 0.05, 2);
			zoomVal = NumUtil.clamp(zoomVal, 0.15, 5);
		}
		else if (Keyboard.isShiftDown()) {
			xPos += Math.signum(change);
			xPos = (xPos < 0) ? 0 : (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos;
		}
		else if (Keyboard.isAltDown()) {
			yPos -= Math.signum(change);
			yPos = (yPos < 0) ? 0 : (yPos > (world.getHeight()) - 1) ? world.getHeight() - 1 : yPos;
		}
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		if (!firstPress && button == 0) { firstPress = true; }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (Keyboard.isWDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) { yPos--; yPos = (yPos < 0) ? 0 : yPos; }
		if (Keyboard.isADown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) { xPos--; xPos = (xPos < 0) ? 0 : xPos; }
		if (Keyboard.isSDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) { yPos++; yPos = (yPos > (world.getHeight() - 1)) ? world.getHeight() - 1 : yPos; }
		if (Keyboard.isDDown() || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { xPos++; xPos = (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos; }
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == up) { yPos--; yPos = (yPos < 0) ? 0 : yPos; }
		if (object == left) { xPos--; xPos = (xPos < 0) ? 0 : xPos; }
		if (object == down) { yPos++; yPos = (yPos > (world.getHeight() - 1)) ? world.getHeight() - 1 : yPos; }
		if (object == right) { xPos++; xPos = (xPos > (world.getWidth() - 1)) ? world.getWidth() - 1 : xPos; }
		
		if (object == distUpX) { distX++; }
		if (object == distDownX) { distX--; distX = (distX < 0) ? 0 : distX; }
		
		if (object == distUpY) { distY++; }
		if (object == distDownY) { distY--; distY = (distY < 0) ? 0 : distY; }
		
		if (object == reload) { loadWorld(); }
		if (object == save) { saveWorld(); }
		
		if (object == back) {
			Stack<GameScreen> hist = getScreenHistory();
			if (hist.peek() instanceof NewMapCreatorScreen) {
				Game.displayScreen(new NewMapCreatorScreen(world)).setScreenHistory(hist);
			}
			else {
				closeScreen();
			}
		}
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
			xPos = world.getWidth() / 2;
			yPos = world.getHeight() / 2;
		}
	}
	
	private void saveWorld() {
		if (mapFile != null) {
			world.saveWorldToFile(mapFile);
		}
		else if (world != null) {
			world.saveWorldToFile();
		}
	}
	
	private void checkArrowPress() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W) || Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) { up.setForceDrawHover(true); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A) || Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) { left.setForceDrawHover(true); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S) || Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) { down.setForceDrawHover(true); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { right.setForceDrawHover(true); }
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
	
	private void drawMouseCoords() {
		worldXPos = (int) worldXPos;
		worldYPos = (int) worldYPos;
		worldXPos = NumUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		drawString("World: " + worldXPos + " " + worldYPos, reload.startX + 10, reload.endY + 180);
	}
	
	private void changeTiles() {
		if (firstPress) {
			if (Mouse.isButtonDown(0)) { world.setTileAt((int) worldXPos, (int) worldYPos, tileSelector.getCurrentTile()); }
			else if (Mouse.isButtonDown(1)) { world.setTileAt((int) worldXPos, (int) worldYPos, null); }
		}
	}
	
}
