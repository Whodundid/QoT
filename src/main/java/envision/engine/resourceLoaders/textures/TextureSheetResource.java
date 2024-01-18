package envision.engine.resourceLoaders.textures;

import java.util.List;

import envision.engine.loader.dtos.GameTextureDTO;
import envision.engine.loader.dtos.SpriteDTO;
import envision.engine.loader.dtos.TextureSheetDTO;
import envision.engine.registry.IGameResource;
import envision.engine.registry.ResourceType;
import envision.engine.registry.types.Sprite;
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
    @Override public ResourceType getResourceType() { return ResourceType.TEXTURE; }
    
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
