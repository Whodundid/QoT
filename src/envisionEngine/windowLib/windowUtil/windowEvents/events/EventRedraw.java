package envision.windowLib.windowUtil.windowEvents.events;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.ObjectEvent;
import envision.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventRedraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.REDRAW);
	}
	
}
