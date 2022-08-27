package envision.events.types.song;

import envision.events.EventType;
import envision.game.sounds.Audio;

public class SongStartedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStartedEvent(Audio theSongIn) {
		super(EventType.SONG_STARTED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
