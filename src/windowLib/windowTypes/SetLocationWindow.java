package windowLib.windowTypes;

import storageUtil.EArrayList;
import storageUtil.StorageBox;
import storageUtil.StorageBoxHolder;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public abstract class SetLocationWindow<E> extends OverlayWindow<E> {
	
	protected StorageBoxHolder<IWindowObject<?>, Boolean> previousStates = new StorageBoxHolder();
	
	protected SetLocationWindow hideAllOnRenderer(IWindowObject<?>... exceptionsIn) {
		previousStates.clear();
		EArrayList exceptions = new EArrayList().add(exceptionsIn);
		
		for (IWindowObject<?> o : getTopParent().getAllChildren()) {
			if (o.isPersistent()) { continue; }
			previousStates.add(o, !o.isHidden());
		}
		
		previousStates.getAVals().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setHidden(true));
		
		return this;
	}
	
	protected SetLocationWindow unideAllOnRenderer(IWindowObject<?>... exceptionsIn) {
		EArrayList exceptions = new EArrayList().add(exceptionsIn);
		
		for (StorageBox<IWindowObject<?>, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getA())) { b.getA().setHidden(!b.getB()); }
		}
		
		return this;
	}
	
}
