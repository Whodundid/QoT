package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.dtos.game.GameWorldDTO;
import envision.engine.loader.registry.BasicResourceManager;
import envision.game.world.GameWorld;
import eutil.datatypes.util.EList;

public class GameWorldResourceManager extends BasicResourceManager<GameWorld, GameWorldDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<GameWorldDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public GameWorldResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<GameWorldDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<GameWorldDTO> dtos, Map<String, GameWorld> resources) throws Exception {
        for (GameWorldDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, GameWorld resourceBeingUnloaded) {
        
    }

}
