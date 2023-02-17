package envision.engine.rendering.shaders.types.basic;

import envision.engine.rendering.shaders.ShaderProgram;

/** Basic shader program. Very limited functionality! */
public class BasicShader extends ShaderProgram {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicShader() {
		super("Basic", new BasicVertex(), new BasicFragment());
	}
	
}
