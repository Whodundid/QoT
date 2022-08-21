package world;

import java.util.Comparator;

import engine.inputHandlers.Keyboard;
import engine.renderEngine.textureSystem.GameTexture;
import engine.topOverlay.GameTopRenderer;
import engine.windowLib.windowUtil.EGui;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import eutil.misc.Rotation;
import game.entities.Entity;
import game.entities.player.Player;
import main.QoT;
import world.worldTiles.WorldTile;

//Author: Hunter Bragg

/** Handles rendering GameWorlds. */
public class WorldRenderer extends EGui {
	
	//--------
	// Fields
	//--------
	
	private GameWorld world;
	private int distX = 31;
	private int distY = 17;

	/** the world coordinates at the center of the screen. */
	public int midDrawX, midDrawY;
	
	private int viewDist = 18;
	
	boolean drawPosBox = false;
	boolean drawEntityHitboxes = false;
	boolean drawEntityOutlines = false;
	
	private EArrayList<WorldTile> entityOrder = new EArrayList();
	
	//--------------
	// Constructors
	//--------------
	
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
	
	//------------------------
	// Overrides : GameScreen
	//------------------------
	
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
			double w = (int) (world.getTileWidth() * world.getZoom());
			//pixel height of each tile
			double h = (int) (world.getTileHeight() * world.getZoom());
			
			//the left most x coordinate for map drawing
			double x = (int) (midX - (distX * w) - (w / 2));
			//the top most y coordinate for map drawing
			double y = (int) (midY - (distY * h) - (h / 2));
			
			renderMap(x, y, w, h);
			renderEntities(x, y, w, h);
			
			if (drawPosBox) {
				drawPosBox(x, y, w, h);
			}
		}
	}
	
	private void renderMap(double x, double y, double w, double h) {
		Player p = QoT.thePlayer;
		
		//keep the player at the center of the world (THIS SHOULD BE CHANGED TO 'CAMERA' AT SOME POINT!)
		midDrawX = p.worldX;
		midDrawY = p.worldY;
		
		//uses the player's (SHOULD BE A CAMERA OBJECT) pixel position as an offset to apply to rendered tiles 
		double offsetX = (p.startX % world.getTileWidth()) * world.zoom;
		double offsetY = (p.startY % world.getTileHeight()) * world.zoom;
		
		//calculations to determine how many tiles to draw out in each direction from the mid of the screen
		int left = NumberUtil.clamp(midDrawX - distX, 0, world.getWidth() - 1);
		int top = NumberUtil.clamp(midDrawY - distY, 0, world.getHeight() - 1);
		int right = NumberUtil.clamp(midDrawX + distX, left, world.getWidth() - 1);
		int bot = NumberUtil.clamp(midDrawY + distY, top, world.getHeight() - 1);
		
		//draw the tiles of the world using the calculated dimensions
		for (int i = left, ix = 0; i <= right; i++, ix++) {
			for (int j = top, jy = 0; j <= bot; j++, jy++) {
				WorldTile t = world.getWorldData()[i][j];
				
				//ignore if either null or texture is null -- actually.. how could it be null?? :thinking:
				if (t == null) continue;
				if (!t.hasTexture()) continue;
				
				//start at the top-left most screen pixel and add relative offsets accordingly
				double drawPosX = x;
				double drawPosY = y;
				
				//if near the edges of the map, add offsets to keep map drawn relative to mid of screen
				if (midDrawX < distX) drawPosX += (distX - midDrawX) * w;
				if (midDrawY < distY) drawPosY += (distY - midDrawY) * h;
				
				//uses the index of the for loop to determine primary screen coordinates
				double dX = drawPosX + (ix * w);
				double dY = drawPosY + (jy * h);
				
				//apply the player's (CAMERA'S) offset to the drawn tile
				dX -= offsetX;
				dY -= offsetY;
				
				//call the tile's self rendering code with the proper screen coordinates and dimensions
				t.renderTile(world, dX, dY, w, h, 0xffffffff, false);
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
			
			boolean flip = ent.getFacing() == Rotation.RIGHT || ent.getFacing() == Rotation.DOWN;
			
			double cameraOffsetX = (QoT.thePlayer.startX % world.getTileWidth()) * world.zoom;
			double cameraOffsetY = (QoT.thePlayer.startY % world.getTileHeight()) * world.zoom;
			double entityOffsetX = (ent.startX % world.getTileWidth()) * world.zoom;
			double entityOffsetY = (ent.startY % world.getTileWidth()) * world.zoom;
			
			//transform the world coordinates of the entity to screen x/y coordinates
			double drawX = (ent.worldX * w) + x;
			double drawY = (ent.worldY * h) + y;
			
			//translate to the middle drawn world tile
			drawX += (distX - midDrawX) * w;
			drawY += (distY - midDrawY) * h;
			
			if (ent != QoT.thePlayer) {
				drawX += entityOffsetX;
				drawY += entityOffsetY;
				drawX -= cameraOffsetX;
				drawY -= cameraOffsetY;
			}
			
			//drawY -= world.getTileHeight() * world.zoom;
			
			//calculate the entity's draw width and height based off of actual dims and zoom
			double drawW = ent.width * world.getZoom();
			double drawH = ent.height * world.getZoom();
			
			drawTexture(tex, drawX, drawY, drawW, drawH, flip);
			
			if (drawEntityHitboxes) {
				double colSX = drawX + (ent.getCollision().startX * world.zoom);
				double colSY = drawY + (ent.getCollision().startY * world.zoom);
				double colEX = colSX + (ent.getCollision().width * world.zoom);
				double colEY = colSY + (ent.getCollision().height * world.zoom);
				
				drawHRect(colSX, colSY, colEX, colEY, 1, EColors.yellow);
			}
			
			if (drawEntityOutlines) {
				double colSX = drawX;
				double colSY = drawY;
				double colEX = colSX + ent.width * world.zoom;
				double colEY = colSY + ent.height * world.zoom;
				
				drawHRect(colSX, colSY, colEX, colEY, 1, EColors.blue);
			}
		}
	}
	
	private void drawPosBox(double x, double y, double w, double h) {
		Player p = QoT.thePlayer;
		
		double drawX = x + w * distX;
		double drawY = y + h * distY;
		
		double offsetX = (p.startX % world.getTileWidth());
		double offsetY = (p.startY % world.getTileHeight());
		
		drawX -= offsetX * world.zoom;
		drawY -= offsetY * world.zoom;
		
		double sX = drawX;
		double sY = drawY;
		double eX = sX + w;
		double eY = sY + h;
		drawHRect(sX, sY, eX, eY, 2, EColors.red);
	}
	
	private void drawViewBox(double x, double y, double w, double h, int distX, int distY) {
		int dsX = (int) (x);
		int dsY = (int) (y);
		int deX = (int) (x + w + (distX * 2 * w));
		int deY = (int) (y + h + (distY * 2 * h));
		drawHRect(dsX, dsY, deX, deY, 2, EColors.red);
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
	
	//---------
	// Getters
	//---------
	
	public int getDistX() { return distX; }
	public int getDistY() { return distY; }
	
	//---------
	// Setters
	//---------
	
	public void setDistX(int in) { distX = in; }
	public void setDistY(int in) { distY = in; }
	
}
