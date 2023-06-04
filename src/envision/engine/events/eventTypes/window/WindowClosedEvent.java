package envision.engine.events.eventTypes.window;

import envision.engine.events.EventType;
import envision.engine.windows.windowTypes.WindowParent;

public class WindowClosedEvent extends WindowEvent {

	private final WindowParent<?> window;
	
	public WindowClosedEvent(WindowParent<?> windowIn) {
		super(EventType.WINDOW_CLOSED, true);
		window = windowIn;
	}
	
	public WindowParent<?> getWindow() { return window; }
	
}
