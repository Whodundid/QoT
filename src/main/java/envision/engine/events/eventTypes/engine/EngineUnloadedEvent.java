package envision.engine.events.eventTypes.engine;

import envision.engine.events.EnvisionEventType;

public class EngineUnloadedEvent extends EngineEvent {
    
    public EngineUnloadedEvent() {
        super(EnvisionEventType.ENGINE_UNLOADED, false);
    }
    
}
