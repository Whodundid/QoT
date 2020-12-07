package envisionEngine.eWindow.windowUtil.windowEvents;

import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

/** The base event class pertaining to all IEnhancedGuiObject events. */
public abstract class ObjectEvent {
	
	protected IWindowObject parentObject = null;
	protected EventType eventType = null;
	
	protected ObjectEvent() {}
	
	public ObjectEvent(IWindowObject parentObjectIn, EventType typeIn) {
		parentObject = parentObjectIn;
		eventType = typeIn;
	}
	
	/** Returns the parent object from which the event was created. */
	public IWindowObject getEventParent() { return parentObject; }
	
	/** Returns the type of event this is */
	public EventType getEventType() { return eventType; }
	
}
