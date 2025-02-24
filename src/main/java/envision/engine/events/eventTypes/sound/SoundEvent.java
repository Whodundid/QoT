package envision.engine.events.eventTypes.sound;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class SoundEvent extends GameEvent {
    
    private final EnvisionEventType soundEventType;
    
    protected SoundEvent(EnvisionEventType soundEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.SOUND, canBeCancelled);
        soundEventType = soundEventTypeIn;
    }
    
    public EnvisionEventType getSoundEventType() { return soundEventType; }
    
}
