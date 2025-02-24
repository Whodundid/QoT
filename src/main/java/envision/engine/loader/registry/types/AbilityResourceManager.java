package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.Ability;
import envision.engine.loader.dtos.game.AbilityDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class AbilityResourceManager extends BasicResourceManager<Ability, AbilityDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<AbilityDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public AbilityResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<AbilityDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<AbilityDTO> dtos, Map<String, Ability> resources) throws Exception {
        for (AbilityDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, Ability resourceBeingUnloaded) {
        
    }

}
