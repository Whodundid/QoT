package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import eWindow.Button;
import input.Keyboard;
import input.Mouse;
import input.WindowResizeListener;
import java.util.Scanner;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import util.renderUtil.WindowSize;

public class Game {
	
	public static long handle = -1;
	private static Game instance = null;
	
	private int width;
	private int height;
	
	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
		}
		return instance;
	}
	
	private Game() {
		
		if (!glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
		}
		
		Scanner reader = new Scanner(System.in);
		int response1 = 0;
		
		Boolean active = true;
		
		while (active) {
			System.out.println("What resolution would you like the screen to be? 640x480 = [1] 1080x720 = [2]");
			response1 = reader.nextInt();
			if (response1 == 1) {
				width = 640;
				height = 480;
				active = false;
			}
			else if (response1 == 2) {
				width = 1080;
				height = 720;
				active = false;
			}
			else {
				active = true;
				System.out.println("Please try again. Type 1 or 2. " + response1 + " was invalid.");
			}
		}
		
		handle = glfwCreateWindow(width, height, "LWJGL Program", 0, 0);
		
		glfwShowWindow(handle);
		glfwMakeContextCurrent(handle);
		
		GL.createCapabilities();
		
		GLFW.glfwSetKeyCallback(handle, Keyboard.getInstance());
		GLFW.glfwSetMouseButtonCallback(handle, Mouse.getInstance());
		GLFW.glfwSetCursorPosCallback(handle, Mouse.getInstance().getCursorPosCallBack());
		GLFW.glfwSetScrollCallback(handle, Mouse.getInstance().getScrollCallBack());
		GLFW.glfwSetWindowSizeCallback(handle, WindowResizeListener.getInstance());
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
		Button b1 = new Button(250, 50, 150, 100);
		
		while (!glfwWindowShouldClose(handle)) {
			
			glfwPollEvents();
			
			if (glfwGetKey(handle, GLFW_KEY_W) == GL_TRUE) {
				yf += 0.01f;
			}
			if (glfwGetKey(handle, GLFW_KEY_A) == GL_TRUE) {
				xf -= 0.01f;
			}
			if (glfwGetKey(handle, GLFW_KEY_S) == GL_TRUE) {
				yf -= 0.01f;
			}
			if (glfwGetKey(handle, GLFW_KEY_D) == GL_TRUE) {
				xf += 0.01f;
			}
			
			if (glfwGetKey(handle, GLFW_KEY_ESCAPE) == GL_TRUE) {
				glfwTerminate();
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
			
			glClear(GL_COLOR_BUFFER_BIT);
			
			glBegin(GL_QUADS);
			glColor4f(1, 1, 1, 0);
			glVertex2d(-1, 1);
			glVertex2d(1, 1);
			glVertex2d(1, -1);
			glVertex2d(-1, -1);
			glEnd();
			
			glBegin(GL_QUADS);
			glColor4f(val1, 0, 0, 0);
			glVertex2f(-0.5f+xf, 0.5f+yf);
			glColor4f(0, val2, 0, 0);
			glVertex2f(0.5f+xf, 0.5f+yf);
			glColor4f(0, 0, val3, 0);
			glVertex2f(0.5f+xf, -0.5f+yf);
			glColor4f(val1, val2, val3, 0);
			glVertex2f(-0.5f+xf, -0.5f+yf);
			glEnd();
			
			b.draw(Mouse.getMx(), Mouse.getMy());
			b1.draw(Mouse.getMx(), Mouse.getMy());
			
			glfwSwapBuffers(handle);
		}
		
		glfwTerminate();
		
	}
	
	public static long getWindowHandle() { return handle; }
	
	public void onWindowResize() {
		
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public static WindowSize getWindowSize() { return new WindowSize(Game.getInstance()); }
	
}
