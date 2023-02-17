package envision.engine.rendering.shaders.util;

import java.io.File;
import java.util.Scanner;
import org.lwjgl.opengl.GL46;

public abstract class VertexShader {
	
	private String name;
	private int vertex;
	private File programPath;
	private String program = "";
	private boolean compiled = false;
	private boolean destroyed = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected VertexShader(String nameIn, String programPathIn) { this(nameIn, new File(programPathIn)); }
	protected VertexShader(String nameIn, File programPathIn) {
		name = nameIn;
		programPath = programPathIn;
		build();
	}
	
	//--------------------
	// Internal Functions
	//--------------------
	
	/** Attempts to build this vertex shader from the provided vertex shader glsl program. */
	private void build() {
		try (Scanner reader = new Scanner(programPath)) {
			while (reader.hasNextLine()) {
				program += reader.nextLine() + "\n";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		//create correct type
		vertex = GL46.glCreateShader(GL46.GL_VERTEX_SHADER);
		
		//compile
		GL46.glShaderSource(vertex, program);
		GL46.glCompileShader(vertex);
		
		//check compilation success
		int[] success = new int[1];
		GL46.glGetShaderiv(vertex, GL46.GL_COMPILE_STATUS, success);
		
		compiled = success[0] == 1;
		
		//print compile error
		if (!compiled) {
			System.out.println(name + ": Compilation Failed!");
			System.out.println(GL46.glGetShaderInfoLog(vertex));
		}
	}
	
	//---------
	// Methods
	//---------
	
	/** Deletes this vertex shader from memory. */
	public void destroy() {
		if (compiled) {
			GL46.glDeleteShader(vertex);
			destroyed = true;
		}
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns true if this vertex shader successfully compiled. */
	public boolean isCompiled() { return compiled; }
	/** Returns true if this vertex shader has been deleted from memory. */
	public boolean isDestroyed() { return destroyed; }
	/** Returns the name of this vertex shader. */
	public String getName() { return name; }
	/** Returns the path to the corresponding glsl vertex shader this was built from. */
	public File getProgramPath() { return new File(programPath.getAbsolutePath()); }
	/** Returns the pointer to the compiled vertex shader in memory. */
	public int getVertexShader() { return vertex; }
	
}
