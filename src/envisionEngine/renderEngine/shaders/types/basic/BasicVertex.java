package envisionEngine.renderEngine.shaders.types.basic;

import envisionEngine.renderEngine.shaders.util.VertexShader;

/** Basic vertex shader. */
public class BasicVertex extends VertexShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicVertex() {
		super("BasicVertex", "resources/shaders/basicVertex.glsl");
	}
	
}
