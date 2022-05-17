package engine.windowLib.windowUtil.windowEvents.events;

import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	IActionObject actionObject = null;
	Object[] args = null;
	
	public EventAction(IWindowObject parentObjectIn, IActionObject actionObjectIn, Object[] argsIn) {
		super(parentObjectIn, EventType.ACTION);
		actionObject = actionObjectIn;
		args = argsIn;
	}
	
	public IActionObject getActionObject() { return actionObject; }
	public Object[] getArgs() { return args; }
	
}
