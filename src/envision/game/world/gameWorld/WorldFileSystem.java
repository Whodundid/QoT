package envision.game.world.gameWorld;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import envision.game.world.util.EntitySpawn;
import envision.game.world.util.Region;
import envision.game.world.worldTiles.WorldTile;
import envision.renderEngine.textureSystem.GameTexture;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import game.settings.QoTSettings;

public class WorldFileSystem {
	
	//--------
	// Fields
	//--------
	
	/** The underlying game world for which this file system pertains to. */
	private final GameWorld theWorld;
	/** True if the world's primary directories have been created. */
	private boolean created = false;
	/** True if the world has been successfully loaded. */
	private boolean fileLoaded = false;
	
	//--------------
	// Constructors
	//--------------
	
	public WorldFileSystem(GameWorld worldIn) {
		if (worldIn == null) throw new RuntimeException("Null game world!");
		theWorld = worldIn;
	}
	
	//---------
	// Methods
	//---------
	
	public void createWorldDir() {
		created = false;
		
		File dir = getWorldDir();
		File dataDir = getDataDir();
		File scriptsDir = getScriptsDir();
		
		try {
			//try to create base dir
			//
			//NOTE: this assumes that the base 'editor worlds dir'
			//      was already successfully created
			if (!dir.exists() && !dir.mkdir())
				throw new RuntimeException("Failed to create world dir! " + theWorld.getWorldName());
			
			//try to create map sub directories
			if (!dataDir.exists() && !dataDir.mkdir())
				throw new RuntimeException("Failed to create map data dir! " + theWorld.getWorldName());
			
			if (!scriptsDir.exists() && !scriptsDir.mkdir())
				throw new RuntimeException("Failed to create map scripts dir! " + theWorld.getWorldName());
			
			//creation success
			created = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//-------------
	// Map Loading
	//-------------
	
	protected synchronized boolean loadWorld() {
		return loadWorldFromFile(getWorldFile());
	}
	
	private File findExistingWorldFile(File toFind) {
		if (toFind == null) return null;
		
		File existing = null;
		String toFindName = toFind.getName();
		
		//strip '.twld' to try to find world of matching name
		toFindName = toFindName.replace(".twld", "");
		
		//iterate across editor worlds dir to try and find a matching name
		for (File f : QoTSettings.getEditorWorldsDir().listFiles()) {
			String fName = f.getName();
			fName = fName.replace(".twld", "");
			
			if (EUtil.isEqual(fName, toFindName)) {
				existing = f;
				break;
			}
		}
		
		return existing;
	}
	
	protected synchronized boolean loadWorldFromFile(File worldFile) {
		if (worldFile == null) return false;
		
		//check if loading direct file path -- otherwise try to parse file name and path
		if (!worldFile.exists()) {
			worldFile = findExistingWorldFile(worldFile);
			if (worldFile == null) return false;
			theWorld.name = worldFile.getName().replace(".twld", "");
			worldFile = getWorldFile();
		}
		
		//System.out.println("THE WORLD FILE: " + worldFile);
		
		//ensure that the world is actually a thyrah world
		if (!worldFile.toString().endsWith(".twld")) return false;
		//ensure the file actually exists before trying to read from it
		if (!worldFile.exists()) return false;
		
		//try to read file
		try (Scanner reader = new Scanner(worldFile)) {
			EArrayList<EntitySpawn> entitySpawnsIn = new EArrayList<>();
			EArrayList<Region> regions = new EArrayList<>();
			
			String mapName = reader.nextLine();
			int mapWidth = reader.nextInt();
			int mapHeight = reader.nextInt();
			int mapTileWidth = reader.nextInt();
			int mapTileHeight = reader.nextInt();
			int spawnX = reader.nextInt();
			int spawnY = reader.nextInt();
			
			reader.nextLine();
			
			WorldTile[][] data = new WorldTile[mapHeight][mapWidth];
			
			for (int i = 0; i < mapHeight; i++) {
				for (int j = 0; j < mapWidth; j++) {
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
							t.setWorldPos(j, i);
						}
						data[j][i] = t;
					}
					else {
						data[j][i] = null;
					}
				}
			}
			
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				if (line.startsWith("r")) {
					Region r = Region.parseRegion(theWorld, line);
					if (r != null) regions.add(r);
				}
				if (line.startsWith("ent")) {
					EntitySpawn spawn = EntitySpawn.parse(line);
					if (spawn != null) entitySpawnsIn.add(spawn);
				}
			}
			
			if (reader.hasNextLine()) {
				String nextLine = reader.nextLine();
				if (nextLine.equals("underground")) theWorld.underground = true;
			}
			
			theWorld.name = mapName;
			theWorld.width = mapWidth;
			theWorld.height = mapHeight;
			theWorld.tileWidth = mapTileWidth;
			theWorld.tileHeight = mapTileHeight;
			theWorld.playerSpawn.setX(spawnX);
			theWorld.playerSpawn.setY(spawnY);
			theWorld.worldData = data;
			theWorld.regionData = regions;
			theWorld.entitySpawns = entitySpawnsIn;
			
			fileLoaded = true;
			//System.out.println("fileLoaded: " + fileLoaded);
			
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("@ Failed to load map: " + worldFile.getName() + "!");
		}
		
		return false;
	}
	
	private void performLoad(File worldFile) {
		
	}
	
	//------------
	// Map Saving
	//------------
	
	protected synchronized boolean saveWorldToFile() {
		return saveWorldToFile(getWorldFile());
	}
	
	protected synchronized boolean saveWorldToFile(File fileIn) {
		try { return performSave(fileIn); }
		catch (Exception e) { e.printStackTrace(); }
		return false;
	}
	
	private boolean performSave(File fileIn) throws FileNotFoundException, UnsupportedEncodingException {
		createWorldDir();
		
		PrintWriter writer = new PrintWriter(fileIn, "UTF-8");
		
		//write map name and dimensions
		writer.println(theWorld.name);
		writer.println(theWorld.width + " " + theWorld.height + " " + theWorld.tileWidth + " " + theWorld.tileHeight);
		writer.println(theWorld.playerSpawn.getX() + " " + theWorld.playerSpawn.getY());
		
		//write map data
		for (int i = 0; i < theWorld.width; i++) {
			for (int j = 0; j < theWorld.height; j++) {
				WorldTile t = theWorld.worldData[i][j];
				
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
		theWorld.regionData.stream().filter(s -> s != null).forEach(writer::println);
		//write entity spawn data
		theWorld.entitySpawns.stream().filter(s -> s != null).forEach(writer::println);
		//underground
		if (theWorld.underground) writer.println("underground");
		
		writer.close();
		return true;
	}
	
	//---------
	// Getters
	//---------
	
	/** The file path to this specific map file. */
	public String getFilePath() { return getWorldFile().toString(); }
	/** The actual map file that directly pertains to this world. */
	public File getWorldFile() { return new File(getWorldDir(), theWorld.name + ".twld"); }
	/** The map directory that contains data for a specific map. */
	public File getWorldDir() { return new File(QoTSettings.getEditorWorldsDir(), theWorld.name); }
	/** The map directory that contains any specific, referencable data that is needed. */
	public File getDataDir() { return new File(getWorldDir(), "data"); }
	/** The map directory that contains any (and all) world/entity scripts. */
	public File getScriptsDir() { return new File(getWorldDir(), "scripts"); }
	/** Returns true if the file representing this map actually exists on the file system. */
	public boolean exists() { return getWorldFile().exists(); }
	
}
