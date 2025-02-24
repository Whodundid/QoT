package envision.engine.events.eventTypes.sound;

import envision.engine.events.EnvisionEventType;
import envision.game.sounds.Audio;

public class SoundStoppedEvent extends SoundEvent {
    
    private final Audio theSound;
    
    public SoundStoppedEvent(Audio theSoundIn) {
        super(EnvisionEventType.SOUND_STOPPED, true);
        theSound = theSoundIn;
    }
    
    public Audio getSound() { return theSound; }
    
}
