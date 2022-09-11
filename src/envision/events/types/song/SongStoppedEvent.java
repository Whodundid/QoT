package envision.events.types.song;

import envision.events.EventType;
import envision.gameEngine.effects.sounds.Audio;

public class SongStoppedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStoppedEvent(Audio theSongIn) {
		super(EventType.SONG_STOPPED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
