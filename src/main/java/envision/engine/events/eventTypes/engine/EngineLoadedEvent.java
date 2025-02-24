package envision.engine.events.eventTypes.engine;

import envision.engine.events.EnvisionEventType;

public class EngineLoadedEvent extends EngineEvent {
    
    public EngineLoadedEvent() {
        super(EnvisionEventType.ENGINE_LOADED, false);
    }
    
}
