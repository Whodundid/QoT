package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Ability;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record AbilityDTO(@Optional String name, String filePath) implements IDataTransferObject<Ability> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public AbilityDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public AbilityDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public Ability fromDto() {
        return null;
    }
    
}
