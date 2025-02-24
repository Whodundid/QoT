package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

// Author: Hunter Bragg

public class EventObjectRemoved extends WindowObjectEvent {
    
    //========
    // Fields
    //========
    
    private final IWindowObject targetObject;    
    //==============
    // Constructors
    //==============
    
    public EventObjectRemoved(IWindowObject parentIn, IWindowObject targetObjectIn) {
        super(parentIn, WindowEventType.REMOVED, true);
        targetObject = targetObjectIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + targetObject;
    }    
    //=========
    // Getters
    //=========
    
    public IWindowObject getTargetObject() { return targetObject; }
    
}
