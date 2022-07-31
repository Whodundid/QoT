package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventFirstDraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.FIRSTDRAW);
	}
	
}
