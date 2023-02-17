package envision.game.events.eventTypes.window;

import envision.engine.windows.windowTypes.WindowParent;
import envision.game.events.EventType;

public class WindowClosedEvent extends WindowEvent {

	private final WindowParent<?> window;
	
	public WindowClosedEvent(WindowParent<?> windowIn) {
		super(EventType.WINDOW_CLOSED, true);
		window = windowIn;
	}
	
	public WindowParent<?> getWindow() { return window; }
	
}
