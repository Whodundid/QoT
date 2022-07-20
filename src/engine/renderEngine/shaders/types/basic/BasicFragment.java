package engine.renderEngine.shaders.types.basic;

import engine.renderEngine.shaders.util.FragmentShader;
import main.settings.QoTSettings;

/** Basic fragment shader. */
public class BasicFragment extends FragmentShader {
	
	//--------------
	// Constructors
	//--------------
	
	public BasicFragment() {
		super("BasicFragment", QoTSettings.getResourcesDir() + "\\shaders\\basicFragment.glsl");
	}
	
}
