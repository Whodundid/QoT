package envision.events.types.entity;

import envision.events.EventType;
import envision.events.GameEvent;

public abstract class EntityEvent extends GameEvent {
	
	private final EventType entityEventType;
	
	protected EntityEvent(EventType entityEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENTITY, canBeCancelled);
		entityEventType = entityEventTypeIn;
	}
	
	public EventType getEntityEvent() { return entityEventType; }
	
}
