package envisionEngine.events.types.window;

import envisionEngine.events.EventType;
import envisionEngine.windowLib.windowTypes.WindowParent;

public class WindowClosedEvent extends WindowEvent {

	private final WindowParent<?> window;
	
	public WindowClosedEvent(WindowParent<?> windowIn) {
		super(EventType.WINDOW_CLOSED, true);
		window = windowIn;
	}
	
	public WindowParent<?> getWindow() { return window; }
	
}
