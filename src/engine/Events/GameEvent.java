package engine.Events;

public abstract class GameEvent {
	
	private final EventType type;
	
	protected GameEvent(EventType typeIn) {
		type = typeIn;
	}
	
	public EventType getType() { return type; }
	
}
