package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import eutil.datatypes.points.Point2i;

//Author: Hunter Bragg

/** Event that is fired when a focus change occurs. */
public class EventFocus extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	public final IWindowObject<?> eventObject;
	public final FocusType type;
	public final int actionCode;
	public final int mX, mY;
	
	//--------------
	// Constructors
	//--------------
	
	public EventFocus(IWindowObject<?> parentObjectIn, IWindowObject<?> eventObjectIn, FocusType typeIn) {
		super(parentObjectIn, EventType.FOCUS);
		eventObject = eventObjectIn;
		type = typeIn;
		actionCode = -1;
		mX = -1;
		mY = -1;
	}
	
	public EventFocus(IWindowObject<?> parentObjectIn, IWindowObject<?> eventObjectIn, FocusType typeIn, int actionCodeIn, int mXIn, int mYIn) {
		super(parentObjectIn, EventType.FOCUS);
		eventObject = eventObjectIn;
		type = typeIn;
		actionCode = actionCodeIn;
		mX = mXIn;
		mY = mYIn;
	}
	
	//---------
	// Getters
	//---------
	
	public IWindowObject<?> getFocusObject() { return eventObject; }
	public FocusType getFocusType() { return type; }
	public int getActionCode() { return actionCode; }
	public Point2i getMousePoint() { return new Point2i(mX, mY); }
	public int getMX() { return mX; }
	public int getMY() { return mY; }
	
}
