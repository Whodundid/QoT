package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

public class EventRedraw extends WindowObjectEvent {    
    //==============
    // Constructors
    //==============
    
    public EventRedraw(IWindowObject parentObjectIn) {
        super(parentObjectIn, WindowEventType.REDRAW, true);
    }
    
}
