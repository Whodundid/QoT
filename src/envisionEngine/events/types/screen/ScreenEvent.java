package envisionEngine.events.types.screen;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class ScreenEvent extends GameEvent {
	
	private final EventType screenEventType;
	
	protected ScreenEvent(EventType screenEventTypeIn, boolean canBeCancelled) {
		super(EventType.SCREEN, canBeCancelled);
		screenEventType = screenEventTypeIn;
	}
	
	public EventType getScreenEventType() { return screenEventType; }
	
}
