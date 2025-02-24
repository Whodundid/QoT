package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.SoundEffectResource;
import envision.engine.loader.dtos.game.SoundEffectResourceDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class SoundEffectResourceManager extends BasicResourceManager<SoundEffectResource, SoundEffectResourceDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<SoundEffectResourceDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public SoundEffectResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<SoundEffectResourceDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<SoundEffectResourceDTO> dtos, Map<String, SoundEffectResource> resources) throws Exception {
        for (SoundEffectResourceDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, SoundEffectResource resourceBeingUnloaded) {
        
    }

}
