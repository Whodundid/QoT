package envision.game.world.gameWorld;

import envision.game.GameObject;
import envision.game.world.worldTiles.WorldTile;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.renderEngine.textureSystem.GameTexture;
import envision.topOverlay.GameTopScreen;
import envision.util.InsertionSort;
import envision.windowLib.windowUtil.EGui;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.ENumUtil;
import game.QoT;
import game.entities.player.QoT_Player;

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
	}
	
	/**
	 * Called when the world has just been loaded and just before it is about
	 * to be rendered.
	 */
	public void onWorldLoaded() {
		onWindowResized();
		
		if (world != null) {
			//load entities
			world.spawnEntities();
			
			//add all walls to a separate list
			int w = world.getWidth();
			int h = world.getHeight();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					WorldTile t = world.getTileAt(j, i);
					if (t == null) continue;
					if (t.isWall()) entityOrder.add(t);
				}
			}
			
			world.setZoom(3);
			//entityOrder.addAll(world.getEntitiesInWorld());
		}
	}
	
	//------------------------
	// Overrides : GameScreen
	//------------------------
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (!GameTopScreen.isTopFocused() && !(Keyboard.isCtrlDown() || Keyboard.isAltDown() || Keyboard.isShiftDown())) {
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
		QoT_Player p = QoT.thePlayer;
		
		//keep the player at the center of the world (THIS SHOULD BE CHANGED TO 'CAMERA' AT SOME POINT!)
		midDrawX = p.worldX;
		midDrawY = p.worldY;
		
		//uses the player's (SHOULD BE A CAMERA OBJECT) pixel position as an offset to apply to rendered tiles 
		double offsetX = (p.startX % world.getTileWidth()) * world.zoom;
		double offsetY = (p.startY % world.getTileHeight()) * world.zoom;
		
		//calculations to determine how many tiles to draw out in each direction from the mid of the screen
		int left = ENumUtil.clamp(midDrawX - distX, 0, world.getWidth() - 1);
		int top = ENumUtil.clamp(midDrawY - distY, 0, world.getHeight() - 1);
		int right = ENumUtil.clamp(midDrawX + distX, left, world.getWidth() - 1);
		int bot = ENumUtil.clamp(midDrawY + distY, top, world.getHeight() - 1);
		
		//draw the tiles of the world using the calculated dimensions
		for (int i = top, iy = 0; i <= bot; i++, iy++) {
			for (int j = left, jx = 0; j <= right; j++, jx++) {
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
				double dX = drawPosX + (jx * w);
				double dY = drawPosY + (iy * h);
				
				//apply the player's (CAMERA'S) offset to the drawn tile
				dX -= offsetX;
				dY -= offsetY;
				
				//call the tile's self rendering code with the proper screen coordinates and dimensions
				t.draw(world, dX, dY, w, h, calcBrightness(j, i), false);
			}
		}
	}
	
	private void renderEntities(double x, double y, double w, double h) {
		var entities = world.getEntitiesInWorld();
		InsertionSort.sort(entities);
		
		for (int i = 0; i < entities.size(); i++) {
			GameObject ent = entities.get(i);
			GameTexture tex = ent.getTexture();
			if (tex == null) continue;
			
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
			
			//calculate the entity's draw width and height based off of actual dims and zoom
			double drawW = ent.width * world.getZoom();
			double drawH = ent.height * world.getZoom();
			
			double cmx = ent.collisionBox.midX; //collision mid x
			double cmy = ent.collisionBox.midY; //collision mid y
			double tw = world.getTileWidth(); //tile width
			double th = world.getTileHeight(); //tile height
			
			//offset world coordinates to middle of collision box
			int mwcx = (int) Math.floor(cmx / tw); //mid world coords x
			int mwcy = (int) Math.floor(cmy / th); //mid world coords y
			
			//light render position
			int lightx = ent.worldX + mwcx;
			int lighty = ent.worldY + mwcy;
			
			int mX = Mouse.getMx();
			int mY = Mouse.getMy();
			boolean mouseOver = (mX >= drawX && mX <= drawX + drawW && mY >= drawY && mY <= drawY + drawH);
			
			//render the entity
			ent.draw(world, drawX, drawY, drawW, drawH, calcBrightness(lightx, lighty), mouseOver);
			
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
		QoT_Player p = QoT.thePlayer;
		
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
			QoT_Player p = QoT.thePlayer;
			
			double cmx = p.collisionBox.midX; //collision mid x
			double cmy = p.collisionBox.midY; //collision mid y
			double tw = world.getTileWidth(); //tile width
			double th = world.getTileHeight(); //tile height
			
			//offset world coordinates to middle of collision box
			int mwcx = (int) Math.ceil(cmx / tw); //mid world coords x
			int mwcy = (int) Math.floor(cmy / th); //mid world coords y
			
			//light render position
			int lightx = p.worldX + mwcx;
			int lighty = p.worldY + mwcy;
			
			int distToPlayer = viewDist - (int) (ENumUtil.distance(x, y, lightx, lighty));
			distToPlayer = ENumUtil.clamp(distToPlayer, 0, distToPlayer);
			int ratio = (distToPlayer * 255) / viewDist;
			return EColors.changeBrightness(0xffffffff, ratio);
		}
		return 0xffffffff;
	}
	
	private void calcWorldBrightness() {
		
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
