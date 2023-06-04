package envision.engine.events.eventTypes.song;

import envision.engine.events.EventType;
import envision.game.effects.sounds.Audio;

public class SongStartedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStartedEvent(Audio theSongIn) {
		super(EventType.SONG_STARTED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
