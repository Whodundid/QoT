package envision.engine.loader.registry.types;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import envision.engine.internal.rendering.textureSystem.TextureSystem;
import envision.engine.loader.built.game.GameTexture;
import envision.engine.loader.dtos.game.TextureResourceDTO;
import envision.engine.loader.registry.BasicResourceManager;
import eutil.datatypes.util.EList;

public class TextureResourceManager extends BasicResourceManager<GameTexture, TextureResourceDTO> {
    
    //========
    // Fields
    //========
    
    private static final TypeReference<List<TextureResourceDTO>> TYPE_REFERENCE = new TypeReference<>() {};
    private static final TextureSystem TS = TextureSystem.getInstance();
    
    //==============
    // Constructors
    //==============
    
    public TextureResourceManager(File resourceFileIn) {
        super(resourceFileIn);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    protected List<TextureResourceDTO> loadDtosFromFile() throws Exception {
        return mapper.readValue(resourceFile, TYPE_REFERENCE);
    }
    
    @Override
    protected void buildResourcesFromDTOs(EList<TextureResourceDTO> dtos, Map<String, GameTexture> resources) throws Exception {
        for (TextureResourceDTO dto : dtos) {
            GameTexture tex = new GameTexture(dto);
            TS.reg(tex);
            resources.put(dto.name(), tex);
        }
    }
    
    @Override
    protected void onUnload(String name, GameTexture resourceBeingUnloaded) {
        TS.destroyTexture(resourceBeingUnloaded);
    }

}
