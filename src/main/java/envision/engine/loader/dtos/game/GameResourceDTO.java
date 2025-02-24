package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.GameResource;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record GameResourceDTO(@Optional String name, String filePath) implements IDataTransferObject<GameResource> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public GameResourceDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public GameResourceDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public GameResource fromDto() {
        return null;
    }
    
}
