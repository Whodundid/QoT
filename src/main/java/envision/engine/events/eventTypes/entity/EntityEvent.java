package envision.engine.events.eventTypes.entity;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class EntityEvent extends GameEvent {
    
    private final EnvisionEventType entityEventType;
    
    protected EntityEvent(EnvisionEventType entityEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.ENTITY, canBeCancelled);
        entityEventType = entityEventTypeIn;
    }
    
    public EnvisionEventType getEntityEvent() { return entityEventType; }
    
}
