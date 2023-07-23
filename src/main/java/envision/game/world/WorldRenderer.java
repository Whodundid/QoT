package envision.game.world;

import envision.Envision;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.rendering.RenderingManager;
import envision.engine.screens.GameTopScreen;
import envision.engine.windows.windowUtil.EGui;
import envision.game.world.layerSystem.WorldDrawLayer;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;

//Author: Hunter Bragg

/** Handles rendering GameWorlds. */
public class WorldRenderer extends EGui {
	
	//--------
	// Fields
	//--------
	
	private GameWorld world;
	private int distX = 22;
	private int distY = 22;

	/** the world coordinates at the center of the screen. */
	public int midDrawX, midDrawY;
	
	//private int viewDist = 18;
	
	public static boolean drawPosBox = false;
	public static boolean drawEntityHitboxes = false;
	public static boolean drawEntityOutlines = false;
	
	private int left, top, right, bot;
	
	/** Temporary list for testing world layers. */
	private EList<WorldDrawLayer> worldLayers = EList.newList();
	
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
	public synchronized void onWorldLoaded() {
		onWindowResized();
		if (world != null) {
			//load entities
			world.spawnEntities();
			
			TEMP_createWorldLayers();
			
			//world.setCameraZoom(3);
		}
	}
	
	private void TEMP_createWorldLayers() {
		WorldDrawLayer layerZero = new WorldDrawLayer(world, 0);
		WorldDrawLayer layerOne = new WorldDrawLayer(world, 1);
		//WorldLayer layerTwo = new WorldLayer(world, 2);
		
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				WorldTile t = world.getTileAt(j, i);
				if (t == null) continue;
				else if (!t.isWall() || t.getWallHeight() < 0.20) {
					t.setRenderLayer(0);
				}
				else {
					t.setRenderLayer(1);
				}
			}
		}
		
//		for (var ent : world.getEntitiesInWorld()) {
//			ent.setRenderLayer(2);
//		}
		
		worldLayers.add(layerZero);
		worldLayers.add(layerOne);
		//worldLayers.add(layerTwo);
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
			//if (typedChar == '.') viewDist++;
			//if (typedChar == ',') viewDist--;
		}
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Should be called from main game render tick, every tick.
	 */
	public void onRenderTick(float partialTicks) {
		world.camera.onRenderTick(partialTicks);
		renderWorld(partialTicks);
	}
	
    private void renderWorld(float partialTicks) {
        if (world == null) {
            RenderingManager.drawStringC("Failed to load!", midX, midY);
            return;
        }
        
        if (world.width < 0 || world.height < 0 || world.tileWidth <= 0 || world.tileHeight <= 0) {
            RenderingManager.drawStringC("Bad world dimensions!", midX, midY);
            return;
        }
        
        //drawRect(0xff4fedff);
        //if (!world.isUnderground()) drawRect(0xff4fbaff);
        //else
        drawRect(0xff000000);
        
        //pixel width of each tile
        double w = (int) (world.getTileWidth() * world.getCameraZoom());
        //pixel height of each tile
        double h = (int) (world.getTileHeight() * world.getCameraZoom());
        
        //the left most x coordinate for map drawing
        double x = (int) (midX - (distX * w) - (w / 2));
        //the top most y coordinate for map drawing
        double y = (int) (midY - (distY * h) - (h / 2));
        
        renderMapLayers();
        
//      if (drawPosBox) {
//          drawPosBox(x, y, w, h);
//      }
        
//        if (WorldRenderer.drawPosBox && world.camera.getFocusedObject() != null) {
//            RenderingManager.drawHRect(tileX, tileY, tileX + w, tileY + h, 4, EColors.red);
//        }
//        
//        if (WorldRenderer.drawEntityHitboxes) {
//            double colSX = drawX + (collisionBox.startX * zoom);
//            double colSY = drawY + (collisionBox.startY * zoom);
//            double colEX = colSX + (collisionBox.width * zoom);
//            double colEY = colSY + (collisionBox.height * zoom);
//            
//            RenderingManager.drawHRect(colSX - 1, colSY, colEX, colEY - 1, 1, EColors.yellow);
//        }
//        
//        if (WorldRenderer.drawEntityOutlines) {
//            RenderingManager.drawHRect(drawX, drawY, drawX + drawW, drawY + drawH, 1, EColors.blue);
//        }
    }
	
	private void renderMapLayers() {
		final var cam = world.camera;
		final var worldWidth = world.getWidth();
		final var worldHeight = world.getHeight();
		
		// keep the player at the center of the world
		midDrawX = cam.getWorldX();
		midDrawY = cam.getWorldY();
		
		// calculations to determine how many tiles to draw out in each direction from the mid of the screen
		left = ENumUtil.clamp(midDrawX - distX, 0, worldWidth - 1);
		top = ENumUtil.clamp(midDrawY - distY, 0, worldHeight - 1);
		right = ENumUtil.clamp(midDrawX + distX, left, worldWidth - 1);
		bot = ENumUtil.clamp(midDrawY + distY, top, worldHeight - 1);
		
		final int size = worldLayers.size();
		for (int i = 0; i < size; i++) {
			final var layer = worldLayers.get(i);
			
			layer.buildLayer(left, top, right, bot);
			layer.renderLayer(world, cam, midDrawX, midDrawY, midX, midY, distX, distY);
		}
	}
	
//	private void drawPosBox(double x, double y, double w, double h) {
//		Player p = Envision.thePlayer;
//		
//		double drawX = x + w * distX;
//		double drawY = y + h * distY;
//		
//		double offsetX = (p.startX % world.getTileWidth());
//		double offsetY = (p.startY % world.getTileHeight());
//		
//		drawX -= offsetX * world.getCameraZoom();
//		drawY -= offsetY * world.getCameraZoom();
//		
//		double sX = drawX;
//		double sY = drawY;
//		double eX = sX + w;
//		double eY = sY + h;
//		GLObject.drawHRect(sX, sY, eX, eY, 2, EColors.red);
//	}
	
	public void onWindowResized() {
		res = Envision.getWindowDims();
		setDimensions(0, 0, res.width, res.height);
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
