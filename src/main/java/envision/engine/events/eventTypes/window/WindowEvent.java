package envision.engine.events.eventTypes.window;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class WindowEvent extends GameEvent {
    
    private final EnvisionEventType windowEventType;
    
    protected WindowEvent(EnvisionEventType windowEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.WINDOW, canBeCancelled);
        windowEventType = windowEventTypeIn;
    }
    
    public EnvisionEventType getWindowEventType() { return windowEventType; }
    
}
