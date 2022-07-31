package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventRedraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.REDRAW);
	}
	
}
