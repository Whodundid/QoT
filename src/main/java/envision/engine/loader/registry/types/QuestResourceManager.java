package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.Quest;
import envision.engine.loader.dtos.game.QuestDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class QuestResourceManager extends BasicResourceManager<Quest, QuestDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<QuestDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public QuestResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<QuestDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<QuestDTO> dtos, Map<String, Quest> resources) throws Exception {
        for (QuestDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, Quest resourceBeingUnloaded) {
        
    }

}
