package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.SpriteSheet;
import envision.engine.loader.dtos.game.SpriteSheetDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class ShaderResourceManager extends BasicResourceManager<SpriteSheet, SpriteSheetDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<SpriteSheetDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public ShaderResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<SpriteSheetDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<SpriteSheetDTO> dtos, Map<String, SpriteSheet> resources) throws Exception {
        for (SpriteSheetDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, SpriteSheet resourceBeingUnloaded) {
        
    }

}
