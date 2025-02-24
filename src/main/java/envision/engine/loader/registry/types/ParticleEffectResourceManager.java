package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.ParticleEffect;
import envision.engine.loader.dtos.game.ParticleEffectDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class ParticleEffectResourceManager extends BasicResourceManager<ParticleEffect, ParticleEffectDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<ParticleEffectDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public ParticleEffectResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<ParticleEffectDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<ParticleEffectDTO> dtos, Map<String, ParticleEffect> resources) throws Exception {
        for (ParticleEffectDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, ParticleEffect resourceBeingUnloaded) {
        
    }

}
