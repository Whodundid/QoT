package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.TileSet;
import envision.engine.loader.dtos.game.TileSetDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class TileSetResourceManager extends BasicResourceManager<TileSet, TileSetDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<TileSetDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public TileSetResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<TileSetDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<TileSetDTO> dtos, Map<String, TileSet> resources) throws Exception {
        for (TileSetDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, TileSet resourceBeingUnloaded) {
        
    }

}
