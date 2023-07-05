package envision.engine.windows.windowUtil;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class EObjectGroup {
	
	//--------
	// Fields
	//--------
	
	private EList<IWindowObject> objects = EList.newList();
	private IWindowObject groupParent;
	
	//--------------
	// Constructors
	//--------------
	
	public EObjectGroup() {}
	public EObjectGroup(IWindowObject parentIn) {
		objects.add(parentIn);
		groupParent = parentIn;
	}
	
	public EObjectGroup(IWindowObject parentIn, IWindowObject... children) {
	    this(parentIn);
	    addObject(children);
	}
	
	//---------
	// Methods
	//---------
	
	/** does not accept duplicates */
	public EObjectGroup addObject(IWindowObject... objectIn) {
	    for (var o : objectIn) {
            objects.addIfNotContains(o);
            o.setObjectGroup(this);
        }
		return this;
	}
	
	public EObjectGroup addObjects(EList<IWindowObject> objectsIn) {
		if (objectsIn != null) {
			for (var o : objectsIn) {
				objects.addIfNotContains(o);
				o.setObjectGroup(this);
			}
		}
		return this;
	}
	
	public EObjectGroup removeObject(IWindowObject... objectIn) {
		var it = objects.iterator();
		while (it.hasNext()) {
			for (var o : objectIn) {
				if (o.equals(it.next())) it.remove();
			}
		}
		return this;
	}
	
	public void notifyGroup(ObjectEvent e) {
		objects.forEach((o) -> {
			//System.out.println("group parent: " + e.getEventParent() + " " + o.equals(e.getEventParent()));
			if (!o.equals(e.getEventParent())) {
				o.onGroupNotification(e);
			}
		});
	}
	
	public boolean doAnyHaveFocus() {
		for (var o : objects) {
			if (o.hasFocus()) return true;
		}
		return false;
	}
	
	public boolean isMouseOverAny(int mXIn, int mYIn) {
		for (var o : objects) {
			if (o.isMouseOver()) return true;
		}
		return false;
	}
	
	//---------
	// Getters
	//---------
	
	public EList<IWindowObject> getObjects() { return objects; }
	public IWindowObject getGroupParent() { return groupParent; }
	
	//---------
	// Setters
	//---------
	
	public EObjectGroup setGroupParent(IWindowObject parentIn) { groupParent = parentIn; return this; }
	
}
