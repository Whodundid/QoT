package envision.engine.rendering.renderingAPI.opengl;

import java.util.Objects;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import envision.Envision;
import envision.engine.inputHandlers.WindowResizeListener;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.renderingAPI.RendererContextType;
import envision.engine.rendering.renderingAPI.RenderingContext;
import envision.engine.rendering.shaders.Shaders;
import qot.launcher.LauncherLogger;

public class OpenGLContext extends RenderingContext {
	
	private boolean isInit = false;
	private long windowHandle;
	private BatchManager batchManager;
	
	public OpenGLContext(long windowHandleIn) {
		super(RendererContextType.OPENGL);
		windowHandle = windowHandleIn;
	}

	@Override
	public void init() {
		GLFW.glfwMakeContextCurrent(windowHandle);
		GL.createCapabilities();
		GL_ErrorReporter.setup();
		Shaders.init();
		batchManager = new BatchManager();
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
		
		Envision.getRenderEngine().getPerspectiveCamera().setupForPerspective(width, height);
		Envision.getRenderEngine().getOrthoCamrea().setupForOrtho(width, height);
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
	public BatchManager getBatchManager() {
		return batchManager;
	}
	
	@Override
	public void drawFrame() {
		BatchManager.draw();
	}

	@Override
	public void destroy() {
		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
		GL.destroy();
		GL_ErrorReporter.destroy();
		Shaders.destroy();
	}
	
}
