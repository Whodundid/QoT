package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

public class EventFirstDraw extends WindowObjectEvent {    
    //==============
    // Constructors
    //==============
    
    public EventFirstDraw(IWindowObject parentObjectIn) {
        super(parentObjectIn, WindowEventType.FIRSTDRAW, true);
    }
    
}
