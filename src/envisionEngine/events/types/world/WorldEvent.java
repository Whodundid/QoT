package envisionEngine.events.types.world;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class WorldEvent extends GameEvent {
	
	private final EventType worldEventType;
	
	protected WorldEvent(EventType worldEventTypeIn, boolean canBeCancelled) {
		super(EventType.WORLD, canBeCancelled);
		worldEventType = worldEventTypeIn;
	}
	
	public EventType getWorldEventType() { return worldEventType; }
	
}
