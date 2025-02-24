package envision.engine.internal.rendering.shaders.types.fixed;

import envision.engine.internal.rendering.shaders.util.VertexShader;

/** Basic vertex shader. */
public class FixedVertex extends VertexShader {    
    //==============
    // Constructors
    //==============
    
    public FixedVertex() {
        super("FixedVertex", "src/main/resources/shaders/fixedVertex.glsl");
    }
    
}
