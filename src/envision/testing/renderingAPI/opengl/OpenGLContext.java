package envision.testing.renderingAPI.opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import envision.testing.renderingAPI.RendererContextType;
import envision.testing.renderingAPI.RenderingContext;
import envision.windowLib.WindowSize;
import game.QoT;

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
	public void onWindowResized() {
		WindowSize size = QoT.getWindowSize();
		GL11.glViewport(0, 0, size.getWidth(), size.getHeight());
	}

	@Override
	public void swapBuffers() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GLFW.glfwSwapBuffers(windowHandle);
	}
	
}
