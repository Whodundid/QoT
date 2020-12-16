package envisionEngine.eWindow.windowUtil.windowEvents.events;

import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.EventType;

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
