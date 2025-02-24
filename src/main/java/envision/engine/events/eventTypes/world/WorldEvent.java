package envision.engine.events.eventTypes.world;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class WorldEvent extends GameEvent {
    
    private final EnvisionEventType worldEventType;
    
    protected WorldEvent(EnvisionEventType worldEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.WORLD, canBeCancelled);
        worldEventType = worldEventTypeIn;
    }
    
    public EnvisionEventType getWorldEventType() { return worldEventType; }
    
}
