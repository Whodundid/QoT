package openGL_Util.shader;

import openGL_Util.shader.util.FragmentShader;
import openGL_Util.shader.util.VertexShader;
import org.lwjgl.opengl.GL46;

public abstract class ShaderProgram {
	
	private String name;
	private VertexShader vertex;
	private FragmentShader fragment;
	
	private int shaderProgram;
	private boolean isLinked = false;
	private boolean isDestroyed = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected ShaderProgram(String nameIn, VertexShader vertexIn, FragmentShader fragmentIn) {
		name = nameIn;
		vertex = vertexIn;
		fragment = fragmentIn;
		build();
	}
	
	//--------------------
	// Internal Functions
	//--------------------
	
	/** Attempts to build this shader with the corresponding vertex and fragment shaders. */
	private void build() {
		if (vertex == null) { throw new RuntimeException(name + ": Vertex Shader is null!"); }
		if (fragment == null) { throw new RuntimeException(name + ": Fragment Shader is null!"); }
		if (!vertex.isCompiled()) { throw new RuntimeException(name + ": Vertex Shader not compiled!"); }
		if (!fragment.isCompiled()) { throw new RuntimeException(name + ": Fragment Shader not compiled!"); }
		
		//create program
		shaderProgram = GL46.glCreateProgram();
		GL46.glAttachShader(shaderProgram, vertex.getVertexShader());
		GL46.glAttachShader(shaderProgram, fragment.getFragmentShader());
		GL46.glLinkProgram(shaderProgram);
		
		//check link success
		int[] success = new int[1];
		GL46.glGetProgramiv(shaderProgram, GL46.GL_LINK_STATUS, success);
		
		isLinked = success[0] == 1;
		
		//print link error
		if (!isLinked) {
			System.out.println(name + ": link error!");
			System.out.println(GL46.glGetProgramInfoLog(shaderProgram));
		}
	}
	
	//---------
	// Methods
	//---------
	
	/** Use this shader program. */
	public void bind() {
		if (isLinked) {
			GL46.glUseProgram(shaderProgram);
		}
	}
	
	/** Deletes the vertex and fragment shaders after building. */
	public void cleanUp() {
		if (isLinked) {
			vertex.destroy();
			fragment.destroy();
		}
	}
	
	/** Deletes this shader program from memory. Cannot be undone! */
	public void destroy() {
		if (isLinked) {
			GL46.glDeleteProgram(shaderProgram);
			isLinked = false;
			isDestroyed = true;
		}
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns true if this shader program has been successfully compiled and linked. */
	public boolean isBuilt() { return isLinked; }
	/** Returns true if this shader program has been deleted. */
	public boolean isDestroyed() { return isDestroyed; }
	/** Returns the name of this shader program. */
	public String getName() { return name; }
	/** Returns this shader program's vertex shader. */
	public VertexShader getVertexShader() { return vertex; }
	/** Returns this shader program's fragment shader. */
	public FragmentShader getFragmentShader() { return fragment; }
	
}
