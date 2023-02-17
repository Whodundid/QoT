package envision.engine.inputHandlers;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

import envision.debug.testStuff.testing.OpenGLTestingEnvironment;
import qot.Main;
import qot.QoT;

public class WindowResizeListener extends GLFWWindowSizeCallback {

	private static WindowResizeListener instance;
	private static int width;
	private static int height;
	
	public static WindowResizeListener getInstance() {
		return instance = (instance != null) ? instance : new WindowResizeListener();
	}
	
	private WindowResizeListener() {
		super();
	}
	
	@Override
	public void invoke(long window, int widthIn, int heightIn) {
		width = widthIn;
		height = heightIn;
		
		if (Main.RUN_OPEN_GL_TESTING_ENVIRONMENT) OpenGLTestingEnvironment.onWindowResized();
		else QoT.getGame().onWindowResize();
	}

	public static int getWidth() { return width; }
	public static int getHeight() { return height; }
	
}
