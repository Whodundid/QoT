package envision.game.events.eventTypes.entity;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class EntityEvent extends GameEvent {
	
	private final EventType entityEventType;
	
	protected EntityEvent(EventType entityEventTypeIn, boolean canBeCancelled) {
		super(EventType.ENTITY, canBeCancelled);
		entityEventType = entityEventTypeIn;
	}
	
	public EventType getEntityEvent() { return entityEventType; }
	
}
