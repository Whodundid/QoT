package envision.engine.events.eventTypes.sound;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class SoundEvent extends GameEvent {
	
	private final EventType soundEventType;
	
	protected SoundEvent(EventType soundEventTypeIn, boolean canBeCancelled) {
		super(EventType.SOUND, canBeCancelled);
		soundEventType = soundEventTypeIn;
	}
	
	public EventType getSoundEventType() { return soundEventType; }
	
}
