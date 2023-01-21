package envision.events.types.sound;

import envision.events.EventType;
import envision.events.GameEvent;

public abstract class SoundEvent extends GameEvent {
	
	private final EventType soundEventType;
	
	protected SoundEvent(EventType soundEventTypeIn, boolean canBeCancelled) {
		super(EventType.SOUND, canBeCancelled);
		soundEventType = soundEventTypeIn;
	}
	
	public EventType getSoundEventType() { return soundEventType; }
	
}
