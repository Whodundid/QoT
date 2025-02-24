package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.loader.built.game.Shop;
import envision.engine.loader.dtos.game.ShopDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class ShopResourceManager extends BasicResourceManager<Shop, ShopDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<ShopDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    
    //==============
    // Constructors
    //==============
    
    public ShopResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<ShopDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<ShopDTO> dtos, Map<String, Shop> resources) throws Exception {
        for (ShopDTO dto : dtos) {
            
        }
    }
    
    @Override
    protected void onUnload(String name, Shop resourceBeingUnloaded) {
        
    }

}
