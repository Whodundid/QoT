package envision;

import envision.engine.events.GameEvent;
import envision.engine.events.IEventListener;
import envision.engine.terminal.TerminalCommandHandler;

public interface EnvisionGame extends IEventListener {
	
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
	
	default void onTerminalLoad(TerminalCommandHandler handler) {}
	
	default void onPreEngineLoad() {}
	default void onPostEngineLoad() {}
	default void onPreEngineUnload() {}
	default void onPostEngineUnload() {}
	
	default void onPreGameUnload() {}
	default void onPostGameUnload() {}
	
}
