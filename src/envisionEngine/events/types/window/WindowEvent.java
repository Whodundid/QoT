package envisionEngine.events.types.window;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class WindowEvent extends GameEvent {
	
	private final EventType windowEventType;
	
	protected WindowEvent(EventType windowEventTypeIn, boolean canBeCancelled) {
		super(EventType.WINDOW, canBeCancelled);
		windowEventType = windowEventTypeIn;
	}
	
	public EventType getWindowEventType() { return windowEventType; }
	
}
