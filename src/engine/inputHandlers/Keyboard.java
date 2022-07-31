package engine.inputHandlers;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import main.QoT;

/**
 * A static helper class for all keyboard related operations and functions.
 * 
 * @author Hunter Bragg
 */
public class Keyboard extends GLFWKeyCallback {
	
	//--------------------
	// Keyboard Singleton
	//--------------------
	
	/** The managed singleton keyboard instance. */
	private static Keyboard instance;
	
	/**
	 * Returns the managed singleton keyboard instance.
	 */
	public static Keyboard getInstance() {
		return instance = (instance != null) ? instance : new Keyboard();
	}
	
	//--------
	// Fields
	//--------
	
	private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static char lastChar = '\u0000'; //empty char by default
	private static int lastKey = -1;
	public static boolean repeatEvents = true;
	
	/** Stores String->Keycode mappings for select known keyboard characters. */
	private static final HashMap<String, Integer> keyMapings = new HashMap();
	
	//--------------
	// Constructors
	//--------------
	
	//prevent outside instantiation
	private Keyboard() {
		super();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		//get the actual character
		char c = '\u0000';
		
		//prevent negative values!
		if (key >= 0 && key < keys.length) {
			c = getKeyChar(key);
			
			if (key == 32) c = ' ';
			
			//set the key state
			keys[key] = (action == GLFW.GLFW_PRESS);
		}
		
		//check for repeat events
		if (key == lastKey && action == GLFW.GLFW_REPEAT) {
			//only distribute if repeat events are enabled
			if (repeatEvents) distribute(action, c, key);
		}
		else {
			//distribute the event
			distribute(action, c, key);
		}
		
		//update the previous values
		lastChar = c;
		lastKey = key;
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Sends the new keyboard action to QoT.
	 * 
	 * @param action
	 * @param typedChar
	 * @param keyCode
	 */
	private void distribute(int action, char typedChar, int keyCode) {
		QoT.keyboardEvent(action, typedChar, keyCode);
	}

	//----------------
	// Static Methods
	//----------------
	
	/**
	 * Returns true if the given key is pressed.
	 * 
	 * @param key The key to check if pressed
	 * @return True if the given key is pressed
	 */
	public static boolean isKeyDown(int key) {
		return GLFW.glfwGetKey(QoT.getWindowHandle(), key) == 1;
	}
	
	/**
	 * Returns true if all of the given keys are pressed.
	 * If no keys are given -- false is returned by default
	 * 
	 * @param keys The keys to check through
	 * @return True if every key given is pressed
	 */
	public static boolean areAllKeysDown(int... keys) {
		//if empty -- false by default
		if (keys.length == 0) return false;
		
		//iterate on each key state
		for (int k : keys) {
			if (!isKeyDown(k)) return false;
		}
		
		return true;
	}
	
	/**
	 * Returns true if all of the given keys are pressed.
	 * If no keys are given -- false is returned by default
	 * 
	 * @param keys The keys to check through
	 * @return True if every key given is pressed
	 */
	public static boolean areAllKeysDown(String... keys) {
		//if empty -- false by default
		if (keys.length == 0) return false;
		
		//iterate on each key state
		for (String k : keys) {
			//get key code
			Integer code = keyMapings.get(k);
			//if code doesn't exist -- false by default
			if (code == null) return false;
			if (!isKeyDown(code)) return false;
		}
		
		return true;
	}
	
	/**
	 * Returns true if any of the given keys are pressed.
	 * Returns true on the fist down key found.
	 * If no keys are given -- false is returned by default
	 * 
	 * @param keys The keys to check through
	 * @return True if any key given is pressed
	 */
	public static boolean isAnyKeyDown(int... keys) {
		//if empty -- false by default
		if (keys.length == 0) return false;
		
		//iterate on each key state
		for (int k : keys) {
			if (isKeyDown(k)) return true;
		}
		
		return false;
	}
	
	/**
	 * Returns true if any of the given keys are pressed.
	 * Returns true on the fist down key found.
	 * If no keys are given -- false is returned by default
	 * 
	 * @param keys The keys to check through
	 * @return True if any key given is pressed
	 */
	public static boolean isAnyKeyDown(String... keys) {
		//if empty -- false by default
		if (keys.length == 0) return false;
		
		//iterate on each key state
		for (String k : keys) {
			//get key code
			Integer code = keyMapings.get(k);
			//if code doesn't exist -- false by default
			if (code == null) return false;
			if (isKeyDown(code)) return true;
		}
		
		return false;
	}
	
	/**
	 * Returns the String name of the given key code (if there is one).
	 * 
	 * @param key The key code to get the name of
	 * @return The name of the key with the give key code
	 */
	public static String getKeyName(int key) {
		return GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
	}
	
	/**
	 * Returns the Character (char) of the given key code (if there is one).
	 * 
	 * @param key The key code to get the name of
	 * @return The name of the key with the give key code
	 */
	public static char getKeyChar(int key) {
		char r = '\u0054';
		String name = getKeyName(key);
		if (name != null && name.length() > 0) { r = name.charAt(0); }
		return r;
	}
	
	/**
	 * Returns true if the given character (as well as keycode) has a typable
	 * character.
	 * 
	 * @param in   The char to check
	 * @param code The key code to check
	 * @return True if the given char/int is has a typable character
	 */
	public static boolean isTypable(char in, int code) {
		boolean typable = true;
		
		if (code >= 0) {
			switch (code) {
			case KEY_ESC:
			case KEY_TAB:
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
		
		return typable;
	}
	
	/**
	 * Iterates across the given string and removes any character that does not have
	 * a typable character.
	 * 
	 * @param in The incoming string
	 * @return The incoming string without untypable characters
	 */
	public static String removeUntypables(String in) {
		String s = "";
		for (char c : in.toCharArray()) {
			if (isTypable(c, -1)) s += c;
		}
		return s;
	}
	
	/**
	 * Returns the uppercase char of the given integer key code. If there is no
	 * uppercase version of the given key -- the same key code is returned.
	 * 
	 * @param in The key code
	 * @return The uppercase version of the key with the given key code.
	 */
	public static char getUppercase(int in) {
		switch (in) {
		case KEY_TILDE: return '~';
		case KEY_1: return '!';
		case KEY_2: return '@';
		case KEY_3: return '#';
		case KEY_4: return '$';
		case KEY_5: return '%';
		case KEY_6: return '^';
		case KEY_7: return '&';
		case KEY_8: return '*';
		case KEY_9: return '(';
		case KEY_0: return ')';
		case KEY_SUBTRACT: return '_';
		case KEY_EQUALS: return '+';
		case KEY_LBRACKET: return '{';
		case KEY_RBRACKET: return '}';
		case KEY_BSLASH: return '|';
		case KEY_SEMICOLON: return ':';
		case KEY_APOSTROPHE: return '"';
		case KEY_COMMA: return '<';
		case KEY_PERIOD: return '>';
		case KEY_FSLASH: return '?';
		default: return Character.toUpperCase((char) in);
		}
	}
	
	public static void enableRepeatEvents() { repeatEvents = true; }
	public static void disableRepeatEvents() { repeatEvents = false; }
	
	/** Returns the last keyboard character (char) that had some action performed. */
	public static char getLastChar() { return lastChar; }
	/** Returns the last keyboard keycode (integer) that had some action performed. */
	public static int getLastKey() { return lastKey; }
	
	/** Returns true if 'w' is pressed. */
	public static boolean isWDown() { return isKeyDown(KEY_W); }
	/** Returns true if 'a' is pressed. */
	public static boolean isADown() { return isKeyDown(KEY_A); }
	/** Returns true if 's' is pressed. */
	public static boolean isSDown() { return isKeyDown(KEY_S); }
	/** Returns true if 'd' is pressed. */
	public static boolean isDDown() { return isKeyDown(KEY_D); }
	
	public static boolean isCtrlDown() { return isAnyKeyDown(KEY_LCTRL, KEY_RCTRL); }
	public static boolean isAltDown() { return isAnyKeyDown(KEY_LALT, KEY_RALT); }
	public static boolean isShiftDown() { return isAnyKeyDown(KEY_LSHIFT, KEY_RSHIFT); }
	
	/**
	 * Returns true if and only if:
	 * 		<li>The left or right ctrl key is down
	 * 		<li>Neither the left or right keys of shift are down
	 * 		<li>Neither the left of right keys of alt are down
	 * 
	 * @return True if only ctrl (out of ctrl, shift, alt) is pressed
	 */
	public static boolean onlyCtrlDown() { return isCtrlDown() && !isShiftDown() && !isAltDown(); }
	
	public static boolean isCtrlA(int keyCode) { return keyCode == KEY_A && onlyCtrlDown(); }
	public static boolean isCtrlX(int keyCode) { return keyCode == KEY_X && onlyCtrlDown(); }
	public static boolean isCtrlC(int keyCode) { return keyCode == KEY_C && onlyCtrlDown(); }
	public static boolean isCtrlV(int keyCode) { return keyCode == KEY_V && onlyCtrlDown(); }
	public static boolean isCtrlZ(int keyCode) { return keyCode == KEY_Z && onlyCtrlDown(); }
	public static boolean isCtrlY(int keyCode) { return keyCode == KEY_Y && onlyCtrlDown(); }
	public static boolean isCtrlS(int keyCode) { return keyCode == KEY_S && onlyCtrlDown(); }
	public static boolean isCtrlD(int keyCode) { return keyCode == KEY_D && onlyCtrlDown(); }
	public static boolean isCtrlF(int keyCode) { return keyCode == KEY_F && onlyCtrlDown(); }
	public static boolean isCtrlR(int keyCode) { return keyCode == KEY_R && onlyCtrlDown(); }
	public static boolean isCtrlQ(int keyCode) { return keyCode == KEY_Q && onlyCtrlDown(); }
	public static boolean isCtrlE(int keyCode) { return keyCode == KEY_E && onlyCtrlDown(); }
	
	//-------------------
	// Clipboard Methods
	//-------------------
	
	/**
	 * Sets the contents of the active system clipboard.
	 * 
	 * @param in The string to set
	 */
	public static void setClipboard(String in) {
		if (StringUtils.isEmpty(in)) return;
		try {
			StringSelection stringselection = new StringSelection(in);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
		}
		catch (Exception e) {}
	}
	
	/**
	 * Returns the current contents of the system clipboard.
	 * 
	 * @return The current clipboard string
	 */
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
	
	static {
		//letters
		keyMapings.put("a", KEY_A);
		keyMapings.put("b", KEY_B);
		keyMapings.put("c", KEY_C);
		keyMapings.put("d", KEY_D);
		keyMapings.put("e", KEY_E);
		keyMapings.put("f", KEY_F);
		keyMapings.put("g", KEY_G);
		keyMapings.put("h", KEY_H);
		keyMapings.put("i", KEY_I);
		keyMapings.put("j", KEY_J);
		keyMapings.put("k", KEY_K);
		keyMapings.put("l", KEY_L);
		keyMapings.put("m", KEY_M);
		keyMapings.put("n", KEY_N);
		keyMapings.put("o", KEY_O);
		keyMapings.put("p", KEY_P);
		keyMapings.put("q", KEY_Q);
		keyMapings.put("r", KEY_R);
		keyMapings.put("s", KEY_S);
		keyMapings.put("t", KEY_T);
		keyMapings.put("u", KEY_U);
		keyMapings.put("v", KEY_V);
		keyMapings.put("w", KEY_W);
		keyMapings.put("x", KEY_X);
		keyMapings.put("y", KEY_Y);
		keyMapings.put("z", KEY_Z);
		
		//numbers
		keyMapings.put("1", KEY_1); keyMapings.put("!", KEY_1);
		keyMapings.put("2", KEY_2); keyMapings.put("@", KEY_2);
		keyMapings.put("3", KEY_3); keyMapings.put("#", KEY_3);
		keyMapings.put("4", KEY_4); keyMapings.put("$", KEY_4);
		keyMapings.put("5", KEY_5); keyMapings.put("%", KEY_5);
		keyMapings.put("6", KEY_6); keyMapings.put("^", KEY_6);
		keyMapings.put("7", KEY_7); keyMapings.put("&", KEY_7);
		keyMapings.put("8", KEY_8); keyMapings.put("*", KEY_8);
		keyMapings.put("9", KEY_9); keyMapings.put("(", KEY_9);
		keyMapings.put("0", KEY_0); keyMapings.put(")", KEY_0);
		
		//special
		keyMapings.put("esc", KEY_ESC); keyMapings.put("escape", KEY_ESC);
		keyMapings.put("`", KEY_TILDE); keyMapings.put("~", KEY_TILDE);
		keyMapings.put("-", KEY_SUBTRACT); keyMapings.put("_", KEY_SUBTRACT);
		keyMapings.put("=", KEY_EQUALS); keyMapings.put("+", KEY_EQUALS);
		keyMapings.put("backsapce", KEY_BACKSPACE);
		keyMapings.put("tab", KEY_TAB);
		keyMapings.put("[", KEY_RBRACKET); keyMapings.put("{", KEY_RBRACKET);
		keyMapings.put("]", KEY_LBRACKET); keyMapings.put("}", KEY_LBRACKET);
		keyMapings.put("\\", KEY_BSLASH); keyMapings.put("|", KEY_BSLASH);
		keyMapings.put("caps", KEY_CAPS); keyMapings.put("capslock", KEY_CAPS);
		keyMapings.put(";", KEY_SEMICOLON); keyMapings.put(":", KEY_SEMICOLON);
		keyMapings.put("'", KEY_APOSTROPHE); keyMapings.put("\"", KEY_APOSTROPHE);
		keyMapings.put("enter", KEY_ENTER);
		keyMapings.put("lshift", KEY_LSHIFT);
		keyMapings.put(",", KEY_COMMA); keyMapings.put("<", KEY_COMMA);
		keyMapings.put(".", KEY_PERIOD); keyMapings.put(">", KEY_PERIOD);
		keyMapings.put("/", KEY_FSLASH); keyMapings.put("?", KEY_FSLASH);
		keyMapings.put("rshift", KEY_RSHIFT);
	}
	
}
