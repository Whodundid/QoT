package envision.events.types.sound;

import envision.events.EventType;
import envision.game.sounds.Audio;

public class SoundStartedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStartedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STARTED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
