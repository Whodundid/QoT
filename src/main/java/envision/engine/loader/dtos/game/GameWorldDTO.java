package envision.engine.loader.dtos.game;

import envision.engine.loader.dtos.IDataTransferObject;
import envision.game.world.GameWorld;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record GameWorldDTO(@Optional String name, String filePath) implements IDataTransferObject<GameWorld> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public GameWorldDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public GameWorldDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public GameWorld fromDto() {
        return null;
    }
    
}
