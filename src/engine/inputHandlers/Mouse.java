package engine.inputHandlers;

import eutil.datatypes.Box2;
import main.QoT;

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
				mX = (int) (xpos / QoT.getGameScale());
				mY = (int) (ypos / QoT.getGameScale());
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
		QoT.mouseEvent(action, mXIn, mYIn, button, change);
	}
	
	//------------------
	// Static Functions
	//------------------
	
	public static boolean isButtonDown(int button) {
		return GLFW.glfwGetMouseButton(QoT.getWindowHandle(), button) == 1;
	}
	
	public static boolean isAnyButtonDown() { return isButtonDown(0) || isButtonDown(1) || isButtonDown(2); }
	
	public static boolean isLeftDown() { return isButtonDown(0); }
	public static boolean isRightDown() { return isButtonDown(1); }
	
	//----------------
	// Static Getters
	//----------------
	
	public static int getMx() { return (int) mX; }
	public static int getMy() { return (int) mY; }
	public static int getButton() { return lastButton; }
	public static Box2<Integer, Integer> getPos() { return new Box2(mX, mY); }
	
	//------------------
	// Callback Getters
	//------------------
	
	public GLFWCursorPosCallback getCursorPosCallBack() { return cursorCallback; }
	public GLFWScrollCallback getScrollCallBack() { return scrollCallback; }
	
}