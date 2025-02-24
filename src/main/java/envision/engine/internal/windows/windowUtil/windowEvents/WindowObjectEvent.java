package envision.engine.internal.windows.windowUtil.windowEvents;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;

//Author: Hunter Bragg

/** The base event class pertaining to all IEnhancedGuiObject events. */
public abstract class WindowObjectEvent {
    
    //========
    // Fields
    //========
    
    private final IWindowObject parentObject;
    private final WindowEventType eventType;
    private final boolean canBeCancelled;
    private boolean isCancelled = false;    
    //==============
    // Constructors
    //==============
    
    public WindowObjectEvent(IWindowObject parentObjectIn, WindowEventType typeIn, boolean canBeCancelledIn) {
        parentObject = parentObjectIn;
        eventType = typeIn;
        canBeCancelled = canBeCancelledIn;
    }    
    //=========
    // Getters
    //=========
    
    /** Returns the parent object from which this event was created for. */
    public IWindowObject getEventParent() { return parentObject; }
    /** Returns the type of object event that this is */
    public WindowEventType getEventType() { return eventType; }
    /** Returns true if this event can be cancelled. */
    public final boolean canBeCancelled() { return canBeCancelled; }
    /** Returns true if this event is cancelled. */
    public boolean isCancelled() { return isCancelled; }    
    //=========
    // Setters
    //=========
    
    /**
     * Specifies whether or not this event is cancelled.
     * <p>
     * NOTE: not every event can be cancelled and non-cancellable events
     * will throw an error upon attempting to cancelled.
     * 
     * @see WindowObjectEvent#canBeCancelled()
     * 
     * @param val The value to set
     */
    public final void setCanceled(boolean val) {
        if (!canBeCancelled && val) throw new RuntimeException("Window - " + eventType + ": cannot be cancelled!");
        isCancelled = val;
    }
    
}
