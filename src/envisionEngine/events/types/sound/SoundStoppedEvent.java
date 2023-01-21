package envisionEngine.events.types.sound;

import envisionEngine.events.EventType;
import envisionEngine.gameEngine.effects.sounds.Audio;

public class SoundStoppedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStoppedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STOPPED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
