package windowLib.windowUtil.windowEvents.events;

import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.windowEvents.ObjectEvent;
import windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	public EventRedraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.REDRAW);
	}
	
}
