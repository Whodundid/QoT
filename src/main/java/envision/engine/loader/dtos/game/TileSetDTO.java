package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.TileSet;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record TileSetDTO(@Optional String name, String filePath) implements IDataTransferObject<TileSet> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public TileSetDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public TileSetDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public TileSet fromDto() {
        return null;
    }
    
}
