package envision.engine.rendering.shaders.types.basic;

import envision.engine.rendering.shaders.util.VertexShader;

/** Basic vertex shader. */
public class BasicVertex extends VertexShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicVertex() {
		super("BasicVertex", "src/main/resources/shaders/basicVertex.glsl");
	}
	
}
