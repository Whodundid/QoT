package input;

import gameSystems.gameRenderer.GameRenderer;
import main.Game;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Mouse extends GLFWMouseButtonCallback {
	
	private GLFWCursorPosCallback cursorCallback;
	private GLFWScrollCallback scrollCallback;
	private boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static int lastButton = -1;
	private static int lastAction = -1;
	private static int lastChange = 0;
	private static int mX = 0, mY = 0;
	private static Mouse instance;
	
	//-----------------
	//Mouse Constructor
	//-----------------
	
	public static Mouse getInstance() {
		return instance = (instance != null) ? instance : new Mouse();
	}
	
	private Mouse() {
		super();
		
		cursorCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mX = (int) (xpos / Game.getGameScale());
				mY = (int) (ypos / Game.getGameScale());
			}
		};
		
		scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				distribute(2, mX, mY, -1, (int) ypos);
				lastChange = 0;
			}
		};
	}
	
	//----------------------------------
	//GLFWMouseButtonCallbackI Overrides
	//----------------------------------	
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		distribute(action, mX, mY, button, 0);
		lastButton = button;
		lastAction = action;
	}
	
	//---------------
	//Mouse Functions
	//---------------
	
	private void distribute(int action, int mXIn, int mYIn, int button, int change) {
		if (Game.getGLInit()) {
			if (Game.currentScreen != null) {
				switch (action) {
				case 0: Game.currentScreen.mouseReleased(mXIn, mYIn, button); break;
				case 1: Game.currentScreen.mousePressed(mXIn, mYIn, button); break;
				case 2: Game.currentScreen.mouseScrolled(change); break;
				default: throw new IllegalArgumentException("Invalid keyboard action type! " + action);
				}
			}
			
			GameRenderer r = Game.getGameRenderer();
			if (r != null) {
				switch (action) {
				case 0: r.mouseReleased(mXIn, mYIn, button); break;
				case 1: r.mousePressed(mXIn, mYIn, button); break;
				case 2: r.mouseScrolled(change); break;
				default: throw new IllegalArgumentException("Invalid keyboard action type! " + action);
				}
			}
		}
	}
	
	//----------------------
	//Mouse Static Functions
	//----------------------
	
	public static boolean isButtonDown(int button) {
		return GLFW.glfwGetMouseButton(Game.getWindowHandle(), button) == 1;
	}
	
	//--------------------
	//Mouse Static Getters
	//--------------------
	
	public static int getMx() { return (int) mX; }
	public static int getMy() { return (int) mY; }
	public static int getButton() { return lastButton; }
	
	//----------------------
	//Mouse Callback Getters
	//----------------------
	
	public GLFWCursorPosCallback getCursorPosCallBack() { return cursorCallback; }
	public GLFWScrollCallback getScrollCallBack() { return scrollCallback; }
	
}
