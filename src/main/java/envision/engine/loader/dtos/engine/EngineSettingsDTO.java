package envision.engine.loader.dtos.engine;

import java.util.HashMap;
import java.util.Map;

import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record EngineSettingsDTO(
    String engineVersion,
    boolean debugMode,
    int fps,
    int tps,
    @Optional Map<String, Object> properties
){
    //==============
    // Constructors
    //==============
    
    public EngineSettingsDTO(
        String engineVersion,
        boolean debugMode,
        int fps,
        int tps,
        @Optional Map<String, Object> properties
    ){
        Assertions.assertNotNull(engineVersion);
        
        if (properties == null) properties = new HashMap<>();
        
        this.engineVersion = engineVersion;
        this.debugMode = debugMode;
        this.fps = fps;
        this.tps = tps;
        this.properties = properties;
    }
    
}
