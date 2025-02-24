package envision.engine.events.eventTypes.terminal;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class TerminalEvent extends GameEvent {
    
    private final EnvisionEventType terminalEventType;
    
    protected TerminalEvent(EnvisionEventType terminalEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.TERMINAL, canBeCancelled);
        terminalEventType = terminalEventTypeIn;
    }
    
    public EnvisionEventType getTerminalEventType() { return terminalEventType; }
    
}
