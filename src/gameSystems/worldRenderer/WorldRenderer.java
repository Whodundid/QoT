package gameSystems.worldRenderer;

import assets.entities.Entity;
import assets.entities.enemy.types.Goblin;
import assets.entities.enemy.types.Thyrah;
import assets.entities.enemy.types.TrollBoar;
import assets.entities.enemy.types.Whodundid;
import assets.entities.player.Player;
import assets.worldTiles.WorldTile;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.mapSystem.GameWorld;
import input.Keyboard;
import java.util.Comparator;
import main.Game;
import randomUtil.RandomUtil;
import renderUtil.EColors;
import storageUtil.EArrayList;
import windowLib.windowUtil.EGui;

//Author: Hunter Bragg

/** Handles rendering GameWorlds. */
public class WorldRenderer extends EGui {
	
	public static WorldRenderer instance;
	private GameWorld world;
	int distX = 15;
	int distY = 10;
	int tileWidth, tileHeight;
	int pixelWidth, pixelHeight;
	int x, y, w, h;
	
	double worldXPos, worldYPos;
	int oldWorldX, oldWorldY;
	
	boolean firstPress = false;
	boolean drawPosBox = false;
	boolean drawEntityHitboxes = true;
	boolean drawEntityOutlines = false;
	
	public static WorldRenderer getInstance() {
		return instance == null ? instance = new WorldRenderer() : instance;
	}
	
	private WorldRenderer() {}
	
	public void onRenderTick() {
		if (world != null && world.isFileLoaded()) { renderWorld(); }
	}
	
	private void renderWorld() {
		checkArrowPress();
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else if (world.getWidth() < 0 || world.getHeight() < 0 || world.getTileWidth() <= 0 || world.getTileHeight() <= 0) { drawStringC("Bad world dimensions!", midX, midY); }
		else {
			
			w = (int) (world.getTileWidth() * world.getZoom());
			h = (int) (world.getTileHeight() * world.getZoom());
			
			x = (int) (midX - (distX * w) - (w / 2));
			y = (int) (midY - (distY * h) - (h / 2));
			
			scissor(x, y, x + w + (distX * 2 * w), y + h + (distY * 2 * h));
			renderMap();
			renderEntities();
			if (drawPosBox) { drawPosBox(); }
			//drawViewBox();
			
			/*
			mouseInMap = checkMousePos(x, y, w, h, mX, mY);
			if (mouseInMap && mouseOver) {
				drawMouseCoords(x, y, w, h);
			}
			*/
			
			endScissor();
		}
		
		if (world != null) {
			int tW = (int) (FontRenderer.getInstance().getStringWidth(world.getName()) / 2);
			drawRect(midX - tW - 8, 7, midX + tW + 8, 43, EColors.black);
			drawRect(midX - tW - 7, 8, midX + tW + 7, 42, EColors.dgray);
			drawStringC(world.getName(), midX, 15);
			
			drawString("Dims: " + world.getWidth() + " " + world.getHeight(), startX + 10, endY + 60);
		}
		
		//drawString("Dist: " + distX + " " + distY, reload.startX + 10, reload.endY + 20);
		//drawString("Zoom: " + NumUtil.roundD2(world.getZoom()), reload.startX + 10, reload.endY + 60);
		//drawString("WPos: " + p.worldX + " " + p.worldY, reload.startX + 10, reload.endY + 100);
		//drawString("PPos: " + p.startX + " " + p.startY, reload.startX + 10, reload.endY + 140);
	}
	
	private void checkArrowPress() {
		Player p = Game.thePlayer;
		if (Keyboard.isWDown()) { p.move(0, -1.5); }
		if (Keyboard.isADown()) { p.move(-1.5, 0); }
		if (Keyboard.isSDown()) { p.move(0, 1.5); }
		if (Keyboard.isDDown()) { p.move(1.5, 0); }
	}
	
	private void renderMap() {
		Player p = Game.thePlayer;
		double offsetX = (p.startX % w);
		double offsetY = (p.startY % h);
		
		WorldTile[][] tiles = world.getTilesAroundPoint(p.worldX, p.worldY, distX, distY);
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {
				WorldTile t = tiles[i][j];
				if (t != null) {
					
					double drawPosX = x + ((p.startX < -world.getTileWidth()) ? Math.abs(p.startX) : -offsetX);
					double drawPosY = y + ((p.startY < -world.getTileHeight()) ? Math.abs(p.startY) : -offsetY);
					
					if (p.worldX < distX) { drawPosX += (distX - p.worldX) * w; }
					if (p.worldY < distY) { drawPosY += (distY - p.worldY) * h; }
					
					if (t.hasTexture()) {
						drawTexture(drawPosX + (i * w), drawPosY + (j * h), w, h, t.getTexture());
					}
				}
			}
		}
	}
	
	private void drawPosBox() {
		Player p = Game.thePlayer;
		
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
	
	private void drawViewBox() {
		int dsX = x;
		int dsY = y;
		int deX = x + w + (distX * 2 * w);
		int deY = y + h + (distY * 2 * h);
		//drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
	}
	
	private void renderEntities() {
		EArrayList<Entity> entities = world.getEntitiesInWorld();
		entities.sort(Comparator.comparingInt(e -> e.startY));
		
		for (Entity e : entities) {
			e.onLivingUpdate();
			if (e.getTexture() != null) {
				double drawX = 0;
				double drawY = 0;
				
				if (e == Game.thePlayer) {
					drawX = x + distX * w;
					drawY = y + distY * h;
					
					drawTexture(drawX, drawY, e.width, e.height, e.getTexture());
				}
				else {
					drawX = x + (e.startX) + (distX - Game.thePlayer.worldX) * w;
					drawY = y + (e.startY) + (distY - Game.thePlayer.worldY) * h;
					
					double offsetX = (Game.thePlayer.startX % w);
					double offsetY = (Game.thePlayer.startY % h);
					drawX -= offsetX;
					drawY -= offsetY;
					
					if (drawX + e.width > x && drawX < x + w + (distX * 2 * w) && drawY + e.height > y && drawY < y + h + (distY * 2 * h)) {
						drawTexture(drawX, drawY, e.width, e.height, e.getTexture());
					}
				}
				
				if (drawEntityHitboxes) {
					double colSX = drawX + e.getCollision().startX;
					double colSY = drawY + e.getCollision().startY;
					double colEX = colSX + e.getCollision().width;
					double colEY = colSY + e.getCollision().height;
					
					//drawHRect(colSX, colSY, colEX, colEY, 1, EColors.yellow);
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
	
	public WorldRenderer setWorld(GameWorld worldIn) {
		world = worldIn;
		
		if (world != null) {
			distX = world.getWidth();
			distY = world.getHeight();
		}
		
		for (int i = 0; i < 10; i++) {
			Entity e = null;
			switch (RandomUtil.getRoll(0, 3)) {
			case 0: e = new Goblin(); break;
			case 1: e = new Thyrah(); break;
			case 2: e = new TrollBoar(); break;
			case 3: e = new Whodundid(); break;
			}
			
			if (e != null) {
				world.addEntity(e);
				int x = RandomUtil.getRoll(0, world.getWidth());
				int y = RandomUtil.getRoll(0, world.getHeight());
				e.setWorldPos(x, y);
			}
		}
		
		onWindowResized();
		return this;
	}
	
	public void onWindowResized() {
		res = Game.getWindowSize();
		setDimensions(0, 0, res.getWidth(), res.getHeight());
		
		if (world != null) {
			//tileWidth = world.getTileWidth();
			//tileHeight = world.getTileHeight();
			
			//distX = (int) 5;
			//distY = (int) 5;
			
			//w = (int) (world.getTileWidth() * world.getZoom());
			//h = (int) (world.getTileHeight() * world.getZoom());
			//x = (int) midX - (distX / 2 * w) - w;
			//y = (int) midY - (distY / 2 * h) - h;
		}
		
	}
	
}
