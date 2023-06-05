package envision.engine.events.eventTypes.entity;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class EntityEvent extends GameEvent {
	
	private final EventType entityEventType;
	
	protected EntityEvent(EventType entityEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENTITY, canBeCancelled);
		entityEventType = entityEventTypeIn;
	}
	
	public EventType getEntityEvent() { return entityEventType; }
	
}
