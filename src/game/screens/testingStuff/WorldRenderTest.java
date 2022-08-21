package game.screens.testingStuff;

import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import game.QoT;
import game.entities.player.QoT_Player;

import java.io.File;
import java.util.Stack;

import org.lwjgl.glfw.GLFW;

import envision.game.entity.Entity;
import envision.game.screens.GameScreen;
import envision.game.world.GameWorld;
import envision.game.world.mapEditor.NewMapCreatorScreen;
import envision.game.world.worldTiles.WorldTile;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowTypes.interfaces.IActionObject;

public class WorldRenderTest extends GameScreen {

	File mapFile;
	GameWorld world;
	WindowButton up, left, down, right;
	WindowButton reload, back;
	WindowButton distUpX, distDownX;
	WindowButton distUpY, distDownY;
	
	int distX = 10;
	int distY = 8;
	
	double worldXPos, worldYPos;
	int oldWorldX, oldWorldY;
	
	boolean firstPress = false;
	boolean drawPosBox = false;
	boolean drawEntityHitboxes = true;
	boolean drawEntityOutlines = false;
	
	boolean mouseInMap = false;
	boolean mouseOver = false;
	
	WorldTile[][] tiles;
	
	public WorldRenderTest(File mapFileIn) {
		super();
		mapFile = mapFileIn;
	}
	
	public WorldRenderTest(GameWorld worldIn) {
		super();
		world = worldIn;
	}
	
	@Override
	public void initScreen() {
		loadWorld();
	}
	
	@Override
	public void initChildren() {
		left = new WindowButton(this, midX - 60, endY - 45, 40, 40, "<");
		down = new WindowButton(this, left.endX + 5, left.startY, 40, 40, "V");
		up = new WindowButton(this, down.startX, down.startY - 45, 40, 40, "^");
		right = new WindowButton(this, down.endX + 5, left.startY, 40, 40, ">");
		
		distUpX = new WindowButton(this, right.endX + 100, left.startY, 40, 40, "+");
		distDownX = new WindowButton(this, distUpX.endX + 5, left.startY, 40, 40, "-");
		
		distUpY = new WindowButton(this, distDownX.endX + 10, left.startY, 40, 40, "+");
		distDownY = new WindowButton(this, distUpY.endX + 5, left.startY, 40, 40, "-");
		
		reload = new WindowButton(this, 10, 10, 140, 40, "Reload");
		back = new WindowButton(this, 10, endY - 45, 140, 40, "Back");
		
		addObject(up, left, down, right);
		addObject(distUpX, distDownX);
		addObject(distUpY, distDownY);
		addObject(reload, back);
	}

	@Override
	public void drawScreen(int mXIn, int mYIn) {
		mouseOver = isMouseOver();
		
		checkArrowPress();
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else if (world.getWidth() < 0 || world.getHeight() < 0 || world.getTileWidth() <= 0 || world.getTileHeight() <= 0) { drawStringC("Bad world dimensions!", midX, midY); }
		else {
			
			int w = (int) (world.getTileWidth() * world.getZoom());
			int h = (int) (world.getTileHeight() * world.getZoom());
			
			int x = (int) (midX - (distX * w) - (w / 2));
			int y = (int) (midY - (distY * h) - (h / 2));
			
			//scissor(x, y, x + w + (distX * 2 * w), y + h + (distY * 2 * h));
			drawMap(x, y, w, h);
			drawEntities(x, y, w, h);
			if (drawPosBox) { drawPosBox(x, y, w, h); }
			drawViewBox(x, y, w, h);
			
			
			mouseInMap = checkMousePos(x, y, w, h, mXIn, mYIn);
			if (mouseInMap && mouseOver) {
				drawMouseCoords(x, y, w, h);
			}
			
			//endScissor();
		}
		
		if (world != null) {
			int tW = (int) (FontRenderer.getStringWidth(world.getName()) / 2);
			drawRect(midX - tW - 8, 7, midX + tW + 8, 43, EColors.black);
			drawRect(midX - tW - 7, 8, midX + tW + 7, 42, EColors.dgray);
			drawStringC(world.getName(), midX, 15);
			
			drawString("Dims: " + world.getWidth() + " " + world.getHeight(), reload.startX + 10, reload.endY + 60);
		}
		
		QoT_Player p = QoT.thePlayer;
		
		drawString("Dist: " + distX + " " + distY, reload.startX + 10, reload.endY + 20);
		//drawString("Zoom: " + NumberUtil.roundD2(world.getZoom()), reload.startX + 10, reload.endY + 60);
		drawString("WPos: " + p.worldX + " " + p.worldY, reload.startX + 10, reload.endY + 100);
		drawString("PPos: " + p.startX + " " + p.startY, reload.startX + 10, reload.endY + 140);
		
		oldWorldX = p.worldX;
		oldWorldY = p.worldY;
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		if (!firstPress && button == 0) { firstPress = true; }
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		//System.out.println("HERE");
		
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) { actionPerformed(up); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) { actionPerformed(left); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) { actionPerformed(down); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { actionPerformed(right); }
		
		if (typedChar == 'h') { drawEntityHitboxes = !drawEntityHitboxes; }
		if (typedChar == 'p') { drawPosBox = !drawPosBox; }
		if (typedChar == 'o') { drawEntityOutlines = !drawEntityOutlines; }
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		QoT_Player p = QoT.thePlayer;
		
		if (object == up) { p.move(0, -1); }
		if (object == left) { p.move(-1, 0); }
		if (object == down) { p.move(0, 1); }
		if (object == right) { p.move(1, 0); }
		
		if (object == distUpX) { distX++; }
		if (object == distDownX) { distX--; distX = (distX < 0) ? 0 : distX; }
		
		if (object == distUpY) { distY++; }
		if (object == distDownY) { distY--; distY = (distY < 0) ? 0 : distY; }
		
		if (object == reload) { loadWorld(); }
		
		if (object == back) {
			Stack<GameScreen> hist = getScreenHistory();
			if (hist.peek() instanceof NewMapCreatorScreen) {
				QoT.displayScreen(new NewMapCreatorScreen(world)).setScreenHistory(hist);
			}
			else {
				closeScreen();
			}
		}
	}
	
	@Override public void onScreenClosed() {}
	
	@Override
	public void onScreenResized() {
		if (world != null) {
			//distX = (Game.getWidth() / world.getTileWidth()) / 2 + 1;
			//distY = (game.getHeight() / world.getTileHeight()) / 2 + 2;
		}
	}
	
	//------------------------
	// Private Editor Methods
	//------------------------
	
	private void loadWorld() {
		if (mapFile != null) {
			world = new GameWorld(mapFile);
			QoT.setPlayer(new QoT_Player("Test Player"));
			
			/*
			for (int i = 0; i < NumberUtil.getRoll(5, 10); i++) {
				Entity g = null;
				
				switch (NumberUtil.getRoll(0, 4)) {
				case 0: g = world.addEntity(new Goblin()); break;
				case 1: g = world.addEntity(new Thyrah()); break;
				case 2: g = world.addEntity(new TrollBoar()); break;
				case 3: g = world.addEntity(new Whodundid()); break;
				case 4: g = world.addEntity(new WhodundidsBrother()); break;
				}
				
				g.setWorldPos(NumberUtil.getRoll(0, world.getWidth() - 1), NumberUtil.getRoll(0, world.getHeight() - 1));
			}
			*/
			
			distX = (QoT.getWidth() / world.getTileWidth()) / 2 + 1;
			distY = (QoT.getHeight() / world.getTileHeight()) / 2 + 2;
			
			Entity e = world.addEntity(QoT.getPlayer());
			e.setWorldPos(0, 0);
		}
		else if (world != null) {
			mapFile = world.getWorldFile();
		}
	}
	
	private void checkArrowPress() {
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_W) || Keyboard.isKeyDown(GLFW.GLFW_KEY_UP)) { up.setForceDrawHover(true); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_A) || Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT)) { left.setForceDrawHover(true); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_S) || Keyboard.isKeyDown(GLFW.GLFW_KEY_DOWN)) { down.setForceDrawHover(true); }
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_D) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT)) { right.setForceDrawHover(true); }
		
		QoT_Player p = QoT.thePlayer;
		
		if (Keyboard.isWDown()) { p.move(0, -1.5); }
		if (Keyboard.isADown()) { p.move(-1.5, 0); }
		if (Keyboard.isSDown()) { p.move(0, 1.5); }
		if (Keyboard.isDDown()) { p.move(1.5, 0); }
	}
	
	private void drawMap(int x, int y, int w, int h) {
		QoT_Player p = QoT.thePlayer;
		double offsetX = (p.startX % w);
		double offsetY = (p.startY % h);
		
		if (p.worldX != oldWorldX || p.worldY != oldWorldY) {
			//tiles = world.getTilesAroundPoint(p.worldX, p.worldY, distX, distY);
			tiles = new WorldTile[0][0];
		}
		
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				WorldTile t = tiles[i][j];
				if (t != null) {
					
					double drawPosX = x + ((p.startX < -world.getTileWidth()) ? Math.abs(p.startX) : -offsetX);
					double drawPosY = y + ((p.startY < -world.getTileHeight()) ? Math.abs(p.startY) : -offsetY);
					
					if (p.worldX < distX) { drawPosX += (distX - p.worldX) * w; }
					if (p.worldY < distY) { drawPosY += (distY - p.worldY) * h; }
					
					if (t.hasTexture()) {
						drawTexture(t.getTexture(), drawPosX + (i * w), drawPosY + (j * h), w, h);
					}
				}
			}
		}
	}
	
	private void drawPosBox(int x, int y, int w, int h) {
		QoT_Player p = QoT.thePlayer;
		
		double drawX = x + w * distX;
		double drawY = y + h * distY;
		
		double offsetX = (p.startX % w);
		double offsetY = (p.startY % h);
		
		drawX -= offsetX;
		drawY -= offsetY;
		
		double sX = drawX;
		double sY = drawY;
		double eX = sX + w;
		double eY = sY + h;
		drawHRect(sX, sY, eX, eY, 2, EColors.red);
	}
	
	private void drawViewBox(int x, int y, int w, int h) {
		int dsX = x;
		int dsY = y;
		int deX = x + w + (distX * 2 * w);
		int deY = y + h + (distY * 2 * h);
		drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
	}
	
	private void drawEntities(int x, int y, int w, int h) {
		EArrayList<Entity> entities = world.getEntitiesInWorld();
		
		for (Entity e : entities) {
			if (e.getTexture() != null) {
				double drawX = 0;
				double drawY = 0;
				
				if (e == QoT.thePlayer) {
					drawX = x + distX * w;
					drawY = y + distY * h;
					
					drawTexture(e.getTexture(), drawX, drawY, e.width, e.height);
				}
				else {
					drawX = x + (e.startX) + (distX - QoT.thePlayer.worldX) * w;
					drawY = y + (e.startY) + (distY - QoT.thePlayer.worldY) * h;
					
					double offsetX = (QoT.thePlayer.startX % w);
					double offsetY = (QoT.thePlayer.startY % h);
					drawX -= offsetX;
					drawY -= offsetY;
					
					if (drawX + e.width > x && drawX < x + w + (distX * 2 * w) && drawY + e.height > y && drawY < y + h + (distY * 2 * h)) {
						drawTexture(e.getTexture(), drawX, drawY, e.width, e.height);
					}
				}
				
				if (drawEntityHitboxes) {
					double colSX = drawX + e.getCollision().startX;
					double colSY = drawY + e.getCollision().startY;
					double colEX = colSX + e.getCollision().width;
					double colEY = colSY + e.getCollision().height;
					
					drawHRect(colSX, colSY, colEX, colEY, 1, EColors.yellow);
				}
				
				if (drawEntityOutlines) {
					double colSX = drawX;
					double colSY = drawY;
					double colEX = colSX + e.width;
					double colEY = colSY + e.height;
					
					drawHRect(colSX, colSY, colEX, colEY, 1, EColors.blue);
				}
			}
		}
	}
	
	private boolean checkMousePos(int x, int y, int w, int h, int mXIn, int mYIn) {
		double offsetX = (QoT.thePlayer.startX % w);
		double offsetY = (QoT.thePlayer.startY % h);
		
		worldXPos = (double) (mXIn - (x - offsetX) - ((distX - QoT.thePlayer.worldX) * w)) / w;
		worldYPos = (double) (mYIn - (y - offsetY) - ((distY - QoT.thePlayer.worldY) * h)) / h;
		boolean inMap = worldXPos >= 0 && worldXPos < world.getWidth() && worldYPos > 0 && worldYPos < world.getHeight();
		
		return mouseOver && inMap && EDimension.of(x, y, x + w + (distX * 2 * w), y + h + (distY * 2 * h)).contains(mXIn, mYIn);
	}
	
	private void drawMouseCoords(int x, int y, int w, int h) {
		worldXPos = (int) worldXPos;
		worldYPos = (int) worldYPos;
		worldXPos = NumberUtil.clamp(worldXPos, 0, world.getWidth() - 1);
		worldYPos = NumberUtil.clamp(worldYPos, 0, world.getHeight() - 1);
		
		WorldTile t = world.getTileAt((int) worldXPos, (int) worldYPos);
		String name = (t != null) ? " : " + t.getName() : "";
		drawString("World: " + worldXPos + " " + worldYPos + name, reload.startX + 10, reload.endY + 180);
		
		double offsetX = (QoT.thePlayer.startX % w);	
		double offsetY = (QoT.thePlayer.startY % h);
		
		double xPos = (x - offsetX) + ((Mouse.getMx() - (int) (x - offsetX)) / w) * w;
		double yPos = (y - offsetY) + ((Mouse.getMy() - (int) (y - offsetY)) / h) * h;
		
		drawHRect(xPos, yPos, xPos + w, yPos + h, 1, EColors.vdgray);
	}
	
}
