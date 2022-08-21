package envision.events.types.sound;

import envision.events.EventType;
import envision.game.sounds.Audio;

public class SoundStoppedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStoppedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STOPPED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
