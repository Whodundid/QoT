package envision.engine.windows.windowTypes;

import envision.Envision;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;

/**
 * A type of window object that is capable of being 'dragged-and-dropped'
 * onto other window objects.
 * <p>
 * Actual Drag-and-Drop behavior logic is decided by the receiving target
 * object.
 * 
 * @author Hunter Bragg
 */
public abstract class DragAndDropObject extends WindowParent {
    
    //==============
    // Constructors
    //==============
    
    protected DragAndDropObject() {
        init(Envision.getDeveloperDesktop());
        
        getTopParent().registerListener(this);
        getTopParent().setDragAndDropObject(this);
        getTopParent().addObject(this);
    }
    
    //===========
    // Abstracts
    //===========
    
    public abstract void onDropped(IWindowObject target);
    
    //===========
    // Overrides
    //===========
    
    /**
     * Drag and drop objects should not be clickable in order to prevent
     * target destination objects from not being the highest z object.
     */
    @Override
    public boolean isClickable() {
        return false;
    }
    
    @Override
    public void addObject(IWindowObject... objs) {
        // make all objects being add to this unclickable so that the
        // highest z object isn't a child of this drag and drop object
        for (IWindowObject o : objs) {
            o.setClickable(false);
            IWindowObject.setClickable(false, o.getAllChildren());
        }
        super.addObject(objs);
    }
    
    @Override
    public void onEvent(ObjectEvent e) {
        if (e instanceof EventMouse em) {
            if (em.getMouseType() != MouseType.RELEASED) return;
            
            // remove this object as the drag and drop object
            final DragAndDropObject cur = getTopParent().getDragAndDropObject();
            if (cur == this) getTopParent().clearDragAndDropObject();
            
            // 'drop' this object onto the highest z level object
            try {
                IWindowObject highest = getTopParent().getHighestZObjectUnderMouse();
                if (highest == null) highest = getTopParent();
                if (highest != null) {
                    highest.onDragAndDrop_i(this);
                    onDropped(highest);
                }
            }
            catch (Exception ee) {
                ee.printStackTrace();
            }
            
            // finally close this object
            //getTopParent().unregisterListener(this);
            close();
        }
    }
    
}
