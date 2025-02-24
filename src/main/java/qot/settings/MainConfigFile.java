package qot.settings;

import java.io.File;

import envision.engine.internal.settings.config.EnvisionConfigFile;
import envision.engine.loader.built.engine.ConfigSetting;
import eutil.datatypes.util.EList;

@Deprecated
public class MainConfigFile extends EnvisionConfigFile {

    public MainConfigFile(File path) {
        super(path, "MainConfig", "Quest of Thyrah Config");
    }
    
    @Override
    public boolean tryLoad() {
        boolean good = true;
        
        EList<ConfigSetting<?>> settings = QoTSettings.instance().getConfigSettings();
        
        if (!exists()) trySave(settings);
        
        // attempt to load settings
        if (!tryLoad(settings)) good = false;
        
        // save again to update the file in case formatting is off
        if (!trySave(settings)) good = false;
        
        return good;
    }
    
    @Override
    public boolean trySave() {
        return trySave(QoTSettings.instance().getConfigSettings());
    }
    
}
