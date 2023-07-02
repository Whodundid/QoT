package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

public class EventAction extends ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final IActionObject actionObject;
	private final Object[] args;
	
	//--------------
	// Constructors
	//--------------
	
	public EventAction(IWindowObject parentObjectIn, IActionObject actionObjectIn, Object[] argsIn) {
		super(parentObjectIn, EventType.ACTION);
		actionObject = actionObjectIn;
		args = argsIn;
	}
	
	//---------
	// Getters
	//---------
	
	public IActionObject getActionObject() { return actionObject; }
	public Object[] getArgs() { return args; }
	
}
