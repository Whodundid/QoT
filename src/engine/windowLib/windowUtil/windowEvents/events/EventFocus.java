package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.FocusType;

//Author: Hunter Bragg

/** Event that is fired when a focus change occurs. */
public class EventFocus extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final IWindowObject<?> eventObject;
	private final FocusType type;
	private final int actionCode;
	private final int mX, mY;
	
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
	public int getMX() { return mX; }
	public int getMY() { return mY; }
	
}
