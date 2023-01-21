package envision.renderEngine.shaders.types.basic;

import envision.renderEngine.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", "resources/shaders/basicFragment.glsl");
	}
	
}
