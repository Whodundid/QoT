package gameSystems.mapSystem;

import assets.entities.Entity;
import assets.worldTiles.WorldTile;
import gameSystems.textureSystem.GameTexture;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import main.Game;
import mathUtil.NumberUtil;
import storageUtil.EArrayList;

public class GameWorld {
	
	public static final int DEFAULT_TILE_WIDTH = 32;
	public static final int DEFAULT_TILE_HEIGHT = 32;
	public static final int DEFAULT_RANGE = 12;
	
	protected String name;
	protected int width, height;
	protected int tileWidth, tileHeight;
	protected double zoom = 1;
	protected WorldTile[][] worldData = new WorldTile[0][0];
	protected EArrayList<Entity> entityData = new EArrayList();
	//protected EArrayList<EScript> globalScriptData;
	protected EArrayList<Region> regionData = new EArrayList();
	protected EArrayList<Region> highlightedRegions = new EArrayList();
	protected PlayerSpawnPosition playerSpawn = new PlayerSpawnPosition(this);
	
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
	}
	
	public GameWorld(File worldFile) {
		if (!loadWorldFromFile(worldFile)) {
			name = worldFile.getName();
			width = 0;
			height = 0;
			tileWidth = 0;
			tileHeight = 0;
			worldData = new WorldTile[0][0];
			entityData = new EArrayList();
			fileLoaded = false;
		}
		else {
			fileLoaded = true;
		}
	}
	
	public void setDefaultValues() {
		name = "Unnamed";
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
	
	public void highlightRegion(Region regionIn) {
		highlightedRegions.add(regionIn);
	}
	
	public void addRegion(Region regionIn) {
		regionData.add(regionIn);
	}
	
	public void entityEnteredRegion(Region region, Entity ent, int xIn, int yIn) {
		
	}
	
	public void entityExitedRegion(Region region, Entity ent, int xIn, int yIn) {
		
	}
	
	public Entity addEntity(Entity ent) {
		entityData.add(ent);
		ent.world = this;
		return ent;
	}
	
	public synchronized WorldTile[][] getTilesAroundPoint(int xIn, int yIn) { return getTilesAroundPoint(xIn, yIn, DEFAULT_RANGE); }
	public synchronized WorldTile[][] getTilesAroundPoint(int xIn, int yIn, int range) { return getTilesAroundPoint(xIn, yIn, range, range); }
	public synchronized WorldTile[][] getTilesAroundPoint(int xIn, int yIn, int rangeX, int rangeY) {
		int left = NumberUtil.clamp(xIn - rangeX, 0, width - 1);
		int top = NumberUtil.clamp(yIn - rangeY, 0, height - 1);
		int right = NumberUtil.clamp(xIn + rangeX, left, width - 1);
		int bot = NumberUtil.clamp(yIn + rangeY, top, height - 1);
		int w = right - left;
		int h = bot - top;
		
		//catch negative dimensions
		if (w < 0 || h < 0) { return new WorldTile[0][0]; }
		
		WorldTile[][] retArr = new WorldTile[w + 1][h + 1];
		
		for (int i = left, x = 0; i <= right; i++, x++) {
			for (int j = top, y = 0; j <= bot; j++, y++) {
				retArr[x][y] = worldData[i][j];
			}
		}
		
		return retArr;
	}
	
	public void convertFileA() {
		File f = getWorldFile();
		if (f != null && f.exists()) {
			try (Scanner reader = new Scanner(f)) {
				
				EArrayList<Integer> ids = new EArrayList();
				
				String mapName = reader.nextLine();
				int mapWidth = reader.nextInt();
				int mapHeight = reader.nextInt();
				int mapTileWidth = reader.nextInt();
				int mapTileHeight = reader.nextInt();
				reader.nextLine();
				
				for (int i = 0; i < mapWidth; i++) {
					for (int j = 0; j < mapHeight; j++) {
						if (reader.hasNextLine()) {
							String tile = reader.nextLine();
							//System.out.println("LINE: " + tile);
							if (!tile.isBlank()) {
								Scanner tileReader = new Scanner(tile);
								
								String tileName = null;
								String texture = null;
								EArrayList<String> additionalArgs = new EArrayList();
								
								if (tileReader.hasNext()) { tileName = tileReader.next(); }
								ids.add(WorldTile.getTileFromName(tileName).getID());
								
								tileReader.close();
							}
						}
					}
				}
				
				PrintWriter writer = new PrintWriter(f, "UTF-8");
				
				writer.println(mapName);
				writer.println(mapWidth + " " + mapHeight + " " + mapTileWidth + " " + mapTileHeight);
				
				int n = 0;
				for (int i = 0; i < mapWidth; i++) {
					for (int j = 0; j < mapHeight; j++) {
						writer.println(ids.get(n++));
					}
				}
				
				writer.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.err.println("@ Failed to load map: " + getWorldFile() + "!");
			}
		}
	}
	
	public synchronized boolean loadWorld() { return loadWorldFromFile(new File(Game.settings.getEditorWorldsDir(), name)); }
	public synchronized boolean loadWorldFromFile(File worldFile) {
		String worldName = worldFile.getName();
		if (!worldName.endsWith(".twld")) { worldName += ".twld"; }
		worldFile = new File(Game.settings.getEditorWorldsDir(), worldName);
		
		if (worldFile != null && worldFile.exists()) {
			try (Scanner reader = new Scanner(worldFile)) {
				
				EArrayList<Entity> entities = new EArrayList();
				EArrayList<Region> regions = new EArrayList();
				
				String mapName = reader.nextLine();
				int mapWidth = reader.nextInt();
				int mapHeight = reader.nextInt();
				int mapTileWidth = reader.nextInt();
				int mapTileHeight = reader.nextInt();
				reader.nextLine();
				
				WorldTile[][] data = new WorldTile[mapWidth][mapHeight];
				
				for (int i = 0; i < mapWidth; i++) {
					for (int j = 0; j < mapHeight; j++) {
						if (reader.hasNext()) {
							String tile = reader.next();
							
							if (!tile.isBlank()) {
								int tileID = -1;
								int childID = 0;
								String[] parts = tile.split(":");
								if (!("n".equals(parts[0]) || "null".equals(parts[0]))) {
									tileID = Integer.parseInt(parts[0]);
									if (parts.length > 1) { childID = Integer.parseInt(parts[1]); }
									
									WorldTile t = WorldTile.getTileFromID(tileID, childID);
									if (parts.length == 1) { t.setWildCard(true); }
									data[i][j] = t;
								}
								else {
									data[i][j] = null;
								}
							}
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
						if (r != null) { regions.add(r); }
					}
				}
				
				name = mapName;
				width = mapWidth;
				height = mapHeight;
				tileWidth = mapTileWidth;
				tileHeight = mapTileHeight;
				worldData = data;
				regionData = regions;
				entityData = entities;
				
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
	
	public synchronized boolean saveWorldToFile() { return saveWorldToFile(name); }
	public synchronized boolean saveWorldToFile(String fileName) { return saveWorldToFile(new File(Game.settings.getEditorWorldsDir(), fileName)); }
	protected synchronized boolean saveWorldToFile(File fileIn) {
		try {
			fileIn = (fileIn.getName().endsWith(".twld")) ? fileIn : new File(fileIn.getPath() + ".twld");
			
			PrintWriter writer = new PrintWriter(fileIn, "UTF-8");
			
			//write map name and dimensions
			writer.println(name);
			writer.println(width + " " + height + " " + tileWidth + " " + tileHeight);
			
			//write map data
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					WorldTile t = worldData[i][j];
					
					if (t == null) { writer.print("n "); }
					else {
						GameTexture tex = t.getTexture();
						writer.print(t.getID() + ((tex.hasParent()) ? ":" + tex.getChildID() : "") + " ");
					}
				}
				writer.println();
			}
			
			//write region data
			for (Region r : regionData) {
				writer.println("r " + r.getName() + " " + r.getColor() + " " + r.startX + " " + r.startY + " " + r.endX + " " + r.endY);
			}
			
			writer.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public GameWorld fillWith(WorldTile t) { return fillWith(t, true); }
	public GameWorld fillWith(WorldTile t, boolean randomize) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				worldData[i][j] = WorldTile.randVariant(t);
			}
		}
		return this;
	}
	
	//---------
	// Getters
	//---------
	
	public EArrayList<Region> getHighlightedRegions() { return highlightedRegions; }
	public EArrayList<Region> getRegionData() { return regionData; }
	public boolean isFileLoaded() { return fileLoaded; }
	public boolean isLoaded() { return loaded; }
	public String getName() { return name; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getTileWidth() { return tileWidth; }
	public int getTileHeight() { return tileHeight; }
	public int getPixelWidth() { return width * tileWidth; }
	public int getPixelHeight() { return height * tileHeight; }
	public WorldTile[][] getWorldData() { return worldData; }
	public File getWorldFile() { return new File(Game.settings.getEditorWorldsDir(), name); }
	public double getZoom() { return zoom; }
	
	public WorldTile getTileAt(int xIn, int yIn) {
		return worldData[xIn][yIn];
	}
	
	public EArrayList<Entity> getEntitiesInWorld() { return entityData; }
	
	//---------
	// Setters
	//---------
	
	public GameWorld setTileAt(int xIn, int yIn, WorldTile in) {
		worldData[xIn][yIn] = in;
		return this;
	}
	
	public GameWorld setLoaded(boolean val) { loaded = val; return this; }
	public GameWorld setZoom(double val) { zoom = val; zoom = NumberUtil.clamp(zoom, 0.25, 5); return this; }
	
}
