package windowLib.windowUtil.windowEvents.events;

import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.windowEvents.ObjectEvent;
import windowLib.windowUtil.windowEvents.eventUtil.EventType;
import windowLib.windowUtil.windowEvents.eventUtil.MouseType;

//Author: Hunter Bragg

public class EventMouse extends ObjectEvent {
	
	MouseType type;
	int mX = 0, mY = 0;
	int button = -1;
	
	public EventMouse(IWindowObject parentObjectIn, int mXIn, int mYIn, int buttonIn, MouseType typeIn) {
		super(parentObjectIn, EventType.MOUSE);
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
