package envision.engine.loader.dtos.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import envision.engine.loader.built.game.SpriteSheet;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

/**
 * Example:
 * 
 * <pre>
 * "SpriteSheetDTO": {
 *      "name": "Example Sprite Sheet",
 *      "texture": "The name of a TextureResourceDTO to reference",
 *      "sprites" : [ {
 *          ?
 *      } ],
 *      "shaders" : [ {
 *          "Shiny Shader"
 *      ] }
 * }
 * </pre>
 * 
 * @param name    The name of this SpriteSheet
 * @param texture The string name of a TextureResourceDTO to reference as this
 *                sheet's texture
 * @param sprites A list of SpriteDTOs
 * @param shaders A list of names for ShaderResourceDTOs
 * 
 * @author Hunter Bragg
 */
public record SpriteSheetDTO(@Optional String name, String texture, List<SpriteDTO> sprites, @Optional List<String> shaders)
    implements IDataTransferObject<SpriteSheet>
{
    //==============
    // Constructors
    //==============
    
    /** SpriteSheet names and shaders are optional. */
    public SpriteSheetDTO(String texture, List<SpriteDTO> sprites) {
        this(null, texture, sprites, new ArrayList<>());
    }
    
    /** Shaders are optional. */
    public SpriteSheetDTO(@Optional String name, String texture, List<SpriteDTO> sprites) {
        this(name, texture, sprites, new ArrayList<>());
    }
    
    /** Constructor to ensure that data is properly set and unmodifiable. */
    public SpriteSheetDTO(@Optional String name, String texture, List<SpriteDTO> sprites, @Optional List<String> shaders) {
        // make sure every value has a non-null value
        Assertions.assertNotNull(texture);
        
        // if the list values are not strictly ArrayList types, make them into ArrayLists
        if (!(sprites instanceof ArrayList<SpriteDTO>)) sprites = new ArrayList<>(sprites);
        if (!(shaders instanceof ArrayList<String>)) shaders = new ArrayList<>(shaders);
        
        // wrap the lists into unmodifiable versions of themselves
        if (sprites != null) sprites = Collections.unmodifiableList(sprites);
        if (shaders != null) shaders = Collections.unmodifiableList(shaders);
        
        // asign each value to the record
        this.name = name;
        this.texture = texture;
        this.shaders = shaders;
        this.sprites = sprites;
    }

    //===========
    // Overrides
    //===========
    
    @Override
    public SpriteSheet fromDto() {
        return null;
    }
    
}
