package envisionEngine.events.types.song;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.effects.sounds.Audio;

public class SongStartedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStartedEvent(Audio theSongIn) {
		super(EventType.SONG_STARTED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
