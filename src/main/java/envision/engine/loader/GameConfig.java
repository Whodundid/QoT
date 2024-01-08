package envision.engine.loader;

import java.io.File;

import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.EnvisionConfigFile;
import eutil.datatypes.util.EList;

public class GameConfig extends EnvisionConfigFile {
    
    //========
    // Fields
    //========
    
    private EnvisionGame game;
    
    //==============
    // Constructors
    //==============
    
    public GameConfig(EnvisionGame game) {
        this.game = game;
        
        File configFile = game.getMainConfigFile();
        
        if (configFile != null) {
            this.configPath = configFile;
            this.configName = configFile.getName();
        }
        else {
            this.configPath = new File(game.getInstallationDirectory(), "GameConfigFile.ini");
            this.configName = "Config";
        }
        
        this.configTitleLine = game.getGameName() + " Config";
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public boolean tryLoad() {
        boolean good = true;
        
        EList<ConfigSetting<?>> settings = game.getConfigSettings();
        
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
        return trySave(game.getConfigSettings());
    }

    @Override
    public boolean tryReset() {
        return super.tryReset(game.getConfigSettings());
    }
    
}
