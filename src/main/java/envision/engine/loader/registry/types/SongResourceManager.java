package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.SongResource;
import envision.engine.loader.dtos.game.SongResourceDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class SongResourceManager extends BasicResourceManager<SongResource, SongResourceDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<SongResourceDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public SongResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<SongResourceDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<SongResourceDTO> dtos, Map<String, SongResource> resources) throws Exception {
        for (SongResourceDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, SongResource resourceBeingUnloaded) {
        
    }

}
