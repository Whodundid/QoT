package envision.engine.events.eventTypes.engine;

import envision.engine.events.EventType;

public class EngineLoadedEvent extends EngineEvent {
	
	public EngineLoadedEvent() {
		super(EventType.ENGINE_LOADED, false);
	}
	
}
