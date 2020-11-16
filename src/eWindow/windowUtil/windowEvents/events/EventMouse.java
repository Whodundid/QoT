package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.eventUtil.EventType;
import eWindow.windowUtil.windowEvents.eventUtil.MouseType;

//Author: Hunter Bragg

public class EventMouse extends ObjectEvent {
	
	MouseType type;
	int mX = 0, mY = 0;
	int button = -1;
	
	public EventMouse(IWindowObject parentObjectIn, int mXIn, int mYIn, int buttonIn, MouseType typeIn) {
		super(parentObjectIn, EventType.Mouse);
		mX = mXIn;
		mY = mYIn;
		button = buttonIn;
		type = typeIn;
	}

	public MouseType getMouseType() { return type; }
	public int getMouseX() { return mX; }
	public int getMouseY() { return mY; }
	public int getMouseButton() { return button; }
	
}
