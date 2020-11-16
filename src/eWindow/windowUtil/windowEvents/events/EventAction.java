package eWindow.windowUtil.windowEvents.events;

import eWindow.windowTypes.interfaces.IActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowUtil.windowEvents.ObjectEvent;

//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	IActionObject actionObject = null;
	Object storedObject = null;
	Object[] args = null;
	
	public EventAction(IWindowObject parentObjectIn, IActionObject actionObjectIn, Object[] argsIn) {
		actionObject = actionObjectIn;
		storedObject = actionObjectIn.getSelectedObject();
		args = argsIn;
	}
	
	public IActionObject getActionObject() { return actionObject; }
	public Object getStoredObject() { return storedObject; }
	public Object[] getArgs() { return args; }
	
}
