package envision.engine.rendering.shaders.types.basic;

import envision.engine.rendering.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", "src/main/resources/shaders/basicFragment.glsl");
	}
	
}
