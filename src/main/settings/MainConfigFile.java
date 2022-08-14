package main.settings;

import java.io.File;

import eutil.datatypes.EList;
import main.settings.config.ConfigSetting;
import main.settings.config.QotConfigFile;

public class MainConfigFile extends QotConfigFile {

	public MainConfigFile(File path) {
		super(path, "MainConfig", "Quest of Thyrah Config");
	}
	
	@Override
	public boolean tryLoad() {
		boolean good = true;
		
		EList<ConfigSetting> settings = QoTSettings.getSettings();
		
		if (!exists()) trySave(settings);
		
		// attempt to load settings
		if (!tryLoad(settings)) good = false;
		
		// save again to update the file in case formatting is off
		if (!trySave(settings)) good = false;
		
		return good;
	}
	
	@Override
	public boolean trySave() {
		return trySave(QoTSettings.getSettings());
	}
	
}
