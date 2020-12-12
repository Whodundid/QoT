package envisionEngine.eWindow.windowUtil.windowEvents.events;

import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.EventType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.ObjectEventType;

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
