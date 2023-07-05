package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;

//Author: Hunter Bragg

public class EventModify extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final IWindowObject modifyingObject;
	private final ObjectModifyType modifyType;
	
	//--------------
	// Constructors
	//--------------
	
	public EventModify(IWindowObject parentObjectIn, IWindowObject modifyingObjectIn, ObjectModifyType modifyingTypeIn) {
		super(parentObjectIn, EventType.MODIFY, true);
		modifyingObject = modifyingObjectIn;
		modifyType = modifyingTypeIn;
	}
	
	//---------
	// Getters
	//---------
	
	public IWindowObject getModifyingObect() { return modifyingObject; }
	public ObjectModifyType getModifyType() { return modifyType; }
	
}
