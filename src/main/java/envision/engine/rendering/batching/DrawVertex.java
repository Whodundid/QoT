package envision.engine.rendering.batching;

public record DrawVertex
(
    //========
    // Fields
    //========
    
    /** Position XYZ */
    float x, float y, float z,
    /** Color RGBA */
    float r, float g, float b, float f,
    /** Texture X/Y Coordinates */
    float tx, float ty,
    /** Texture ID */
    float tid
    
)
{
    //==============
    // Constructors
    //==============
    
    /** 2D position */
    public DrawVertex(float x, float y) {
        this(x, y, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }
    
    /** 3D position */
    public DrawVertex(float x, float y, float z) {
        this(x, y, z, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f);
    }
    
    public DrawVertex(float x, float y, float z, float r, float g, float b, float f) {
        this(x, y, z, r, g, b, f, 0.0f, 0.0f, 0.0f);
    }
    
    public DrawVertex(float x, float y, float r, float g, float b, float f, float tx, float ty, float tid) {
        this(x, y, 0.0f, r, g, b, f, 0.0f, 0.0f, 0.0f);
    }
    
}
