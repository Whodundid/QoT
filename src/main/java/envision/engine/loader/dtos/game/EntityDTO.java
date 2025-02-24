package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Entity;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record EntityDTO(@Optional String name, String filePath) implements IDataTransferObject<Entity> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public EntityDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public EntityDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public Entity fromDto() {
        return null;
    }
    
}
