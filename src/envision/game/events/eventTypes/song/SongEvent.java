package envision.game.events.eventTypes.song;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class SongEvent extends GameEvent {
	
	private final EventType songEventType;
	
	protected SongEvent(EventType songEventTypeIn, boolean canBeCancelled) {
		super(EventType.SONG, canBeCancelled);
		songEventType = songEventTypeIn;
	}
	
	public EventType getSongEventType() { return songEventType; }
	
}
