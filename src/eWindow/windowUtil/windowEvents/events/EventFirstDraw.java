package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	public EventFirstDraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.FirstDraw);
	}
	
}
