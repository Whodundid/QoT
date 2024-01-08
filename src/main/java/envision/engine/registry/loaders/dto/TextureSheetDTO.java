package envision.engine.registry.loaders.dto;

import java.util.List;

import eutil.datatypes.util.EList;

/**
 * Ordered in:
 * <TextureSheet>
 *      <TextureSheetName>name</TextureSheetName>
 * </TextureSheet>
 * 
 * @author Hunter Bragg
 */
public record TextureSheetDTO(String name, GameTextureDTO texture, List<SpriteDTO> sprites) {
    
    public TextureSheetDTO(String name, GameTextureDTO texture, SpriteDTO... sprites) {
        this(name, texture, List.of(sprites));
    }
    
    public TextureSheetDTO(String name, String textureName, String texturePath, EList<SpriteDTO> sprites) {
        this(name, new GameTextureDTO(textureName, texturePath), sprites.toArrayList());
    }
    
    public TextureSheetDTO(String name, GameTextureDTO texture, EList<SpriteDTO> sprites) {
        this(name, texture, sprites.toArrayList());
    }
    
}
