package input;

import main.QoT;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

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
		
		QoT.getGame().onWindowResize();
	}

	public static int getWidth() { return width; }
	public static int getHeight() { return height; }
	
}
