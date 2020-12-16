package envisionEngine.input;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import main.Game;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard extends GLFWKeyCallback {
	
	private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static char lastChar = '\u0000';
	private static int lastKey = -1;
	private static Keyboard instance;
	public static boolean repeatEvents = true;
	
	//-----------------------
	// Static Key References
	//-----------------------
	
	public static final int
	KEY_A = GLFW.GLFW_KEY_A,
	KEY_B = GLFW.GLFW_KEY_B,
	KEY_C = GLFW.GLFW_KEY_C,
	KEY_D = GLFW.GLFW_KEY_D,
	KEY_E = GLFW.GLFW_KEY_E,
	KEY_F = GLFW.GLFW_KEY_F,
	KEY_G = GLFW.GLFW_KEY_G,		
	KEY_H = GLFW.GLFW_KEY_H,
	KEY_I = GLFW.GLFW_KEY_I,
	KEY_J = GLFW.GLFW_KEY_J,
	KEY_K = GLFW.GLFW_KEY_K,
	KEY_L = GLFW.GLFW_KEY_L,
	KEY_M = GLFW.GLFW_KEY_M,
	KEY_N = GLFW.GLFW_KEY_N,
	KEY_O = GLFW.GLFW_KEY_O,
	KEY_P = GLFW.GLFW_KEY_P,
	KEY_Q = GLFW.GLFW_KEY_Q,
	KEY_R = GLFW.GLFW_KEY_R,
	KEY_S = GLFW.GLFW_KEY_S,
	KEY_T = GLFW.GLFW_KEY_T,
	KEY_U = GLFW.GLFW_KEY_U,
	KEY_V = GLFW.GLFW_KEY_V,
	KEY_W = GLFW.GLFW_KEY_W,
	KEY_X = GLFW.GLFW_KEY_X,
	KEY_Y = GLFW.GLFW_KEY_Y,
	KEY_Z = GLFW.GLFW_KEY_Z,
	KEY_0 = GLFW.GLFW_KEY_0,
	KEY_1 = GLFW.GLFW_KEY_1,
	KEY_2 = GLFW.GLFW_KEY_2,
	KEY_3 = GLFW.GLFW_KEY_3,
	KEY_4 = GLFW.GLFW_KEY_4,
	KEY_5 = GLFW.GLFW_KEY_5,
	KEY_6 = GLFW.GLFW_KEY_6,
	KEY_7 = GLFW.GLFW_KEY_7,
	KEY_8 = GLFW.GLFW_KEY_8,
	KEY_9 = GLFW.GLFW_KEY_9,
	KEY_ESC = GLFW.GLFW_KEY_ESCAPE,
	KEY_TILDE = GLFW.GLFW_KEY_GRAVE_ACCENT,
	KEY_SUBTRACT = GLFW.GLFW_KEY_MINUS,
	KEY_EQUALS = GLFW.GLFW_KEY_EQUAL,
	KEY_BACKSPACE = GLFW.GLFW_KEY_BACKSPACE,
	KEY_TAB = GLFW.GLFW_KEY_TAB,
	KEY_LBRACKET = GLFW.GLFW_KEY_LEFT_BRACKET,
	KEY_RBRACKET = GLFW.GLFW_KEY_RIGHT_BRACKET,
	KEY_BSLASH = GLFW.GLFW_KEY_BACKSLASH,
	KEY_CAPS = GLFW.GLFW_KEY_CAPS_LOCK,
	KEY_SEMICOLON = GLFW.GLFW_KEY_SEMICOLON,
	KEY_APOSTROPHE = GLFW.GLFW_KEY_APOSTROPHE,
	KEY_ENTER = GLFW.GLFW_KEY_ENTER,
	KEY_LSHIFT = GLFW.GLFW_KEY_LEFT_SHIFT,
	KEY_COMMA = GLFW.GLFW_KEY_COMMA,
	KEY_PERIOD = GLFW.GLFW_KEY_PERIOD,
	KEY_FSLASH = GLFW.GLFW_KEY_SLASH,
	KEY_RSHIFT = GLFW.GLFW_KEY_RIGHT_SHIFT,
	KEY_LCTRL = GLFW.GLFW_KEY_LEFT_CONTROL,
	KEY_LWIN = GLFW.GLFW_KEY_LEFT_SUPER,
	KEY_LALT = GLFW.GLFW_KEY_LEFT_ALT,
	KEY_SPACE = GLFW.GLFW_KEY_SPACE,
	KEY_RALT = GLFW.GLFW_KEY_RIGHT_ALT,
	KEY_FUNC = GLFW.GLFW_KEY_RIGHT_SUPER,
	KEY_RCTRL = GLFW.GLFW_KEY_RIGHT_CONTROL,
	KEY_F1 = GLFW.GLFW_KEY_F1,
	KEY_F2 = GLFW.GLFW_KEY_F2,
	KEY_F3 = GLFW.GLFW_KEY_F3,
	KEY_F4 = GLFW.GLFW_KEY_F4,
	KEY_F5 = GLFW.GLFW_KEY_F5,
	KEY_F6 = GLFW.GLFW_KEY_F6,
	KEY_F7 = GLFW.GLFW_KEY_F7,
	KEY_F8 = GLFW.GLFW_KEY_F8,
	KEY_F9 = GLFW.GLFW_KEY_F9,
	KEY_F10 = GLFW.GLFW_KEY_F10,
	KEY_F11 = GLFW.GLFW_KEY_F11,
	KEY_F12 = GLFW.GLFW_KEY_F12,
	KEY_PRNTSCREEN = GLFW.GLFW_KEY_PRINT_SCREEN,
	KEY_SCRLK = GLFW.GLFW_KEY_SCROLL_LOCK,
	KEY_PAUSE = GLFW.GLFW_KEY_PAUSE,
	KEY_INSERT = GLFW.GLFW_KEY_INSERT,
	KEY_HOME = GLFW.GLFW_KEY_HOME,
	KEY_PAGEUP = GLFW.GLFW_KEY_PAGE_UP,
	KEY_DELETE = GLFW.GLFW_KEY_DELETE,
	KEY_END = GLFW.GLFW_KEY_END,
	KEY_PAGEDOWN = GLFW.GLFW_KEY_PAGE_DOWN,
	KEY_UP = GLFW.GLFW_KEY_UP,
	KEY_LEFT = GLFW.GLFW_KEY_LEFT,
	KEY_DOWN = GLFW.GLFW_KEY_DOWN,
	KEY_RIGHT = GLFW.GLFW_KEY_RIGHT;
	
	//--------------------
	//Keyboard Constructor
	//--------------------
	
	public static Keyboard getInstance() {
		return instance = (instance != null) ? instance : new Keyboard();
	}
	
	private Keyboard() {
		super();
	}
	
	//--------------------------
	//GLFWKeyCallbackI Overrides
	//--------------------------
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		//get the actual character
		char c = '\u0000';
		
		//prevent negative values!
		if (key >= 0 && key < keys.length) {
			c = getKeyChar(key);
			
			if (key == 32) { c = ' '; }
			
			//set the key state
			keys[key] = action == GLFW.GLFW_PRESS;
		}
		
		//System.out.println("action: " + action + " c: " + c + " key: " + key);
		
		//update the previous values
		lastChar = c;
		lastKey = key;
		
		//distribute the event
		distribute(action, c, key);
	}
	
	//------------------
	//Keyboard Functions
	//------------------
	
	private void distribute(int action, char typedChar, int keyCode) {
		Game.keyboardEvent(action, typedChar, keyCode);
	}

	//-------------------------
	//Keyboard Static Functions
	//-------------------------
	
	public static boolean isKeyDown(int key) {
		return GLFW.glfwGetKey(Game.getWindowHandle(), key) == 1;
	}
	
	public static String getKeyName(int key) {
		return GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
	}
	
	public static char getKeyChar(int key) {
		char r = '\u0054';
		String name = getKeyName(key);
		if (name != null && name.length() > 0) { r = name.charAt(0); }
		return r;
	}
	
	public static boolean isWDown() { return isKeyDown(GLFW.GLFW_KEY_W); }
	public static boolean isADown() { return isKeyDown(GLFW.GLFW_KEY_A); }
	public static boolean isSDown() { return isKeyDown(GLFW.GLFW_KEY_S); }
	public static boolean isDDown() { return isKeyDown(GLFW.GLFW_KEY_D); }
	
	//-----------------------
	//Keyboard Static Getters
	//-----------------------
	
	public static boolean isTypable(char in, int code) {
		boolean typable = true;
		
		if (code >= 0) {
			switch (code) {
			case KEY_ESC:
			case KEY_LCTRL:
			case KEY_RCTRL:
			case KEY_LALT:
			case KEY_RALT:
			case KEY_LSHIFT:
			case KEY_RSHIFT:
				typable = false;
			}
		}
		
		if (typable == true) {
			typable = in != 167 && in >= 32 && in != 127;
		}
		
		//System.out.println(in + " " + code + " : " + typable);
		
		return typable;
	}
	
	public static String removeUntypables(String in) {
		String s = "";
		for (char c : in.toCharArray()) {
			if (isTypable(c, -1)) { s += c; }
		}
		return s;
	}
	
	public static void setClipboard(String in) {
		if (!StringUtils.isEmpty(in)) {
			try {
				StringSelection stringselection = new StringSelection(in);
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
			}
			catch (Exception e) {}
		}
	}
	
	public static String getClipboard() {
		try {
			Transferable s = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
			if (s != null && s.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String) s.getTransferData(DataFlavor.stringFlavor);
			}
		}
		catch (Exception e) {}
		return "";
	}
	
	public static void enableRepeatEvents() { repeatEvents = true; }
	public static void disableRepeatEvents() { repeatEvents = false; }
	
	public static char getLastChar() { return lastChar; }
	public static int getLastKey() { return lastKey; }
	
	public static boolean isCtrlDown() { return Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_CONTROL); }
	public static boolean isAltDown() { return Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_ALT); }
	public static boolean isShiftDown() { return Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) || Keyboard.isKeyDown(GLFW.GLFW_KEY_RIGHT_SHIFT); }
	
	public static boolean isCtrlA(int keyCode) { return keyCode == GLFW.GLFW_KEY_A && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlX(int keyCode) { return keyCode == GLFW.GLFW_KEY_X && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlC(int keyCode) { return keyCode == GLFW.GLFW_KEY_C && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlV(int keyCode) { return keyCode == GLFW.GLFW_KEY_V && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlZ(int keyCode) { return keyCode == GLFW.GLFW_KEY_Z && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlY(int keyCode) { return keyCode == GLFW.GLFW_KEY_Y && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlS(int keyCode) { return keyCode == GLFW.GLFW_KEY_S && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlD(int keyCode) { return keyCode == GLFW.GLFW_KEY_D && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlF(int keyCode) { return keyCode == GLFW.GLFW_KEY_F && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlR(int keyCode) { return keyCode == GLFW.GLFW_KEY_R && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlQ(int keyCode) { return keyCode == GLFW.GLFW_KEY_Q && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	public static boolean isCtrlE(int keyCode) { return keyCode == GLFW.GLFW_KEY_E && isCtrlDown() && !isShiftDown() && !isAltDown(); }
	
}
