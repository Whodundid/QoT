package envision.engine.loader.dtos.game;

import envision.engine.loader.built.game.Sprite;
import envision.engine.loader.dtos.IDataTransferObject;
import eutil.assertions.Assertions;
import eutil.debug.Optional;

/**
 * Example:
 * 
 * <pre>
 * "SpriteDTO": {
 *      "name": "Sprite 1",
 *      "sx": 0,
 *      "sy": 0,
 *      "ex": 16,
 *      "ey": 16
 * }
 * </pre>
 * 
 * @param name   The name of this sprite so that it is referencable by other DTO
 *               objects
 * @param coords The texture coordinates of this sprite
 * 
 * @author Hunter
 */
public record SpriteDTO(@Optional String name, int sx, int sy, int ex, int ey) implements IDataTransferObject<Sprite> {
    
    //==============
    // Constructors
    //==============
    
    /**
     * A sprite can still be created directly if the texture coordinates are
     * provided.
     */
    public SpriteDTO(String name, int sx, int sy, int ex, int ey) {
        Assertions.assertNotNull(name);
        
        this.name = name;
        this.sx = sx;
        this.sy = sy;
        this.ex = ex;
        this.ey = ey;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public Sprite fromDto() {
        return null;
    }
    
}
