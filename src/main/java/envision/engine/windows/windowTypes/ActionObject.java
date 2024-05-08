package envision.engine.windows.windowTypes;

import java.util.function.Consumer;

import envision.engine.windows.windowTypes.interfaces.IActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;

//Author: Hunter Bragg

public abstract class ActionObject<T> extends WindowObject implements IActionObject {

	//========
	// Fields
	//========
	
	protected boolean runActionOnPress = false;
	protected boolean runActionOnRelease = false;
	protected IWindowObject actionReceiver;
	protected Runnable onPressAction = null;
	protected Consumer<T> onPressConsumer = null;
	
	//==============
	// Constructors
	//==============
	
	/** Used for internal or highly specific purposes. */
	protected ActionObject() {}
	/** Instantiates an ActionObject with the given parent object. */
	protected ActionObject(IWindowObject parentIn) {
		actionReceiver = parentIn;
	}
	
	//==========================
	// Overrides : WindowObject
	//==========================
	
	@Override
	public void init(IWindowObject objIn, double xIn, double yIn) {
		super.init(objIn, xIn, yIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void init(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) {
		super.init(objIn, xIn, yIn, widthIn, heightIn);
		actionReceiver = objIn;
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		super.mouseReleased(mXIn, mYIn, button);
		
		//perform the action if set to run on falling edge
		if (runActionOnRelease) performAction();
	}
	
	public void setAction(Runnable action) {
	    runActionOnPress = true;
	    onPressAction = action;
	    onPressConsumer = null;
	}
	
	public void setActionWithArg(Consumer<T> action) {
	    runActionOnPress = true;
	    onPressAction = null;
	    onPressConsumer = action;
	}
	
	//===========================
	// Overrides : IActionObject
	//===========================
	
	@Override
	public void performAction(Object... args) {
	    if (actionReceiver == null) return;
	    IWindowParent p = actionReceiver.getWindowParent();
        if (p != null) p.bringToFront();
        actionReceiver.actionPerformed(this, args);
        if (onPressAction != null) onPressAction.run();
        
        if (onPressConsumer != null) {
            T arg = null;
            try {
                if (args.length == 1) arg = (T) args[0];                
            }
            catch (ClassCastException e) {}
            onPressConsumer.accept(arg);
        }
	}
	
	@Override
	public void press(int button) {
		if (runActionOnPress) performAction(button);
	}
	
	@Override public boolean runsActionOnPress() { return runActionOnPress; }
	@Override public boolean runsActionOnRelease() { return runActionOnRelease; }
	@Override public void setRunActionOnPress(boolean value) { runActionOnPress = value; }
	@Override public void setRunActionOnRelease(boolean val) { runActionOnRelease = val; }
	@Override public void setActionReceiver(IWindowObject objIn) { actionReceiver = objIn; }
	@Override public IWindowObject getActionReceiver() { return actionReceiver; }
	
}
