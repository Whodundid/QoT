package envision.engine.windows.windowTypes;

import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import qot.QoT;

//Author: Hunter Bragg

public abstract class ActionWindowParent<E> extends WindowParent<E> implements IActionObject<E> {

	//--------
	// Fields
	//--------
	
	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected IWindowObject<?> actionReceiver;
	
	//--------------
	// Constructors
	//--------------
	
	/** Instantiates this ActionWindowParent with the given parent. */
	protected ActionWindowParent(IWindowObject<?> parentIn) {
		actionReceiver = parentIn;
		windowInstance = this;
		res = QoT.getWindowSize();
	}
	
	//---------------------------
	// Overrides : IActionObject
	//---------------------------
	
	@Override
	public void performAction(Object... args) {
		if (actionReceiver != null) {
			EUtil.nullDo(actionReceiver.getWindowParent(), o -> bringToFront());
			actionReceiver.actionPerformed(this, args);
		}
	}
	
	@Override public void onPress(int button) {}
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public void setRunActionOnPress(boolean value) { runActionOnPress = value; }
	@Override public void setRunActionOnRelease(boolean val) { runActionOnRelease = val; }
	@Override public void setActionReceiver(IWindowObject<?> objIn) { actionReceiver = objIn; }
	@Override public IWindowObject<?> getActionReceiver() { return actionReceiver; }
	
}
