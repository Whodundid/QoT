package envisionEngine.windowLib.windowUtil.windowEvents.events;

import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final IActionObject<?> actionObject;
	private final Object[] args;
	
	//--------------
	// Constructors
	//--------------
	
	public EventAction(IWindowObject<?> parentObjectIn, IActionObject<?> actionObjectIn, Object[] argsIn) {
		super(parentObjectIn, EventType.ACTION);
		actionObject = actionObjectIn;
		args = argsIn;
	}
	
	//---------
	// Getters
	//---------
	
	public IActionObject<?> getActionObject() { return actionObject; }
	public Object[] getArgs() { return args; }
	
}
