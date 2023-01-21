package envisionEngine.events.types.engine;

import envisionEngine.events.EventType;

public class EngineLoadedEvent extends EngineEvent {
	
	public EngineLoadedEvent() {
		super(EventType.ENGINE_LOADED, false);
	}
	
}
