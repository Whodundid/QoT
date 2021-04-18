package windowLib.windowUtil.windowEvents.events;

import windowLib.windowTypes.interfaces.IWindowObject;
import windowLib.windowUtil.windowEvents.ObjectEvent;
import windowLib.windowUtil.windowEvents.eventUtil.EventType;
import windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;

//Author: Hunter Bragg

public class EventObjects extends ObjectEvent {
	
	ObjectEventType type;
	IWindowObject targetObject;
	
	public EventObjects(IWindowObject parentIn, IWindowObject targetObjectIn, ObjectEventType typeIn) {
		super(parentIn, EventType.OBJECT);
		targetObject = targetObjectIn;
		type = typeIn;
	}
	
	public ObjectEventType getObjectEventType() { return type; }
	public IWindowObject getTargetObject() { return targetObject; }
	
}
