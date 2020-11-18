package eWindow.windowTypes;

import eWindow.windowTypes.interfaces.IActionObject;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowTypes.interfaces.IWindowParent;

//Author: Hunter Bragg

public abstract class ActionObject extends WindowObject implements IActionObject {

	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected Object selectedObject = null;
	protected Object storedObject = null;
	protected IWindowObject actionReceiver;
	
	//-------------------------
	//AcitonObject Constructors
	//-------------------------
	
	/** Used for internal or highly specific purposes. */
	protected ActionObject() {}
	/** Instantiates an ActionObject with the given parent object. */
	protected ActionObject(IWindowObject parentIn) {
		actionReceiver = parentIn;
	}
	
	//----------------------
	//WindowObject Overrides
	//----------------------
	
	@Override
	public void init(IWindowObject objIn, double xIn, double yIn) {
		super.init(objIn, xIn, yIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) {
		init(objIn, xIn, yIn, widthIn, heightIn, -1);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn, int objectIdIn) {
		super.init(objIn, xIn, yIn, widthIn, heightIn, objectIdIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		//perform the action if set to run on falling edge
		if (runActionOnRelease) { performAction(); }
	}
	
	//-----------------------
	//IActionObject Overrides
	//-----------------------
	
	@Override
	public void performAction(Object... args) {
		if (actionReceiver != null) {
			IWindowParent p = actionReceiver.getWindowParent();
			if (p != null) { p.bringToFront(); }
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
		
	@Override public IActionObject setStoredObject(Object objIn) { storedObject = objIn; return this; }
	@Override public Object getStoredObject() { return storedObject; }
	@Override public IActionObject setSelectedObject(Object objIn) { selectedObject = objIn; return this; }
	@Override public Object getSelectedObject() { return selectedObject; }
	
}