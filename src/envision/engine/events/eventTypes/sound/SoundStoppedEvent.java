package envision.engine.events.eventTypes.sound;

import envision.engine.events.EventType;
import envision.game.effects.sounds.Audio;

public class SoundStoppedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStoppedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STOPPED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
