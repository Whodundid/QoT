package envision.game.events.eventTypes.sound;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class SoundEvent extends GameEvent {
	
	private final EventType soundEventType;
	
	protected SoundEvent(EventType soundEventTypeIn, boolean canBeCancelled) {
		super(EventType.SOUND, canBeCancelled);
		soundEventType = soundEventTypeIn;
	}
	
	public EventType getSoundEventType() { return soundEventType; }
	
}
