package envision.game.world.layerSystem;

import envision.game.objects.GameObject;
import envision.game.objects.IDrawable;
import envision.game.objects.InsertionSort;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.EArrayList;
import eutil.datatypes.ExpandableGrid;

public class WorldDrawLayer {
	
	//--------
	// Fields
	//--------
	
	private int layer = 0;
	
	private IGameWorld world;
	private ExpandableGrid<WorldTile> worldData;
	private EArrayList<GameObject> gameObjects;
	
	private EArrayList<IDrawable> builtLayer = new EArrayList<>();
	private boolean built = false;
	
	//--------------
	// Constructors
	//--------------
	
	public WorldDrawLayer(IGameWorld worldIn, int layerIn) {
		world = worldIn;
		layer = layerIn;
		worldData = new ExpandableGrid<>(world.getWidth(), world.getHeight());
		gameObjects = new EArrayList<>();
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Gathers all world tiles and entities within the given region to be
	 * rendered. Can be called independently from renderLayer in order to
	 * save on processing time. For instance, this method could be called
	 * every game tick instead of every frame tick.
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bot
	 */
	public void buildLayer(int left, int top, int right, int bot) {
		built = false;
		builtLayer.clear();
		
		//get data
		worldData.clear();
		gameObjects.clear();
		
		for (int i = 0; i < world.getHeight(); i++) {
			for (int j = 0; j < world.getWidth(); j++) {
				worldData.set(world.getTileAt(j, i), j, i);
			}
		}
		
		gameObjects.addAll(world.getEntitiesInWorld());
		
		//add all world tiles within the specified area
		for (int i = top; i <= bot; i++) {
			for (int j = left; j <= right; j++) {
				var tile = worldData.get(j, i);
				if (tile == null) continue;
				if (tile.getRenderLayer() == layer) builtLayer.add(tile);
			}
		}
		
		if (layer == 1) {
			//add all objects within the specified area
			for (var obj : gameObjects) {
				//if (obj.getDimensions().contains(left, top, right, bot)) {
					builtLayer.add(obj);
				//}
			}
		}
		
		//System.out.println(builtLayer);
		
		built = true;
	}
	
	/**
	 * To be called on every frame tick.
	 * 
	 * @param world
	 * @param midX TODO
	 * @param midY TODO
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param brightness
	 * @param mouseOver
	 */
	public void renderLayer(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY, int distX, int distY) {
		if (!built) return;
		
		//sort the layer before draw
		InsertionSort.sort(builtLayer);
//		if (layer == 1) {
//			for (var e : builtLayer) {
//				DebugToolKit.println(e, e.getSortPoint());
//			}
//		}
		
		//if (layer == 1) System.out.println(builtLayer);
		//System.out.println(builtLayer);
		
		//draw each object on the layer
		for (var obj : builtLayer) {
			//if (layer == 1) System.out.println(obj);
			obj.draw(world, camera, midDrawX, midDrawY, midX, midY, distX, distY);
		}
	}
	
	public void addObject(GameObject object) {
		gameObjects.add(object);
	}
	
	//---------
	// Getters
	//---------
	
	public IGameWorld getWorld() { return world; }
	public int getLayer() { return layer; }
	public ExpandableGrid<WorldTile> getWorldData() { return worldData; }
	public EArrayList<GameObject> getGameObjects() { return gameObjects; }
	public EArrayList<IDrawable> getDrawnObjects() { return builtLayer; }
	public boolean isBuilt() { return built; }
	
	//---------
	// Setters
	//---------
	
	public void setTileAt(WorldTile in, int xIn, int yIn) {
		worldData.set(in, xIn, yIn);
	}
	
}
