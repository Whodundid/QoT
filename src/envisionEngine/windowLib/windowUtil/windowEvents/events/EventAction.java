package envision.windowLib.windowUtil.windowEvents.events;

import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.ObjectEvent;
import envision.windowLib.windowUtil.windowEvents.eventUtil.EventType;

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
