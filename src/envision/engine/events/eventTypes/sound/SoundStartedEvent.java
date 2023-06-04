package envision.engine.events.eventTypes.sound;

import envision.engine.events.EventType;
import envision.game.effects.sounds.Audio;

public class SoundStartedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStartedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STARTED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
