package util.openGL_Util.shader.types.basic;

import util.openGL_Util.shader.util.FragmentShader;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", "bin/shaders/basicFragment.glsl");
	}
	
}
