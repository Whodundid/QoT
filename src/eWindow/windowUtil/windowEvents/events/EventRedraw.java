package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	public EventRedraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.Redraw);
	}
	
}
