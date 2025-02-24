package envision.engine.events.eventTypes.song;

import envision.engine.events.EnvisionEventType;
import envision.game.sounds.Audio;

public class SongStartedEvent extends SongEvent {
    
    private final Audio theSong;
    
    public SongStartedEvent(Audio theSongIn) {
        super(EnvisionEventType.SONG_STARTED, true);
        theSong = theSongIn;
    }
    
    public Audio getSong() { return theSong; }
    
}
