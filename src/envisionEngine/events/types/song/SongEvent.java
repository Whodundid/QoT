package envisionEngine.events.types.song;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class SongEvent extends GameEvent {
	
	private final EventType songEventType;
	
	protected SongEvent(EventType songEventTypeIn, boolean canBeCancelled) {
		super(EventType.SONG, canBeCancelled);
		songEventType = songEventTypeIn;
	}
	
	public EventType getSongEventType() { return songEventType; }
	
}
