package envisionEngine.events.types.sound;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class SoundEvent extends GameEvent {
	
	private final EventType soundEventType;
	
	protected SoundEvent(EventType soundEventTypeIn, boolean canBeCancelled) {
		super(EventType.SOUND, canBeCancelled);
		soundEventType = soundEventTypeIn;
	}
	
	public EventType getSoundEventType() { return soundEventType; }
	
}
