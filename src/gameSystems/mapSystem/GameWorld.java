package gameSystems.mapSystem;

import entities.Entity;
import gameSystems.mapSystem.worldTiles.WorldTile;
import gameSystems.textureSystem.GameTexture;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import main.Game;
import util.mathUtil.NumUtil;
import util.storageUtil.EArrayList;

public class GameWorld {
	
	public static final int DEFAULT_TILE_WIDTH = 32;
	public static final int DEFAULT_TILE_HEIGHT = 32;
	public static final int DEFAULT_RANGE = 12;
	
	protected String name;
	protected int width, height;
	protected int tileWidth, tileHeight;
	protected double zoom = 1;
	protected WorldTile[][] worldData;
	protected EArrayList<Entity> entityData;
	
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
	
	//---------
	// Methods
	//---------
	
	public Entity addEntity(Entity ent) {
		entityData.add(ent);
		ent.world = this;
		return ent;
	}
	
	public synchronized WorldTile[][] getTilesAroundPoint(int xIn, int yIn) { return getTilesAroundPoint(xIn, yIn, DEFAULT_RANGE); }
	public synchronized WorldTile[][] getTilesAroundPoint(int xIn, int yIn, int range) { return getTilesAroundPoint(xIn, yIn, range, range); }
	public synchronized WorldTile[][] getTilesAroundPoint(int xIn, int yIn, int rangeX, int rangeY) {
		int left = NumUtil.clamp(xIn - rangeX, 0, width - 1);
		int top = NumUtil.clamp(yIn - rangeY, 0, height - 1);
		int right = NumUtil.clamp(xIn + rangeX, left, width - 1);
		int bot = NumUtil.clamp(yIn + rangeY, top, height - 1);
		int w = right - left;
		int h = bot - top;
		
		//System.out.println(left + " " + top + " " + right + " " + bot + " : [" + w + ", " + h + "]");
		
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
	
	public void convertFile() {
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
	
	public synchronized boolean loadWorld() {
		return loadWorldFromFile(new File(Game.settings.getEditorWorldsDir(), name));
	}
	
	public synchronized boolean loadWorldFromFile(File worldFile) {
		String worldName = worldFile.getName();
		if (!worldName.endsWith(".twld")) { worldName += ".twld"; }
		worldFile = new File(Game.settings.getEditorWorldsDir(), worldName);
		
		//System.out.println(worldFile + " " + worldFile.exists());
		
		if (worldFile != null && worldFile.exists()) {
			try (Scanner reader = new Scanner(worldFile)) {
				
				String mapName = reader.nextLine();
				int mapWidth = reader.nextInt();
				int mapHeight = reader.nextInt();
				int mapTileWidth = reader.nextInt();
				int mapTileHeight = reader.nextInt();
				reader.nextLine();
				
				WorldTile[][] data = new WorldTile[mapWidth][mapHeight];
				
				for (int i = 0; i < mapWidth; i++) {
					for (int j = 0; j < mapHeight; j++) {
						if (reader.hasNextLine()) {
							String tile = reader.nextLine();
							//System.out.println("LINE: " + tile);
							if (!tile.isBlank()) {
								Scanner tileReader = new Scanner(tile);
								
								int tileID = -1;
								
								if (tileReader.hasNext()) { tileID = tileReader.nextInt(); }
								
								WorldTile t = WorldTile.getTileFromID(tileID);
								data[i][j] = t;
								
								tileReader.close();
							}
						}
						else {
							data[i][j] = null;
						}
					}
				}
				
				name = mapName;
				width = mapWidth;
				height = mapHeight;
				tileWidth = mapTileWidth;
				tileHeight = mapTileHeight;
				worldData = data;
				entityData = new EArrayList();
				
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
	
	public synchronized boolean saveWorldToFile(String fileName) {

		return saveWorldToFile(new File(Game.settings.getEditorWorldsDir(), fileName));
	}
	
	public synchronized boolean saveWorldToFile(File fileIn) {
		try {
			fileIn = (fileIn.getName().endsWith(".twld")) ? fileIn : new File(fileIn.getPath() + ".twld");
			
			PrintWriter writer = new PrintWriter(fileIn, "UTF-8");
			
			writer.println(name);
			writer.println(width + " " + height + " " + tileWidth + " " + tileHeight);
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					WorldTile t = worldData[i][j];
					
					if (t == null) { writer.println("null"); }
					else {
						GameTexture tex = t.getTexture();
						String texName = (tex != null) ? tex.getName() : "null";
						
						writer.println(t.getID());
					}
				}
			}
			
			writer.close();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public GameWorld fillWith(WorldTile t) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				worldData[i][j] = t;
			}
		}
		return this;
	}
	
	//---------
	// Getters
	//---------
	
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
	public GameWorld setZoom(double val) { zoom = val; zoom = NumUtil.clamp(zoom, 0.15, 5); return this; }
	
}
