package envision.game.world.worldEditor;

import java.io.File;

import envision.game.GameObject;
import envision.game.entities.Entity;
import envision.game.entities.EntitySpawn;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.Region;
import envision.game.world.WorldRenderer;
import envision.game.world.layerSystem.LayerSystem;
import envision.game.world.worldEditor.editorParts.util.EditorObject;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.VoidTile;
import envision.game.world.worldTiles.WorldTile;
import envision_lang._launch.EnvisionProgram;
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
	
	//========
    // Fields
    //========
	
	private int nextEntityID = 0;
	public String getNextEntityID() { return String.valueOf(nextEntityID++); }
	
	private GameWorld actualWorld;
	private String worldName;
	private int width, height;
	private int tileWidth, tileHeight;
	public EList<ExpandableGrid<EditorObject>> worldLayers = EList.newList();
	private final EList<EditorObject> objectsInWorld = new EArrayList<>();
	private final EList<EditorObject> entitiesInWorld = new EArrayList<>();
	private final EList<Region> regionData = new EArrayList<>();
	private PlayerSpawnPoint playerSpawn;
	private boolean underground = false;
	protected LayerSystem layers = new LayerSystem();
	
	//==============
    // Constructors
    //==============
	
	public EditorWorld(String nameIn, int widthIn, int heightIn) {
		this(nameIn, widthIn, heightIn, GameWorld.DEFAULT_TILE_WIDTH, GameWorld.DEFAULT_TILE_HEIGHT);
	}
	
	public EditorWorld(String nameIn, int widthIn, int heightIn, int tileWidthIn, int tileHeightIn) {
		worldName = nameIn;
		width = widthIn;
		height = heightIn;
		tileWidth = tileWidthIn;
		tileHeight = tileHeightIn;
		
		ExpandableGrid<EditorObject> layer0 = new ExpandableGrid<>(width, height);
		worldLayers.add(layer0);
	}
	
	public EditorWorld(GameWorld worldIn) {
		actualWorld = worldIn;
		loadWorld();
	}
	
	//===========
    // Overrides
    //===========
	
	@Override public boolean isLoaded() { return true; }
	@Override public void onGameTick(float dt) {}
	@Override public void onRenderTick(float partialTicks) {}
	@Override public WorldRenderer getWorldRenderer() { return null; }
	@Override public File getWorldFile() { return actualWorld.getWorldFile(); }
	
	//=========
    // Methods
    //=========
	
	public EditorWorld fillWith(WorldTile t) { return fillWith(0, t, true); }
	public EditorWorld fillWith(WorldTile t, boolean randomize) { return fillWith(0, t, randomize); }
	public EditorWorld fillWith(int layer, WorldTile t) { return fillWith(t, true); }
	public EditorWorld fillWith(int layer, WorldTile t, boolean randomize) {
		if (t == null) t = new VoidTile();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				var tile = WorldTile.randVariant(t);
				setTileAt(tile, layer, x, y);
			}
		}
		return this;
	}
	
	public void expandWorld(AnchorPoint anchor, int amount) { expandWorld(anchor, amount, null); }
	public void expandWorld(AnchorPoint anchor, int amount, WorldTile tile) {
		for (int i = 0; i < worldLayers.size(); i++) {
			worldLayers.get(i).expand(anchor, amount, EditorObject.of(tile));
		}
		width = worldLayers.get(0).getWidth();
		height = worldLayers.get(0).getHeight();
	}
	
	public void expandWorld(Direction dir, int amount) { expandWorld(dir, amount, new VoidTile()); }
	public void expandWorld(Direction dir, int amount, WorldTile tile) {
		if (tile == null) tile = new VoidTile();
		final var eoTile = EditorObject.of(tile);
		final int layerSize = worldLayers.size();
		
		// resize the world
		for (int i = 0; i < layerSize; i++) {
			worldLayers.get(i).expand(dir, amount, eoTile);
		}
		
		// update tile positions
		for (int l = 0; l < layerSize; l++) {
			final var curLayer = worldLayers.get(l);
			final int layerH = curLayer.getHeight();
			final int layerW = curLayer.getWidth();
			
			for (int y = 0; y < layerH; y++) {
				for (int x = 0; x < layerW; x++) {
					final var layerTile = curLayer.get(x, y).getTile();
					layerTile.setWorldPos(x, y);
				}
			}
		}
		
		switch (dir) {
//		case NE, NW, SE, SW:
//			moveObjects(amount, amount);
//			break;
		case N:
			moveObjects(0, amount);
			break;
		case W:
			moveObjects(amount, 0);
			break;
//		case LATITUDE:
//			moveObjects(0, amount);
//			break;
//		case LONGITUDE:
//			moveObjects(amount, 0);
//			break;
		default:
			break;
		}
		
		width = worldLayers.get(0).getWidth();
		height = worldLayers.get(0).getHeight();
	}
	
	private void moveObjects(int xOffset, int yOffset) {
		for (var e : getEntitiesInWorld()) {
			var x = e.worldX;
			var y = e.worldY;
			e.setWorldPos(x + xOffset, y + yOffset);
		}
		
		for (var e : getObjectsInWorld()) {
			var x = e.worldX;
			var y = e.worldY;
			e.worldX = x + xOffset;
			e.worldY = y + yOffset;
		}
		
		for (var r : getRegionData()) {
			var sx = r.startX;
			var sy = r.startY;
			var ex = r.endX;
			var ey = r.endY;
			r.startX = sx + xOffset * this.tileWidth;
			r.endX = ex + xOffset * this.tileWidth;
			r.startY = sy + yOffset * this.tileHeight;
			r.endY = ey + yOffset * this.tileHeight;
		}
		
		{
			var x = playerSpawn.startX;
			var y = playerSpawn.startY;
			setPlayerSpawn(x + xOffset, y + yOffset);
		}
	}
	
	@Override
	public GameObject addObjectToWorld(GameObject objIn) {
		//assign world and add
		objIn.world = this;
		entitiesInWorld.add(EditorObject.of(objIn));
		
		//assign entity ID
		objIn.setWorldID(getNextEntityID());
		return objIn;
	}
	
	public void addLayerAbove(int layer) {
	    if (layer < 0 || layer > worldLayers.size()) return;
	    ExpandableGrid<EditorObject> newLayer = new ExpandableGrid<>(width, height);
	    if (layer == worldLayers.size()) worldLayers.add(newLayer);
	    else worldLayers.add(layer + 1, newLayer);
	}
	
	public void addLayerBelow(int layer) {
	    if (layer < 0 || layer >= worldLayers.size()) return;
	    ExpandableGrid<EditorObject> newLayer = new ExpandableGrid<>(width, height);
	    worldLayers.add(layer, newLayer);
	}
	
	public void removeLayer(int layer) {
	    if (layer < 0 || layer >= worldLayers.size()) return;
	    if (layer == 0 && worldLayers.size() == 1) {
	        System.out.println("Can't delete only layer!");
	        return;
	    }
	    worldLayers.remove(layer);
	}
	
	//========
	// Saving
	//========
	
	@Override
	public boolean loadWorld() {
		worldName = actualWorld.getWorldName();
		width = actualWorld.getWidth();
		height = actualWorld.getHeight();
		tileWidth = actualWorld.getTileWidth();
		tileHeight = actualWorld.getTileWidth();
		playerSpawn = actualWorld.getPlayerSpawn();
		underground = actualWorld.isUnderground();
		int numLayers = actualWorld.getWorldLayers().size();
		
		for (int layerIndex = 0; layerIndex < numLayers; layerIndex++) {
			ExpandableGrid<EditorObject> layerData = new ExpandableGrid<>(width, height);
			worldLayers.add(layerData);
			
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					WorldTile t = actualWorld.getTileAt(layerIndex, j, i);
					if (t == null) t = VoidTile.instance;
					setTileAt(t, layerIndex, j, i);
				}
			}
		}
		
		for (var spawn : actualWorld.getEntitySpawns()) {
			var ent = spawn.getEntity(actualWorld);
			var obj = EditorObject.of(ent);
			entitiesInWorld.add(obj);
//			entitySpawns.add(spawn);
		}
		
		for (Region r : actualWorld.getRegionData()) {
			regionData.add(r);
		}
		
		return true;
	}
	
	@Override
	public boolean saveWorldToFile() {
		actualWorld.setWorldName(worldName);
		actualWorld.setWorldDims(width, height, tileWidth, tileHeight);
		actualWorld.setEntityData(getEntitiesInWorld());
		actualWorld.setRegionData(regionData);
		actualWorld.setUnderground(underground);
		actualWorld.setPlayerSpawn(playerSpawn);
		
		EList<EntitySpawn> spawns = new EArrayList<>();
		for (var ent : getEntitiesInWorld()) {
			spawns.add(new EntitySpawn((int) ent.startX, (int) ent.startY, ent));
		}
		
		actualWorld.setEntitySpawns(spawns);
		actualWorld.setNumWorldLayers(worldLayers.size());
		
		for (int i = 0; i < worldLayers.size(); i++) {
			var layer = worldLayers.get(i);
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					var obj = layer.get(x, y);
					WorldTile tile = (obj != null) ? obj.getTile() : null;
					actualWorld.setTileAt(tile, i, x, y);
				}
			}
		}
		
		return actualWorld.saveWorldToFile();
	}
	
	//=========
    // Getters
    //=========
	
	@Override
	public WorldTile getTileAt(int layerIn, int xIn, int yIn) {
		var obj = worldLayers.get(layerIn).get(xIn, yIn);
		return (obj != null) ? obj.getTile() : null;
	}
	
	public EditorObject getEditorTile(int layerIn, int xIn, int yIn) {
		return worldLayers.get(layerIn).get(xIn, yIn);
	}
	
	@Override public int getNumberOfLayers() { return worldLayers.size(); }
	
	@Override
	public EList<GameObject> getObjectsInWorld() {
		return objectsInWorld.map(obj -> obj.getGameObject());
	}
	
	@Override
	public EList<Entity> getEntitiesInWorld() {
		return entitiesInWorld.map(obj -> obj.getEntity());
	}
	
	public EList<EditorObject> getEditorEntities() {
		return entitiesInWorld;
	}
	
	@Override public String getWorldName() { return worldName; }
	@Override public EList<Region> getRegionData() { return regionData; }
	@Override public EList<EntitySpawn> getEntitySpawns() { return null; }
	@Override public PlayerSpawnPoint getPlayerSpawn() { return playerSpawn; }
	
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
	@Override public int getTileWidth() { return tileWidth; }
	@Override public int getTileHeight() { return tileHeight; }
	
	@Override public boolean isUnderground() { return underground; }
	@Override public void setUnderground(boolean val) { underground = val; }
	
	//=========
    // Setters
    //=========
	
	@Override
	public void setTileAt(WorldTile in, int layerIn, int xIn, int yIn) {
		if (in == null) in = new VoidTile();
		setTileAt(EditorObject.of(in.copy()), layerIn, xIn, yIn);
	}
	
	public void setTileAt(EditorObject in, int layerIn, int xIn, int yIn) {
	    if (in == null) in = EditorObject.of(new VoidTile());
		if (!in.isTile()) return;
		WorldTile t = in.getTile();
		t.setWidthHeight(tileWidth, tileHeight);
		t.setWorldPos(xIn, yIn);
		t.setCameraLayer(layerIn);
		worldLayers.get(layerIn).set(in, xIn, yIn);
	}
	
	@Override
	public void setPlayerSpawn(double x, double y) {
		playerSpawn = new PlayerSpawnPoint(actualWorld, x, y);
	}
	
	@Override
	public void setWorldName(String nameIn) {
		worldName = nameIn;
	}
	
	@Override public void onLoad(String... args) {}
	@Override public void setLoaded(boolean val) {}

	@Override public EnvisionProgram getStartupScript() { return actualWorld.getStartupScript(); }
	@Override public void setStartupScript(EnvisionProgram program) { actualWorld.setStartupScript(program); }
	
}
