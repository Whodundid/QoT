package engine.renderEngine.shaders.types.basic;

import engine.renderEngine.shaders.ShaderProgram;

/** Basic shader program. Very limited functionality! */
public class BasicShader extends ShaderProgram {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicShader() {
		super("Basic", new BasicVertex(), new BasicFragment());
	}
	
}
