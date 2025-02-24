package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;
import eutil.datatypes.points.Point2i;

//Author: Hunter Bragg

/** Event that is fired when a focus change occurs. */
public class EventFocus extends WindowObjectEvent {
    
    //========
    // Fields
    //========
    
    public final IWindowObject eventObject;
    public final FocusType type;
    public final int actionCode;
    public final int mX, mY;    
    //==============
    // Constructors
    //==============
    
    public EventFocus(IWindowObject parentObjectIn, IWindowObject eventObjectIn, FocusType typeIn) {
        super(parentObjectIn, WindowEventType.FOCUS, true);
        eventObject = eventObjectIn;
        type = typeIn;
        actionCode = -1;
        mX = -1;
        mY = -1;
    }
    
    public EventFocus(IWindowObject parentObjectIn, IWindowObject eventObjectIn, FocusType typeIn, int actionCodeIn, int mXIn, int mYIn) {
        super(parentObjectIn, WindowEventType.FOCUS, true);
        eventObject = eventObjectIn;
        type = typeIn;
        actionCode = actionCodeIn;
        mX = mXIn;
        mY = mYIn;
    }    
    //=========
    // Getters
    //=========
    
    public IWindowObject getFocusObject() { return eventObject; }
    public FocusType getFocusType() { return type; }
    public int getActionCode() { return actionCode; }
    public Point2i getMousePoint() { return new Point2i(mX, mY); }
    public int getMX() { return mX; }
    public int getMY() { return mY; }
    
}
