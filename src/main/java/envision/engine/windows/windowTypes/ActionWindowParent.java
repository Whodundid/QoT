package envision.engine.windows.windowTypes;

import envision.Envision;
import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;

//Author: Hunter Bragg

public abstract class ActionWindowParent extends WindowParent implements IActionObject {

	//========
	// Fields
	//========
	
	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected IWindowObject actionReceiver;
	
	//==============
	// Constructors
	//==============
	
	/** Instantiates this ActionWindowParent with the given parent. */
	protected ActionWindowParent(IWindowObject parentIn) {
		actionReceiver = parentIn;
		windowInstance = this;
		res = Envision.getWindowDims();
	}
	
	//===========================
	// Overrides : IActionObject
	//===========================
	
	@Override
	public void performAction(Object... args) {
		if (actionReceiver != null) {
			EUtil.nullDo(actionReceiver.getWindowParent(), o -> bringToFront());
			actionReceiver.actionPerformed(this, args);
		}
	}
	
	@Override public void press(int button) {}
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public void setRunActionOnPress(boolean value) { runActionOnPress = value; }
	@Override public void setRunActionOnRelease(boolean val) { runActionOnRelease = val; }
	@Override public void setActionReceiver(IWindowObject objIn) { actionReceiver = objIn; }
	@Override public IWindowObject getActionReceiver() { return actionReceiver; }
	
}
