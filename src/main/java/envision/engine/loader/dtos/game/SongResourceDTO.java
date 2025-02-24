package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.SongResource;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record SongResourceDTO(@Optional String name, String filePath) implements IDataTransferObject<SongResource> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public SongResourceDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public SongResourceDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public SongResource fromDto() {
        return null;
    }
    
}
