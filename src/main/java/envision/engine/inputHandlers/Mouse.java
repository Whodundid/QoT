package envision.engine.inputHandlers;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.system.MemoryUtil;

import envision.Envision;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;

public class Mouse {
	
	//========
	// Fields
	//========
	
    /** Listens to mouse buttons. */
    private GLFWMouseButtonCallback mouseButtonCallback;
    /** Listens to the cursor position. */
	private GLFWCursorPosCallback cursorCallback;
	/** Listens to the scroll wheel. */
	private GLFWScrollCallback scrollCallback;
	/** Listens to files being dragged and dropped into the window. */
	private GLFWDropCallback dropCallback;
	/** Listens to when the cursor entered/exited the window. */
	private GLFWCursorEnterCallback cursorEnterCallback;
	
	//private boolean[] buttons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST];
	private static int lastButton = -1;
	private static int lastAction = -1;
	private static int lastChange = 0;
	private static int mX = 0, mY = 0;
	private static final EList<String> lastDroppedFiles = EList.newList();
	private static final Point2i mouseEnteredPos = new Point2i();
	private static final Point2i mouseExitedPos = new Point2i();
	private static boolean isMouseInside = false;
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
		
		mouseButtonCallback = new GLFWMouseButtonCallback() {
		    @Override
		    public void invoke(long window, int button, int action, int mods) {
		        distributeMouseEvent(action, mX, mY, button, 0);
		        lastButton = button;
		        lastAction = action;
		    }
		};
		
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
				distributeMouseEvent(2, mX, mY, -1, (int) ypos);
				lastChange = 0;
			}
		};
		
		dropCallback = new GLFWDropCallback() {
            @Override
            public void invoke(long window, int count, long names) {
                PointerBuffer nameBuffer = MemoryUtil.memPointerBuffer(names, count);
                lastDroppedFiles.clear();
                for (int i = 0; i < count; i++) {
                    String value = MemoryUtil.memUTF8(MemoryUtil.memByteBufferNT1(nameBuffer.get(i)));
                    lastDroppedFiles.add(value);
                }
                distributeDropEvent();
            }
		};
		
		cursorEnterCallback = new GLFWCursorEnterCallback() {
            @Override
            public void invoke(long window, boolean entered) {
                isMouseInside = entered;
                if (entered) mouseEnteredPos.set(mX, mY);
                else mouseExitedPos.set(mX, mY);
                distributeMouseEnteredExitedEvent();
            }
        };
		
		if (windowHandle > 0) {
			GLFW.glfwSetMouseButtonCallback(windowHandle, mouseButtonCallback);
			GLFW.glfwSetCursorPosCallback(windowHandle, cursorCallback);
			GLFW.glfwSetScrollCallback(windowHandle, scrollCallback);
			GLFW.glfwSetDropCallback(windowHandle, dropCallback);
			GLFW.glfwSetCursorEnterCallback(windowHandle, cursorEnterCallback);
		}
	}
	
	//=========
	// Methods
	//=========
	
	private void distributeMouseEvent(int action, int mXIn, int mYIn, int button, int change) {
		if (receiver != null) {
			receiver.onMouseInput(action, mXIn, mYIn, button, change);
		}
	}
	
	private void distributeDropEvent() {
	    if (receiver != null) {
	        receiver.onDroppedFiles(lastDroppedFiles);
	    }
	}
	
	private void distributeMouseEnteredExitedEvent() {
	    if (receiver != null) {
	        if (isMouseInside) receiver.onMouseEnteredWindow(mouseEnteredPos.x, mouseEnteredPos.y);
	        else receiver.onMouseExitedWindow(mouseExitedPos.x, mouseExitedPos.y);
	    }
	}
	
	public Mouse setReceiver(IMouseInputReceiver receiverIn) {
		receiver = receiverIn;
		return this;
	}
	
	//================
	// Static Methods
	//================
	
	public static void destroy() {
		if (instance != null) {
			instance.mouseButtonCallback.free();
			instance.cursorCallback.free();
			instance.scrollCallback.free();
			instance.dropCallback.free();
		}
	}
	
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
	public static EList<String> getLastDroppedFileNames() { return EList.newList(lastDroppedFiles); }
	
	//==================
	// Callback Getters
	//==================
	
	public GLFWMouseButtonCallback getMouseButtonCallBack() { return mouseButtonCallback; }
	public GLFWCursorPosCallback getCursorPosCallBack() { return cursorCallback; }
	public GLFWScrollCallback getScrollCallBack() { return scrollCallback; }
	public GLFWDropCallback getDropCallBack() { return dropCallback; }
	
}
