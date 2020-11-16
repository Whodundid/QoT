package input;

import gameSystems.gameRenderer.GameRenderer;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import main.Game;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard extends GLFWKeyCallback {
	
	private boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];
	private static char lastChar = '\u0000';
	private static int lastKey = -1;
	private static Keyboard instance;
	
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
		if (key >= 0) {
			c = getKeyChar(key);
			
			if (key == 32) { c = ' '; }
			
			//set the key state
			keys[key] = action == GLFW.GLFW_PRESS;
		}
		
		//distribute the event
		distribute(action, c, key);
		
		//update the previous values
		lastChar = c;
		lastKey = key;
	}
	
	//------------------
	//Keyboard Functions
	//------------------
	
	private void distribute(int action, char typedChar, int keyCode) {
		if (Game.getGLInit()) {
			GameRenderer r = Game.getGameRenderer();
			if (r != null) {
				switch (action) {
				case 0: r.keyPressed(typedChar, keyCode); break;
				case 1: r.keyReleased(typedChar, keyCode); break;
				case 2: r.keyHeld(typedChar, keyCode); break;
				default: throw new IllegalArgumentException("Invalid keyboard action type! " + action);
				}
			}
		}
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
	
	//-----------------------
	//Keyboard Static Getters
	//-----------------------
	
	public static boolean isTypable(char in) {
		return in != 167 && in >= 32 && in != 127;
	}
	
	public static String removeUntypables(String in) {
		String s = "";
		for (char c : in.toCharArray()) {
			if (isTypable(c)) { s += c; }
		}
		return s;
	}
	
	public static void setClipboard(String in) {
		//if (!StringUtils.isEmpty(in)) {
		//try {
		//		StringSelection stringselection = new StringSelection(in);
          //      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
		//	}
			//catch (Exception e) {}
		//}
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
	
}
