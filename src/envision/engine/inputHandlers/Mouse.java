package envision.engine.inputHandlers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import envision.Envision;
import eutil.datatypes.points.Point2i;

public class Mouse extends GLFWMouseButtonCallback {
	
	//========
	// Fields
	//========
	
	private GLFWCursorPosCallback cursorCallback;
	private GLFWScrollCallback scrollCallback;
	private boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static int lastButton = -1;
	private static int lastAction = -1;
	private static int lastChange = 0;
	private static int mX = 0, mY = 0;
	private static Mouse instance;
	
	private IMouseInputReceiver receiver;
	
	//==================
	// Static Singleton
	//==================
	
	public static Mouse create() { return create(-1, (IMouseInputReceiver) null); }
	public static Mouse create(long windowHandle, IMouseInputReceiver receiverIn) {
		return instance = new Mouse(windowHandle, receiverIn);
	}
	
	public static Mouse getInstance() {
		return instance = (instance != null) ? instance : create();
	}
	
	//==============
	// Constructors
	//==============
	
	private Mouse(long windowHandle, IMouseInputReceiver receiverIn) {
		super();
		receiver = receiverIn;
		
		cursorCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				mX = (int) (xpos / Envision.getGameScale());
				mY = (int) (ypos / Envision.getGameScale());
			}
		};
		
		scrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				distribute(2, mX, mY, -1, (int) ypos);
				lastChange = 0;
			}
		};
		
		if (windowHandle > 0) {
			GLFW.glfwSetMouseButtonCallback(windowHandle, this);
			GLFW.glfwSetCursorPosCallback(windowHandle, cursorCallback);
			GLFW.glfwSetScrollCallback(windowHandle, scrollCallback);
		}
	}
	
	//======================================
	// Overrides : GLFWMouseButtonCallbackI
	//======================================
	
	@Override
	public void invoke(long window, int button, int action, int mods) {
		distribute(action, mX, mY, button, 0);
		lastButton = button;
		lastAction = action;
	}
	
	//=========
	// Methods
	//=========
	
	private void distribute(int action, int mXIn, int mYIn, int button, int change) {
		if (receiver != null) {
			receiver.onMouseInput(action, mXIn, mYIn, button, change);
		}
	}
	
	public Mouse setReceiver(IMouseInputReceiver receiverIn) {
		receiver = receiverIn;
		return this;
	}
	
	//================
	// Static Methods
	//================
	
	public static boolean isButtonDown(int button) {
		return GLFW.glfwGetMouseButton(Envision.getWindowHandle(), button) == 1;
	}
	
	public static boolean isAnyButtonDown() { return isButtonDown(0) || isButtonDown(1) || isButtonDown(2); }
	
	public static boolean isLeftDown() { return isButtonDown(0); }
	public static boolean isRightDown() { return isButtonDown(1); }
	
	//================
	// Static Getters
	//================
	
	public static int getMx() { return (int) mX; }
	public static int getMy() { return (int) mY; }
	public static int getButton() { return lastButton; }
	public static Point2i getPos() { return new Point2i(mX, mY); }
	
	//==================
	// Callback Getters
	//==================
	
	public GLFWCursorPosCallback getCursorPosCallBack() { return cursorCallback; }
	public GLFWScrollCallback getScrollCallBack() { return scrollCallback; }
	
}
