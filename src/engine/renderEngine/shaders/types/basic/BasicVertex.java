package engine.renderEngine.shaders.types.basic;

import engine.renderEngine.shaders.util.VertexShader;
import main.settings.QoTSettings;

/** Basic vertex shader. */
public class BasicVertex extends VertexShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicVertex() {
		super("BasicVertex", QoTSettings.getResourcesDir() + "\\shaders\\basicVertex.glsl");
	}
	
}
