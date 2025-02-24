package envision.engine.events.eventTypes.sound;

import envision.engine.events.EnvisionEventType;
import envision.game.sounds.Audio;

public class SoundStartedEvent extends SoundEvent {
    
    private final Audio theSound;
    
    public SoundStartedEvent(Audio theSoundIn) {
        super(EnvisionEventType.SOUND_STARTED, true);
        theSound = theSoundIn;
    }
    
    public Audio getSound() { return theSound; }
    
}
