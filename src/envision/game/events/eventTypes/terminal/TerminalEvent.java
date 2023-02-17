package envision.game.events.eventTypes.terminal;

import envision.game.events.EventType;
import envision.game.events.GameEvent;

public abstract class TerminalEvent extends GameEvent {
	
	private final EventType terminalEventType;
	
	protected TerminalEvent(EventType terminalEventTypeIn, boolean canBeCancelled) {
		super(EventType.TERMINAL, canBeCancelled);
		terminalEventType = terminalEventTypeIn;
	}
	
	public EventType getTerminalEventType() { return terminalEventType; }
	
}
