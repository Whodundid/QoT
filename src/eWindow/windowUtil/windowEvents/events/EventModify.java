package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.eventUtil.EventType;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;

//Author: Hunter Bragg

public class EventModify extends ObjectEvent {
	
	IWindowObject modifyingObject = null;
	ObjectModifyType modifyType = ObjectModifyType.None;
	
	public EventModify(IWindowObject parentObjectIn, IWindowObject modifyingObjectIn, ObjectModifyType modifyingTypeIn) {
		super(parentObjectIn, EventType.Modify);
		parentObject = parentObjectIn;
		modifyingObject = modifyingObjectIn;
		modifyType = modifyingTypeIn;
	}
	
	public IWindowObject getModifyingObect() { return modifyingObject; }
	public ObjectModifyType getModifyType() { return modifyType; }
	
}
