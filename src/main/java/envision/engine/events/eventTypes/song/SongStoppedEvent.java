package envision.engine.events.eventTypes.song;

import envision.engine.events.EnvisionEventType;
import envision.game.sounds.Audio;

public class SongStoppedEvent extends SongEvent {
    
    private final Audio theSong;
    
    public SongStoppedEvent(Audio theSongIn) {
        super(EnvisionEventType.SONG_STOPPED, true);
        theSong = theSongIn;
    }
    
    public Audio getSong() { return theSong; }
    
}
