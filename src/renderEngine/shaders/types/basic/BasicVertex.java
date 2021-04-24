package renderEngine.shaders.types.basic;

import renderEngine.shaders.util.VertexShader;

/** Basic vertex shader. */
public class BasicVertex extends VertexShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicVertex() {
		super("BasicVertex", "bin/shaders/basicVertex.glsl");
	}
	
}
