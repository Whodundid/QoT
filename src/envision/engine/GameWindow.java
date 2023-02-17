package envision.engine;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import envision.engine.windows.WindowSize;
import eutil.math.dimensions.EDimension;
import eutil.math.vectors.Vec2i;

public class GameWindow {
	
	/** The GLFW address of this window. */
	private long windowHandle;
	/** The physical x and y coordinates on a monitor of where this window is being drawn. */
	private int windowX, windowY;
	/** The Width and Height of this game window. */
	private int width, height;
	/** An index referring to the specific computer monitor this window is being drawn on. */
	private int monitorIndex;
	/** The most recently known dimensions of this game window on screen. */
	private EDimension windowDims = new EDimension();
	/** The title of the window. */
	private String windowTitle;
	
	private int[] xPos = new int[1], yPos = new int[1];
	
	// Framerate stuff
	private long FPS = 60; //60 fps by default -- user can modify
	private double timeF = 1000000000 / FPS;
	private double deltaF = 0;
	private long startTime = 0l;
	private long runningTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	//==============
	// Constructors
	//==============
	
	public GameWindow() { this ("Game", 800, 640); }
	public GameWindow(int widthIn, int heightIn) { this("Game", widthIn, heightIn); }
	public GameWindow(String windowTitleIn, int widthIn, int heightIn) {
		windowTitle = windowTitleIn;
		width = widthIn;
		height = heightIn;
		
		init();
		
		onWindowResized(width, height);
	}
	
	protected void init() {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			
			GLFW.glfwGetWindowSize(windowHandle, w, h);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			
			xPos[0] = ((vidmode.width() - w.get(0)) / 2);
			yPos[0] = (vidmode.height() - h.get(0)) / 2;
			GLFW.glfwSetWindowPos(windowHandle, xPos[0], yPos[0]);
		}
	}
	
	/** Internal method which should be called every time the game window is resized. */
	public void onWindowResized(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
		
		// recalculate window dims
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(windowHandle, x, y);
		windowX = x.get();
		windowY = y.get();
		windowDims.set(windowX, windowY, windowX + width, windowY + height);
		
		GL11.glViewport(0, 0, width, height);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		// back to model view for drawing
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
		GL11.glTranslatef(-1.0f, -1.0f, 0.0f);
		GL11.glScalef(2.0f / width, 2.0f / height, 1);
		
		GL11.glTranslatef(2.0f, 2.0f, 0.0f);
	}
	
	/**
	 * Returns a WindowSize object containing values pertaining to the
	 * active game window.
	 */
	public WindowSize getWindowSize() { return new WindowSize(width, height); }
	/** Returns the game window's width in pixels. */
	public int getWidth() { return width; }
	/** Returns the game window's height in pixels. */
	public int getHeight() { return height; }
	
	/** Returns the X and Y coordinates of where this window is located on the screen. */
	public Vec2i getWindowPosition() {
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(windowHandle, x, y);
		windowX = x.get();
		windowY = y.get();
		return new Vec2i(windowX, windowY);
	}
	
	/** Returns the X coordinate of where this window is located on the screen. */
	public int getX() { return (int) getWindowPosition().x; }
	/** Returns the X coordinate of where this window is located on the screen. */
	public int getY() { return (int) getWindowPosition().y; }
	
	/** Returns the screen dimensions of where this window is located on the screen. */
	public EDimension getWindowDims() {
		Vec2i pos = getWindowPosition();
		return new EDimension(pos.x, pos.y, pos.x + width, pos.y + height);
	}
	
	public long getWindowHandle() { return windowHandle; }
	
}
