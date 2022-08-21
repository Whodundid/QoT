package engine.Events.types.world;

import engine.Events.EventType;
import engine.Events.GameEvent;

public abstract class WorldEvent extends GameEvent {
	
	private final EventType worldEventType;
	
	protected WorldEvent(EventType worldEventTypeIn) {
		super(EventType.WORLD);
		worldEventType = worldEventTypeIn;
	}
	
	public EventType getWorldEventType() { return worldEventType; }
	
}
