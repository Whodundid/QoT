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
	private boolean iterating = false;
	
	//--------------
	// Constructors
	//--------------
	
	public ObjectEventHandler(IWindowObject parentIn) {
		parent = parentIn;
	}
	
	//---------
	// Methods
	//---------
	
	public void processEvent(ObjectEvent e) {
		if (parent.getObjectGroup() != null) parent.getObjectGroup().notifyGroup(e);
		sendListenEvent(e);
	}
	
	public void unregisterAllObjects() {
		toBeRemoved.addAll(listeners);
		updateList();
	}
	
	public void registerObject(IWindowObject object) {
		if (object != null && listeners.notContains(object)) {
			toBeAdded.add(object);
		}
		updateList();
	}
	
	public void unregisterObject(IWindowObject object) {
		if (object != null) {
			toBeRemoved.add(object);
		}
		updateList();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void sendListenEvent(ObjectEvent e) {
		iterating = true;
		listeners.forEach(o -> o.onEvent(e));
		iterating = false;
		updateList();
	}
	
	private void updateList() {
		if (iterating) return;
		
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
