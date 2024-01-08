package envision.engine.registry.loaders;

import org.joml.Vector2f;

import envision.engine.rendering.textureSystem.GameTexture;
import eutil.math.dimensions.Dimension_i;

public class Sprite {
    
    //private SpriteSheet parentSheet;
    /** The location of where this sprite is located within a sprite sheet. */
    //private Dimension_i spriteLocation;
    
    private GameTexture texture;
    private Vector2f[] texCoords;
    private Dimension_i dimensions;
    
    public Sprite(GameTexture texture) {
        this.texture = texture;
        this.texCoords = new Vector2f[] {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
        };
        this.dimensions = new Dimension_i(0, 0, texture.getWidth(), texture.getHeight());
    }
    
    public Sprite(GameTexture texture, Vector2f[] texCoords, Dimension_i locationIn) {
        this.texture = texture;
        this.texCoords = texCoords;
        this.dimensions = locationIn;
    }
    
    public GameTexture getTexture() { return texture; }
    public Vector2f[] getTextureCoords() { return texCoords; }
    public Dimension_i getDimensions() { return dimensions; }
    
    public int getWidth() { return dimensions.width; }
    public int getHeight() { return dimensions.height; }
    
}
