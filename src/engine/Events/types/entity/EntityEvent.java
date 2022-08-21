package engine.Events.types.entity;

import engine.Events.EventType;
import engine.Events.GameEvent;

public abstract class EntityEvent extends GameEvent {
	
	private final EventType entityEventType;
	
	protected EntityEvent(EventType entityEventTypeIn) {
		super(EventType.ENTITY);
		entityEventType = entityEventTypeIn;
	}
	
	public EventType getEntityEvent() { return entityEventType; }
	
}
