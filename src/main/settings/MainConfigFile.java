package main.settings;

import java.io.File;

import eutil.datatypes.EArrayList;
import main.settings.config.ConfigSetting;
import main.settings.config.QotConfigFile;

public class MainConfigFile extends QotConfigFile {

	public MainConfigFile(File path) {
		super(path, "MainConfig", "Quest of Thyrah Config");
	}
	
	@Override
	public boolean tryLoad() {
		boolean good = true;
		
		EArrayList<ConfigSetting> settings = QoT_Settings.getSettings();
		
		if (!exists()) { trySave(settings); }
		
		// attempt to load settings
		if (!tryLoad(settings)) { good = false; }
		
		// save again to update the file in case formatting is off
		if (!trySave(settings)) { good = false; }
		
		return good;
	}
	
	@Override
	public boolean trySave() {
		return trySave(QoT_Settings.getSettings());
	}
	
}
