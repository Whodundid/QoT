package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectEventType;

//Author: Hunter Bragg

public class EventObjects extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final ObjectEventType type;
	private final IWindowObject targetObject;
	
	//--------------
	// Constructors
	//--------------
	
	public EventObjects(IWindowObject parentIn, IWindowObject targetObjectIn, ObjectEventType typeIn) {
		super(parentIn, EventType.OBJECT, true);
		targetObject = targetObjectIn;
		type = typeIn;
	}
	
	//---------
	// Getters
	//---------
	
	public ObjectEventType getObjectEventType() { return type; }
	public IWindowObject getTargetObject() { return targetObject; }
	
}
