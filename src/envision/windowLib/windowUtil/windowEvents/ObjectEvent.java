package envision.windowLib.windowUtil.windowEvents;

import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

/** The base event class pertaining to all IEnhancedGuiObject events. */
public abstract class ObjectEvent {
	
	//--------
	// Fields
	//--------
	
	private final IWindowObject<?> parentObject;
	private final EventType eventType;
	
	//--------------
	// Constructors
	//--------------
	
	public ObjectEvent(IWindowObject<?> parentObjectIn, EventType typeIn) {
		parentObject = parentObjectIn;
		eventType = typeIn;
	}
	
	//---------
	// Getters
	//---------
	
	/** Returns the parent object from which the event was created. */
	public IWindowObject<?> getEventParent() { return parentObject; }
	/** Returns the type of event this is */
	public EventType getEventType() { return eventType; }
	
}
