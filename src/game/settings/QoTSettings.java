package game.settings;

import java.io.File;

import envision.settings.MainConfigFile;
import envision.settings.config.ConfigSetting;
import eutil.datatypes.EArrayList;
import game.QoT;

public class QoTSettings {
	
	private static File localGameDir;
	private static File savesDir;
	private static File editorWorldsDir;
	private static File menuWorldsDir;
	private static File resourcesDir;
	
	/** The primary configuration file for the game. */
	private static MainConfigFile mainConfig;
	
	// holder
	private static final EArrayList<ConfigSetting> settings = new EArrayList();
	
	//-----------------------
	//       settings
	//-----------------------
	
	public static final ConfigSetting<Integer> musicVolume = new ConfigSetting(Integer.class, "musicVolume", "Music Volume", 60);
	public static final ConfigSetting<Integer> targetFPS = new ConfigSetting(Integer.class, "targetFPS", "Target FPS", 60);
	public static final ConfigSetting<Integer> targetUPS = new ConfigSetting(Integer.class, "targetUPS", "Target UPS", 150);
	public static final ConfigSetting<String> lastEditorMap = new ConfigSetting(String.class, "lastEditorMap", "Last Editor Map", "");
	public static final ConfigSetting<String> lastMap = new ConfigSetting(String.class, "lastMap", "Last Map", "");
	public static final ConfigSetting<Boolean> drawFPS = new ConfigSetting(Boolean.class, "drawFPS", "Draw FPS", false);
	public static final ConfigSetting<Boolean> fullscreen = new ConfigSetting(Boolean.class, "fullscreen", "Fullscreen", false);
	public static final ConfigSetting<Boolean> vsync = new ConfigSetting(Boolean.class, "vSync", "V-Sync", false);
	public static final ConfigSetting<Integer> resolutionScale = new ConfigSetting(Integer.class, "resScale", "Resolution Scale", 1);
	
	//-----------------------
	
	static {
		settings.add(musicVolume);
		settings.add(targetFPS);
		settings.add(targetUPS);
		settings.add(lastEditorMap);
		settings.add(lastMap);
		settings.add(drawFPS);
		settings.add(fullscreen);
		settings.add(vsync);
	}
	
	public static EArrayList<ConfigSetting> getSettings() { return settings; }
	
	//--------------
	// initializers
	//--------------
	
	public static void init(File installDir, boolean useInternalResources) {
		//ensure installation directory actually exists
		if (!installDir.exists()) {
			throw new RuntimeException(String.format("Failed to bind to installation directory '%s'!", installDir));
		}
		
		//initialize game directories using installation directory
		initDirectories(installDir, useInternalResources);
		
		//establish game main config
		mainConfig = new MainConfigFile(new File(installDir, "MainConfig.ini"));
		mainConfig.tryLoad();
	}
	
	private static boolean initDirectories(File dirPath, boolean useInternalResources) {
		localGameDir = dirPath;
		
		//maps resources to internal jar resources instead of the installation directory
		if (useInternalResources) {
			resourcesDir = new File("resources\\");
		}
		else {
			resourcesDir = new File(dirPath, "resources");
			
			//error if resources dir does not exist
			if (!resourcesDir.exists()) {
				throw new RuntimeException(String.format("Failed to bind to resources in installation dir '%s'!", dirPath));
			}
		}
		
		//create mappings to game directories
		savesDir = new File(localGameDir, "saves");
		editorWorldsDir = new File(localGameDir, "editorWorlds");
		menuWorldsDir = new File(localGameDir, "menuWorlds");
		
		if (!savesDir.exists()) {
			QoT.warn("Profiles directory not found! Attempting to create new one!");
			savesDir.mkdirs();
		}
		
		if (!editorWorldsDir.exists()) {
			QoT.warn("Editor worlds directory not found! Attempting to create new one!");
			editorWorldsDir.mkdirs();
		}
		
		if (!menuWorldsDir.exists()) {
			QoT.warn("Menu worlds directory not found! Attempting to create new one!");
			menuWorldsDir.mkdirs();
		}
		
		return true;
	}
	
	public static boolean saveConfig() {
		return mainConfig.trySave();
	}
	
	public static File getResourcesDir() { return resourcesDir; }
	public static File getLocalGameDir() { return localGameDir; }
	public static File getSavesDir() { return savesDir; }
	public static File getEditorWorldsDir() { return editorWorldsDir; }
	public static File getMenuWorldsDir() { return menuWorldsDir; }
	
	public static MainConfigFile getGameConfig() { return mainConfig; }
	
}
