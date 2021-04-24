package renderEngine.shaders.types.basic;

import renderEngine.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", "bin/shaders/basicFragment.glsl");
	}
	
}
