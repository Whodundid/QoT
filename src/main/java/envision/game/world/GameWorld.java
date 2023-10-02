package envision.game.world;

import java.io.File;

import envision.Envision;
import envision.debug.DebugSettings;
import envision.engine.events.eventTypes.world.WorldAddedEntityEvent;
import envision.game.GameObject;
import envision.game.entities.Entity;
import envision.game.entities.EntitySpawn;
import envision.game.manager.LevelManager;
import envision.game.manager.rules.Rule_DoDaylightCycle;
import envision.game.world.layerSystem.LayerSystem;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.WorldTile;
import envision_lang._launch.EnvisionProgram;
import eutil.datatypes.EArrayList;
import eutil.datatypes.points.Point2d;
import eutil.datatypes.util.EList;
import eutil.debug.Inefficient;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.math.dimensions.Dimension_i;
import eutil.misc.Direction;
import qot.world_tiles.VoidTile;

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
	
	protected EList<WorldLayer> worldLayers = new EArrayList<>();
	
	//protected WorldTile[][] worldData = new WorldTile[0][0];
	protected EList<GameObject> worldObjects = EList.newList();
	protected EList<Entity> entityData = EList.newList();
	protected EList<EntitySpawn> entitySpawns = EList.newList();
	protected EList<Region> regionData = EList.newList();
	protected PlayerSpawnPoint playerSpawn = new PlayerSpawnPoint(this);
	/** The script to be executed when the world first gets loaded. */
	protected EnvisionProgram worldLoadScript;
	protected WorldRenderer worldRenderer;
	protected WorldCamera camera;
	protected boolean underground = false;
	protected LayerSystem layers = new LayerSystem();
	
	/** The time of day that the world will start with if there wasn't a previous world to inherit from. */
	protected int initialTime = 120000; // mid day
	/** The current time of day measured in game ticks. */
	protected int timeOfDay = 0;
	/**
	 * The full length of one day measured in game ticks. Default is 10 min
	 * based on 60 tps.
	 */
	protected int lengthOfDay = 240000;
	protected int ambientLightLevel = 255; // between [0-255]
	protected boolean isDay = false;
	protected boolean isNight = false;
	protected boolean isSunrise = false;
	protected boolean isSunset = false;
	
	protected final WorldFileSystem worldFileSystem;
	
	/** This one is true if this world is THE active world in the engine. */
	private boolean loaded = false;
	private boolean fileLoaded = false;
	
	private EList<GameObject> toDelete = EList.newList();
	private EList<GameObject> toAdd = EList.newList();
	
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
		
		WorldLayer layer0 = new WorldLayer(width, height);
		worldLayers.add(layer0);
				
		worldObjects = EList.newList();
		entityData = EList.newList();
		fileLoaded = true;
		worldFileSystem = new WorldFileSystem(this);
		worldRenderer = new WorldRenderer(this);
		camera = new WorldCamera(this);
		
		timeOfDay = lengthOfDay / 2;
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
		
		timeOfDay = lengthOfDay / 2;
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
		
		worldObjects = EList.newList();
		entityData = EList.newList();
		
		//copy tile data
		
		for (int i = 0; i < worldIn.worldLayers.size(); i++) {
			worldLayers.add(worldIn.worldLayers.get(i).copyLayer());
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
		
		timeOfDay = lengthOfDay / 2;
	}
	
	public void setDefaultValues() {
		//name = "Unnamed";
		width = 0;
		height = 0;
		tileWidth = 0;
		tileHeight = 0;
		
		WorldLayer layer0 = new WorldLayer(width, height);
		worldLayers.add(layer0);
		
		worldObjects = EList.newList();
		entityData = EList.newList();
		fileLoaded = false;
		
		timeOfDay = lengthOfDay / 2;
	}
	
	//---------
	// Methods
	//---------
	
	@Override
	public void onLoad(String... args) {
		if (worldLoadScript != null) {
			try {
				//Envision.envision.runProgram(worldLoadScript);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		worldRenderer.onWorldLoaded();
		
		if (Envision.thePlayer != null) {
			if (entityData.notContains(Envision.thePlayer) && toAdd.notContains(Envision.thePlayer)) {
				addEntity(Envision.thePlayer);
			}
			Envision.thePlayer.setWorldPos(playerSpawn.getX(), playerSpawn.getY());
			camera.setFocusedObject(Envision.thePlayer);
		}
	}
	
	public void onRenderTick(float partialTicks) {
		getWorldRenderer().onRenderTick(partialTicks);
	}
	
	public synchronized void onGameTick(float dt) {
		//update time of day
		updateTime();
		
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
		
		updateEntities(dt);
		updateRegions(dt);
	}
	
	public void setPlayerSpawnPosition(int x, int y) {
		playerSpawn.setPos(x, y);
	}
	
	public void addRegion(Region regionIn) {
		regionData.add(regionIn);
	}
	
	public void updateEntities(float dt) {
		for (int i = 0; i < entityData.size(); i++) {
			GameObject e = entityData.get(i);
			if (e != null) {
				e.onGameTick(dt);
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
			Envision.warn("'" + nullEntities + "' were null in world: '" + getWorldName() + "' and have been removed!");
		}
		
		//temporary world tile update
		
		int size = worldLayers.size();
		for (int i = 0; i < size; i++) {
			worldLayers.get(i).onWorldTick();
		}
	}
	
	/**
	 * Iterates across each region and then each entity to see if an entity is
	 * entering or exiting it.
	 */
	@Inefficient(reason="Could impact performance if there are too many regions/entities in world")
	protected void updateRegions(float dt) {
		//this is not efficient. :')
		
		for (Region r : regionData) {
			Dimension_i rDims = r.getRegionDimensions();
			//re-evaluate current region data
			r.updateRegion(dt);
			
			//check if any entities are in a region or are entering or exiting one
			for (Entity ent : entityData) {
				Dimension_d entDims = ent.getCollisionDims();
				
				if (rDims.partiallyContains(entDims)) {
					//check if 'r' already contains the entity
					if (r.containsEntity(ent)) continue;
					r.addEntity(ent);
				}
			}
		}
	}
	
	/**
	 * Is there a better way to do this?
	 * <p>
	 * You BET your sweet BIPPY there is!
	 */
	protected void updateTime() {
	    if (!LevelManager.isLoaded()) return;
	    if (!LevelManager.rules().isRuleEnabledAndEquals(Rule_DoDaylightCycle.NAME, true)) return;
	    
        if (timeOfDay++ >= lengthOfDay) timeOfDay = 0;
        
        int minLight = 100; // the minimum brightness of the world
        int maxLight = 255; // the maximum brightness of the world
        int deltaLight = maxLight - minLight;

        // if underground, don't bother doing daylight calculations
        if (underground) {
            ambientLightLevel = minLight;
            return;
        }
        
        int transitionPeriodLength = lengthOfDay / 4;
        int sunrise = (int) (lengthOfDay * 0.2);
        int sunriseEnd = sunrise + transitionPeriodLength;
        int sunset = (int) (lengthOfDay * 0.6D);          
        int sunsetEnd = sunset + transitionPeriodLength;
        
        // check still night
        if (timeOfDay < sunrise) {
            isNight = true; isDay = false; isSunrise = false; isSunset = false;
            ambientLightLevel = minLight;
        }
        // check sunrise
        else if (timeOfDay <= (sunrise + transitionPeriodLength)) {
            isNight = false; isDay = false; isSunrise = true; isSunset = false;
            ambientLightLevel = minLight + ((timeOfDay - sunrise) * deltaLight) / transitionPeriodLength;
        }
        // check sunset
        else if (timeOfDay >= sunset && timeOfDay <= (sunset + transitionPeriodLength)) {
            isNight = false; isDay = false; isSunrise = false; isSunset = true;
            ambientLightLevel = minLight + (deltaLight - (((timeOfDay - sunset) * deltaLight) / transitionPeriodLength));
        }
        // check day
        else if (timeOfDay >= sunriseEnd && timeOfDay < sunset) {
            isNight = false; isDay = true; isSunrise = false; isSunset = false;
            ambientLightLevel = maxLight;
        }
        // check night
        else if (timeOfDay >= sunsetEnd) {
            isNight = true; isDay = false; isSunrise = false; isSunset = false;
            ambientLightLevel = minLight;
        }
	}
	
	// Any GameObject
	@Override public synchronized <E extends GameObject> E addObjectToWorld(E ent) { return (E) toAdd.addR(ent); }
	@Override public synchronized <E extends GameObject> void addObjectToWorld(E... ents) { toAdd.add(ents); }
	@Override public synchronized <E extends GameObject> void removeObjectFromWorld(E... ents) { toDelete.add(ents); }
	
	// Entity specific
	@Override public synchronized Entity addEntity(Entity ent) { return addObjectToWorld(ent); }
	@Override public synchronized void addEntity(Entity... ents) { addObjectToWorld(ents); }
	@Override public synchronized void removeEntity(Entity... ents) { removeObjectFromWorld(ents); }
	
	private void addEntityInternal(Entity ent) {
		//assign world and add
		ent.world = this;
		entityData.add(ent);
		
		//assign entity ID
		ent.setWorldID(getNextEntityID());
		ent.setWorldPos(ent.worldX, ent.worldY);
		ent.onAddedToWorld(this);
		Envision.getEventHandler().postEvent(new WorldAddedEntityEvent(this, ent));
		
		//check if player
		if (ent == Envision.thePlayer) {
			Envision.thePlayer.setWorldPos(playerSpawn.getX(), playerSpawn.getY());
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
	@Override
	public double getDistance(GameObject a, GameObject b) {
		if (a == null || b == null) return -1;
		if (!worldObjects.containsEach(a, b)) return -1;
		
		//var ax = a.midX + a.collisionBox.midX;
		//var ay = a.midY + a.collisionBox.midY;
		//var bx = b.midX + b.collisionBox.midX;
		//var by = b.midY + b.collisionBox.midY;
		
		var ax = a.midX;
		var ay = a.midY;
		var bx = b.midX;
		var by = b.midY;
		
		return ENumUtil.distance(ax, ay, bx, by);
	}
	
	@Override
	public double distanceTo(GameObject ent, Point2d point) {
		if (ent == null || point == null) return -1;
		if (!worldObjects.contains(ent)) return -1;
		
		return ENumUtil.distance(ent.midX, ent.midY, point.x, point.y);
	}
	
	@Override
	public EList<GameObject> getAllGameObjectsWithinDistance(GameObject obj, double maxDistance) {
		if (!worldObjects.contains(obj)) return EList.newList();
		if (maxDistance <= 0) return EList.newList();
		
		int arrLen = worldObjects.size() / 4;
		if (arrLen <= 10) arrLen = 10;
		EList<GameObject> r = new EArrayList<>(arrLen);
		
		final int size = worldObjects.size();
		for (int i = 0; i < size; i++) {
			final GameObject o = worldObjects.get(i);
			final double dist = ENumUtil.distance(o.midX, o.midY, obj.midX, obj.midY);
			
			if (dist < maxDistance) r.add(o);
		}
		
		return r;
	}
	
	@Override
	public EList<Entity> getAllEntitiesWithinDistance(GameObject obj, double maxDistance) {
		if (!worldObjects.contains(obj)) return EList.newList();
		if (maxDistance <= 0) return EList.newList();
		
		int arrLen = worldObjects.size() / 4;
		if (arrLen <= 10) arrLen = 10;
		EList<Entity> r = new EArrayList<>(arrLen);
		
		var list = worldObjects.stream()
							   .filter(Entity.class::isInstance)
							   .map(o -> (Entity) o)
							   .collect(EList.toEList());
		
		final int size = list.size();
		for (int i = 0; i < size; i++) {
			final Entity o = list.get(i);
			final double dist = ENumUtil.distance(o.midX, o.midY, obj.midX, obj.midY);
			
			if (dist < maxDistance) r.add(o);
		}
		
		return r;
	}
	
	@Override
	public Direction getDirectionTo(GameObject start, GameObject dest) {
		if (start == null || dest == null) return Direction.OUT;
		if (!worldObjects.containsEach(start, dest)) return Direction.OUT;
		
		double dX = dest.midX - start.midX;
		double dY = dest.midY - start.midY;
		
		// to prevent entity 'shaking'
		if (dX > -1.0 && dX < 1.00) dX = 0;
		if (dY > -1.0 && dY < 1.00) dY = 0;
		
		if (dX == 0)
			if (dY > 0) return Direction.S;
			else if (dY == 0) return Direction.OUT;
			else if (dY < 0) return Direction.N;
		if (dY == 0)
			if (dX > 0) return Direction.E;
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
	
	/** This is trash :) */
	public void setNumWorldLayers(int num) {
		for (int i = 0; i < num; i++) {
			addWorldLayer();
		}
	}
	
	public WorldLayer addWorldLayer() {
		return worldLayers.addR(new WorldLayer(width, height));
	}
	
	public void addWorldLayer(WorldLayer layerIn) {
		worldLayers.add(layerIn);
	}
	
	public void removeWorldLayer(int layer) {
		worldLayers.remove(layer);
	}
	
	//--------------------------
	// WORLD LOADING AND SAVING
	//--------------------------
	
	public synchronized boolean loadWorld() { return worldFileSystem.loadWorld(); }
	public synchronized boolean saveWorldToFile() { return worldFileSystem.saveWorldToFile(); }
	
	//---------
	// Getters
	//---------
	
	@Override public EList<Region> getRegionData() { return regionData; }
	public boolean isFileLoaded() { return fileLoaded; }
	public boolean isLoaded() { return loaded; }
	@Override public String getWorldName() { return name; }
	@Override public int getWidth() { return width; }
	@Override public int getHeight() { return height; }
	@Override public int getTileWidth() { return tileWidth; }
	@Override public int getTileHeight() { return tileHeight; }
	@Override public int getPixelWidth() { return width * tileWidth; }
	@Override public int getPixelHeight() { return height * tileHeight; }
	public WorldTile[][] getWorldLayerData() { return getWorldLayerData(0); }
	public WorldTile[][] getWorldLayerData(int layer) { return worldLayers.get(layer).worldData; }
	public boolean isUnderground() { return underground; }
	@Override public PlayerSpawnPoint getPlayerSpawn() { return playerSpawn; }
	@Override public void setPlayerSpawn(int x, int y) { playerSpawn = new PlayerSpawnPoint(this, x, y); }
	@Override public EnvisionProgram getStartupScript() { return worldLoadScript; }
	@Override public void setStartupScript(EnvisionProgram program) { worldLoadScript = program; }
	
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
	public EList<WorldLayer> getWorldLayers() { return worldLayers; }
	
	@Override
	public WorldTile getTileAt(int layer, int xIn, int yIn) {
		return worldLayers.get(layer).getTileAt(xIn, yIn);
	}
	
	@Override public EList<GameObject> getObjectsInWorld() { return worldObjects; }
	@Override public EList<Entity> getEntitiesInWorld() { return entityData; }
	public EList<EntitySpawn> getEntitySpawns() { return entitySpawns; }
	
	/** Returns this world's rendering system. */
	public WorldRenderer getWorldRenderer() { return worldRenderer; }
	
	public LayerSystem getLayerSystem() { return layers; }
	
	@Override public WorldCamera getCamera() { return camera; }
	@Override public double getInitialCameraZoom() { return 2.0; }
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
	public void setTileAt(WorldTile in, int layerIn, int xIn, int yIn) {
		if (in == null) in = new VoidTile();
		worldLayers.get(layerIn).setTileAt(in, xIn, yIn);
	}
	
	/**
	 * Changes the dimensions of the map.
	 * WARNING! This action completely clears the map data!
	 * 
	 * @param widthIn
	 * @param heightIn
	 */
	public void setWorldDims(int widthIn, int heightIn, int tileWidthIn, int tileHeightIn) {
		worldLayers.forEach(l -> l.setDimensions(widthIn, heightIn));
		this.width = widthIn;
		this.height = heightIn;
		this.tileWidth = tileWidthIn;
		this.tileHeight = tileHeightIn;
	}
	
	public void setEntityData(EList<Entity> entsIn) { entityData = entsIn; }
	public void setEntitySpawns(EList<EntitySpawn> spawns) { entitySpawns = spawns; }
	public void setRegionData(EList<Region> regions) { regionData = regions; }
	public void setPlayerSpawn(PlayerSpawnPoint point) { playerSpawn = point; }
	
	@Override public void setLoaded(boolean val) { loaded = val && isFileLoaded(); }
	public void setUnderground(boolean val) { underground = val; }
	
	public void fillWith(WorldTile t) { fillWith(0, t); }
	public void fillWith(int layer, WorldTile t) {
		worldLayers.get(layer).fillWith(t);
	}
    
	@Override public int getInitialTime() { return initialTime; }
    @Override public int getTime() { return timeOfDay; }
    @Override public int getDayLength() { return lengthOfDay; }
    @Override public void setTime(int timeInTicks) { timeOfDay = ENumUtil.clamp(timeInTicks, 0, lengthOfDay); updateTime(); }
    @Override public void setDayLength(int timeInTicks) { lengthOfDay = ENumUtil.clamp(timeInTicks, 0, lengthOfDay); updateTime(); }
    @Override public boolean isDay() { return isDay; }
    @Override public boolean isNight() { return isNight; }
    @Override public boolean isSunrise() { return isSunrise; }
    @Override public boolean isSunset() { return isSunset; }
    @Override public int getAmbientLightLevel() { return ambientLightLevel; }
    
}