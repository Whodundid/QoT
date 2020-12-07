package util.openGL_Util.shader.util;

import java.io.File;
import java.util.Scanner;
import org.lwjgl.opengl.GL46;

public class FragmentShader {
	
	private String name;
	private int fragment;
	private File programPath;
	private String program = "";
	private boolean compiled = false;
	private boolean destroyed = false;
	
	//--------------
	// Constructors
	//--------------
	
	protected FragmentShader(String nameIn, String programPathIn) { this(nameIn, new File(programPathIn)); }
	protected FragmentShader(String nameIn, File programPathIn) {
		name = nameIn;
		programPath = programPathIn;
		build();
	}
	
	//--------------------
	// Internal Functions
	//--------------------
	
	/** Attempts to build this fragment shader from the provided fragment shader glsl program. */
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
		fragment = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER);
		
		//compile
		GL46.glShaderSource(fragment, program);
		GL46.glCompileShader(fragment);
		
		//check compilation success
		int[] success = new int[1];
		GL46.glGetShaderiv(fragment, GL46.GL_COMPILE_STATUS, success);
		
		compiled = success[0] == 1;
		
		//print compile error
		if (!compiled) {
			System.out.println(name + " Compilation Failed!");
			System.out.println(GL46.glGetShaderInfoLog(fragment));
		}
	}
	
	//---------
	// Methods
	//---------
	
	/** Deletes this fragment shader from memory. */
	public void destroy() {
		if (compiled) {
			GL46.glDeleteShader(fragment);
			destroyed = true;
		}
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns true if this fragment shader successfully compiled. */
	public boolean isCompiled() { return compiled; }
	/** Returns true if thsi fragment shader has been deleted from memory. */
	public boolean isDestroyed() { return destroyed; }
	/** Returns the name of this fragment shader. */
	public String getName() { return name; }
	/** Returns the path to the corresponding glsl fragment shader this was built from. */
	public File getProgramPath() { return new File(programPath.getAbsolutePath()); }
	/** Returns the pointer to the compiled fragment shader in memory. */
	public int getFragmentShader() { return fragment; }
	
}
