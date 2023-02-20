package envision.engine.rendering.renderingAPI.opengl;

import java.util.Objects;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import envision.engine.inputHandlers.WindowResizeListener;
import envision.engine.rendering.OpenGLBatchManager;
import envision.engine.rendering.renderingAPI.RendererContextType;
import envision.engine.rendering.renderingAPI.RenderingContext;
import envision.engine.rendering.shaders.ShaderProgram;
import qot.launcher.LauncherLogger;

public class OpenGLContext extends RenderingContext {
	
	private boolean isInit = false;
	private long windowHandle;
	private OpenGLBatchManager primaryBatch;
	
	public OpenGLContext(long windowHandleIn) {
		super(RendererContextType.OPENGL);
		windowHandle = windowHandleIn;
	}

	@Override
	public void init() {
		GLFW.glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();
		GL_ErrorReporter.setup();
		primaryBatch = new OpenGLBatchManager();
		isInit = true;
	}
	
	@Override
	public boolean isInit() {
		return isInit;
	}
	
	@Override
	public void onWindowResized() {
		int width = WindowResizeListener.getWidth();
		int height = WindowResizeListener.getHeight();
		GL11.glViewport(0, 0, width, height);
	}

	@Override
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(windowHandle);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void clearErrors() {
		while (GL11.glGetError() != GL11.GL_NO_ERROR);
	}

	@Override
	public boolean checkErrors() {
		int error = -1;
		while ((error = GL11.glGetError()) != GL11.GL_NO_ERROR) {
			String errorString = "[OpenGL Error] (0x" + Integer.toHexString(error) + ")";
			System.err.println(errorString);
			LauncherLogger.logError(errorString);
			return true;
		}
		return false;
	}
	
	@Override
	public void call(Runnable r) {
		Objects.requireNonNull(r);
		clearErrors();
		r.run();
		if (checkErrors()) throw new RuntimeException();
	}

	@Override
	public OpenGLBatchManager getPrimaryBatch() {
		return primaryBatch;
	}
	
	@Override
	public void drawFrame() {
		primaryBatch.draw();
	}

	@Override
	public void useShader(ShaderProgram programIn) {
		primaryBatch.setShader(programIn);
	}
	
}
