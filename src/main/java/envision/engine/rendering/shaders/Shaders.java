package envision.engine.rendering.shaders;

import envision.engine.rendering.shaders.types.basic.BasicShader;
import envision.engine.rendering.shaders.types.fixed.FixedShader;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

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
	private static EList<ShaderProgram> shaders = new EArrayList<>();
	
	//---------
	// Shaders
	//---------
	
	
	/** Basic shader. */
	public static ShaderProgram basic;
	
	/** Fixed function pipeline shader (IDK) */
	public static ShaderProgram fixed;
	
	
	//-----------------------
	// Static Shader Methods
	//-----------------------
	
	public static void bind(ShaderProgram program) {
		if (init && program != null && program.isBuilt()) {
			program.bind();
		}
	}
	
	public static void unbind(ShaderProgram program) {
		if (init && program != null && program.isBuilt()) {
			program.unbind();
		}
	}
	
	//---------------------
	// Initializer Methods
	//---------------------
	
	/** Initializes each shader. Will not double initialize. */
	public static void init() {
		if (!init) {
			
			shaders.add(basic = new BasicShader());
			shaders.add(fixed = new FixedShader());
			
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
	
	/** Deletes each shader program. */
	public static void destroy() {
		if (init) {
			shaders.filterNull().forEach(s -> s.destroy());
		}
	}
	
	/** Returns true if each shader has been successfully built. */
	public static boolean isInit() { return init; }
	
}
