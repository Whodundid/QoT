package envisionEngine.events.types.song;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.effects.sounds.Audio;

public class SongStoppedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStoppedEvent(Audio theSongIn) {
		super(EventType.SONG_STOPPED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
