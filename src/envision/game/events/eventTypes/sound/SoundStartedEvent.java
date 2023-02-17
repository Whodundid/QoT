package envision.game.events.eventTypes.sound;

import envision.game.events.EventType;
import envision.game.objects.effects.sounds.Audio;

public class SoundStartedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStartedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STARTED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
