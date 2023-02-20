package envision.engine.inputHandlers;

public interface IEnvisionInputReceiver extends IKeyboardInputReceiver,
												IMouseInputReceiver,
												IWindowResizeEventReceiver
{
	
	@Override default void onKeyboardInput(int action, char typedChar, int keyCode) {}
	@Override default void onMouseInput(int action, int mXIn, int mYIn, int button, int change) {}
	@Override default void onWindowResized(long window, int newWidth, int newHeight) {}
}
