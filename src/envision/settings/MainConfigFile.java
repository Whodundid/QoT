package envision.settings;

import java.io.File;

import envision.settings.config.ConfigSetting;
import envision.settings.config.QotConfigFile;
import eutil.datatypes.EArrayList;
import game.settings.QoTSettings;

public class MainConfigFile extends QotConfigFile {

	public MainConfigFile(File path) {
		super(path, "MainConfig", "Quest of Thyrah Config");
	}
	
	@Override
	public boolean tryLoad() {
		boolean good = true;
		
		EArrayList<ConfigSetting> settings = QoTSettings.getSettings();
		
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