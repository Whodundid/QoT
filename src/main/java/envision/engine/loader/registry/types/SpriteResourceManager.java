package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.Sprite;
import envision.engine.loader.dtos.game.SpriteDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class SpriteResourceManager extends BasicResourceManager<Sprite, SpriteDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<SpriteDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public SpriteResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<SpriteDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<SpriteDTO> dtos, Map<String, Sprite> resources) throws Exception {
        for (SpriteDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, Sprite resourceBeingUnloaded) {
        
    }

}
