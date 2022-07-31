package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;

//Author: Hunter Bragg

public class EventObjects extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final ObjectEventType type;
	private final IWindowObject<?> targetObject;
	
	//--------------
	// Constructors
	//--------------
	
	public EventObjects(IWindowObject<?> parentIn, IWindowObject<?> targetObjectIn, ObjectEventType typeIn) {
		super(parentIn, EventType.OBJECT);
		targetObject = targetObjectIn;
		type = typeIn;
	}
	
	//---------
	// Getters
	//---------
	
	public ObjectEventType getObjectEventType() { return type; }
	public IWindowObject<?> getTargetObject() { return targetObject; }
	
}
