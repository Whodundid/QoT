package eWindow.windowTypes;

import eWindow.windowTypes.interfaces.IWindowObject;
import main.Game;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public abstract class SetLocationWindow extends OverlayWindow {
	
	protected StorageBoxHolder<IWindowObject, Boolean> previousStates = new StorageBoxHolder();
	
	protected SetLocationWindow hideAllOnRenderer(IWindowObject... exceptionsIn) {
		previousStates.clear();
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (IWindowObject o : Game.getGameRenderer().getAllChildren()) {
			if (o.isPersistent()) { continue; }
			previousStates.add(o, !o.isHidden());
		}
		
		previousStates.getAVals().stream().filter(o -> exceptions.notContains(o)).forEach(o -> o.setHidden(true));
		
		return this;
	}
	
	protected SetLocationWindow unideAllOnRenderer(IWindowObject... exceptionsIn) {
		EArrayList exceptions = new EArrayList().addA(exceptionsIn);
		
		for (StorageBox<IWindowObject, Boolean> b : previousStates) {
			if (exceptions.notContains(b.getA())) { b.getA().setHidden(!b.getB()); }
		}
		
		return this;
	}
	
}