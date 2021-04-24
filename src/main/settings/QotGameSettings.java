package main.settings;

import java.io.File;
import main.settings.config.ConfigSetting;
import storageUtil.EArrayList;

public class QotGameSettings {
	
	private static File localGameDir;
	private static File profilesDir;
	private static File editorWorldsDir;
	
	// holder
	private static final EArrayList<ConfigSetting> settings = new EArrayList();
	
	//-----------------------
	//       settings
	//-----------------------
	
	public static final ConfigSetting<Integer> musicVolume = new ConfigSetting(Integer.class, "musicVolume", "Music Volume", 100);
	public static final ConfigSetting<String> lastMap = new ConfigSetting(String.class, "lastMap", "Last Editor Map", "");
	
	//-----------------------
	
	static {
		settings.add(musicVolume);
		settings.add(lastMap);
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
		
		if (!profilesDir.exists()) { profilesDir.mkdir(); }
		if (!editorWorldsDir.exists()) { editorWorldsDir.mkdir(); }
		
		return true;
	}
	
	public static File getLocalGameDir() { return localGameDir; }
	public static File getProfilesDir() { return profilesDir; }
	public static File getEditorWorldsDir() { return editorWorldsDir; }
	
}
