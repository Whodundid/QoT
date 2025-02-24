package envision.engine.events.eventTypes.screen;

import envision.engine.events.EnvisionEventType;
import envision.engine.events.GameEvent;

public abstract class ScreenEvent extends GameEvent {
    
    private final EnvisionEventType screenEventType;
    
    protected ScreenEvent(EnvisionEventType screenEventTypeIn, boolean canBeCancelled) {
        super(EnvisionEventType.SCREEN, canBeCancelled);
        screenEventType = screenEventTypeIn;
    }
    
    public EnvisionEventType getScreenEventType() { return screenEventType; }
    
}
