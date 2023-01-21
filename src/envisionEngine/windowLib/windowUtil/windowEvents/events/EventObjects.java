package envision.windowLib.windowUtil.windowEvents.events;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.ObjectEvent;
import envision.windowLib.windowUtil.windowEvents.eventUtil.EventType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;

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
