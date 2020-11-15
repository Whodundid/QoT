package openGL_Util.shader;

import openGL_Util.shader.types.basic.BasicShader;
import org.lwjgl.glfw.GLFW;
import util.storageUtil.EArrayList;

/** Stores references to all available shaders. */
public final class Shaders {
	
	// Hide Constructor
	private Shaders() {}
	
	//----------------
	// Private Fields
	//----------------
	
	/** Value indicating whether or not each shader has been initialized. */
	private static boolean init = false;
	/** List containing each shader. */
	private static EArrayList<ShaderProgram> shaders = new EArrayList();
	
	//---------
	// Shaders
	//---------
	
	
	/** Basic shader. */
	public static ShaderProgram basic;
	
	
	//-----------------------
	// Static Shader Methods
	//-----------------------
	
	public static void bind(ShaderProgram program) {
		if (init && program != null && program.isBuilt()) { program.bind(); }
	}
	
	//---------------------
	// Initializer Methods
	//---------------------
	
	/** Initializes each shader. Will not double initialize. */
	public static void init() {
		if (GLFW.glfwInit() && !init) {
			
			shaders.add(basic = new BasicShader());
			
			init = true;
		}
	}
	
	/** Deletes each shader program's vertex and fragment shader programs after they have been linked. */
	public static void cleanUp() {
		if (init) {
			shaders.filterNull().forEach(s -> s.cleanUp());
		}
	}
	
	/** Attempts to rebuild each shader. */
	public static void reInit() {
		if (init) {
			shaders.clear();
		}
		init();
	}
	
	/** Returns true if each shader has been successfully built. */
	public static boolean isInit() { return init; }
	
}
