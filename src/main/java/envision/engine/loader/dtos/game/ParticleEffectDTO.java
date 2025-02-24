package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.ParticleEffect;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

public record ParticleEffectDTO(@Optional String name, String filePath) implements IDataTransferObject<ParticleEffect> {
    
    //==============
    // Constructors
    //==============
    
    /** Names are optional. */
    public ParticleEffectDTO(String filePath) {
        this(null, filePath);
    }
    
    /** Constructor to ensure that values are property set. */
    public ParticleEffectDTO(@Optional String name, String filePath) {
        Assertions.assertNotNull(filePath);
        
        this.name = name;
        this.filePath = filePath;
    }
    
    //===========
    // Overrides
    //===========

    @Override
    public ParticleEffect fromDto() {
        return new ParticleEffect(this);
    }
    
}
