package envision.engine.events.eventTypes.song;

import envision.engine.events.EventType;
import envision.game.effects.sounds.Audio;

public class SongStoppedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStoppedEvent(Audio theSongIn) {
		super(EventType.SONG_STOPPED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
