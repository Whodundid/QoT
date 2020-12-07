package envisionEngine.eWindow.windowUtil.windowEvents.events;

import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEvent;

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
