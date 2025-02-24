package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.EnvisionScript;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record ScriptResourceDTO(@Optional String name, String filePath) implements IDataTransferObject<EnvisionScript> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public ScriptResourceDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public ScriptResourceDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(name, filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public EnvisionScript fromDto() {
        return null;
    }
    
}
