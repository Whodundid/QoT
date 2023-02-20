package envision.engine.rendering.shaders;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import envision.engine.rendering.shaders.util.FragmentShader;
import envision.engine.rendering.shaders.util.VertexShader;
import eutil.math.vectors.Vec2f;
import eutil.math.vectors.Vec3f;
import eutil.math.vectors.Vec4f;

public abstract class ShaderProgram {
	
	private String name;
	private VertexShader vertex;
	private FragmentShader fragment;
	
	private int shaderProgram;
	private boolean isLinked = false;
	private boolean isDestroyed = false;
	
	private static FloatBuffer matrix = MemoryUtil.memAllocFloat(16);
	
	private int uniform_transform;
	private int uniform_view;
	private int uniform_projection;
	private int uniform_isTexture;
	private int uniform_lightPos;
	private int uniform_lightColor;
	
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
		if (vertex == null) throw new RuntimeException(name + ": Vertex Shader is null!");
		if (fragment == null) throw new RuntimeException(name + ": Fragment Shader is null!");
		if (!vertex.isCompiled()) throw new RuntimeException(name + ": Vertex Shader not compiled!");
		if (!fragment.isCompiled()) throw new RuntimeException(name + ": Fragment Shader not compiled!");
		
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
		
		//allows each shader to individually assign their own attribute values
		//createAttribs();
		
		//allows each shader to individually assign their own uniform values
		//createUniforms();
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
		if (!isLinked) return;
		GL20.glUseProgram(0);
		
		if (vertex != null) {
			GL20.glDetachShader(shaderProgram, vertex.getVertexShader());
			vertex.destroy();
		}
		
		if (fragment != null) {
			GL20.glDetachShader(shaderProgram, fragment.getFragmentShader());
			fragment.destroy();
		}
		
		GL20.glDeleteProgram(shaderProgram);
		isLinked = false;
		isDestroyed = true;
	}
	
	protected void createAttribs() {
		bindAttrib(0, "in_position");
		bindAttrib(2, "in_color");
		bindAttrib(1, "in_texCoord");
	}
	
	protected void createUniforms() {
		uniform_view = gul("u_projection");
		uniform_transform = gul("u_transform");
		uniform_transform = gul("u_model");
		//uniform_isTexture = gul("isTexture");
		//uniform_lightPos = gul("lightPos");
		//uniform_lightColor = gul("lightColor");
	}
	
	public void enableDebugOut() {
		final CharSequence[] feedbackVaryings = { "debugX", "debugY", "debugZ" };
		GL30.glTransformFeedbackVaryings(shaderProgram, feedbackVaryings, GL30.GL_INTERLEAVED_ATTRIBS);
	}
	
	public void enableAttribs() {
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
	}

	public void disableAttribs() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
	}
	
//	public void loadTransform(Matrix4f transform) { setUniform(uniform_transform, transform); }
//	public void loadView(Matrix4f view) { setUniform(uniform_view, view); }
//	public void loadProjection(Matrix4f projection) { setUniform(uniform_projection, projection); }
//	public void loadIsTexture(boolean isTexture) { setUniform(uniform_isTexture, isTexture); }
	
	public void setUniform(int location, float val) { GL20.glUniform1f(location, val); }
	public void setUniform(int location, int val) { GL20.glUniform1i(location, val); }
	public void setUniform(int location, boolean val) { GL20.glUniform1i(location, val ? 1 : 0); }
	public void setUniform(int location, Vec2f val) { GL20.glUniform2f(location, val.x, val.y); }
	public void setUniform(int location, Vec3f val) { GL20.glUniform3f(location, val.x, val.y, val.z); }
	public void setUniform(int location, Vec4f val) { GL20.glUniform4f(location, val.x, val.y, val.z, val.w); }
	public void setUniform(int location, Matrix4f val) {
		val.get(matrix).flip();
		GL20.glUniformMatrix4fv(location, false, matrix);
	}

	public int gul(String n) {  return GL20.glGetUniformLocation(shaderProgram, n); }
	public int getUniformLocation(String name) { return GL20.glGetUniformLocation(shaderProgram, name); }
	
	public void setUniform(String name, float val) { GL20.glUniform1f(gul(name), val); }
	public void setUniform(String name, int val) { GL20.glUniform1i(gul(name), val); }
	public void setUniform(String name, boolean val) { GL20.glUniform1i(gul(name), val ? 1 : 0); }
	public void setUniform(String name, Vec2f val) { GL20.glUniform2f(gul(name), val.x, val.y); }
	public void setUniform(String name, Vec3f val) { GL20.glUniform3f(gul(name), val.x, val.y, val.z); }
	public void setUniform(String name, Vec4f val) { GL20.glUniform4f(gul(name), val.x, val.y, val.z, val.w); }
	public void setUniform(String name, Matrix4f val) {
		val.get(matrix).flip();
		GL20.glUniformMatrix4fv(gul(name), false, matrix);
	}
	
	public void setUniform(String name, int[] array) {
		GL20.glUniform1iv(gul(name), array);
	}
	
	protected void bindAttrib(int attribute, String variableName) {
		GL20.glBindAttribLocation(shaderProgram, attribute, variableName);
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
