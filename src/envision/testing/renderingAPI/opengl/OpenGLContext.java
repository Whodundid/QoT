package envision.testing.renderingAPI.opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;

import envision.testing.renderingAPI.RendererContextType;
import envision.testing.renderingAPI.RenderingContext;

public class OpenGLContext extends RenderingContext {
	
	private boolean isInit = false;
	private long windowHandle;
	
	public OpenGLContext(long windowHandleIn) {
		super(RendererContextType.OPENGL);
		windowHandle = windowHandleIn;
	}

	@Override
	public void init() {
		GLFW.glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();
		isInit = true;
	}
	
	@Override
	public boolean isInit() {
		return isInit;
	}

	@Override
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(windowHandle);
	}
	
}
