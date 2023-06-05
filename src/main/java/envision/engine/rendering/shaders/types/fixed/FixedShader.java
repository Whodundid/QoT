package envision.engine.rendering.shaders.types.fixed;

import envision.engine.rendering.shaders.ShaderProgram;

/** Basic shader program. Very limited functionality! */
public class FixedShader extends ShaderProgram {
	
	//--------------
	// Constructors
	//--------------
	
	public FixedShader() {
		super("Fixed", new FixedVertex(), new FixedFragment());
	}
	
}
