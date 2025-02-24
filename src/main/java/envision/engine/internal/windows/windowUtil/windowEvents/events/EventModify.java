package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

public class EventModify extends WindowObjectEvent {
    
    //========
    // Fields
    //========
    
    private final IWindowObject modifyingObject;
    private final ObjectModifyType modifyType;    
    //==============
    // Constructors
    //==============
    
    public EventModify(IWindowObject parentObjectIn, IWindowObject modifyingObjectIn, ObjectModifyType modifyingTypeIn) {
        super(parentObjectIn, WindowEventType.MODIFY, true);
        modifyingObject = modifyingObjectIn;
        modifyType = modifyingTypeIn;
    }    
    //=========
    // Getters
    //=========
    
    public IWindowObject getModifyingObect() { return modifyingObject; }
    public ObjectModifyType getModifyType() { return modifyType; }
    
}
