package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Item;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record ItemDTO(@Optional String name, String filePath) implements IDataTransferObject<Item> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public ItemDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public ItemDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public Item fromDto() {
        return null;
    }
    
}
