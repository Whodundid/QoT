package envisionEngine.events.types.terminal;

import envisionEngine.events.EventType;
import envisionEngine.events.GameEvent;

public abstract class TerminalEvent extends GameEvent {
	
	private final EventType terminalEventType;
	
	protected TerminalEvent(EventType terminalEventTypeIn, boolean canBeCancelled) {
		super(EventType.TERMINAL, canBeCancelled);
		terminalEventType = terminalEventTypeIn;
	}
	
	public EventType getTerminalEventType() { return terminalEventType; }
	
}
