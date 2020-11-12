package util.renderUtil;

import WindowCreation.Window;
import org.lwjgl.glfw.GLFW;

public class WindowSize {
	
	int width;
	int height;
	
	private int[] w = new int[1];
	private int[] h = new int[1];
	
	public WindowSize(Window gameIn) {
		GLFW.glfwGetWindowSize(gameIn.getWindowHandle(), w, h);
		
		width = w[0];
		height = h[0];
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	@Override public String toString() { return width + ", " + height; }

}
