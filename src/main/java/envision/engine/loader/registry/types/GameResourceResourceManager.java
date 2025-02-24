package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.GameResource;
import envision.engine.loader.dtos.game.GameResourceDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class GameResourceResourceManager extends BasicResourceManager<GameResource, GameResourceDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<GameResourceDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public GameResourceResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<GameResourceDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<GameResourceDTO> dtos, Map<String, GameResource> resources) throws Exception {
        for (GameResourceDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, GameResource resourceBeingUnloaded) {
        
    }

}
