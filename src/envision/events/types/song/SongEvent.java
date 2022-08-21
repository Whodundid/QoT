package envision.events.types.song;

import envision.events.EventType;
import envision.events.GameEvent;

public abstract class SongEvent extends GameEvent {
	
	private final EventType songEventType;
	
	protected SongEvent(EventType songEventTypeIn, boolean canBeCancelled) {
		super(EventType.SONG, canBeCancelled);
		songEventType = songEventTypeIn;
	}
	
	public EventType getSongEventType() { return songEventType; }
	
}
