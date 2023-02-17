package envision.game.events.eventTypes.sound;

import envision.game.events.EventType;
import envision.game.objects.effects.sounds.Audio;

public class SoundStoppedEvent extends SoundEvent {
	
	private final Audio theSound;
	
	public SoundStoppedEvent(Audio theSoundIn) {
		super(EventType.SOUND_STOPPED, true);
		theSound = theSoundIn;
	}
	
	public Audio getSound() { return theSound; }
	
}
