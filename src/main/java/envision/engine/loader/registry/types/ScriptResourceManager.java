package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.EnvisionScript;
import envision.engine.loader.dtos.game.ScriptResourceDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class ScriptResourceManager extends BasicResourceManager<EnvisionScript, ScriptResourceDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<ScriptResourceDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public ScriptResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<ScriptResourceDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<ScriptResourceDTO> dtos, Map<String, EnvisionScript> resources) throws Exception {
        for (ScriptResourceDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, EnvisionScript resourceBeingUnloaded) {
        
    }

}
