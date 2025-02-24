package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IActionObject;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

public class EventActionPerformed extends WindowObjectEvent {
    
    //========
    // Fields
    //========
    
    private final IActionObject actionObject;
    private final Object[] args;    
    //==============
    // Constructors
    //==============
    
    public EventActionPerformed(IWindowObject parentObjectIn, IActionObject actionObjectIn, Object[] argsIn) {
        super(parentObjectIn, WindowEventType.ACTION, true);
        actionObject = actionObjectIn;
        args = argsIn;
    }    
    //=========
    // Getters
    //=========
    
    public IActionObject getActionObject() { return actionObject; }
    public Object[] getArgs() { return args; }
    
}
