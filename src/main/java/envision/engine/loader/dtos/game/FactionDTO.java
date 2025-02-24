package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Faction;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record FactionDTO(@Optional String name, String filePath) implements IDataTransferObject<Faction> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public FactionDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public FactionDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public Faction fromDto() {
        return null;
    }
    
}
