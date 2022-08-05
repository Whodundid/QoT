package opengl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class OpenGLTestingEnvironment {
	
	private static long windowHandle;
	
	//---------------------------------------------------------------------------------------------
	// OpenGL Back-end Setup
	//---------------------------------------------------------------------------------------------
	
	public static void runTestingEnvironment(long handle) {
		windowHandle = handle;
		onAttach();
		runTestLoop();
	}
	
	public static void onAttach() {
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		float[] vertices = new float[] {
			-0.5f, -0.5f, 0.0f,
			 0.5f, -0.5f, 0.0f,
			 0.5f,  0.5f, 0.0f,
			-0.5f,  0.5f, 0.0f
		};
		
		//glCreateVertexArrays(1, )
	}
	
	public static void runTestLoop() {
		while (!glfwWindowShouldClose(windowHandle)) {
			glClear(GL_COLOR_BUFFER_BIT);
			glfwPollEvents();
			
			glfwSwapBuffers(windowHandle);
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
