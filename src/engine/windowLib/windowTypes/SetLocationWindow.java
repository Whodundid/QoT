package engine.windowLib.windowTypes;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.Box2;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;

//Author: Hunter Bragg

public abstract class SetLocationWindow<E> extends OverlayWindow<E> {
	
	//--------
	// Fields
	//--------
	
	protected BoxList<IWindowObject<?>, Boolean> previousStates = new BoxList();
	
	//---------
	// Methods
	//---------
	
	protected SetLocationWindow hideAllOnRenderer(IWindowObject<?>... exceptionsIn) {
		previousStates.clear();
		EList<IWindowObject<?>> exceptions = new EArrayList<IWindowObject<?>>().addA(exceptionsIn);
		
		for (IWindowObject<?> o : getTopParent().getAllChildren()) {
			if (o.isAlwaysVisible()) continue;
			previousStates.add(o, !o.isHidden());
		}
		
		previousStates.getAVals().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setHidden(true));
		
		return this;
	}
	
	protected SetLocationWindow unideAllOnRenderer(IWindowObject<?>... exceptionsIn) {
		EList<IWindowObject<?>> exceptions = new EArrayList<IWindowObject<?>>().addA(exceptionsIn);
		
		for (Box2<IWindowObject<?>, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getA())) b.getA().setHidden(!b.getB());
		}
		
		return this;
	}
	
}
