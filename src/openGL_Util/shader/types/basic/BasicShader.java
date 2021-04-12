package openGL_Util.shader.types.basic;

import openGL_Util.shader.ShaderProgram;

/** Basic shader program. Very limited functionality! */
public class BasicShader extends ShaderProgram {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicShader() {
		super("Basic", new BasicVertex(), new BasicFragment());
	}
	
}
