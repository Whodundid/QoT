package envisionEngine.events.types.window;

import envisionEngine.events.EventType;
import envisionEngine.windowLib.windowTypes.WindowParent;

public class WindowOpenedEvent extends WindowEvent {

	private final WindowParent<?> window;
	
	public WindowOpenedEvent(WindowParent<?> windowIn) {
		super(EventType.WINDOW_OPENED, true);
		window = windowIn;
	}
	
	public WindowParent<?> getWindow() { return window; }
	
}
