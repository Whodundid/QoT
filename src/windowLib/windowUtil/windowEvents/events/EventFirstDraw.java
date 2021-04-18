package windowLib.windowUtil.windowEvents.events;

import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.windowEvents.ObjectEvent;
import windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	public EventFirstDraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.FIRSTDRAW);
	}
	
}
