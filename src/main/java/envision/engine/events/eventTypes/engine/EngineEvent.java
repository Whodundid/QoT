package envision.engine.events.eventTypes.engine;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class EngineEvent extends GameEvent {
    
    private final EnvisionEventType engineEventType;
    
    protected EngineEvent(EnvisionEventType engineEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.ENGINE, canBeCancelled);
        engineEventType = engineEventTypeIn;
    }
    
    public EnvisionEventType getEngineEventType() { return engineEventType; }
    
}
