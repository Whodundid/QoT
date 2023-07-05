package envision.engine.windows.windowUtil.windowEvents;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class ObjectEventHandler {
	
	//--------
	// Fields
	//--------
	
	private final IWindowObject parent;
	private final EList<IWindowObject> listeners = EList.newList();
	private final EList<IWindowObject> toBeAdded = EList.newList();
	private final EList<IWindowObject> toBeRemoved = EList.newList();
	private volatile boolean iterating = false;
	
	//--------------
	// Constructors
	//--------------
	
	public ObjectEventHandler(IWindowObject parentIn) {
		parent = parentIn;
	}
	
	//---------
	// Methods
	//---------
	
	public synchronized boolean processEvent(ObjectEvent e) {
		if (parent.getObjectGroup() != null) parent.getObjectGroup().notifyGroup(e);
		sendListenEvent(e);
		return !e.isCancelled();
	}
	
	public synchronized void unregisterAllObjects() {
		toBeRemoved.addAll(listeners);
		updateList();
	}
	
	public synchronized void registerObject(IWindowObject object) {
		if (object != null && listeners.notContains(object)) {
			toBeAdded.add(object);
		}
		updateList();
	}
	
	public synchronized void unregisterObject(IWindowObject object) {
		if (object != null) {
			toBeRemoved.add(object);
		}
		updateList();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private synchronized void sendListenEvent(ObjectEvent e) {
		iterating = true;
		listeners.forEach(o -> o.onEvent(e));
		iterating = false;
		updateList();
	}
	
	private void updateList() {
		if (iterating) return;
        // remove old listeners that are closed
        for (var o : listeners) {
            if (o.isClosed()) toBeRemoved.add(o);
        }
        
        if (toBeAdded.isNotEmpty()) {
            listeners.addAll(toBeAdded);
            toBeAdded.clear();
        }
        
        if (toBeRemoved.isNotEmpty()) {
            listeners.removeAll(toBeRemoved);
            toBeRemoved.clear();
        }
	}
	
	//---------
	// Getters
	//---------
	
	public EList<IWindowObject> getListenerObjects() { return listeners; }
	
}
