package envisionEngine.renderEngine.shaders.types.basic;

import envisionEngine.renderEngine.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", "resources/shaders/basicFragment.glsl");
	}
	
}
