package envisionEngine.events.types.sound;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.effects.sounds.Audio;

public class SoundStartedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStartedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STARTED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
