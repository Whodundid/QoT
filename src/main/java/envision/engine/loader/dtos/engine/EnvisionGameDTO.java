package envision.engine.loader.dtos.engine;

import java.util.HashMap;
import java.util.Map;

import eutil.assertions.Assertions;
import eutil.debug.Optional;

/**
 * 
 * Possible properties:
 * <li> resourcesDir
 * <li> savesDir
 * <li> mainConfigFile
 * <li> startScreen
 * <li> mainMenuScreen
 * 
 * @param gameName the 
 * 
 * @author Hunter
 */
public record EnvisionGameDTO(
    String gameName,
    @Optional String gameVersion,
    String resourcesDir,
    @Optional String savesDir,
    @Optional String mainConfigFile,
    @Optional String startScreen,
    @Optional String mainMenuScreen,
    @Optional Map<String, Object> properties
){
    //==============
    // Constructors
    //==============
    
    public EnvisionGameDTO(
        String gameName,
        @Optional String gameVersion,
        String resourcesDir,
        @Optional String savesDir,
        @Optional String mainConfigFile,
        @Optional String startScreen,
        @Optional String mainMenuScreen,
        @Optional Map<String, Object> properties
    ){
        Assertions.assertNotNull(gameName, resourcesDir);
        
        if (gameVersion == null) gameVersion = "0.0.1";
        if (properties == null) properties = new HashMap<>();
        
        this.gameName = gameName;
        this.gameVersion = gameVersion;
        this.resourcesDir = resourcesDir;
        this.savesDir = savesDir;
        this.mainConfigFile = mainConfigFile;
        this.startScreen = startScreen;
        this.mainMenuScreen = mainMenuScreen;
        this.properties = properties;
    }
    
}
