package envisionEngine.windowLib.windowUtil.windowEvents.events;

import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventFirstDraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.FIRSTDRAW);
	}
	
}
