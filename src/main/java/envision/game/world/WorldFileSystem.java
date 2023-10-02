package envision.game.world;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.entities.EntitySpawn;
import envision.game.world.worldFileSystem.WorldSavingSystem;
import envision.game.world.worldTiles.WorldTile;
import eutil.EUtil;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;
import eutil.file.LineReader;
import eutil.math.ENumUtil;
import eutil.misc.EByteBuilder;
import eutil.strings.EStringUtil;
import qot.settings.QoTSettings;
import qot.world_tiles.VoidTile;

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
		File worldConfig = getWorldConfigFile();
		
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
			
			//try to create config file
			if (!worldConfig.exists() && !worldConfig.createNewFile())
				throw new RuntimeException("Failed to create map config file! " + theWorld.getWorldName());
			
			//creation success
			created = true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateConfigFile() throws IOException {
		PrintWriter writer = new PrintWriter(getWorldConfigFile());
		
		writer.println("# " + theWorld.getWorldName() + " config");
		writer.println("# " + EStringUtil.repeatString("-", theWorld.getWorldName().length() + " config".length()));
		writer.println();
		writer.println("underground=" + theWorld.isUnderground());
		writer.println("timeOfDay=" + theWorld.getTime());
		writer.println("lengthOfDay=" + theWorld.getDayLength());
		
		writer.close();
	}
	
	//-------------
	// Map Loading
	//-------------
	
	protected synchronized boolean loadWorld() {
		return loadWorldFromFile(getWorldFile());
	}
	
	private File findExistingWorldFile(File baseDir, File toFind) {
		if (toFind == null) return null;
		
		File existing = null;
		String toFindName = toFind.getName();
		
		//strip '.twld' to try to find world of matching name
		toFindName = toFindName.replace(".twld", "");
		
		//iterate across editor worlds dir to try and find a matching name
		for (File f : baseDir.listFiles()) {
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
		if (worldFile.isDirectory()) {
			worldFile = findExistingWorldFile(QoTSettings.getEditorWorldsDir(), worldFile);
			if (worldFile == null) return false;
			theWorld.name = worldFile.getName().replace(".twld", "");
			worldFile = getWorldFile();
		}
		
		//System.out.println("THE WORLD FILE: " + worldFile);
		
		//ensure that the world is actually a thyrah world
		if (!worldFile.toString().endsWith(".twld")) return false;
		//ensure the file actually exists before trying to read from it
		if (!worldFile.exists()) return false;
		
		theWorld.worldLayers.clear();
		
		//read in world file
		try (var reader = new LineReader(worldFile)) {
			//read name
			String mapName = reader.nextLine();
			
			//read map dims
			String[] mapDims = reader.nextLine().split(" ");
			int mapWidth = ENumUtil.parseInt(mapDims, 0, -1);
			int mapHeight = ENumUtil.parseInt(mapDims, 1, -1);
			int mapTileWidth = ENumUtil.parseInt(mapDims, 2, -1);
			int mapTileHeight = ENumUtil.parseInt(mapDims, 3, -1);
			
			//read player spawn
			String[] playerSpawn = reader.nextLine().split(" ");
			int spawnX = ENumUtil.parseInt(playerSpawn, 0, -1);
			int spawnY = ENumUtil.parseInt(playerSpawn, 1, -1);
			
			//get underground state
			boolean underground = 1 == ENumUtil.parseInt(reader.nextLine(), 0);
			
			// parse number of world layers and the data on that layer
			int numLayers = ENumUtil.parseInt(reader.nextLine(), 0);
			
			for (int layerNum = 0; layerNum < numLayers; layerNum++) {
				//create map data array using parsed dims
				WorldLayer layer = new WorldLayer(mapWidth, mapHeight);
				
				for (int i = 0; i < mapHeight; i++) {
					//grab the next line
					String mapLine = reader.nextLine();
					String[] lineTiles = mapLine.split(" ");
					
//					System.out.println("[" + i + ", " + lineTiles.length + "] " + EStringUtil.toString(lineTiles));
					
					for (int j = 0; j < mapWidth; j++) {
						String tile = lineTiles[j];
						
						//break out into child parts (if there are any)
						int tileID = -1;
						int childID = 0;
						String[] parts = tile.split(":");
						
						if (!"n".equals(parts[0]) && !"null".equals(parts[0])) {
							tileID = Integer.parseInt(parts[0]);
							if (parts.length > 1) childID = Integer.parseInt(parts[1]);
							
							//get tile from the parsed ID, add children, and store in map data
							WorldTile t = WorldTile.getTileFromID(tileID, childID);
							if (t == null) System.out.println("NULL: " + tileID + " : " + childID);
							if (t != null) {
								if (parts.length == 1) t.setWildCard(true);
								t.setWorldPos(j, i);
							}
							layer.setTileAt(t, j, i);
						}
						else {
							layer.setTileAt(new VoidTile(), j, i);
						}
					}
				}
				
				theWorld.addWorldLayer(layer);
			}
			
			EList<EntitySpawn> entitySpawnsIn = new EArrayList<>();
			EList<Region> regions = new EArrayList<>();
			
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
			
			theWorld.name = mapName;
			theWorld.width = mapWidth;
			theWorld.height = mapHeight;
			theWorld.tileWidth = mapTileWidth;
			theWorld.tileHeight = mapTileHeight;
			theWorld.playerSpawn.setX(spawnX);
			theWorld.playerSpawn.setY(spawnY);
			theWorld.underground = underground;
			theWorld.regionData = regions;
			theWorld.entitySpawns = entitySpawnsIn;
			
			fileLoaded = true;
			
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
	
	private boolean performSave(File fileIn) throws IOException {
		createWorldDir();
		
		new WorldSavingSystem().saveWorld(this);
		
		PrintWriter writer = new PrintWriter(fileIn, "UTF-8");
		FileOutputStream mapWriter = new FileOutputStream(new File(getDataDir(), theWorld.name + "_data"));
		
		//write map name and dimensions
		writer.println(theWorld.name);
		writer.println(theWorld.width + " " + theWorld.height + " " + theWorld.tileWidth + " " + theWorld.tileHeight);
		writer.println(theWorld.playerSpawn.getX() + " " + theWorld.playerSpawn.getY());
		writer.println(theWorld.underground ? 1 : 0);
		//writer.println(theWorld.getWorldLayers().size());
		writer.println(1); // layers
		
		//write map data
		for (int layerNum = 0; layerNum < theWorld.getWorldLayers().size(); layerNum++) {
			if (layerNum > 0) break;
			for (int i = 0; i < theWorld.height; i++) {
				EByteBuilder sb = new EByteBuilder();
				for (int j = 0; j < theWorld.width; j++) {
					WorldTile t = theWorld.getWorldLayers().get(layerNum).getTileAt(j, i);
					String end = (j == (theWorld.width - 1)) ? "" : " ";
					
					if (t == null) {
						writer.print("n" + end);
						sb.append(("n" + end).getBytes());
					}
					else {
						Sprite tex = t.sprite;
						if (tex == null) {
							writer.print("n" + end);
							sb.append(("n" + end).getBytes());
						}
						else {
							String l = t.getID() + ((tex.getTexture().hasParent()) ? ":" + tex.getTexture().getChildID() : "") + end;
							writer.print(l);
							sb.append(l.getBytes());
						}
					}
				}
				
				writer.println();
				try {
					mapWriter.write(sb.toByteArray());
					mapWriter.write('\n');
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//write region data
		theWorld.regionData.stream().filter(s -> s != null).forEach(writer::println);
		//write entity spawn data
		theWorld.entitySpawns.stream().filter(s -> s != null).forEach(writer::println);
		
		writer.close();
		try {
			updateConfigFile();
			mapWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	//---------
	// Getters
	//---------
	
	public GameWorld getWorld() { return theWorld; }
	
	/** The file path to this specific map file. */
	public String getFilePath() { return getWorldFile().toString(); }
	/** The config file for the map. */
	public File getWorldConfigFile() { return new File(getWorldDir(), theWorld.name + "_config.ini"); }
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
