package envision.game.events.eventTypes.engine;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class EngineEvent extends GameEvent {
	
	private final EventType engineEventType;
	
	protected EngineEvent(EventType engineEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENGINE, canBeCancelled);
		engineEventType = engineEventTypeIn;
	}
	
	public EventType getEngineEventType() { return engineEventType; }
	
}
