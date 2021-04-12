package openGL_Util.shader.types.basic;

import openGL_Util.shader.util.VertexShader;

/** Basic vertex shader. */
public class BasicVertex extends VertexShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicVertex() {
		super("BasicVertex", "bin/shaders/basicVertex.glsl");
	}
	
}
