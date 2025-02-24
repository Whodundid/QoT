package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Effect;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record EffectDTO(@Optional String name, String filePath) implements IDataTransferObject<Effect> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public EffectDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public EffectDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public Effect fromDto() {
        return null;
    }
    
}
