package envision.engine.internal.rendering.shaders.types.fixed;

import envision.engine.internal.rendering.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class FixedFragment extends FragmentShader {    
    //==============
    // Constructors
    //==============
    
    public FixedFragment() {
        super("FixedFragment", "src/main/resources/shaders/fixedFragment.glsl");
    }
    
}
