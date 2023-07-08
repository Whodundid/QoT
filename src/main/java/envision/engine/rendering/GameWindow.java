package envision.engine.rendering;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import envision.engine.windows.WindowSize;
import eutil.datatypes.points.Point2i;
import eutil.math.dimensions.Dimension_d;
import eutil.math.dimensions.Dimension_i;
import qot.settings.QoTSettings;

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
	private Dimension_d windowDims = new Dimension_d();
	/** The title of the window. */
	private String windowTitle;
	
	private int preFullscreenX;
	private int preFullscreenY;
	private int preFullscreenWidth;
	private int preFullscreenHeight;
	private boolean isFullscreen = false;
	
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
	
	public GameWindow() { this ("Game", 1600, 900); }
	public GameWindow(int widthIn, int heightIn) { this("Game", widthIn, heightIn); }
	public GameWindow(String windowTitleIn, int widthIn, int heightIn) {
		windowTitle = windowTitleIn;
		width = widthIn;
		height = heightIn;
		
		init();
		
		onWindowResized(width, height);
	}
	
	protected void init() {
		windowHandle = GLFW.glfwCreateWindow(width, height, windowTitle, 0, 0);
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
	public Point2i getWindowPosition() {
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(windowHandle, x, y);
		windowX = x.get();
		windowY = y.get();
		return new Point2i(windowX, windowY);
	}
	
	/** Returns the X coordinate of where this window is located on the screen. */
	public int getX() { return (int) getWindowPosition().x; }
	/** Returns the X coordinate of where this window is located on the screen. */
	public int getY() { return (int) getWindowPosition().y; }
	
	/** Returns the screen dimensions of where this window is located on the screen. */
	public Dimension_i getWindowDims() {
		Point2i pos = getWindowPosition();
		return new Dimension_i(pos.x, pos.y, pos.x + width, pos.y + height);
	}
	
	public void enableVSync(boolean val) {
	    int interval = (QoTSettings.vsync.get()) ? 1 : 0;
        GLFW.glfwSwapInterval(interval);
	}
	
    public void setFullscreen(boolean val) {
        if (val && !isFullscreen) {
            final var dims = getWindowDims();
            preFullscreenX = (int) dims.startX;
            preFullscreenY = (int) dims.startY;
            preFullscreenWidth = (int) dims.width;
            preFullscreenHeight = (int) dims.height;
            long primary = GLFW.glfwGetPrimaryMonitor();
            var mode = GLFW.glfwGetVideoMode(primary);
            GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, mode.redBits());
            GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, mode.greenBits());
            GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, mode.blueBits());
            GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, mode.refreshRate());
            GLFW.glfwSetWindowMonitor(getWindowHandle(), primary, 0, 0, mode.width(), mode.height(), mode.refreshRate());
            width = mode.width();
            height = mode.height();
            isFullscreen = true;
        }
        else if (!val && isFullscreen) {
            int x = preFullscreenX;
            int y = preFullscreenY;
            int w = preFullscreenWidth;
            int h = preFullscreenHeight;
            var mode = GLFW.glfwGetVideoMode(getWindowHandle());
            GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, mode.redBits());
            GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, mode.greenBits());
            GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, mode.blueBits());
            GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, mode.refreshRate());
            GLFW.glfwSetWindowMonitor(getWindowHandle(), 0, x, y, w, h, mode.refreshRate());
            isFullscreen = false;
        }
    }
	
	public void destroy() {
		GLFW.glfwDestroyWindow(windowHandle);
		GLFW.glfwTerminate();
	}
	
	public long getWindowHandle() { return windowHandle; }
	public boolean isFullscreen() { return isFullscreen; }
	
}
