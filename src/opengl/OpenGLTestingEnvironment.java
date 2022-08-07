package opengl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import opengl.renderingAPI.RenderingContext;
import opengl.renderingAPI.opengl.OpenGLContext;

public class OpenGLTestingEnvironment {
	
	private static RenderingContext context;
	private static long windowHandle;
	private static int vaoID, vboID, iboID;
	
	//---------------------------------------------------------------------------------------------
	// OpenGL Back-end Setup
	//---------------------------------------------------------------------------------------------
	
	public static void runTestingEnvironment(long handle) {
		windowHandle = handle;
		context = new OpenGLContext(handle);
		onAttach();
		runTestLoop();
	}
	
	public static void onAttach() {
		context.init();
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		float[] vertices = new float[] {
			-0.5f, -0.5f, 0.0f,
			 0.5f, -0.5f, 0.0f,
			 0.0f,  0.5f, 0.0f
		};
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		iboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
		
		int[] indices = new int[] {0, 1, 2};
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}
	
	public static void runTestLoop() {
		while (!glfwWindowShouldClose(windowHandle)) {
			glClear(GL_COLOR_BUFFER_BIT);
			glfwPollEvents();
			
			glBindVertexArray(vaoID);
			glDrawElements(GL_TRIANGLES, 3, GL_UNSIGNED_INT, 0);
			
			context.swapBuffers();
		}
		
		glfwDestroyWindow(windowHandle);
		glfwTerminate();
	}
	
	public static void onMouseEvent(int action, int mXIn, int mYIn, int button, int change) {
		
	}
	
	public static void onKeyboardEvent(int action, char typedChar, int keyCode) {
		
	}
	
	public static void onWindowResized() {
		
	}
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	
}
