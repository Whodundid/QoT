package envision.engine.events.eventTypes.song;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class SongEvent extends GameEvent {
	
	private final EventType songEventType;
	
	protected SongEvent(EventType songEventTypeIn, boolean canBeCancelled) {
		super(EventType.SONG, canBeCancelled);
		songEventType = songEventTypeIn;
	}
	
	public EventType getSongEventType() { return songEventType; }
	
}
