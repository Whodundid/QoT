package envision.game.world.worldFileSystem;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import envision.engine.rendering.textureSystem.GameTexture;
import envision.game.world.WorldFileSystem;
import envision.game.world.WorldLayer;
import eutil.strings.EStringBuilder;
import eutil.strings.EStringUtil;

public class WorldSavingSystem {
	
	private WorldFileSystem fileSystem;
	
	public synchronized void saveWorld(WorldFileSystem fileSystemIn) throws IOException {
		fileSystem = fileSystemIn;
		
		saveConfigFile();
		saveDataFiles();
		saveScriptFiles();
	}
	
	//=========================================================
	
	private void saveConfigFile() throws IOException {
		PrintWriter writer = new PrintWriter(fileSystem.getWorldConfigFile());
		
		var world = fileSystem.getWorld();
		var name = world.getWorldName();
		
		String dashes = EStringUtil.repeatString("-", name.length() + " config".length() + 4);
		
		writer.println("#" + dashes);
		writer.println("# '" + name + "' config");
		writer.println("#" + dashes);
		writer.println();
		writer.println("mapName=" + world.getWorldName());
		writer.println("spawn=" + world.getPlayerSpawn());
		writer.println("underground=" + world.isUnderground());
		writer.println("timeOfDay=" + world.getTime());
		writer.println("lengthOfDay=" + world.getDayLength());
		
		writer.close();
	}
	
	//=========================================================
	
	private void saveDataFiles() throws IOException {
		final var world = fileSystem.getWorld();
		final var layers = world.getWorldLayers();
		final int wSize = layers.size();
		
		// save world layers
		for (int i = 0; i < wSize; i++) {
			saveWorldLayer(i, layers.get(i));
		}
		
		// save regions
		saveRegions();
		
		// save entities
		saveEntities();
	}
	
	private void saveWorldLayer(int index, WorldLayer layer) throws IOException {
		if (layer == null) throw new RuntimeException("NULL World layer! index='" + index + "'");
		
		final var world = fileSystem.getWorld();
		final File dataDir = fileSystem.getDataDir();
		final File dataLayer = new File(dataDir, "data_" + index);
		
		final var sb = new EStringBuilder();
		final int width = world.getWidth();
		final int height = world.getHeight();
		final int tWidth = world.getTileWidth();
		final int tHeight = world.getTileHeight();
		
		//print dims
		sb.println(width, ' ', height, ' ', tWidth, ' ', tHeight);
		
		for (int y = 0; y < height; y++) {
			var lb = new EStringBuilder(); // line builder
			
			for (int x = 0; x < width; x++) {
				final var tile = layer.getTileAt(x, y);
				
				if (tile == null || tile.getTexture() == null) {
					lb.a("n ");
					continue;
				}
				
				GameTexture tex = tile.getTexture();
				lb.a(tile.getID(), ((tex.hasParent()) ? ":" + tex.getChildID() : ""), ' ');
			}
			
			var trim = lb.toString().trim();
			sb.println(trim);
		}
		
		Files.write(dataLayer.toPath(), sb.toString().getBytes());
	}
	
	private void saveRegions() throws IOException {
		final var world = fileSystem.getWorld();
		final int rSize = world.getRegionData().size();
		final File dataDir = fileSystem.getDataDir();
		final File regionFile = new File(dataDir, "regions");
		
		final var sb = new EStringBuilder();
		
		for (int i = 0; i < rSize; i++) {
			var r = world.getRegionData().get(i);
			
			sb.println(r);
		}
		
		Files.write(regionFile.toPath(), sb.toString().getBytes());
	}
	
	private void saveEntities() throws IOException {
		final var world = fileSystem.getWorld();
		final int eSize = world.getEntitySpawns().size();
		final File dataDir = fileSystem.getDataDir();
		final File entityFile = new File(dataDir, "entities");
		
		final var sb = new EStringBuilder();
		
		for (int i = 0; i < eSize; i++) {
			var e = world.getEntitySpawns().get(i);
			
			sb.println(e);
		}
		
		Files.write(entityFile.toPath(), sb.toString().getBytes());
	}
	
	//=========================================================
	
	private void saveScriptFiles() throws IOException {
		final var world = fileSystem.getWorld();
		final int rSize = world.getRegionData().size();
		final File dataDir = fileSystem.getDataDir();
		final File regionFile = new File(dataDir, "regions");
		
		for (int i = 0; i < rSize; i++) {
			var r = world.getRegionData().get(i);
			
			r.toSaveString();
		}
	}
	
}
