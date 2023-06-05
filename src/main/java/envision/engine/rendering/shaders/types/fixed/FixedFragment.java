package envision.engine.rendering.shaders.types.fixed;

import envision.engine.rendering.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class FixedFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public FixedFragment() {
		super("FixedFragment", "src/main/resources/shaders/fixedFragment.glsl");
	}
	
}
