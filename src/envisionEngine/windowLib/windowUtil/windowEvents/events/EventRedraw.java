package envisionEngine.windowLib.windowUtil.windowEvents.events;

import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventRedraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.REDRAW);
	}
	
}
