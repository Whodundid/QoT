package envision.engine.windows.windowUtil.windowEvents.events;

import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.EventType;

//Author: Hunter Bragg

/**
 * A type of object event that occurs whenever an object is being
 * dragged-and-dropped onto another object.
 * 
 * @author Hunter Bragg
 */
public class EventDragAndDrop extends ObjectEvent {
	
    //========
    // Fields
    //========
    
    /** The object being dropped onto the target object. */
    private final DragAndDropObject objectBeingDropped;
    /** The destination object that the drag-and-drop object is being dropped onto. */
    private final IWindowObject target;
    
	//==============
	// Constructors
	//==============
	
	public EventDragAndDrop(DragAndDropObject objectBeingDroppedIn, IWindowObject targetIn) {
		super(objectBeingDroppedIn, EventType.DRAG_AND_DROP, true);
		
		objectBeingDropped = objectBeingDroppedIn;
		target = targetIn;
	}
	
	//=========
	// Getters
	//=========
	
	public DragAndDropObject getObjectBeingDropped() { return objectBeingDropped; }
	public IWindowObject getTargetObject() { return target; }
	
}
