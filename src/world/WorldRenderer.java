package world;

import java.util.Comparator;
import engine.GameTopRenderer;
import engine.input.Keyboard;
import engine.windowLib.windowUtil.EGui;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import eutil.misc.Rotation;
import game.entities.Entity;
import game.entities.Player;
import game.worldTiles.WorldTile;
import main.QoT;

//Author: Hunter Bragg

/** Handles rendering GameWorlds. */
public class WorldRenderer extends EGui {
	
	private GameWorld world;
	int distX = 31;
	int distY = 17;
	int tileWidth, tileHeight;
	int pixelWidth, pixelHeight;
	int x, y, w, h;
	
	double worldXPos, worldYPos;
	int oldWorldX = Integer.MIN_VALUE, oldWorldY = Integer.MIN_VALUE;
	
	int viewDist = 18;
	
	boolean firstPress = false;
	boolean drawPosBox = false;
	boolean drawEntityHitboxes = false;
	boolean drawEntityOutlines = false;
	
	private EArrayList<WorldTile> entityOrder = new EArrayList();
	
	//---------------------
	int left;
	int top;
	int right;
	int bot;
	int dw; //draw width
	int dh; //draw height
	//----------------------
	
	public WorldRenderer(GameWorld worldIn) {
		world = worldIn;
		onWindowResized();
		
		if (world != null) {
			//load entities
			world.getEntitySpawns().forEach(e -> e.spawnEntity(world));
			
			//add all walls to a separate list
			int w = world.getWidth();
			int h = world.getHeight();
			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					WorldTile t = world.getTileAt(i, j);
					if (t == null) continue;
					if (t.isWall()) entityOrder.add(t);
				}
			}
			
			//entityOrder.addAll(world.getEntitiesInWorld());
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!GameTopRenderer.isTopFocused() && !(Keyboard.isCtrlDown() || Keyboard.isAltDown() || Keyboard.isShiftDown())) {
			if (typedChar == 'h') drawEntityHitboxes = !drawEntityHitboxes;
			if (typedChar == 'p') drawPosBox = !drawPosBox;
			if (typedChar == 'o') drawEntityOutlines = !drawEntityOutlines;
			if (typedChar == '.') viewDist++;
			if (typedChar == ',') viewDist--;
		}
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Should be called from main game render tick, every tick.
	 */
	public void onRenderTick() {
		renderWorld();
	}
	
	private void renderWorld() {
		if (world == null) drawStringC("Failed to load!", midX, midY);
		else if (world.width < 0 || world.height < 0 || world.tileWidth <= 0 || world.tileHeight <= 0) {
			drawStringC("Bad world dimensions!", midX, midY);
		}
		else {
			//drawRect(0xff4fedff);
			if (!world.isUnderground()) drawRect(0xff4fbaff);
			
			//pixel width of each tile
			w = (int) (world.getTileWidth() * world.getZoom());
			//pixel height of each tile
			h = (int) (world.getTileHeight() * world.getZoom());
			
			//the left most x coordinate for map drawing
			x = (int) (midX - (distX * w) - (w / 2));
			//the top most y coordinate for map drawing
			y = (int) (midY - (distY * h) - (h / 2));
			
			renderMap();
			//renderWalls();
			renderEntities();
			//drawViewBox();
			
			if (drawPosBox) drawPosBox();
			
			oldWorldX = QoT.thePlayer.worldX;
			oldWorldY = QoT.thePlayer.worldY;
		}
	}
	
	private void renderMap() {
		Player p = QoT.thePlayer;
		double offsetX = (p.startX % w);
		double offsetY = (p.startY % h);
		
		//only update values if needed
		//if (p.worldX != oldWorldX || p.worldY != oldWorldY) {
			//restrict drawing to the dimensions of the world
			left = NumberUtil.clamp(p.worldX - distX, 0, world.getWidth() - 1);
			top = NumberUtil.clamp(p.worldY - distY, 0, world.getHeight() - 1);
			right = NumberUtil.clamp(p.worldX + distX, left, world.getWidth() - 1);
			bot = NumberUtil.clamp(p.worldY + distY, top, world.getHeight() - 1);
			dw = right - left; //draw width
			dh = bot - top; //draw height
		//}
		
		int worldTileWidth = -world.getTileWidth();
		int worldTileHeight = -world.getTileHeight();
		double pStartX = Math.abs(p.startX);
		double pStartY = Math.abs(p.startY);
		
		//this route should not process wall tiles
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				WorldTile t = world.getWorldData()[i][j];
				if (t == null || !t.hasTexture()) continue;
				
				double drawPosX = x + ((p.startX < worldTileWidth) ? pStartX : -offsetX);
				double drawPosY = y + ((p.startY < worldTileHeight) ? pStartY : -offsetY);
				
				if (p.worldX < distX) drawPosX += (distX - p.worldX) * w;
				if (p.worldY < distY) drawPosY += (distY - p.worldY) * h;
				
				double dX = drawPosX + (ix * w);
				double dY = drawPosY + (jy * h);
				
				int brightness = calcBrightness(t.getWorldX() - 1, t.getWorldY() - 1);
				
				t.renderTile(world, dX, dY, w, h, brightness);
				//drawTexture(tex, dX, dY, w, h, false, brightness);
				
				/*
				//draw bottom of map edge or if right above a tile with no texture/void
				WorldTile tileBelow = null;
				if ((j + 1) <= bot) tileBelow = world.getWorldData()[i][j + 1];
				if ((tileBelow == null || !tileBelow.hasTexture()) && !t.isWall()) {
					drawTexture(tex, dX, dY + h, w, h / 2, false, EColors.changeBrightness(brightness, 125));
				}
				*/
			}
		}
	}
	
	private void sortEntitiesAndWalls() {
		for (int i = 1; i < entityOrder.size(); i++) {
			
		}
	}
	
	private void renderWalls() {
		Player p = QoT.thePlayer;
		double offsetX = (p.startX % w);
		double offsetY = (p.startY % h);
		
		int worldTileWidth = -world.getTileWidth();
		int worldTileHeight = -world.getTileHeight();
		double pStartX = Math.abs(p.startX);
		double pStartY = Math.abs(p.startY);
		
		//the tile must be a wall here
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				WorldTile t = world.getWorldData()[i][j];
				if (t == null) continue;
				if (!t.isWall()) continue;
				if (!t.hasTexture()) continue;
				//double wh = h * t.getWallHeight(); //wh == 'wallHeight'
				//GameTexture tex = t.getTexture();
				
				double drawPosX = x + ((p.startX < worldTileWidth) ? pStartX : -offsetX);
				double drawPosY = y + ((p.startY < worldTileHeight) ? pStartY : -offsetY);
				
				if (p.worldX < distX) drawPosX += (distX - p.worldX) * w;
				if (p.worldY < distY) drawPosY += (distY - p.worldY) * h;
				
				double dX = drawPosX + (ix * w);
				double dY = drawPosY + (jy * h);
				
				//determine tile brightness
				int brightness = calcBrightness(t.getWorldX() - 1, t.getWorldY() - 1);
				//int tileBrightness = brightness;
				//int wallBrightness = brightness;
				
				//if (wh < 0) tileBrightness = EColors.changeBrightness(brightness, 200);
				
				t.renderTile(world, dX, dY, w, h, brightness);
				
				/*
				//draw main texture slightly above main location
				drawTexture(tex, dX, dY - wh, w, h, false, tileBrightness);
				
				//check if the tile directly above is a wall
				//if so - don't draw wall side
				WorldTile tb = null; // tb == 'tileBelow'
				if ((j + 1) <= bot) tb = world.getWorldData()[i][j + 1];
				if ((tb == null ||
					!tb.hasTexture() ||
					 tb.getWallHeight() < wh) ||
					!tb.isWall())
				{
					double yPos;
					
					if (wh > 0) {
						yPos = dY + h - wh;
						wallBrightness = EColors.changeBrightness(brightness, 125);
					}
					else {
						yPos = dY - wh;
						wallBrightness = brightness;
					}
					
					//draw wall side slightly below
					drawTexture(tex, dX, yPos, w, wh, false, wallBrightness);
				}
				
				//draw bottom of map edge or if right above a tile with no texture/void
				WorldTile tileBelow = null;
				if ((j + 1) <= bot) tileBelow = world.getWorldData()[i][j + 1];
				if ((tileBelow == null || !tileBelow.hasTexture())) {
					drawTexture(tex, dX, dY + h, w, h / 2, false, EColors.changeBrightness(brightness, 125));
				}
				*/
			}
		}
	}
	
	private void renderEntities() {
		EArrayList<Entity> entities = world.getEntitiesInWorld();
		entities.sort(Comparator.comparingInt(e -> e.startY));
		
		for (int i = entities.size() - 1; i >= 0; i--) {
			Entity e = entities.get(i);
			if (e.getTexture() == null) continue;
			
			double drawX = 0;
			double drawY = 0;
			boolean flip = e.getFacing() == Rotation.RIGHT || e.getFacing() == Rotation.DOWN;
			
			if (e == QoT.thePlayer) {
				drawX = x + distX * w;
				drawY = y + distY * h;
				double dw = e.width * world.zoom;
				double dh = e.height * world.zoom;
				
				//shadows
				for (int s = 0; s < 6; s++) {
					double rX = (e.width / 4) - (s * e.width / 80);
					double rY = (e.height / 16) - (s * e.height / 64);
					drawFilledEllipse((drawX + dw / 2), (drawY + dh), rX, rY, 16, EColors.dgray.opacity(0x16));
				}
				
				drawTexture(e.getTexture(), drawX, drawY, dw, dh, flip, calcBrightness(e.worldX, e.worldY));
				drawStringC(e.getHeadText(), drawX + dw / 2, drawY - dh / 2);
			}
			else {
				drawX = x + (e.startX) + (distX - QoT.thePlayer.worldX) * w;
				drawY = y + (e.startY) + (distY - QoT.thePlayer.worldY) * h;
				
				double offsetX = (QoT.thePlayer.startX % w);
				double offsetY = (QoT.thePlayer.startY % h);
				drawX -= offsetX;
				drawY -= offsetY;
				
				if (drawX + e.width > x && drawX < x + w + (distX * 2 * w) && drawY + e.height > y && drawY < y + h + (distY * 2 * h)) {
					double dw = e.width * world.zoom;
					double dh = e.height * world.zoom;
					
					//shadows
					for (int s = 0; s < 12; s++) {
						double rX = (e.width / 3) - (s * e.width / 80);
						double rY = (e.height / 8) - (s * e.height / 64);
						drawFilledEllipse((drawX + dw / 2), (drawY + dh - dh / 32), rX, rY, 16, EColors.vdgray.opacity(0x16));
					}
					
					drawTexture(e.getTexture(), drawX, drawY, dw, dh, flip, calcBrightness(e.worldX, e.worldY));
					drawStringC(e.getHeadText(), drawX + e.width / 2, drawY - e.height / 2);
				}
			}
			
			if (drawEntityHitboxes) {
				double colSX = drawX + (e.getCollision().startX * world.zoom);
				double colSY = drawY + (e.getCollision().startY * world.zoom);
				double colEX = colSX + (e.getCollision().width * world.zoom);
				double colEY = colSY + (e.getCollision().height * world.zoom);
				
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
	
	private int calcBrightness(int x, int y) {
		if (world.underground) {
			Player p = QoT.thePlayer;
			int distToPlayer = viewDist - (int) (NumberUtil.distance(x, y, p.worldX, p.worldY));
			distToPlayer = NumberUtil.clamp(distToPlayer, 0, distToPlayer);
			int ratio = (distToPlayer * 255) / viewDist;
			return EColors.changeBrightness(0xffffffff, ratio);
		}
		return 0xffffffff;
	}
	
	private void drawPosBox() {
		Player p = QoT.thePlayer;
		
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
		drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
	}
	
	public void onWindowResized() {
		res = QoT.getWindowSize();
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
	
	public int getDistX() { return distX; }
	public int getDistY() { return distY; }
	public void setDistX(int in) { distX = in; }
	public void setDistY(int in) { distY = in; }
	
}
