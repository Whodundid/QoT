package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.MouseType;

//Author: Hunter Bragg

public class EventMouse extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final int mX, mY;
	private final int button;
	private final MouseType type;
	
	//--------------
	// Constructors
	//--------------
	
	public EventMouse(IWindowObject<?> parentObjectIn, int mXIn, int mYIn, int buttonIn, MouseType typeIn) {
		super(parentObjectIn, EventType.MOUSE);
		mX = mXIn;
		mY = mYIn;
		button = buttonIn;
		type = typeIn;
	}

	//---------
	// Getters
	//---------
	
	public int getMouseX() { return mX; }
	public int getMouseY() { return mY; }
	public int getMouseButton() { return button; }
	public MouseType getMouseType() { return type; }
	
}
