package envision.game.events.eventTypes.song;

import envision.game.events.EventType;
import envision.game.objects.effects.sounds.Audio;

public class SongStartedEvent extends SongEvent {
	
	private final Audio theSong;
	
	public SongStartedEvent(Audio theSongIn) {
		super(EventType.SONG_STARTED, true);
		theSong = theSongIn;
	}
	
	public Audio getSong() { return theSong; }
	
}
