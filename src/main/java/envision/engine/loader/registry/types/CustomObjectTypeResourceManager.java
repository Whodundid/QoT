package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.CustomObjectType;
import envision.engine.loader.dtos.game.CustomObjectTypeDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class CustomObjectTypeResourceManager extends BasicResourceManager<CustomObjectType, CustomObjectTypeDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<CustomObjectTypeDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public CustomObjectTypeResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<CustomObjectTypeDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<CustomObjectTypeDTO> dtos, Map<String, CustomObjectType> resources) throws Exception {
        for (CustomObjectTypeDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, CustomObjectType resourceBeingUnloaded) {
        
    }

}
