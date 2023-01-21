package envision.events.types.engine;

import envision.events.EventType;

public class EngineUnloadedEvent extends EngineEvent {
	
	public EngineUnloadedEvent() {
		super(EventType.ENGINE_UNLOADED, false);
	}
	
}
