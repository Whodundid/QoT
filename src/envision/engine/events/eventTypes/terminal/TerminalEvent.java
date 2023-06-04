package envision.engine.events.eventTypes.terminal;

import envision.engine.events.EventType;
import envision.engine.events.GameEvent;

public abstract class TerminalEvent extends GameEvent {
	
	private final EventType terminalEventType;
	
	protected TerminalEvent(EventType terminalEventTypeIn, boolean canBeCancelled) {
		super(EventType.TERMINAL, canBeCancelled);
		terminalEventType = terminalEventTypeIn;
	}
	
	public EventType getTerminalEventType() { return terminalEventType; }
	
}
