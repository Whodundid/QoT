package envision.engine.internal.windows.windowUtil.windowEvents.events;

import envision.engine.internal.windows.windowTypes.DragAndDropObject;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.WindowObjectEvent;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

/**
 * A type of object event that occurs whenever an object is being
 * dragged-and-dropped onto another object.
 * 
 * @author Hunter Bragg
 */
public class EventDragAndDrop extends WindowObjectEvent {
    
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
        super(objectBeingDroppedIn, WindowEventType.DRAG_AND_DROP, true);
        
        objectBeingDropped = objectBeingDroppedIn;
        target = targetIn;
    }    
    //=========
    // Getters
    //=========
    
    public DragAndDropObject getObjectBeingDropped() { return objectBeingDropped; }
    public IWindowObject getTargetObject() { return target; }
    
}
