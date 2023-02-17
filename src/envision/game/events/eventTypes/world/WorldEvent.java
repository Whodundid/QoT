package envision.game.events.eventTypes.world;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class WorldEvent extends GameEvent {
	
	private final EventType worldEventType;
	
	protected WorldEvent(EventType worldEventTypeIn, boolean canBeCancelled) {
		super(EventType.WORLD, canBeCancelled);
		worldEventType = worldEventTypeIn;
	}
	
	public EventType getWorldEventType() { return worldEventType; }
	
}
