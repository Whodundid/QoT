package envision.engine.internal.rendering.shaders.types.basic;

import envision.engine.internal.rendering.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {    
    //==============
    // Constructors
    //==============
    
    public BasicFragment() {
        super("BasicFragment", "src/main/resources/shaders/basicFragment.glsl");
    }
    
}
