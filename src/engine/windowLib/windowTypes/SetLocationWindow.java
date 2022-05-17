package engine.windowLib.windowTypes;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;

//Author: Hunter Bragg

public abstract class SetLocationWindow<E> extends OverlayWindow<E> {
	
	protected BoxList<IWindowObject<?>, Boolean> previousStates = new BoxList();
	
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
		
		for (Box2<IWindowObject<?>, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getA())) { b.getA().setHidden(!b.getB()); }
		}
		
		return this;
	}
	
}
