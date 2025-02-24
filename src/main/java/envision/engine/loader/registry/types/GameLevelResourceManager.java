package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.GameLevel;
import envision.engine.loader.dtos.game.GameLevelDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class GameLevelResourceManager extends BasicResourceManager<GameLevel, GameLevelDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<GameLevelDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public GameLevelResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<GameLevelDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<GameLevelDTO> dtos, Map<String, GameLevel> resources) throws Exception {
        for (GameLevelDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, GameLevel resourceBeingUnloaded) {
        
    }

}
