package envision.engine.events.eventTypes.engine;

import envision.engine.events.EventType;

public class EngineUnloadedEvent extends EngineEvent {
	
	public EngineUnloadedEvent() {
		super(EventType.ENGINE_UNLOADED, false);
	}
	
}
