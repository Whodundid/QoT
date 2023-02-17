package envision.game.events.eventTypes.window;

import envision.engine.windows.windowTypes.WindowParent;
import envision.game.events.EventType;

public class WindowOpenedEvent extends WindowEvent {

	private final WindowParent<?> window;
	
	public WindowOpenedEvent(WindowParent<?> windowIn) {
		super(EventType.WINDOW_OPENED, true);
		window = windowIn;
	}
	
	public WindowParent<?> getWindow() { return window; }
	
}
