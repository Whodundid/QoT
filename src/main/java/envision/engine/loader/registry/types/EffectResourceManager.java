package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.Effect;
import envision.engine.loader.dtos.game.EffectDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class EffectResourceManager extends BasicResourceManager<Effect, EffectDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<EffectDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public EffectResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<EffectDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<EffectDTO> dtos, Map<String, Effect> resources) throws Exception {
        for (EffectDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, Effect resourceBeingUnloaded) {
        
    }

}
