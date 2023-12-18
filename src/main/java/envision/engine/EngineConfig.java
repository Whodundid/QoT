package envision.engine;

import java.io.File;

import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.EnvisionConfigFile;
import eutil.datatypes.util.EList;

public class EngineConfig extends EnvisionConfigFile {

    public EngineConfig(File path) {
        super(path, "Enivision-Config", "Envision Engine Main Config File");
    }
    
    @Override
    public boolean tryLoad() {
        boolean good = true;
        
        EList<ConfigSetting<?>> settings = EngineSettings.getSettings();
        
        // check if the config file even exists
        if (!exists()) {
            // create the base config file if it doesn't already exist
            good &= trySave(settings);
            // attempt to load settings
            good &= tryLoad(settings);
            // save again to update the file in case formatting is off
            good &= trySave(settings);
        }
        else {
            // attempt to load settings
            good &= tryLoad(settings);
            // save to try to correct file problems
            if (!good) good = trySave(settings);
        }
        
        return good;
    }
    
    @Override
    public boolean trySave() {
        return trySave(EngineSettings.getSettings());
    }

    @Override
    public boolean tryReset() {
        return super.tryReset(EngineSettings.getSettings());
    }
    
}
