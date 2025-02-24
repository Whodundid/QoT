package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Shop;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record ShopDTO(@Optional String name, String filePath) implements IDataTransferObject<Shop> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public ShopDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public ShopDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public Shop fromDto() {
        return null;
    }
    
}
