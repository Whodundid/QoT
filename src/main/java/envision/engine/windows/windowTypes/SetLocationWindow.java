package envision.engine.windows.windowTypes;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public abstract class SetLocationWindow extends OverlayWindow {
	
	//--------
	// Fields
	//--------
	
	protected BoxList<IWindowObject, Boolean> previousStates = new BoxList<>();
	
	//---------
	// Methods
	//---------
	
	protected SetLocationWindow hideAllOnRenderer(IWindowObject... exceptionsIn) {
		previousStates.clear();
		var exceptions = EList.newList(exceptionsIn);
		
		for (IWindowObject o : getTopParent().getAllChildren()) {
			if (o.isAlwaysVisible()) continue;
			previousStates.add(o, !o.isHidden());
		}
		
		previousStates.getAVals().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setHidden(true));
		
		return this;
	}
	
	protected SetLocationWindow unideAllOnRenderer(IWindowObject... exceptionsIn) {
		var exceptions = EList.newList(exceptionsIn);
		
		for (Box2<IWindowObject, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getA())) {
				b.getA().setHidden(!b.getB());
			}
		}
		
		return this;
	}
	
}
