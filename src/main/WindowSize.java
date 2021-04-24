package main;

import org.lwjgl.glfw.GLFW;

public class WindowSize {
	
	int width;
	int height;
	
	private int[] w = new int[1];
	private int[] h = new int[1];
	
	public WindowSize(QoT gameIn) {
		GLFW.glfwGetWindowSize(gameIn.getWindowHandle(), w, h);
		
		width = w[0];
		height = h[0];
	}
	
	@Override public String toString() { return width + ", " + height; }
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }

}
