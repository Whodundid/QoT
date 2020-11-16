package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.eventUtil.EventType;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectEventType;

//Author: Hunter Bragg

public class EventObjects extends ObjectEvent {
	
	ObjectEventType type;
	IWindowObject targetObject;
	
	public EventObjects(IWindowObject parentIn, IWindowObject targetObjectIn, ObjectEventType typeIn) {
		super(parentIn, EventType.Object);
		targetObject = targetObjectIn;
		type = typeIn;
	}
	
	public ObjectEventType getObjectEventType() { return type; }
	public IWindowObject getTargetObject() { return targetObject; }
	
}
