package engine;

public class WindowSize {
	
	int width;
	int height;
	
	//private int[] w = new int[1];
	//private int[] h = new int[1];
	
	public WindowSize(QoT gameIn) {
		//GLFW.glfwGetWindowSize(QoT.getWindowHandle(), w, h);
		
		width = QoT.getWidth();
		height = QoT.getHeight();
	}
	
	@Override public String toString() { return width + ", " + height; }
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }

}
