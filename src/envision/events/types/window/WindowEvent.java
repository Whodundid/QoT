package envision.events.types.window;

import envision.events.EventType;
import envision.events.GameEvent;

public abstract class WindowEvent extends GameEvent {
	
	private final EventType windowEventType;
	
	protected WindowEvent(EventType windowEventTypeIn, boolean canBeCancelled) {
		super(EventType.WINDOW, canBeCancelled);
		windowEventType = windowEventTypeIn;
	}
	
	public EventType getWindowEventType() { return windowEventType; }
	
}
