package envision.engine.events.eventTypes.window;

import envision.engine.events.EnvisionEventType;
import envision.engine.internal.windows.windowTypes.WindowParent;

public class WindowClosedEvent extends WindowEvent {

    private final WindowParent window;
    
    public WindowClosedEvent(WindowParent windowIn) {
        super(EnvisionEventType.WINDOW_CLOSED, true);
        window = windowIn;
    }
    
    public WindowParent getWindow() { return window; }
    
}
