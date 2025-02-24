package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.dtos.game.WorldTileDTO;
import envision.engine.loader.registry.BasicResourceManager;
import envision.game.world.worldTiles.WorldTile;
import eutil.datatypes.util.EList;

public class WorldTileResourceManager extends BasicResourceManager<WorldTile, WorldTileDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<WorldTileDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public WorldTileResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<WorldTileDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<WorldTileDTO> dtos, Map<String, WorldTile> resources) throws Exception {
        for (WorldTileDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, WorldTile resourceBeingUnloaded) {
        
    }

}
