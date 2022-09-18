package envision.gameEngine.world.gameWorld;

import java.awt.Point;
import java.io.File;

import envision.events.types.world.WorldAddedEntityEvent;
import envision.gameEngine.GameObject;
import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameObjects.entity.EntitySpawn;
import envision.gameEngine.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.gameEngine.world.worldTiles.WorldTile;
import envision.gameEngine.world.worldUtil.Region;
import envision.gameEngine.world.worldUtil.WorldCamera;
import envision.layers.LayerSystem;
import envision_lang._launch.EnvisionProgram;
import eutil.datatypes.EArrayList;
import eutil.debug.Inefficient;
import eutil.math.EDimension;
import eutil.math.EDimensionI;
import eutil.math.ENumUtil;
import eutil.misc.Direction;
import game.QoT;

public class GameWorld implements IGameWorld {
	
	//-------------------------------------------------------------
	
	private int nextEntityID = 0;
	public int getNextEntityID() { return nextEntityID++; }
	
	//-------------------------------------------------------------
	
	public static final int DEFAULT_TILE_WIDTH = 32;
	public static final int DEFAULT_TILE_HEIGHT = 32;
	public static final int DEFAULT_RANGE = 12;
	
	protected String name;
	protected File worldFile;
	protected int width, height;
	protected int tileWidth, tileHeight;
	protected WorldTile[][] worldData = new WorldTile[0][0];
	protected EArrayList<GameObject> worldObjects = new EArrayList<>();
	protected EArrayList<Entity> entityData = new EArrayList<>();
	protected EArrayList<EntitySpawn> entitySpawns = new EArrayList<>();
	protected EArrayList<Region> regionData = new EArrayList<>();
	protected PlayerSpawnPoint playerSpawn = new PlayerSpawnPoint(this);
	/** The script to be executed when the world first gets loaded. */
	protected EnvisionProgram worldLoadScript;
	protected WorldRenderer worldRenderer;
	protected WorldCamera camera;
	protected boolean underground = false;
	protected LayerSystem layers = new LayerSystem();
	
	/** The current time of day measured in game ticks. */
	protected long timeOfDay = 0l;
	/** The full length of one day measured in game ticks.
	 *  Default is 10 min based on 60 tps. */
	protected long lengthOfDay = 36000l;
	
	protected final WorldFileSystem worldFileSystem;
	
	/** IDK WHAT THIS ONE IS!!! */
	private boolean loaded = false;
	private boolean fileLoaded = false;
	
	private EArrayList<GameObject> toDelete = new EArrayList<>();
	private EArrayList<GameObject> toAdd = new EArrayList<>();
	
	//--------------
	// Constructors
	//--------------
	
	public GameWorld(String nameIn, int widthIn, int heightIn) { this(nameIn, widthIn, heightIn, DEFAULT_TILE_WIDTH, DEFAULT_TILE_HEIGHT); }
	public GameWorld(String nameIn, int widthIn, int heightIn, int tileWidthIn, int tileHeightIn) {
		name = nameIn;
		width = widthIn;
		height = heightIn;
		tileWidth = tileWidthIn;
		tileHeight = tileHeightIn;
		worldData = new WorldTile[width][height];
		worldObjects = new EArrayList<>();
		entityData = new EArrayList<>();
		fileLoaded = true;
		worldFileSystem = new WorldFileSystem(this);
		worldRenderer = new WorldRenderer(this);
		camera = new WorldCamera(this);
	}
	
	public GameWorld(File worldFile) {
		worldFileSystem = new WorldFileSystem(this);
		
		if (!worldFileSystem.loadWorldFromFile(worldFile)) {
			name = worldFile.getName();
			setDefaultValues();
		}
		else {
			fileLoaded = true;
			this.worldFile = worldFile;
		}
		
		worldRenderer = new WorldRenderer(this);
		camera = new WorldCamera(this);
	}
	
	/**
	 * Copies the existing world.
	 * 
	 * @param worldIn
	 */
	public GameWorld(GameWorld worldIn) {
		name = worldIn.name;
		width = worldIn.width;
		height = worldIn.height;
		tileWidth = worldIn.tileWidth;
		tileHeight = worldIn.tileHeight;
		underground = worldIn.underground;
		playerSpawn = worldIn.playerSpawn;
		worldData = new WorldTile[width][height];
		worldObjects = new EArrayList<>();
		entityData = new EArrayList<>();
		
		//copy tile data
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				var tile = worldIn.getTileAt(i, j);
				if (tile == null) continue;
				worldData[i][j] = WorldTile.copy(worldIn.getTileAt(i, j));
			}
		}
		
		//copy entity data
		for (GameObject obj : worldIn.worldObjects) worldObjects.add(obj);
		for (EntitySpawn spawn : worldIn.entitySpawns) entitySpawns.add(new EntitySpawn(spawn));
		for (Entity ent : worldIn.entityData) entityData.add(ent);
		
		//copy region data
		for (Region r : worldIn.regionData) regionData.add(r);
		
		worldFileSystem = new WorldFileSystem(this);
		worldRenderer = new WorldRenderer(this);
		camera = new WorldCamera(this);
		fileLoaded = true;
	}
	
	public void setDefaultValues() {
		//name = "Unnamed";
		width = 0;
		height = 0;
		tileWidth = 0;
		tileHeight = 0;
		worldData = new WorldTile[0][0];
		worldObjects = new EArrayList<>();
		entityData = new EArrayList<>();
		fileLoaded = false;
	}
	
	//---------
	// Methods
	//---------
	
	public void onInitialLoad() {
		if (worldLoadScript != null) {
			try {
				QoT.envision.runProgram(worldLoadScript);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		worldRenderer.onWorldLoaded();
		
		if (QoT.thePlayer != null) {
			addEntity(QoT.thePlayer);
			QoT.thePlayer.setWorldPos(playerSpawn.getX(), playerSpawn.getY());
			camera.setFocusedObject(QoT.thePlayer);
		}
	}
	
	public synchronized void onGameTick() {
		//update time of day
		if (timeOfDay++ >= lengthOfDay) timeOfDay = 0;
		
		//add all incoming game objects
		if (toAdd.isNotEmpty()) {
			for (GameObject o : toAdd) {
				//if entity -- add to entity data
				if (o instanceof Entity e) addEntityInternal(e);
				worldObjects.add(o);
			}
			toAdd.clear();
		}
		
		//remove all outgoing objects
		if (toDelete.isNotEmpty()) {
			for (GameObject o : toDelete) {
				//if entity -- remove from entity data
				if (o instanceof Entity e) entityData.remove(e);
				worldObjects.remove(o);
			}
			toDelete.clear();
		}
		
		updateEntities();
		updateRegions();
	}
	
	public void setPlayerSpawnPosition(int x, int y) {
		playerSpawn.setPos(x, y);
	}
	
	public void addRegion(Region regionIn) {
		regionData.add(regionIn);
	}
	
	public void updateEntities() {
		for (int i = 0; i < entityData.size(); i++) {
			GameObject e = entityData.get(i);
			if (e != null) {
				e.onGameTick();
			}
		}
		
		//remove null entities
		int nullEntities = 0;
		for (int i = 0; i < entityData.size(); i++) {
			if (entityData.get(i) == null) {
				entityData.remove(i--);
				nullEntities++;
			}
		}
		
		if (nullEntities > 0) {
			QoT.warn("'" + nullEntities + "' were null in world: '" + getWorldName() + "' and have been removed!");
		}
		
		//temporary world tile update
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				WorldTile t = worldData[y][x];
				if (t != null) t.onWorldTick();
			}
		}
	}
	
	/**
	 * Iterates across each region and then each entity to see if an entity is
	 * entering or exiting it.
	 */
	@Inefficient(reason="Could impact performance if there are too many regions/entities in world")
	protected void updateRegions() {
		//this is not efficient. :')
		
		for (Region r : regionData) {
			EDimensionI rDims = r.getRegionDimensions();
			//re-evaluate current region data
			r.updateRegion();
			
			//check if any entities are in a region or are entering or exiting one
			for (Entity ent : entityData) {
				EDimension entDims = ent.getCollisionDims();
				
				if (rDims.partiallyContains(entDims)) {
					//check if 'r' already contains the entity
					if (r.containsEntity(ent)) continue;
					r.addEntity(ent);
				}
			}
		}
	}
	
	//Any GameObject
	public synchronized <E extends GameObject> E addObjectToWorld(E ent) { return (E) toAdd.addR(ent); }
	public synchronized <E extends GameObject> void addObjectToWorld(E... ents) { toAdd.add(ents); }
	public synchronized <E extends GameObject> void removeObjectFromWorld(E... ents) { toDelete.add(ents); }
	
	//Entity specific
	public synchronized Entity addEntity(Entity ent) { return addObjectToWorld(ent); }
	public synchronized void addEntity(Entity... ents) { addObjectToWorld(ents); }
	public synchronized void removeEntity(Entity... ents) { removeObjectFromWorld(ents); }
	
	private void addEntityInternal(Entity ent) {
		//assign world and add
		ent.world = this;
		entityData.add(ent);
		
		//assign entity ID
		ent.setObjectID(getNextEntityID());
		QoT.getEventHandler().postEvent(new WorldAddedEntityEvent(this, ent));
		
		//check if player
		if (ent == QoT.thePlayer) {
			QoT.thePlayer.setWorldPos(playerSpawn.getX(), playerSpawn.getY());
		}
	}
	
	public void addEntitySpawn(Entity entIn) {
		entitySpawns.add(new EntitySpawn(entIn.worldX, entIn.worldY, entIn));
	}
	
	public void addEntitySpawn(EntitySpawn spawnIn) {
		entitySpawns.add(spawnIn);
	}
	
	public void spawnEntities() {
		entitySpawns.forEach(e -> e.spawnEntity(this));
	}
	
	//----------------------------------
	// Map Entity Distance Calculations
	//----------------------------------
	
	/**
	 * Returns the distance in pixels from one entity to another. Returns
	 * -1 in the event that the entities aren't in the world or they are null.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double getDistance(GameObject a, GameObject b) {
		if (a == null || b == null) return -1;
		if (!worldObjects.containsEach(a, b)) return -1;
		
		return ENumUtil.distance(a.midX, a.midY, b.midX, b.midY);
	}
	
	public double distanceTo(GameObject ent, Point point) {
		if (ent == null || point == null) return -1;
		if (!worldObjects.contains(ent)) return -1;
		
		return ENumUtil.distance(ent.midX, ent.midY, point.x, point.y);
	}
	
	public Direction getDirectionTo(GameObject start, GameObject dest) {
		if (start == null || dest == null) return Direction.OUT;
		if (!worldObjects.containsEach(start, dest)) return Direction.OUT;
		
		double dX = dest.midX - start.midX;
		double dY = dest.midY - start.midY;
		
		if (dX == 0)
			if (dY > 0) return Direction.S;
			else if (dY == 0) return Direction.OUT;
			else if (dY < 0) return Direction.N;
		if (dY == 0)
			if (dX > 0) return Direction.E;
			else if (dX == 0) return Direction.OUT;
			else if (dX < 0) return Direction.W;
		if (dX > 0)
			if (dY > 0) return Direction.SE;
			else return Direction.NE;
		else if (dX < 0)
			if (dY > 0) return Direction.SW;
			else return Direction.NW;
		
		return Direction.OUT;
	}
	
	/**
	 * Sets all world tiles to have a light level of 0.
	 */
	public void resetLighting() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				WorldTile t = getTileAt(j, i);
				if (t == null) continue;
				t.setLightLevel(0);
			}
		}
	}
	
	public void calculateWorldBrightness() {
		resetLighting();
		
		//bresenham's line algorithm
	}
	
	//--------------------------
	// WORLD LOADING AND SAVING
	//--------------------------
	
	public synchronized boolean loadWorld() { return worldFileSystem.loadWorld(); }
	public synchronized boolean saveWorldToFile() { return worldFileSystem.saveWorldToFile(); }
	
	//---------
	// Getters
	//---------
	
	@Override public EArrayList<Region> getRegionData() { return regionData; }
	public boolean isFileLoaded() { return fileLoaded; }
	public boolean isLoaded() { return loaded; }
	@Override public String getWorldName() { return name; }
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
	@Override public int getTileWidth() { return tileWidth; }
	@Override public int getTileHeight() { return tileHeight; }
	public int getPixelWidth() { return width * tileWidth; }
	public int getPixelHeight() { return height * tileHeight; }
	public WorldTile[][] getWorldData() { return worldData; }
	public boolean isUnderground() { return underground; }
	@Override public PlayerSpawnPoint getPlayerSpawn() { return playerSpawn; }
	public EnvisionProgram getStartupScript() { return worldLoadScript; }
	
	/** The file path to this specific map file. */
	public String getFilePath() { return worldFileSystem.getFilePath(); }
	/** The actual map file that directly pertains to this world. */
	public File getWorldFile() { return worldFileSystem.getWorldFile(); }
	/** The map directory that contains data for a specific map. */
	public File getWorldDir() { return worldFileSystem.getWorldDir(); }
	/** Returns true if the file representing this map actually exists on the file system. */
	public boolean exists() { return worldFileSystem.exists(); }
	/** Returns this world's file system manager. */
	public WorldFileSystem getWorldFileSystem() { return worldFileSystem; }
	
	@Override
	public WorldTile getTileAt(int xIn, int yIn) {
		return worldData[yIn][xIn];
	}
	
	@Override public EArrayList<GameObject> getObjectsInWorld() { return worldObjects; }
	@Override public EArrayList<Entity> getEntitiesInWorld() { return entityData; }
	public EArrayList<EntitySpawn> getEntitySpawns() { return entitySpawns; }
	
	public long getTimeOfDay() { return timeOfDay; }
	public long getLengthOfDay() { return lengthOfDay; }
	
	/** Returns this world's rendering system. */
	public WorldRenderer getWorldRenderer() { return worldRenderer; }
	
	public LayerSystem getLayerSystem() { return layers; }
	
	@Override public WorldCamera getCamera() { return camera; }
	@Override public double getCameraZoom() { return camera.getZoom(); }
	@Override public void setCameraZoom(double zoomIn) { camera.setZoom(zoomIn); }
	
	//---------
	// Setters
	//---------
	
	@Override
	public void setWorldName(String nameIn) {
		name = nameIn;
	}
	
	@Override
	public void setTileAt(WorldTile in, int xIn, int yIn) {
		if (in != null) in.setWorldPos(xIn, yIn);
		worldData[yIn][xIn] = in;
	}
	
	/**
	 * Changes the dimensions of the map.
	 * WARNING! This action completely clears the map data!
	 * 
	 * @param widthIn
	 * @param heightIn
	 */
	public void setWorldDims(int widthIn, int heightIn, int tileWidthIn, int tileHeightIn) {
		worldData = new WorldTile[heightIn][widthIn];
		this.width = widthIn;
		this.height = heightIn;
		this.tileWidth = tileWidthIn;
		this.tileHeight = tileHeightIn;
	}
	
	public void setEntityData(EArrayList<Entity> entsIn) { entityData = entsIn; }
	public void setEntitySpawns(EArrayList<EntitySpawn> spawns) { entitySpawns = spawns; }
	public void setRegionData(EArrayList<Region> regions) { regionData = regions; }
	public void setPlayerSpawn(PlayerSpawnPoint point) { playerSpawn = point; }
	
	public GameWorld setLoaded(boolean val) { loaded = val && isFileLoaded(); return this; }
	public GameWorld setWorldLoadScript(EnvisionProgram scriptIn) { worldLoadScript = scriptIn; return this; }
	public GameWorld setUnderground(boolean val) { underground = val; return this; }
	
	public GameWorld setTimeOfDay(long timeInTicks) {
		timeOfDay = ENumUtil.clamp(timeInTicks, 0, lengthOfDay);
		return this;
	}
	public GameWorld setLengthOfDay(long timeInTicks) {
		lengthOfDay = ENumUtil.clamp(timeInTicks, 0, Long.MAX_VALUE);
		return this;
	}
	
	public GameWorld fillWith(WorldTile t) { return fillWith(t, true); }
	public GameWorld fillWith(WorldTile t, boolean randomize) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				worldData[i][j] = WorldTile.randVariant(t).setWorldPos(i, j);
			}
		}
		return this;
	}
	
}