package envision.engine.internal.windows.windowTypes;

import java.util.function.Consumer;

import envision.Envision;
import envision.engine.internal.windows.windowTypes.interfaces.IActionObject;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.internal.windows.windowTypes.interfaces.IWindowParent;

// Author: Hunter Bragg

public abstract class ActionWindowParent<T> extends WindowParent implements IActionObject {
    
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
    
    /** Instantiates this ActionWindowParent with the given parent. */
    protected ActionWindowParent(IWindowObject parentIn) {
        actionReceiver = parentIn;
        windowInstance = this;
        res = Envision.getWindowDims();
    }    
    //=========
    // Methods
    //=========
    
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
    
    @Override public void press(int button) {}
    @Override public boolean runsActionOnPress() { return runActionOnPress; }
    @Override public boolean runsActionOnRelease() { return runActionOnRelease; }
    @Override public void setRunActionOnPress(boolean value) { runActionOnPress = value; }
    @Override public void setRunActionOnRelease(boolean val) { runActionOnRelease = val; }
    @Override public void setActionReceiver(IWindowObject objIn) { actionReceiver = objIn; }
    @Override public IWindowObject getActionReceiver() { return actionReceiver; }
    
}
