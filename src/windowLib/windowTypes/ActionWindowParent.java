package windowLib.windowTypes;

import eutil.EUtil;
import main.QoT;
import windowLib.windowTypes.interfaces.IActionObject;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public abstract class ActionWindowParent<E> extends WindowParent<E> implements IActionObject<E> {

	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected IWindowObject<?> actionReceiver;
	
	//-------------------------------
	//ActionWindowParent Constructors
	//-------------------------------
	
	/** Instantiates this ActionWindowParent with the givent parent. */
	protected ActionWindowParent(IWindowObject<?> parentIn) {
		actionReceiver = parentIn;
		windowInstance = this;
		objectInstance = this;
		res = QoT.getWindowSize();
	}
	
	//-----------------------
	//IActionObject Overrides
	//-----------------------
	
	@Override
	public void performAction(Object... args) {
		if (actionReceiver != null) {
			EUtil.nullDo(actionReceiver.getWindowParent(), o -> bringToFront());
			actionReceiver.actionPerformed(this, args);
		}
	}
	
	@Override public void onPress() {}
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public IActionObject<E> setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IActionObject<E> setRunActionOnRelease(boolean val) { runActionOnRelease = val; return this; }
	@Override public IActionObject<E> setActionReceiver(IWindowObject objIn) { actionReceiver = objIn; return this; }
	@Override public IWindowObject<?> getActionReceiver() { return actionReceiver; }
	
}
