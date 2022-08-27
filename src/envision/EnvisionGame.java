package envision;

import envision.events.GameEvent;
import envision.events.IEventListener;

public interface EnvisionGame extends IEventListener {
	
	default void onEngineLoad() {}
	default void onEngineUnload() {}
	
	default void onRenderTick() {}
	default void onGameTick() {}
	
	default void onMouseInput(int action, int mX, int mY, int button, int change) {}
	default void onMousePress(int mX, int mY, int button) {}
	default void onMouseRelease(int mX, int mY, int button) {}
	
	default void onKeyInput(int action, char typedChar, int keyCode) {}
	default void onKeyPress(char typedChar, int keyCode) {}
	default void onKeyRelease(char typedChar, int keyCode) {}
	
	default void onWindowResized() {}
	
	default void onEvent(GameEvent e) {}
	
	default void onGameSetup() {}
	default void onGameUnload() {}
	
}
