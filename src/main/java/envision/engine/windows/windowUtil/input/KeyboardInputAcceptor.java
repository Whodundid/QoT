package envision.engine.windows.windowUtil.input;

import envision.engine.inputHandlers.Keyboard;

public interface KeyboardInputAcceptor {
	
	public default void handleKeyboardInput(int action, char typedChar, int keyCode) {
		switch (action) {
		case 0: keyReleased(typedChar, keyCode); break;
		case 1: keyPressed(typedChar, keyCode); break;
		case 2: if (Keyboard.repeatEvents) { keyPressed(typedChar, keyCode); } break;
		default: throw new IllegalArgumentException("Invalid keyboard action type! " + action);
		}
	}
	
	public default void keyPressed(char typedChar, int keyCode) {}
	public default void keyReleased(char typedChar, int keyCode) {}
	
}
