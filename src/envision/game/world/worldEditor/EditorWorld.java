package envision.game.world.worldEditor;

import envision.game.objects.GameObject;
import envision.game.objects.entities.Entity;
import envision.game.objects.entities.EntitySpawn;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.Region;
import envision.game.world.WorldCamera;
import envision.game.world.layerSystem.LayerSystem;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.EArrayList;
import eutil.datatypes.ExpandableGrid;
import eutil.datatypes.util.AnchorPoint;
import eutil.datatypes.util.EList;
import eutil.misc.Direction;

/**
 * A game world that is used within the map editor.
 * 
 * @author Hunter Bragg
 */
public class EditorWorld implements IGameWorld {
	
	//--------
	// Fields
	//--------
	
	private int nextEntityID = 0;
	public int getNextEntityID() { return nextEntityID++; }
	
	private GameWorld actualWorld;
	
	private String worldName;
	private int width, height;
	private int tileWidth, tileHeight;
	private ExpandableGrid<EditorObject> worldData;
	private final EArrayList<EditorObject> objectsInWorld = new EArrayList<>();
	private final EArrayList<EditorObject> entitiesInWorld = new EArrayList<>();
	private final EArrayList<EntitySpawn> entitySpawns = new EArrayList<>();
	private final EArrayList<Region> regionData = new EArrayList<>();
	private PlayerSpawnPoint playerSpawn;
	private boolean underground = false;
	protected LayerSystem layers = new LayerSystem();
	
	//--------------
	// Constructors
	//--------------
	
	public EditorWorld(String nameIn, int widthIn, int heightIn) {
		this(nameIn, widthIn, heightIn, GameWorld.DEFAULT_TILE_WIDTH, GameWorld.DEFAULT_TILE_HEIGHT);
	}
	
	public EditorWorld(String nameIn, int widthIn, int heightIn, int tileWidthIn, int tileHeightIn) {
		worldName = nameIn;
		width = widthIn;
		height = heightIn;
		tileWidth = tileWidthIn;
		tileHeight = tileHeightIn;
		worldData = new ExpandableGrid<>(width, height);
	}
	
	public EditorWorld(GameWorld worldIn) {
		actualWorld = worldIn;
		worldName = worldIn.getWorldName();
		width = worldIn.getWidth();
		height = worldIn.getHeight();
		tileWidth = worldIn.getTileWidth();
		tileHeight = worldIn.getTileWidth();
		playerSpawn = worldIn.getPlayerSpawn();
		underground = worldIn.isUnderground();
		
		worldData = new ExpandableGrid<>(width, height);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				WorldTile t = worldIn.getTileAt(j, i);
				if (t == null) continue;
				EditorObject obj = EditorObject.of(t);
				worldData.set(obj, j, i);
			}
		}
		
		for (var spawn : worldIn.getEntitySpawns()) {
			var ent = spawn.getEntity(worldIn);
			var obj = EditorObject.of(ent);
			entitiesInWorld.add(obj);
			entitySpawns.add(spawn);
		}
		
		for (Region r : worldIn.getRegionData()) {
			regionData.add(r);
		}
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override public boolean isLoaded() { return true; }
	
	//---------
	// Methods
	//---------
	
	public EditorWorld fillWith(WorldTile t) { return fillWith(t, true); }
	public EditorWorld fillWith(WorldTile t, boolean randomize) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				var tile = WorldTile.randVariant(t).setWorldPos(j, i);
				worldData.set(EditorObject.of(tile), j, i);
			}
		}
		return this;
	}
	
	public EditorWorld fillWith(int layer, WorldTile t) { return fillWith(t, true); }
	public EditorWorld fillWith(int layer, WorldTile t, boolean randomize) {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				var tile = WorldTile.randVariant(t).setWorldPos(j, i);
				worldData.set(EditorObject.of(tile), j, i);
			}
		}
		return this;
	}
	
	public void expandWorld(AnchorPoint anchor, int amount) { expandWorld(anchor, amount, null); }
	public void expandWorld(AnchorPoint anchor, int amount, WorldTile tile) {
		worldData.expand(anchor, amount, EditorObject.of(tile));
		width = worldData.getWidth();
		height = worldData.getHeight();
	}
	
	public void expandWorld(Direction dir, int amount) { expandWorld(dir, amount, null); }
	public void expandWorld(Direction dir, int amount, WorldTile tile) {
		worldData.expand(dir, amount, EditorObject.of(tile));
		width = worldData.getWidth();
		height = worldData.getHeight();
	}
	
	public void addEntity(GameObject objIn) {
		//assign world and add
		objIn.world = this;
		entitiesInWorld.add(EditorObject.of(objIn));
		
		//assign entity ID
		objIn.setObjectID(getNextEntityID());
	}
	
	public void addEntitySpawn(Entity entIn) {
		entitySpawns.add(new EntitySpawn(entIn.worldX, entIn.worldY, entIn));
	}
	
	public void addEntitySpawn(EntitySpawn spawnIn) {
		entitySpawns.add(spawnIn);
	}
	
	//--------
	// Saving
	//--------
	
	public boolean saveGameWorld() {
		actualWorld.setWorldName(worldName);
		actualWorld.setWorldDims(width, height, tileWidth, tileHeight);
		actualWorld.setEntityData(getEntitiesInWorld());
		actualWorld.setEntitySpawns(entitySpawns);
		actualWorld.setRegionData(regionData);
		actualWorld.setUnderground(underground);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				var obj = worldData.get(j, i);
				WorldTile tile = (obj != null) ? obj.getTile() : null;
				actualWorld.setTileAt(tile, j, i);
			}
		}
		
		return actualWorld.saveWorldToFile();
	}
	
	//---------
	// Getters
	//---------
	
	@Override
	public WorldTile getTileAt(int xIn, int yIn) {
		var obj = worldData.get(xIn, yIn);
		return (obj != null) ? obj.getTile() : null;
	}
	
	public EditorObject getEditorTile(int xIn, int yIn) {
		return worldData.get(xIn, yIn);
	}
	
	@Override
	public EList<GameObject> getObjectsInWorld() {
		return objectsInWorld.map(obj -> obj.getGameObject());
	}
	
	@Override
	public EList<Entity> getEntitiesInWorld() {
		return entitiesInWorld.map(obj -> obj.getEntity());
	}
	
	public EArrayList<EditorObject> getEditorEntities() {
		return entitiesInWorld;
	}
	
	@Override public String getWorldName() { return worldName; }
	@Override public EArrayList<Region> getRegionData() { return regionData; }
	@Override public EArrayList<EntitySpawn> getEntitySpawns() { return entitySpawns; }
	@Override public PlayerSpawnPoint getPlayerSpawn() { return playerSpawn; }
	
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
	@Override public int getTileWidth() { return tileWidth; }
	@Override public int getTileHeight() { return tileHeight; }
	
	@Override public boolean isUnderground() { return underground; }
	
	//---------
	// Setters
	//---------
	
	@Override
	public void setTileAt(WorldTile in, int xIn, int yIn) {
		if (in != null) in.setWorldPos(xIn, yIn);
		worldData.set(EditorObject.of(in), xIn, yIn);
	}
	
	public void setTileAt(EditorObject in, int xIn, int yIn) {
		if (in == null || !in.isTile()) return;
		in.getTile().setWorldPos(xIn, yIn);
		worldData.set(in, xIn, yIn);
	}
	
	//@Override
	public void setPlayerSpawn(int x, int y) {
		playerSpawn = new PlayerSpawnPoint(actualWorld, x, y);
	}
	
	@Override
	public void setWorldName(String nameIn) {
		worldName = nameIn;
	}

	@Override public WorldCamera getCamera() { return null; }
	@Override public double getCameraZoom() { return 0; }
	@Override public void setCameraZoom(double zoomIn) {}
	
}
