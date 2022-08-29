package envision.game.world.mapEditor;

import envision.game.entity.Entity;
import envision.game.world.EntitySpawn;
import envision.game.world.GameWorld;
import envision.game.world.mapEditor.editorParts.util.EditorObject;
import envision.game.world.mapEditor.editorUtil.PlayerSpawnPosition;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.EArrayList;

/**
 * A game world that is used within the map editor.
 * 
 * @author Hunter Bragg
 */
public class EditorWorld {
	
	//--------
	// Fields
	//--------
	
	private GameWorld actualWorld;
	
	private String worldName;
	private int width, height;
	private int tileWidth, tileHeight;
	private EditorObject[][] worldData;
	private final EArrayList<EditorObject> entitiesInWorld = new EArrayList<>();
	private final EArrayList<EntitySpawn> entitySpawns = new EArrayList<>();
	private PlayerSpawnPosition playerSpawn;
	private boolean underground = false;
	
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
		worldData = new EditorObject[width][height];
	}
	
	public EditorWorld(GameWorld worldIn) {
		actualWorld = worldIn;
		worldName = worldIn.getName();
		width = worldIn.getWidth();
		height = worldIn.getHeight();
		tileWidth = worldIn.getTileWidth();
		tileHeight = worldIn.getTileWidth();
		playerSpawn = worldIn.getPlayerSpawn();
		underground = worldIn.isUnderground();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				WorldTile t = worldIn.getTileAt(i, j);
				if (t == null) continue;
				EditorObject obj = EditorObject.of(t);
				worldData[i][j] = obj;
			}
		}
		
		for (var spawn : worldIn.getEntitySpawns()) {
			var ent = spawn.getEntity(worldIn);
			var obj = EditorObject.of(ent);
			entitiesInWorld.add(obj);
		}
	}
	
	//---------
	// Methods
	//---------
	
	public EditorWorld fillWith(WorldTile t) { return fillWith(t, true); }
	public EditorWorld fillWith(WorldTile t, boolean randomize) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				var tile = WorldTile.randVariant(t).setWorldPos(i, j);
				worldData[i][j] = EditorObject.of(tile);
			}
		}
		return this;
	}
	
	//---------
	// Getters
	//---------
	
	public EArrayList<Entity> getEntitiesInWorld() {
		return entitiesInWorld.map(obj -> (Entity) obj.getGameObject());
	}
	
	public EArrayList<EntitySpawn> getEntitySpawns() {
		return entitySpawns;
	}
	
}
