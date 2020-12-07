package envisionEngine.eWindow.windowUtil.windowEvents.events;

import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventRedraw extends ObjectEvent {
	
	public EventRedraw(IWindowObject parentObjectIn) {
		super(parentObjectIn, EventType.Redraw);
	}
	
}
