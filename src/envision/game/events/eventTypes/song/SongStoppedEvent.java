package envision.game.events.eventTypes.song;

import envision.game.events.EventType;
import envision.game.objects.effects.sounds.Audio;

public class SongStoppedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStoppedEvent(Audio theSongIn) {
		super(EventType.SONG_STOPPED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
