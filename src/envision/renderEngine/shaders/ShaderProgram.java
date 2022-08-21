package envision.renderEngine.shaders;

import org.lwjgl.opengl.GL20;

import envision.renderEngine.shaders.util.FragmentShader;
import envision.renderEngine.shaders.util.VertexShader;
import eutil.math.Vec2f;
import eutil.math.Vec3f;
import eutil.math.Vec4f;

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
		shaderProgram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderProgram, vertex.getVertexShader());
		GL20.glAttachShader(shaderProgram, fragment.getFragmentShader());
		GL20.glLinkProgram(shaderProgram);
		
		//check link success
		int[] success = new int[1];
		GL20.glGetProgramiv(shaderProgram, GL20.GL_LINK_STATUS, success);
		
		isLinked = success[0] == 1;
		
		//print link error
		if (!isLinked) {
			System.out.println(name + ": link error!");
			System.out.println(GL20.glGetProgramInfoLog(shaderProgram));
		}
	}
	
	//---------
	// Methods
	//---------
	
	/** Use this shader program. */
	public void bind() {
		if (isLinked) {
			GL20.glUseProgram(shaderProgram);
		}
	}
	
	/** Stop using this shader program. */
	public void unbind() {
		if (isLinked) {
			GL20.glUseProgram(0);
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
			if (vertex != null && !vertex.isDestroyed()) { vertex.destroy(); }
			if (fragment != null && !fragment.isDestroyed()) { fragment.destroy(); }
			
			GL20.glDeleteProgram(shaderProgram);
			isLinked = false;
			isDestroyed = true;
		}
	}
	
	public int gul(String n) {  return GL20.glGetUniformLocation(shaderProgram, n); }
	public int getUniformLocation(String name) { return GL20.glGetUniformLocation(shaderProgram, name); }
	
	public void setUniform(String name, float val) { GL20.glUniform1f(gul(name), val); }
	public void setUniform(String name, int val) { GL20.glUniform1i(gul(name), val); }
	public void setUniform(String name, boolean val) { GL20.glUniform1i(gul(name), val ? 1 : 0); }
	public void setUniform(String name, Vec2f val) { GL20.glUniform2f(gul(name), val.x, val.y); }
	public void setUniform(String name, Vec3f val) { GL20.glUniform3f(gul(name), val.x, val.y, val.z); }
	public void setUniform(String name, Vec4f val) { GL20.glUniform4f(gul(name), val.x, val.y, val.z, val.w); }
	/*
	public void setUniform(String name, Matrix4f val) {
		FloatBuffer matrix = MemoryUtil.memAllocFloat(Matrix4f.SIZE * Matrix4f.SIZE);
		matrix.put(val.getAll()).flip();
		GL20.glUniformMatrix4fv(gul(name), true, matrix);
	}
	*/
	
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
