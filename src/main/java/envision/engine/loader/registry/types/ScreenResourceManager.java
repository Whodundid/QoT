package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.GameScreen;
import envision.engine.loader.dtos.game.GameScreenDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class ScreenResourceManager extends BasicResourceManager<GameScreen, GameScreenDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<GameScreenDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public ScreenResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<GameScreenDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<GameScreenDTO> dtos, Map<String, GameScreen> resources) throws Exception {
        for (GameScreenDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, GameScreen resourceBeingUnloaded) {
        
    }

}
