package envision.events.types.engine;

import envision.events.EventType;

public class EngineLoadedEvent extends EngineEvent {
	
	public EngineLoadedEvent() {
		super(EventType.ENGINE_LOADED, false);
	}
	
}
