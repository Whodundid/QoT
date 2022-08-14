package engine.windowLib.windowUtil.windowEvents;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.EArrayList;

//Author: Hunter Bragg

public class ObjectEventHandler {
	
	//--------
	// Fields
	//--------
	
	private final IWindowObject<?> parent;
	private final EArrayList<IWindowObject<?>> listeners = new EArrayList<>();
	private final EArrayList<IWindowObject<?>> toBeAdded = new EArrayList<>();
	private final EArrayList<IWindowObject<?>> toBeRemoved = new EArrayList<>();
	private boolean iterating = false;
	
	//--------------
	// Constructors
	//--------------
	
	public ObjectEventHandler(IWindowObject<?> parentIn) {
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
	
	public void registerObject(IWindowObject<?> object) {
		if (object != null && listeners.notContains(object)) {
			toBeAdded.add(object);
		}
		updateList();
	}
	
	public void unregisterObject(IWindowObject<?> object) {
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
	
	public EArrayList<IWindowObject<?>> getListenerObjects() { return listeners; }
	
}
