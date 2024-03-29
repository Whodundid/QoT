package envision.engine.events.eventTypes.window;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class WindowEvent extends GameEvent {
	
	private final EventType windowEventType;
	
	protected WindowEvent(EventType windowEventTypeIn, boolean canBeCancelled) {
		super(EventType.WINDOW, canBeCancelled);
		windowEventType = windowEventTypeIn;
	}
	
	public EventType getWindowEventType() { return windowEventType; }
	
}
