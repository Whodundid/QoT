package envision.engine.internal.windows.windowUtil.windowEvents;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowUtil.windowEvents.eventUtil.WindowEventType;
import eutil.datatypes.util.EList;

/**
 * Manages the distribution of object events to listeners.
 * 
 * @author Hunter
 */
public class ObjectEventHandler {
    
    //========
    // Fields
    //========
    
    private final IWindowObject parent;
    private final ConcurrentMap<WindowEventType, EList<IWindowObject>> listeners = new ConcurrentHashMap<>();    
    //==============
    // Constructors
    //==============
    
    public ObjectEventHandler(IWindowObject parentIn) {
        parent = parentIn;
    }    
    //=========
    // Methods
    //=========
    
    /**
     * Distributes the given event to all listeners on this manager.
     * <p>
     * NOTE: If an object is
     * 
     * @param event The event to distribute to listeners
     * 
     * @return True if the event event is NOT cancelled
     */
    public synchronized boolean processEvent(WindowObjectEvent event) {
        var g = parent.getObjectGroup();
        if (g != null) g.notifyGroup(event);
        
        WindowEventType t = event.getEventType();
        
        // get explicit listeners for this event type
        EList<IWindowObject> typeListeners = listeners.get(t);
        if (typeListeners != null) {
            for (var l : typeListeners) {
                l.onEvent(event);
            }
        }
        
        // distribute event to those who listen for any event type
        EList<IWindowObject> genericListeners = listeners.get(WindowEventType.GENERIC);
        if (genericListeners != null) {
            // trim list to prevent double notifications for the same event
            EList<IWindowObject> list = EList.newList(genericListeners);
            if (typeListeners != null) {
                list = genericListeners.filter(l -> typeListeners.notContains(l));
            }
            
            for (var l : list) {
                l.onEvent(event);
            }
        }
        
        return !event.isCancelled();
    }
    
    public synchronized void registerObject(IWindowObject object, WindowEventType... eventTypes) {
        if (object == null) return;
        
        // generic event type (listens to all events)
        if (eventTypes.length == 0) {
            var genericList = getOrCreateListenerList(WindowEventType.GENERIC);
            genericList.addIfNotContains(object);
        }
        else {
            boolean containsGeneric = false;
            
            // check if contains generic event type
            for (int i = 0; i < eventTypes.length; i++) {
                containsGeneric |= (eventTypes[i] == WindowEventType.GENERIC);
            }
            
            // if the given set of event types contains the 'GENERIC' event type, then we can
            // wrap all of the other given event types into one so as to not distribute duplicates.
            if (containsGeneric) {
                var genericList = getOrCreateListenerList(WindowEventType.GENERIC);
                genericList.addIfNotContains(object);
            }
            // otherwise, register this object to the specific event types they requested
            else {
                for (int i = 0; i < eventTypes.length; i++) {
                    var t = eventTypes[i];
                    var list = getOrCreateListenerList(t);
                    list.addIfNotContains(object);
                }
            }
        }
    }
    
    public synchronized void unregisterObject(IWindowObject object, WindowEventType... eventTypes) {
        if (object == null) return;
        
        if (eventTypes.length == 0) {
            var typeList = listeners.get(WindowEventType.GENERIC);
            if (typeList != null) {
                typeList.remove(object);
            }
        }
        else {
            for (int i = 0; i < eventTypes.length; i++) {
                var t = eventTypes[i];
                
                var typeList = listeners.get(t);
                if (typeList != null) {
                    typeList.remove(object);
                }
            }
        }
        
        validateCurrentListeners();
    }
    
    public synchronized void unregisterAllObjects() {
        listeners.clear();
    }
    
    //==================
    // Internal Methods
    //==================
    
    /**
     * Remove any event types that don't have listeners and removes listeners
     * that are closed.
     */
    private void validateCurrentListeners() {
        var it = listeners.entrySet().iterator();
        while (it.hasNext()) {
            var t = it.next();
            
            var itt = t.getValue().iterator();
            while (itt.hasNext()) {
                var tt = itt.next();
                
                // remove old listeners that are closed
                if (tt.isClosed()) {
                    itt.remove();
                }
            }
            
            // remove event types that no longer have listeners
            if (t.getValue().isEmpty()) {
                it.remove();
            }
        }
    }
    
    /**
     * Gets or creates (and puts) the list of listeners for a given event type.
     * 
     * @param type The event type to get an object list for
     * 
     * @return The list of objects for the given event type
     */
    private EList<IWindowObject> getOrCreateListenerList(WindowEventType type) {
        return listeners.computeIfAbsent(type, t -> EList.newList());
    }    
    //=========
    // Getters
    //=========
    
    public Set<WindowEventType> getRegisteredEventTypes() {
        return listeners.keySet();
    }
    
    public EList<IWindowObject> getAllListeners() {
        EList<IWindowObject> all = EList.newList();
        
        for (var t : listeners.entrySet()) {
            all.addAll(t.getValue());
        }
        
        return all;
    }
    
    public EList<IWindowObject> getListenersForEventType(WindowEventType type) {
        // don't allow null types
        if (type == null) return null;
        
        EList<IWindowObject> objects = EList.newList();
        
        var typeList = listeners.get(type);
        if (typeList != null) objects.addAll(typeList);
        
        return objects;
    }
    
}
