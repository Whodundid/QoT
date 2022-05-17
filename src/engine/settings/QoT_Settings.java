package engine.settings;

import java.io.File;

import engine.settings.config.ConfigSetting;
import eutil.datatypes.EArrayList;

public class QoT_Settings {
	
	private static File localGameDir;
	private static File profilesDir;
	private static File editorWorldsDir;
	
	// holder
	private static final EArrayList<ConfigSetting> settings = new EArrayList();
	
	//-----------------------
	//       settings
	//-----------------------
	
	public static final ConfigSetting<Integer> musicVolume = new ConfigSetting(Integer.class, "musicVolume", "Music Volume", 100);
	public static final ConfigSetting<Integer> targetFPS = new ConfigSetting(Integer.class, "targetFPS", "Target FPS", 60);
	public static final ConfigSetting<Integer> targetUPS = new ConfigSetting(Integer.class, "targetUPS", "Target UPS", 60);
	public static final ConfigSetting<String> lastEditorMap = new ConfigSetting(String.class, "lastEditorMap", "Last Editor Map", "");
	public static final ConfigSetting<String> lastMap = new ConfigSetting(String.class, "lastMap", "Last Map", "");
	public static final ConfigSetting<Boolean> drawFPS = new ConfigSetting(Boolean.class, "drawFPS", "Draw FPS", false);
	public static final ConfigSetting<Boolean> fullscreen = new ConfigSetting(Boolean.class, "fullscreen", "Fullscreen", false);
	public static final ConfigSetting<Boolean> vsync = new ConfigSetting(Boolean.class, "vSync", "V-Sync", false);
	
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
	
	public boolean initDirectories(File dirPath) {
		localGameDir = dirPath;
		
		// check if file directory has been created and create it if it does not exist
		if (!dirPath.exists() && !dirPath.mkdir()) {
			// setup failed
			return false;
		}
		
		// create game directories
		profilesDir = new File(localGameDir, "saves");
		editorWorldsDir = new File(localGameDir, "editorWorlds");
		
		if (!profilesDir.exists()) profilesDir.mkdir();
		if (!editorWorldsDir.exists()) editorWorldsDir.mkdir();
		
		return true;
	}
	
	public static File getLocalGameDir() { return localGameDir; }
	public static File getProfilesDir() { return profilesDir; }
	public static File getEditorWorldsDir() { return editorWorldsDir; }
	
}
