package envision.engine.events.eventTypes.engine;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class EngineEvent extends GameEvent {
	
	private final EventType engineEventType;
	
	protected EngineEvent(EventType engineEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENGINE, canBeCancelled);
		engineEventType = engineEventTypeIn;
	}
	
	public EventType getEngineEventType() { return engineEventType; }
	
}
