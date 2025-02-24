package envision.engine.events.eventTypes.song;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class SongEvent extends GameEvent {
    
    private final EnvisionEventType songEventType;
    
    protected SongEvent(EnvisionEventType songEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.SONG, canBeCancelled);
        songEventType = songEventTypeIn;
    }
    
    public EnvisionEventType getSongEventType() { return songEventType; }
    
}
