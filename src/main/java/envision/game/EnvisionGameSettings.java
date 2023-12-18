package envision.game;

import java.io.File;

import envision.engine.settings.config.ConfigSetting;
import eutil.datatypes.util.EList;
import eutil.strings.EStringUtil;

public abstract class EnvisionGameSettings {
    
    public abstract File getResourcesDirectory();
    public abstract File getInstallationDirectory();
    public abstract File getSavedGamesDirectory();
    
    public String getResourcesPath() { return EStringUtil.toString(getResourcesDirectory()); }
    public String getInstallationPath() { return EStringUtil.toString(getInstallationDirectory()); }
    public String getSavedGamesPath() { return EStringUtil.toString(getSavedGamesDirectory()); }
    
    public abstract EList<ConfigSetting<?>> getConfigSettings();
    
    //============
    // Initialize
    //============
    
    public void initializeSettings(File installDir, boolean useInternalResources) {
        // ensure installation directory actually exists
        if (!installDir.exists()) {
            throw new RuntimeException(String.format("Failed to bind to installation directory '%s'!", installDir));
        }
        
        // initialize game directories using installation directory
        initializeGameDirectories(installDir, useInternalResources);
    }
    
    protected abstract void initializeGameDirectories(File installDir, boolean useInternalResources);
    
}
