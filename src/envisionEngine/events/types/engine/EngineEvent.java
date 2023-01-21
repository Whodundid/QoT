package envisionEngine.events.types.engine;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class EngineEvent extends GameEvent {
	
	private final EventType engineEventType;
	
	protected EngineEvent(EventType engineEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENGINE, canBeCancelled);
		engineEventType = engineEventTypeIn;
	}
	
	public EventType getEngineEventType() { return engineEventType; }
	
}
