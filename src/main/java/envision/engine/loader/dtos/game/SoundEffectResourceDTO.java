package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.SoundEffectResource;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record SoundEffectResourceDTO(@Optional String name, String filePath) implements IDataTransferObject<SoundEffectResource> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public SoundEffectResourceDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public SoundEffectResourceDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public SoundEffectResource fromDto() {
        return null;
    }
    
}
