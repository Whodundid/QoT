package main.settings;

import gameSystems.configSystem.QotConfigFile;
import java.io.File;

public class MainConfigFile extends QotConfigFile {

	public MainConfigFile(File path) {
		super(path, "MainConfig", "Quest of Thyrah Config");
	}
	
	@Override
	public boolean tryLoad() {
		boolean good = true;
		
		if (!exists()) { trySave(QotGameSettings.getSettings()); }
		
		// attempt to load settings
		if (!tryLoad(QotGameSettings.getSettings())) { good = false; }
		
		// save again to update the file in case formatting is off
		if (!trySave(QotGameSettings.getSettings())) { good = false; }
		
		return false;
	}
	
	@Override
	public boolean trySave() {
		return trySave(QotGameSettings.getSettings());
	}
	
}
