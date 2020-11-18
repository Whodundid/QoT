package eWindow.windowTypes;

import eWindow.windowTypes.interfaces.IActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import main.Game;
import util.EUtil;

//Author: Hunter Bragg

public abstract class ActionWindowParent extends WindowParent implements IActionObject {

	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected Object storredObject = null;
	protected Object selectedObject = null;
	protected IWindowObject actionReceiver;
	
	//-------------------------------
	//ActionWindowParent Constructors
	//-------------------------------
	
	/** Instantiates this ActionWindowParent with the givent parent. */
	protected ActionWindowParent(IWindowObject parentIn) {
		actionReceiver = parentIn;
		windowInstance = this;
		objectInstance = this;
		res = Game.getWindowSize();
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
	@Override public IActionObject setRunActionOnPress(boolean value) { runActionOnPress = value; return this; }
	@Override public IActionObject setRunActionOnRelease(boolean val) { runActionOnRelease = val; return this; }
	@Override public IActionObject setActionReceiver(IWindowObject objIn) { actionReceiver = objIn; return this; }
	@Override public IWindowObject getActionReceiver() { return actionReceiver; }
	
	@Override public IActionObject setStoredObject(Object objIn) { storredObject = objIn; return this; }
	@Override public Object getStoredObject() { return storredObject; }
	@Override public IActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
	
}