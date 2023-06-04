package envision.engine.events.eventTypes.world;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class WorldEvent extends GameEvent {
	
	private final EventType worldEventType;
	
	protected WorldEvent(EventType worldEventTypeIn, boolean canBeCancelled) {
		super(EventType.WORLD, canBeCancelled);
		worldEventType = worldEventTypeIn;
	}
	
	public EventType getWorldEventType() { return worldEventType; }
	
}
