package envision.events.types.engine;

import envision.events.EventType;
import envision.events.GameEvent;

public abstract class EngineEvent extends GameEvent {
	
	private final EventType engineEventType;
	
	protected EngineEvent(EventType engineEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENGINE, canBeCancelled);
		engineEventType = engineEventTypeIn;
	}
	
	public EventType getEngineEventType() { return engineEventType; }
	
}
