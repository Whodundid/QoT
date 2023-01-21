package envisionEngine.events.types.engine;

import envisionEngine.events.EventType;

public class EngineUnloadedEvent extends EngineEvent {
	
	public EngineUnloadedEvent() {
		super(EventType.ENGINE_UNLOADED, false);
	}
	
}
