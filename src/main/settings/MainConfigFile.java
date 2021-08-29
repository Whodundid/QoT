package main.settings;

import eutil.storage.EArrayList;
import java.io.File;
import main.settings.config.ConfigSetting;
import main.settings.config.QotConfigFile;

public class MainConfigFile extends QotConfigFile {

	public MainConfigFile(File path) {
		super(path, "MainConfig", "Quest of Thyrah Config");
	}
	
	@Override
	public boolean tryLoad() {
		boolean good = true;
		
		EArrayList<ConfigSetting> settings = QotGameSettings.getSettings();
		
		if (!exists()) { trySave(settings); }
		
		// attempt to load settings
		if (!tryLoad(settings)) { good = false; }
		
		// save again to update the file in case formatting is off
		if (!trySave(settings)) { good = false; }
		
		return false;
	}
	
	@Override
	public boolean trySave() {
		return trySave(QotGameSettings.getSettings());
	}
	
}
