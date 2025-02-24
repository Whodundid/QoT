package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.Item;
import envision.engine.loader.dtos.game.ItemDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class ItemResourceManager extends BasicResourceManager<Item, ItemDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<ItemDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public ItemResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<ItemDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<ItemDTO> dtos, Map<String, Item> resources) throws Exception {
        for (ItemDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, Item resourceBeingUnloaded) {
        
    }

}
