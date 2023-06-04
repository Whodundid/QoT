package envision.engine.events.eventTypes.screen;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class ScreenEvent extends GameEvent {
	
	private final EventType screenEventType;
	
	protected ScreenEvent(EventType screenEventTypeIn, boolean canBeCancelled) {
		super(EventType.SCREEN, canBeCancelled);
		screenEventType = screenEventTypeIn;
	}
	
	public EventType getScreenEventType() { return screenEventType; }
	
}
