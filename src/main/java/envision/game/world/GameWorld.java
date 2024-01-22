package envision.game.world;

import java.io.File;

import envision.Envision;
import envision.engine.events.eventTypes.world.WorldAddedEntityEvent;
import envision.game.GameObject;
import envision.game.entities.Doodad;
import envision.game.entities.Entity;
import envision.game.entities.EntitySpawn;
import envision.game.world.layerSystem.LayerSystem;
import envision.game.world.worldEditor.editorUtil.PlayerSpawnPoint;
import envision.game.world.worldTiles.VoidTile;
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

public class GameWorld implements IGameWorld {
	
	//-------------------------------------------------------------
	
	private int nextEntityID = 0;
	public String getNextEntityID() { return String.valueOf(nextEntityID++); }
	
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
	protected boolean underground = false;
	protected LayerSystem layers = new LayerSystem();
	
	protected final WorldFileSystem worldFileSystem;
	
	/** This one is true if this world is THE active world in the engine. */
	private boolean loaded = false;
	private boolean fileLoaded = false;
	public boolean isFirstLoad = true;
	
	private EList<GameObject> toDelete = EList.newList();
	private EList<GameObject> toAdd = EList.newList();
	
	public int lastPlayerWorldX, lastPlayerWorldY;
	public double lastPlayerStartX, lastPlayerStartY;
	
	//==============
    // Constructors
    //==============
	
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
		fileLoaded = true;
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
	}
	
	//=========
    // Methods
    //=========
	
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
		
		if (isFirstLoad) {
		    // load entities
	        spawnEntities();
		    
		    isFirstLoad = false;
		}
		
		worldRenderer.onWorldLoaded();
		
		if (Envision.thePlayer != null) {
			if (entityData.notContains(Envision.thePlayer) && toAdd.notContains(Envision.thePlayer)) {
				addEntity(Envision.thePlayer);
			}
			Envision.thePlayer.setPixelPos(playerSpawn.startX, playerSpawn.startY);
			Envision.levelManager.getCamera().setFocusedObject(Envision.thePlayer);
		}
	}
	
	public void onRenderTick(float partialTicks) {
		getWorldRenderer().onRenderTick(partialTicks);
	}
	
	public synchronized void onGameTick(float dt) {
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
	
	public void setPlayerSpawnPosition(double x, double y) {
		playerSpawn.setPixelPos(x, y);
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
		ent.setPixelPos(ent.startX, ent.startY);
		ent.onAddedToWorld(this);
		Envision.getEventHandler().postEvent(new WorldAddedEntityEvent(this, ent));
		
		//check if player
		if (ent == Envision.thePlayer) {
		    Envision.thePlayer.setPixelPos(playerSpawn.startX, playerSpawn.startY);
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
		
		var ac = a.getCollisionDims();
		var bc = b.getCollisionDims();
		
//		double ax = a.midX + a.collisionBox.midX;
//		double ay = a.midY + a.collisionBox.midY;
//		double bx = b.midX + b.collisionBox.midX;
//		double by = b.midY + b.collisionBox.midY;
		
//		var ax = a.midX;
//		var ay = a.midY;
//		var bx = b.midX;
//		var by = b.midY;
		
		return ENumUtil.distance(ac.midX, ac.midY, bc.midX, bc.midY);
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
    public EList<Entity> getAllEntitiesWithinSquaredDistance(GameObject obj, double maxDistance) {
	    if (!worldObjects.contains(obj)) return EList.newList();
        if (maxDistance <= 0) return EList.newList();
        
        int arrLen = worldObjects.size() / 4;
        if (arrLen <= 10) arrLen = 10;
        EList<Entity> r = new EArrayList<>(arrLen);
        
        var list = worldObjects.stream()
                               .filter(e -> e != obj)
                               .filter(Entity.class::isInstance)
                               .filter(e -> !(e instanceof Doodad))
                               .map(o -> (Entity) o)
                               .collect(EList.toEList());
        
        final int size = list.size();
        for (int i = 0; i < size; i++) {
            final Entity o = list.get(i);
            final double dist = o.midX * obj.midX + o.midY * obj.midY;
            
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
		                       .filter(e -> e != obj)
							   .filter(Entity.class::isInstance)
							   .map(o -> (Entity) o)
							   .filter(e -> !(e instanceof Doodad))
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
	
	@Override
	public double getAngleInDegressTo(GameObject start, GameObject dest) {
	    if (start == null || dest == null) return Double.NaN;
        if (!worldObjects.containsEach(start, dest)) return Double.NaN;
	    
	    double y = start.midY - dest.midY;
	    double x = start.midX - dest.midX;
	    
	    double angle = Math.abs(180.0 - Math.atan2(y, x) * (180.0 / Math.PI)) % 360.0;
	    return angle;
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
	    worldLayers.clear();
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
	@Override public boolean isFileLoaded() { return fileLoaded; }
	@Override public boolean isLoaded() { return loaded; }
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
	@Override public void setPlayerSpawn(double x, double y) { playerSpawn = new PlayerSpawnPoint(this, x, y); }
	@Override public EnvisionProgram getStartupScript() { return worldLoadScript; }
	@Override public void setStartupScript(EnvisionProgram program) { worldLoadScript = program; }
	
	@Override public int getNumberOfLayers() { return worldLayers.size(); }
	
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
	
	@Override
	public int getAmbientLightLevel() {
	    int level = Envision.levelManager.getAmbientLightLevel();
	    if (underground) return 100;
	    return level;
	}
	
	//=========
    // Setters
    //=========
	
	@Override
	public void setWorldName(String nameIn) {
		name = nameIn;
	}
	
	@Override
	public void setTileAt(WorldTile in, int layerIn, int xIn, int yIn) {
		if (in == null) in = new VoidTile();
        in.setWidthHeight(tileWidth, tileHeight);
        in.setWorldPos(xIn, yIn);
        in.setCameraLayer(layerIn);
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
	    for (int y = 0; y < height; y++) {
	        for (int x = 0; x < width; x++) {
	            this.setTileAt(t, x, y);
	        }
	    }
	}
	
}