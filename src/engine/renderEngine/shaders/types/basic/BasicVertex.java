package engine.renderEngine.shaders.types.basic;

import engine.renderEngine.shaders.util.VertexShader;

/** Basic vertex shader. */
public class BasicVertex extends VertexShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicVertex() {
		super("BasicVertex", "resources/shaders/basicVertex.glsl");
	}
	
}
