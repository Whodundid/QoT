package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.EntityComponent;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record EntityComponentDTO(@Optional String name, String filePath) implements IDataTransferObject<EntityComponent> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public EntityComponentDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public EntityComponentDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public EntityComponent fromDto() {
        return null;
    }
    
}
