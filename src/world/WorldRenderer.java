package world;

import engine.GameTopRenderer;
import engine.QoT;
import engine.windowLib.windowUtil.EGui;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import eutil.misc.Rotation;
import game.entities.Entity;
import game.entities.Player;
import world.resources.WorldTile;

import java.util.Comparator;

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
	int oldWorldX, oldWorldY;
	
	int viewDist = 30;
	
	boolean firstPress = false;
	boolean drawPosBox = false;
	boolean drawEntityHitboxes = false;
	boolean drawEntityOutlines = false;
	
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
		}
	}
	
	public void onRenderTick() {
		if (world != null && world.isFileLoaded()) { renderWorld(); }
	}
	
	private void renderWorld() {
		Player p = QoT.thePlayer;
		
		if (world == null) { drawStringC("Failed to load!", midX, midY); }
		else if (world.getWidth() < 0 || world.getHeight() < 0 || world.getTileWidth() <= 0 || world.getTileHeight() <= 0) { drawStringC("Bad world dimensions!", midX, midY); }
		else {
			
			w = (int) (world.getTileWidth() * world.getZoom());
			h = (int) (world.getTileHeight() * world.getZoom());
			
			x = (int) (midX - (distX * w) - (w / 2));
			y = (int) (midY - (distY * h) - (h / 2));
			
			//drawTexture((double) -p.worldX * (double) ((double) world.getTileWidth() / 3.5),
			//			(double) -p.worldY * (double) ((double) world.getTileHeight() / 3.5),
			//			QoT.getWidth() * 4, QoT.getHeight() * 4, WorldTextures.wood);
			
			renderMap();
			renderEntities();
			
			
			
			if (drawPosBox) { drawPosBox(); }
		}
		
		if (world != null) {
			//int tW = (int) (FontRenderer.getStringWidth(world.getName()) / 2);
			//drawRect(midX - tW - 8, 7, midX + tW + 8, 43, EColors.black);
			//drawRect(midX - tW - 7, 8, midX + tW + 7, 42, EColors.dgray);
			//drawStringC(world.getName(), midX, 15);
			
			//drawString("Dims: " + world.getWidth() + " " + world.getHeight(), startX + 10, endY + 60);
		}
		
		//drawString("Dist: " + distX + " " + distY, reload.startX + 10, reload.endY + 20);
		//drawString("Zoom: " + NumUtil.roundD2(world.getZoom()), reload.startX + 10, reload.endY + 60);
		//drawString("WPos: " + p.worldX + " " + p.worldY, reload.startX + 10, reload.endY + 100);
		//drawString("PPos: " + p.startX + " " + p.startY, 10, 10);
		
		//this.viewDist = 1000;
		
		oldWorldX = p.worldX;
		oldWorldY = p.worldY;
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!GameTopRenderer.isTopFocused()) {
			//if (typedChar == 'e') QoT.getActiveTopParent().displayWindow(new InventoryWindow(QoT.getPlayer()));
			if (typedChar == 'h') drawEntityHitboxes = !drawEntityHitboxes;
			if (typedChar == 'p') drawPosBox = !drawPosBox;
			if (typedChar == 'o') drawEntityOutlines = !drawEntityOutlines;
		}
	}
	
	private void renderMap() {
		Player p = QoT.thePlayer;
		double offsetX = (p.startX % w);
		double offsetY = (p.startY % h);
		
		//only update values if needed
		if (p.worldX != oldWorldX || p.worldY != oldWorldY) {
			left = NumberUtil.clamp(p.worldX - distX, 0, world.getWidth() - 1);
			top = NumberUtil.clamp(p.worldY - distY, 0, world.getHeight() - 1);
			right = NumberUtil.clamp(p.worldX + distX, left, world.getWidth() - 1);
			bot = NumberUtil.clamp(p.worldY + distY, top, world.getHeight() - 1);
			dw = right - left; //draw width
			dh = bot - top; //draw height
		}
		
		int worldTileWidth = -world.getTileWidth();
		int worldTileHeight = -world.getTileHeight();
		double pStartX = Math.abs(p.startX);
		double pStartY = Math.abs(p.startY);
		
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				WorldTile t = world.getWorldData()[i][j];
				if (t != null) {
					
					double drawPosX = x + ((p.startX < worldTileWidth) ? pStartX : -offsetX);
					double drawPosY = y + ((p.startY < worldTileHeight) ? pStartY : -offsetY);
					
					if (p.worldX < distX) { drawPosX += (distX - p.worldX) * w; }
					if (p.worldY < distY) { drawPosY += (distY - p.worldY) * h; }
					
					double dX = drawPosX + (ix * w);
					double dY = drawPosY + (jy * h);
					
					//String coords = ("(" + (ix + i) + ", " + (jy + j) + ") : " + "(" + p.worldX + ", " + p.worldY + ")");
					//drawString(t.getWorldX() + " : " + t.getWorldY() / w, 0, 50, EColors.white);
					
					if (t.hasTexture()) {
						drawTexture(dX, dY, w, h, t.getTexture(), false, calcBrightness(t.getWorldX() - 1, t.getWorldY() - 1));
					}
				}
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
		//int dsX = x;
		//int dsY = y;
		//int deX = x + w + (distX * 2 * w);
		//int deY = y + h + (distY * 2 * h);
		//drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
	}
	
	private void renderEntities() {
		EArrayList<Entity> entities = world.getEntitiesInWorld();
		entities.sort(Comparator.comparingInt(e -> e.startY));
		
		for (Entity e : entities) {
			if (e.getTexture() != null) {
				double drawX = 0;
				double drawY = 0;
				boolean flip = e.getFacing() == Rotation.RIGHT || e.getFacing() == Rotation.DOWN;
				
				if (e == QoT.thePlayer) {
					drawX = x + distX * w;
					drawY = y + distY * h;
					
					drawTexture(drawX, drawY, e.width, e.height, e.getTexture(), flip, calcBrightness(e.worldX, e.worldY));
					drawStringC(e.getHeadText(), drawX + e.width / 2, drawY - e.height / 2);
				}
				else {
					drawX = x + (e.startX) + (distX - QoT.thePlayer.worldX) * w;
					drawY = y + (e.startY) + (distY - QoT.thePlayer.worldY) * h;
					
					double offsetX = (QoT.thePlayer.startX % w);
					double offsetY = (QoT.thePlayer.startY % h);
					drawX -= offsetX;
					drawY -= offsetY;
					
					if (drawX + e.width > x && drawX < x + w + (distX * 2 * w) && drawY + e.height > y && drawY < y + h + (distY * 2 * h)) {
						drawTexture(drawX, drawY, e.width, e.height, e.getTexture(), flip, calcBrightness(e.worldX, e.worldY));
						drawStringC(e.getHeadText(), drawX + e.width / 2, drawY - e.height / 2);
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
	
}
