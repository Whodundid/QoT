package world;

import engine.renderEngine.textureSystem.GameTexture;
import eutil.datatypes.EArrayList;
import eutil.math.NumberUtil;
import eutil.misc.Direction;
import game.entities.Entity;
import main.QoT;
import main.settings.QoTSettings;
import world.mapEditor.editorUtil.PlayerSpawnPosition;
import world.worldTiles.WorldTile;

import java.awt.Point;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class GameWorld {
	
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
	protected double zoom = 1;
	protected WorldTile[][] worldData = new WorldTile[0][0];
	protected EArrayList<Entity> entityData = new EArrayList();
	protected EArrayList<EntitySpawn> entitySpawns = new EArrayList();
	protected EArrayList<Region> regionData = new EArrayList();
	protected EArrayList<Region> highlightedRegions = new EArrayList();
	protected PlayerSpawnPosition playerSpawn = new PlayerSpawnPosition(this);
	protected WorldRenderer worldRenderer;
	protected boolean underground = false;
	
	private boolean loaded = false;
	private boolean fileLoaded = false;
	
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
		entityData = new EArrayList();
		fileLoaded = true;
		
		worldRenderer = new WorldRenderer(this);
	}
	
	public GameWorld(File worldFile) {
		if (!loadWorldFromFile(worldFile)) {
			name = worldFile.getName();
			setDefaultValues();
		}
		else {
			fileLoaded = true;
			this.worldFile = worldFile;
		}
		
		worldRenderer = new WorldRenderer(this);
	}
	
	public void setDefaultValues() {
		//name = "Unnamed";
		width = 0;
		height = 0;
		tileWidth = 0;
		tileHeight = 0;
		zoom = 1;
		worldData = new WorldTile[0][0];
		entityData = new EArrayList();
		fileLoaded = false;
	}
	
	//---------
	// Methods
	//---------
	
	public void setPlayerSpawnPosition(int x, int y) {
		playerSpawn.setPos(x, y);
	}
	
	//---------
	// Regions
	//---------
	
	public void highlightRegion(Region regionIn) {
		highlightedRegions.add(regionIn);
	}
	
	public void addRegion(Region regionIn) {
		regionData.add(regionIn);
	}
	
	public void updateEntities() {
		for (int i = 0; i < entityData.size(); i++) {
			Entity e = entityData.get(i);
			if (e != null) {
				e.onLivingUpdate();
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
			QoT.warn("'" + nullEntities + "' were null in world: '" + getName() + "' and have been removed!");
		}
		
		//temporary world tile update
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				WorldTile t = worldData[x][y];
				if (t != null) t.onWorldTick();
			}
		}
	}
	
	public Entity addEntity(Entity ent) {
		//assign world and add
		ent.world = this;
		entityData.add(ent);
		
		//assign entity ID
		ent.setEntityID(getNextEntityID());
		
		//check if player
		if (ent == QoT.thePlayer) {
			QoT.thePlayer.setWorldPos(playerSpawn.getX(), playerSpawn.getY());
		}
		
		return ent;
	}
	
	public void addEntitySpawn(Entity entIn) {
		entitySpawns.add(new EntitySpawn(entIn.worldX, entIn.worldY, entIn));
	}
	
	public void addEntitySpawn(EntitySpawn spawnIn) {
		entitySpawns.add(spawnIn);
	}
	
	//----------------------------------
	// Map Entity Distance Calculations
	//----------------------------------
	
	/**
	 * Returns the distance in pixels from one entity to another. Returns
	 * -1 in the event that the entities aren't in the world or they are null.
	 * 
	 * @param ent1
	 * @param ent2
	 * @return
	 */
	public double getDistance(Entity ent1, Entity ent2) {
		if (ent1 == null || ent2 == null) return -1;
		if (!entityData.containsEach(ent1, ent2)) return -1;
		
		return NumberUtil.distance(ent1.midX, ent1.midY, ent2.midX, ent2.midY);
	}
	
	public double distanceTo(Entity ent, Point point) {
		if (ent == null || point == null) return -1;
		if (!entityData.contains(ent)) return -1;
		
		return NumberUtil.distance(ent.midX, ent.midY, point.x, point.y);
	}
	
	public Direction getDirectionTo(Entity start, Entity dest) {
		if (start == null || dest == null) return Direction.OUT;
		if (!entityData.containsEach(start, dest)) return Direction.OUT;
		
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
	
	//--------------------------
	// WORLD LOADING AND SAVING
	//--------------------------
	
	public synchronized boolean loadWorld() { return loadWorldFromFile(new File(QoTSettings.getEditorWorldsDir(), name)); }
	public synchronized boolean loadWorldFromFile(File worldFile) {
		String worldName = worldFile.getName();
		if (!worldName.endsWith(".twld")) worldName += ".twld";
		worldFile = new File(QoTSettings.getEditorWorldsDir(), worldName);
		
		if (worldFile != null && worldFile.exists()) {
			try (Scanner reader = new Scanner(worldFile)) {
				
				EArrayList<EntitySpawn> entitySpawnsIn = new EArrayList();
				EArrayList<Region> regions = new EArrayList();
				
				String mapName = reader.nextLine();
				int mapWidth = reader.nextInt();
				int mapHeight = reader.nextInt();
				int mapTileWidth = reader.nextInt();
				int mapTileHeight = reader.nextInt();
				int spawnX = reader.nextInt();
				int spawnY = reader.nextInt();
				
				reader.nextLine();
				
				WorldTile[][] data = new WorldTile[mapWidth][mapHeight];
				
				for (int i = 0; i < mapWidth; i++) {
					for (int j = 0; j < mapHeight; j++) {
						if (!reader.hasNext()) {
							data[i][j] = null;
							continue;
						}
						
						String tile = reader.next();
						if (tile.isBlank()) continue;
						
						int tileID = -1;
						int childID = 0;
						String[] parts = tile.split(":");
						
						if (!("n".equals(parts[0]) || "null".equals(parts[0]))) {
							tileID = Integer.parseInt(parts[0]);
							if (parts.length > 1) childID = Integer.parseInt(parts[1]);
							
							WorldTile t = WorldTile.getTileFromID(tileID, childID);
							if (t == null) System.out.println("NULL: " + tileID + " : " + childID);
							if (t != null) {
								if (parts.length == 1) t.setWildCard(true);
								t.setWorldPos(i, j);
							}
							data[i][j] = t;
						}
						else {
							data[i][j] = null;
						}
					}
				}
				
				while (reader.hasNextLine()) {
					String line = reader.nextLine();
					if (line.startsWith("r")) {
						Region r = Region.parseRegion(this, line);
						if (r != null) regions.add(r);
					}
					if (line.startsWith("ent")) {
						EntitySpawn spawn = EntitySpawn.parse(line);
						if (spawn != null) entitySpawnsIn.add(spawn);
					}
				}
				
				if (reader.hasNextLine()) {
					String nextLine = reader.nextLine();
					if (nextLine.equals("underground")) underground = true;
				}
				
				name = mapName;
				width = mapWidth;
				height = mapHeight;
				tileWidth = mapTileWidth;
				tileHeight = mapTileHeight;
				playerSpawn.setX(spawnX);
				playerSpawn.setY(spawnY);
				worldData = data;
				regionData = regions;
				entitySpawns = entitySpawnsIn;
				
				fileLoaded = true;
				//System.out.println("fileLoaded: " + fileLoaded);
				
				return true;
			}
			catch (Exception e) {
				e.printStackTrace();
				System.err.println("@ Failed to load map: " + worldFile.getName() + "!");
			}
		}
		return false;
	}
	
	public synchronized boolean saveWorldToFile() { return saveWorldToFile(new File(QoTSettings.getEditorWorldsDir(), name)); }
	protected synchronized boolean saveWorldToFile(File fileIn) {
		try {
			fileIn = (fileIn.getName().endsWith(".twld")) ? fileIn : new File(fileIn.getPath() + ".twld");
			
			PrintWriter writer = new PrintWriter(fileIn, "UTF-8");
			
			//write map name and dimensions
			writer.println(name);
			writer.println(width + " " + height + " " + tileWidth + " " + tileHeight);
			writer.println(playerSpawn.getX() + " " + playerSpawn.getY());
			
			//write map data
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					WorldTile t = worldData[i][j];
					
					if (t == null) writer.print("n ");
					else {
						GameTexture tex = t.getTexture();
						if (tex == null) writer.print("n ");
						else writer.print(t.getID() + ((tex.hasParent()) ? ":" + tex.getChildID() : "") + " ");
					}
				}
				writer.println();
			}
			
			//write region data
			regionData.stream().filter(s -> s != null).forEach(writer::println);
			//write entity spawn data
			entitySpawns.stream().filter(s -> s != null).forEach(writer::println);
			//underground
			if (underground) writer.println("underground");
			
			writer.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//---------
	// Getters
	//---------
	
	public EArrayList<Region> getHighlightedRegions() { return highlightedRegions; }
	public EArrayList<Region> getRegionData() { return regionData; }
	public boolean isFileLoaded() { return fileLoaded; }
	public boolean isLoaded() { return loaded; }
	public String getName() { return name; }
	public String getFileName() { return QoTSettings.getEditorWorldsDir().toString() + "\\" + name + ".twld"; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileWidth() { return tileWidth; }
	public int getTileHeight() { return tileHeight; }
	public int getPixelWidth() { return width * tileWidth; }
	public int getPixelHeight() { return height * tileHeight; }
	public WorldTile[][] getWorldData() { return worldData; }
	public File getWorldFile() { return new File(getFileName()); }
	public boolean exists() { return getWorldFile().exists(); }
	public double getZoom() { return zoom; }
	public boolean isUnderground() { return underground; }
	
	public WorldTile getTileAt(int xIn, int yIn) {
		return worldData[xIn][yIn];
	}
	
	public EArrayList<Entity> getEntitiesInWorld() { return entityData; }
	public EArrayList<EntitySpawn> getEntitySpawns() { return entitySpawns; }
	
	/** Returns this world's rendering system. */
	public WorldRenderer getWorldRenderer() { return worldRenderer; }
	
	//---------
	// Setters
	//---------
	
	public GameWorld setWorldName(String nameIn) {
		name = nameIn;
		return this;
	}
	
	public GameWorld setTileAt(int xIn, int yIn, WorldTile in) {
		if (in != null) in.setWorldPos(xIn, yIn);
		worldData[xIn][yIn] = in;
		return this;
	}
	
	public GameWorld setLoaded(boolean val) { loaded = val && isFileLoaded(); return this; }
	public GameWorld setZoom(double val) { zoom = val; zoom = NumberUtil.clamp(zoom, 0.25, 5); return this; }
	public GameWorld setUnderground(boolean val) { underground = val; return this; }
	
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