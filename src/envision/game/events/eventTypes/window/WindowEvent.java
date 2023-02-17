package envision.game.events.eventTypes.window;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class WindowEvent extends GameEvent {
	
	private final EventType windowEventType;
	
	protected WindowEvent(EventType windowEventTypeIn, boolean canBeCancelled) {
		super(EventType.WINDOW, canBeCancelled);
		windowEventType = windowEventTypeIn;
	}
	
	public EventType getWindowEventType() { return windowEventType; }
	
}
