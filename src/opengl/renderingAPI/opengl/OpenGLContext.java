package opengl.renderingAPI.opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import opengl.renderingAPI.RendererContextType;
import opengl.renderingAPI.RenderingContext;

public class OpenGLContext extends RenderingContext {
	
	private long windowHandle;
	
	public OpenGLContext(long windowHandleIn) {
		super(RendererContextType.OPENGL);
		windowHandle = windowHandleIn;
	}

	@Override
	public void init() {
		GLFW.glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();
	}

	@Override
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(windowHandle);
	}
	
}
