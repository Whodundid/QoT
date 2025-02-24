package envision.engine.events.eventTypes.window;

import envision.engine.events.EnvisionEventType;
import envision.engine.internal.windows.windowTypes.WindowParent;

public class WindowOpenedEvent extends WindowEvent {

    private final WindowParent window;
    
    public WindowOpenedEvent(WindowParent windowIn) {
        super(EnvisionEventType.WINDOW_OPENED, true);
        window = windowIn;
    }
    
    public WindowParent getWindow() { return window; }
    
}
