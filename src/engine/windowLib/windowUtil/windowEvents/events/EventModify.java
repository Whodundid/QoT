package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;

//Author: Hunter Bragg

public class EventModify extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final IWindowObject<?> modifyingObject;
	private final ObjectModifyType modifyType;
	
	//--------------
	// Constructors
	//--------------
	
	public EventModify(IWindowObject<?> parentObjectIn, IWindowObject<?> modifyingObjectIn, ObjectModifyType modifyingTypeIn) {
		super(parentObjectIn, EventType.MODIFY);
		modifyingObject = modifyingObjectIn;
		modifyType = modifyingTypeIn;
	}
	
	//---------
	// Getters
	//---------
	
	public IWindowObject<?> getModifyingObect() { return modifyingObject; }
	public ObjectModifyType getModifyType() { return modifyType; }
	
}
