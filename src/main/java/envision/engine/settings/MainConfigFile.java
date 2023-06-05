package envision.engine.settings;

import java.io.File;

import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.EnvisionConfigFile;
import eutil.datatypes.util.EList;
import qot.settings.QoTSettings;

public class MainConfigFile extends EnvisionConfigFile {

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
