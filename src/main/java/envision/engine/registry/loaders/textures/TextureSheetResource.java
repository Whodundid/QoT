package envision.engine.registry.loaders.textures;

import java.util.List;

import envision.engine.registry.loaders.IGameResource;
import envision.engine.registry.loaders.ResourceType;
import envision.engine.registry.loaders.Sprite;
import envision.engine.registry.loaders.dto.GameTextureDTO;
import envision.engine.registry.loaders.dto.SpriteDTO;
import envision.engine.registry.loaders.dto.TextureSheetDTO;
import envision.engine.rendering.textureSystem.GameTexture;
import eutil.datatypes.util.EList;

public class TextureSheetResource implements IGameResource {
    
    //========
    // Fields
    //========

    private TextureSheetDTO sheetDTO;
    
    private String textureSheetName;
    private GameTexture texture;
    private EList<Sprite> sprites;
    
    private boolean isLoaded;
    
    //==============
    // Constructors
    //==============
    
    public TextureSheetResource(TextureSheetDTO sheetDTO) {
        this.sheetDTO = sheetDTO;
        this.textureSheetName = sheetDTO.name();
    }
    
    //===========
    // Overrides
    //===========
    
    @Override public String getResourceName() { return textureSheetName; }
    @Override public boolean isLoaded() { return isLoaded; }
    @Override public ResourceType getResourceType() { return ResourceType.TEXTURE_SHEET; }
    
    @Override public void setLoaded(boolean value) { isLoaded = value; }
    
    //=========
    // Getters
    //=========
    
    public TextureSheetDTO getTextureSheetDTO() { return sheetDTO; }
    public GameTextureDTO getGameTextureDTO() { return sheetDTO.texture(); }
    public List<SpriteDTO> getSpriteDTOs() { return sheetDTO.sprites(); }
    
    public GameTexture getGameTexture() { return texture; }
    public EList<Sprite> getSprites() { return sprites; }
    
    //====================
    // Setters : Internal
    //====================
    
    /** Set by the resource loader. */
    void setGameTexture(GameTexture textureIn) { texture = textureIn; }
    /** Set by the resource loader. */
    void setSprites(EList<Sprite> spritesIn) { sprites = spritesIn; }
    
}
