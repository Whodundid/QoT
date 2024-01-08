package envision.engine.loader;

import java.io.File;

import envision.engine.settings.config.ConfigSetting;
import eutil.datatypes.util.EList;

@Deprecated
public class LoadedGameSettings extends GameSettings {
    
    //========
    // Fields
    //========
    
    private EnvisionGame game;
    private File resourcesDirectory;
    private File installationDirectory;
    private File savedGamesDirectory;
    
    //==============
    // Constructors
    //==============
    
    public LoadedGameSettings(EnvisionGame gameIn) {
        game = gameIn;
    }
    
    //===========
    // Overrides
    //===========
    
    public File getResourcesDirectory() { return resourcesDirectory; }
    public File getInstallationDirectory() { return installationDirectory; }
    public File getSavedGamesDirectory() { return savedGamesDirectory; }
    
    @Override public EList<ConfigSetting<?>> getConfigSettings() { return null; }
    
    //=========
    // Methods
    //=========
    
    @Override
    protected void initializeGameDirectories(File installDir, boolean useInternalResources) {
        
    }
    
    //=========
    // Getters
    //=========
    
    public EnvisionGame getGame() { return game; }
    
    //=========
    // Setters
    //=========
    
}
