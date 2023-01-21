package envisionEngine.windowLib.windowTypes;

import envisionEngine.windowLib.windowTypes.interfaces.IActionObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowParent;

//Author: Hunter Bragg

public abstract class ActionObject<E> extends WindowObject<E> implements IActionObject<E> {

	//--------
	// Fields
	//--------
	
	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected IWindowObject<?> actionReceiver;
	protected Runnable onPressAction = null;
	
	//--------------
	// Constructors
	//--------------
	
	/** Used for internal or highly specific purposes. */
	protected ActionObject() {}
	/** Instantiates an ActionObject with the given parent object. */
	protected ActionObject(IWindowObject<?> parentIn) {
		actionReceiver = parentIn;
	}
	
	//--------------------------
	// Overrides : WindowObject
	//--------------------------
	
	@Override
	public void init(IWindowObject<?> objIn, Number xIn, Number yIn) {
		super.init(objIn, xIn, yIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IWindowObject<?> objIn, Number xIn, Number yIn, Number widthIn, Number heightIn) {
		super.init(objIn, xIn, yIn, widthIn, heightIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		//perform the action if set to run on falling edge
		if (runActionOnRelease) performAction();
	}
	
	public void setOnPressAction(Runnable action) { onPressAction = action; }
	
	//---------------------------
	// Overrides : IActionObject
	//---------------------------
	
	@Override
	public void performAction(Object... args) {
		if (actionReceiver != null) {
			IWindowParent<?> p = actionReceiver.getWindowParent();
			if (p != null) p.bringToFront();
			actionReceiver.actionPerformed(this, args);
		}
	}
	
	@Override
	public void onPress(int button) {
		if (onPressAction != null) onPressAction.run();
		if (runActionOnPress) performAction(button);
	}
	
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public void setRunActionOnPress(boolean value) { runActionOnPress = value; }
	@Override public void setRunActionOnRelease(boolean val) { runActionOnRelease = val; }
	@Override public void setActionReceiver(IWindowObject<?> objIn) { actionReceiver = objIn; }
	@Override public IWindowObject<?> getActionReceiver() { return actionReceiver; }
	
}
