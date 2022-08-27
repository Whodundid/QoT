package envision.windowLib.windowUtil.windowEvents.events;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.ObjectEvent;
import envision.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventFirstDraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.FIRSTDRAW);
	}
	
}
