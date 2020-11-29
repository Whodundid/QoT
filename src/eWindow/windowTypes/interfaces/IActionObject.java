package eWindow.windowTypes.interfaces;

//Author: Hunter Bragg

/** An interface outlining behavior for IWindowObjects which can perform actions. */
public interface IActionObject<E> extends IWindowObject {
	
	//-------
	//actions
	//-------
	
	/** Used to notify the action receiver object that this action object is performing its action. */
	public void performAction(Object... args);
	/** Event fired whenever the left mouse button presses this IActionObject. */
	public void onPress();
	/** Returns true if this object will perform its action when pressed by the left mouse button. (rising edge) */
	public boolean runsActionOnPress();
	/** Returns true if this object will perform its action when the left mouse button is released. (falling edge) */
	public boolean runsActionOnRelease();
	/** Sets this object to perform its action on left mouse presses. */
	public IActionObject setRunActionOnPress(boolean val);
	/** Sets this object to perform its action when the left mouse button is released. */
	public IActionObject setRunActionOnRelease(boolean val);
	/** Specifies the object that will receive updates on when actions by this object are performed. */
	public IActionObject setActionReceiver(IWindowObject objIn);
	/** Returns the object that is receiving actions by this object. */
	public IWindowObject getActionReceiver();
	
	//-------
	//objects
	//-------
	
	/** Stores some object or argument to be preserved for future use. */
	public IActionObject setStoredObject(E objIn);
	/** Returns the object or arguemnt currently stored. */
	public E getStoredObject();
	/** Specifies an object that was selected. */
	public IActionObject setSelectedObject(Object objIn);
	/** Returns the object that was selected. */
	public Object getSelectedObject();
	
	//---------------
	//Static Setters
	//---------------
	
	/** Used to specify the action receiver for multiple IActionObjects at once. */
	public static void setActionReceiver(IWindowObject receiver, IActionObject... objects) {
		for (IActionObject o : objects) { o.setActionReceiver(receiver); }
	}
	
}
