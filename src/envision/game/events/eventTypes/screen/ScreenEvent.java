package envision.game.events.eventTypes.screen;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class ScreenEvent extends GameEvent {
	
	private final EventType screenEventType;
	
	protected ScreenEvent(EventType screenEventTypeIn, boolean canBeCancelled) {
		super(EventType.SCREEN, canBeCancelled);
		screenEventType = screenEventTypeIn;
	}
	
	public EventType getScreenEventType() { return screenEventType; }
	
}
