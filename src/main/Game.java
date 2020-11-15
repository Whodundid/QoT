package main;

import eWindow.Button;
import input.Keyboard;
import input.Mouse;
import input.WindowResizeListener;
import openGL_Util.GLObject;
import openGL_Util.shader.Shaders;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import util.renderUtil.EColors;
import util.renderUtil.WindowSize;

public class Game {
	
	public static long handle = -1;
	private static Game instance = null;
	
	private int width;
	private int height;
	
	public long startTime = 0l;
	public long runningTime = 0l;
	private long frameTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}
	
	private Game() {
		
		if (!GLFW.glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
		}
		
		width = 1080;
		height = 720;
		
		handle = GLFW.glfwCreateWindow(width, height, "LWJGL Program", 0, 0);
		
		GLFW.glfwShowWindow(handle);
		GLFW.glfwMakeContextCurrent(handle);
		
		GL.createCapabilities();
		
		GLFW.glfwSetKeyCallback(handle, Keyboard.getInstance());
		GLFW.glfwSetMouseButtonCallback(handle, Mouse.getInstance());
		GLFW.glfwSetCursorPosCallback(handle, Mouse.getInstance().getCursorPosCallBack());
		GLFW.glfwSetScrollCallback(handle, Mouse.getInstance().getScrollCallBack());
		GLFW.glfwSetWindowSizeCallback(handle, WindowResizeListener.getInstance());
		
		//initialize shaders
		Shaders.init();
	}
	
	public void runGame() {
		Thread t = new Thread(Main.m);
		t.start();
		
		float val1 = 1;
		float val2 = 1;
		float val3 = 1;
		
		float xf = 0;
		float yf = 0;
		
		Button b = new Button(50, 50, 150, 100);
		//Button b1 = new Button(250, 50, 150, 100);
		
		while (!GLFW.glfwWindowShouldClose(handle)) {
			
			GLFW.glfwPollEvents();
			updateFramerate();
			
			int mX = Mouse.getMx();
			int mY = Mouse.getMy();
			String mouseCoords = "(" + mX + ", " + mY + ")";
			
			GLFW.glfwSetWindowTitle(Game.getWindowHandle(), "QoT      FPS: " + curFrameRate + "              " + mouseCoords);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			
			if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_W) == GL11.GL_TRUE) {
				yf += 0.01f;
			}
			if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_A) == GL11.GL_TRUE) {
				xf -= 0.01f;
			}
			if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_S) == GL11.GL_TRUE) {
				yf -= 0.01f;
			}
			if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_D) == GL11.GL_TRUE) {
				xf += 0.01f;
			}
			
			if (GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_ESCAPE) == GL11.GL_TRUE) {
				GLFW.glfwTerminate();
			}
			
			if (Mouse.isButtonDown(0)) {
				val1 = 0.25f;
				val2 = 0.77f;
				val3 = 0.25f;
			}
			else {
				val1 = 1;
				val2 = 1;
				val3 = 1;
			}
			
			GLObject.drawRect(0, 0, width, height, EColors.lgray);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(val1, 0, 0, 0);
			GL11.glVertex2f(-0.5f+xf, 0.5f+yf);
			GL11.glColor4f(0, val2, 0, 0);
			GL11.glVertex2f(0.5f+xf, 0.5f+yf);
			GL11.glColor4f(0, 0, val3, 0);
			GL11.glVertex2f(0.5f+xf, -0.5f+yf);
			GL11.glColor4f(val1, val2, val3, 0);
			GL11.glVertex2f(-0.5f+xf, -0.5f+yf);
			GL11.glEnd();
			
			b.draw(Mouse.getMx(), Mouse.getMy());
			//b1.draw(Mouse.getMx(), Mouse.getMy());

			GLFW.glfwSwapBuffers(handle);
		}
		
		GLFW.glfwTerminate();
		
	}
	
	/** Simple framerate calculator. */
	private void updateFramerate() {
		frames++;
		if (System.currentTimeMillis() > frameTime + 1000) {
			curFrameRate = frames;
			frameTime = System.currentTimeMillis();
			frames = 0;
		}
	}
	
	public static long getWindowHandle() { return handle; }
	
	public void onWindowResize() {
		WindowResizeListener w = WindowResizeListener.getInstance();
		width = w.getWidth();
		height = w.getHeight();
		GL11.glViewport(0, 0, width, height);
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public static WindowSize getWindowSize() { return new WindowSize(Game.getInstance()); }
	
}
