package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventFirstDraw extends ObjectEvent {
	
	//--------------
	// Constructors
	//--------------
	
	public EventFirstDraw(IWindowObject<?> parentObjectIn) {
		super(parentObjectIn, EventType.FIRSTDRAW);
	}
	
}