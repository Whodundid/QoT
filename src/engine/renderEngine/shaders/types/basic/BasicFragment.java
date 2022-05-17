package engine.renderEngine.shaders.types.basic;

import engine.renderEngine.shaders.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", "bin/shaders/basicFragment.glsl");
	}
	
}
