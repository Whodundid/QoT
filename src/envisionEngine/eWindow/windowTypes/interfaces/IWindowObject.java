package envisionEngine.eWindow.windowTypes.interfaces;

import envisionEngine.eWindow.windowObjects.advancedObjects.header.WindowHeader;
import envisionEngine.eWindow.windowUtil.EObjectGroup;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEvent;
import envisionEngine.eWindow.windowUtil.windowEvents.ObjectEventHandler;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventFocus;
import java.util.function.Consumer;
import util.EUtil;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;
import util.storageUtil.StorageBox;

//Author: Hunter Bragg

/** An interface outlining behavior for WindowObjects. */
public interface IWindowObject {
	
	//----
	//init
	//----
	
	/** Returns true if this object has been fully initialized with all of its values and children. */
	public boolean isInit();
	/** Returns true if initObjects has been fully completed. */
	public boolean isObjectInit();
	/** Internal method used to denote that this object and all of its children have been fully initialized. */
	public void completeInit();
	/** Event fired from the top parent upon being fully added to the parent so that this object can safely initialize all of it's own children. */
	public void initObjects();
	/** Removes all children and re-runs the initObjects method. */
	public void reInitObjects();
	/** Internal event that happens before objects are reinitialized. */
	public void preReInit();
	/** Internal event that happens after objects are reinitialized. */
	public void postReInit();
	/** Event called when this object has actually been added to its parent. */
	public void onAdded();
	
	//---------
	//main draw
	//---------
	
	/** Event fired from the top parent to draw this object. */
	public void drawObject(int mX, int mY);
	/** Event fired from the top parent when the object is drawn for the first time. */
	public void onFirstDraw();
	/** Returns true if this object has been drawn at least once. */
	public boolean hasFirstDraw();
	/** Event fired from this object's pre draw setup to perform cursorImage changes. */
	public void updateCursorImage();
	/** Event fired from the top parent when the mouse has been hovering over this object for a short period of time. */
	public void onMouseHover(int mX, int mY);
	/** Hook that indicates when the object is drawing its hovering layer. */
	public boolean isDrawingHover();
	/** Sets generic mouse hovering background with specified text. */
	public IWindowObject setHoverText(String textIn);
	/** Sets hover text color. */
	public IWindowObject setHoverTextColor(int colorIn);
	/** Gets the hover text. */
	public String getHoverText();
	
	//-------
	//obj ids
	//-------
	
	/** Returns this object's set ID number. */
	public long getObjectID();
	/** Designates this object with the specified ID number. Useful for ordering objects and referencing objects by shorthand calls. */
	public IWindowObject setObjectID(long l);
	/** Returns the name of this object. */
	public String getObjectName();
	/** Sets the name of this object. */
	public IWindowObject setObjectName(String nameIn);
	
	//--------------
	//drawing checks
	//--------------
	
	/** Returns true if this object will be drawn on the next draw cycle. */
	public boolean checkDraw();
	/** Returns true if this object is currently enabled. */
	public boolean isEnabled();
	/** Returns true if this object is visible. */
	public boolean isVisible();
	/** Returns true if this object is hidden when the hud is not drawn. */
	public boolean isHidden();
	/** Returns true if this object will be drawn regardless of it being visible or enabled. */
	public boolean isPersistent();
	/** Returns true if this object's mouse checks are enforced by a boundary. */
	public boolean isBoundaryEnforced();
	/** Returns true if this object is resizing. */
	public boolean isResizing();
	/** Returns true if this object is moving. */
	public boolean isMoving();
	/** Returns true if this object will always be drawn on top. */
	public boolean isAlwaysOnTop();
	/** Set this object's enabled state. */
	public IWindowObject setEnabled(boolean val);
	/** Set this object's visibility. A non-visible object can still run actions if it is still enabled. */
	public IWindowObject setVisible(boolean val);
	/** Sets this object to be drawn regardless of it being visible or enabled. */
	public IWindowObject setPersistent(boolean val);
	/** Sets this object to always be drawn on top. */
	public IWindowObject setAlwaysOnTop(boolean val);
	/** Sets this object as hidden when the hud is not drawn. */
	public IWindowObject setHidden(boolean val);
	
	//----
	//size
	//----
	
	/** Returns true if this object has a header. */
	public boolean hasHeader();
	/** Returns true if this object is resizeable. */
	public boolean isResizeable();
	/** If this object has a header, returns the header object, otherwise returns null. */
	public WindowHeader getHeader();
	/** Returns the minimum width that this object can have. */
	public double getMinWidth();
	/** Returns the minimum height that this object can have. */
	public double getMinHeight();
	/** Returns the maximum width that this object can have. */
	public double getMaxWidth();
	/** Returns the maximum height that this object can have. */
	public double getMaxHeight();
	/** Sets both the minimum width and height for this object. */
	public IWindowObject setMinDims(double widthIn, double heightIn);
	/** Sets both the maximum width and height for this object. */
	public IWindowObject setMaxDims(double widthIn, double heightIn);
	/** Sets the minimum width for this object when resizing. */
	public IWindowObject setMinWidth(double widthIn);
	/** Sets the minimum height for this object when resizing. */
	public IWindowObject setMinHeight(double heightIn);
	/** Sets the maximum width for this object when resizing. */
	public IWindowObject setMaxWidth(double widthIn);
	/** Sets the maximum height for this object when resizing. */
	public IWindowObject setMaxHeight(double heightIn);
	/** Sets whether this object can be resized or not. */
	public IWindowObject setResizeable(boolean val);
	/** Resizes this object by an amount in both the x and y axies, specified by the given Direction. */
	public IWindowObject resize(double xIn, double yIn, ScreenLocation areaIn);
	
	//--------
	//position
	//--------
	
	/** Moves the object by the specified x and y values. Does not move the object to specified coordinates however. Use setPosition() instead. */
	public void move(double newX, double newY);
	/** Returns true if this object's position cannot be modified. */
	public boolean isMoveable();
	/** Moves this object back to it's initial position that it had upon its creation. */
	public IWindowObject resetPosition();
	/** Move this object and all of its children to the specified x and y coordinates. The specified position represents the top left corner of this object.
	    All children will remain in their original positions relative to the parent object. */
	public IWindowObject setPosition(double newX, double newY);
	/** Sets this object's position as unmodifiable. */
	public IWindowObject setMoveable(boolean val);
	/** Specifies this objects position, width, and height using an EDimension object. */
	public IWindowObject setDimensions(EDimension dimIn);
	/** Specifies this object's width and height based on the current starting position. */
	public IWindowObject setDimensions(double widthIn, double heightIn);
	/** Specifies this objects position, width, and height. (x, y, width, height) */
	public IWindowObject setDimensions(double startXIn, double startYIn, double widthIn, double heightIn);
	/** Specifies the position this object will relocate to when its' position is reset. */
	public IWindowObject setInitialPosition(double startXIn, double startYIn);
	/** Returns the position this object will relocate to when reset. */
	public StorageBox<Double, Double> getInitialPosition();
	/** Centers the object around the center of the screen with proper dimensions. */
	public IWindowObject centerObjectWithSize(double widthIn, double heightIn);
	/** Returns the current dimensions of this object. */
	public EDimension getDimensions();
	
	//-------
	//objects
	//-------
	
	/** Checks if this object is a child of the specified object. */
	public boolean isChildOf(IWindowObject objIn);
	/** Adds a child IWindowObject to this object. The object is added before the next draw cycle. 
	 * @param obj TODO*/
	public IWindowObject addObject(IWindowObject obj, IWindowObject... additional);
	/** Removes a child IWindowObject to this object. If this object does not contain the specified child, no action is performed. The object is removed before the next draw cycle. 
	 * @param obj TODO*/
	public IWindowObject removeObject(IWindowObject obj, IWindowObject... additional);
	/** Returns this object's object group, if any. */
	public EObjectGroup getObjectGroup();
	/** Sets this object's object group. */
	public IWindowObject setObjectGroup(EObjectGroup groupIn);
	/** Event fired when any object within the object group fires an event. */
	public void onGroupNotification(ObjectEvent e);
	/** Returns a list of all objects that are directly children of this object. */
	public EArrayList<IWindowObject> getObjects();
	/** Returns a list of all objects that are going to be added on the next draw cycle */
	public EArrayList<IWindowObject> getAddingObjects();
	/** Returns a list of all objects that are going to be removed on the next draw cycle */
	public EArrayList<IWindowObject> getRemovingObjects();
	/** Returns a list of all objects that descend from this parent. */
	public EArrayList<IWindowObject> getAllChildren();
	/** Returns a list of all children from 'getAllChildren()' that are currently under the mouse. */
	public EArrayList<IWindowObject> getAllChildrenUnderMouse();
	/** Returns true if the specified object is a child of the parent or is being added to the parent. */
	public boolean containsObject(IWindowObject object);
	/** Returns true if the specified object type is a child of the parent or is being added to the parent. */
	public <T> boolean containsObject(Class<T> objIn);
	/** Returns a list combining the objects currently within within this object as well as the ones being added. */
	public default EArrayList<IWindowObject> getCombinedObjects() { return EArrayList.combineLists(getObjects(), getAddingObjects()); }
	
	//-------
	//parents
	//-------
	
	/** Returns this object's direct parent object. */
	public IWindowObject getParent();
	/** Sets this object's parent. */
	public IWindowObject setParent(IWindowObject parentIn);
	/** Returns the top most parent object in the parent chain. */
	public ITopParent getTopParent();
	/** Returns the first instance of a WindowParent in the parent chain. */
	public IWindowParent getWindowParent();
	
	//-----
	//focus
	//-----
	
	/** Returns true if this object is the current focus owner in it's top parent object. */
	public boolean hasFocus();
	/** Signals the top parent to transfer focus from this object to the top parent's default focus object on the next draw cycle.
	    If this object has a focus lock set, the lock will be removed and focus will be transfered to the top parent's default focus object on the next draw cycle. */
	public boolean relinquishFocus();
	/** Focus event that is called when this object is given focus from its top parent. */
	public void onFocusGained(EventFocus eventIn);
	/** Focus event that is called when this object loses focus in any way. */
	public void onFocusLost(EventFocus eventIn);
	/** Signals the top parent to transfer focus from this object to the object specified on the next draw cycle. */
	public void transferFocus(IWindowObject objIn);
	/** Used to draw a visible border around an object whose focus is locked. A focus lock does not need to be in place in order for this to be called however. */
	public void drawFocusLockBorder();
	/** Signals the top parent to try transfering focus to this object on the next draw cycle. If another object has a focus lock, this object will not receive focus */ 
	public IWindowObject requestFocus();
	/** Same as the previous request focus but the exact type of focus event can be specified. */
	public IWindowObject requestFocus(FocusType typeIn);
	/** Returns the object that will recieve foucs by default when the base object has foucs transfered to it. */
	public IWindowObject getDefaultFocusObject();
	/** Sets a default focus object for this object. When the main object recieves focus, the top parent will attempt to transfer focus to the specified default focus object. */
	public IWindowObject setDefaultFocusObject(IWindowObject objectIn);
	
	//------------
	//mouse checks
	//------------
	
	/** Returns true if the mouse is on the edge of an object. */
	public boolean isMouseOnObjEdge(int mX, int mY);
	/** Returns the edge type that the mouse is currently hovering over, if any. */
	public ScreenLocation getEdgeAreaMouseIsOn();
	/** Event fired upon the mouse entering this object. */
	public void mouseEntered(int mX, int mY);
	/** Event fired upon the mouse exiting this object. */
	public void mouseExited(int mX, int mY);
	/** Returns true if the mouse is currently inside this object regardless of z-level. If a boundary enforcer is set, this method will return true if the mouse is inside of the the specified boundary. */
	public boolean isMouseInside();
	/** Returns true if the mouse is currently inside this object and that this is the top most object inside of the parent. */
	public boolean isMouseOver();
	/** Specifies a region that this object will adhere to for mouse checks. */
	public IWindowObject setBoundaryEnforcer(EDimension dimIn);
	/** Returns an EDimension object containing the boundary this object is bounded by */
	public EDimension getBoundaryEnforcer();
	/** Returns true if this object can be clicked on. */
	public boolean isClickable();
	/** Specifies if this object can be clicked on. */
	public IWindowObject setClickable(boolean valIn);
	/** Sets this object and every child to be clickable or not. */
	public IWindowObject setEntiretyClickable(boolean val);
	
	//------------
	//basic inputs
	//------------
	
	/** Event fired on every new MouseEvent processed from the topParent's 'handleMouseInput' method which indicates the current position of the mouse on screen. */
	public void parseMousePosition(int mX, int mY);
	/** Event fired when a mouse button is pressed on this object. */
	public void mousePressed(int mX, int mY, int button);
	/** Event fired when the pressed mouse button is released on this object. */
	public void mouseReleased(int mX, int mY, int button);
	/** Event fired evertime the mouse is moved if it had been pressed on this object. */ 
	public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick);
	/** Event fired when the mouse scrolls over this object. The object must be hovering over the object in order for this event to be called. */
	public void mouseScrolled(int change);
	/** Event fired when the mouse has left clicked on this object at least 2 times in quick succession. */
	public void onDoubleClick();
	/** Event fired when a key is pressed on this object. */
	public void keyPressed(char typedChar, int keyCode);
	/** Event fired when a pressed key is released on this object. */
	public void keyReleased(char typedChar, int keyCode);
	
	//------
	//events
	//------
	
	/** Used to send some kind of message to this object. */
	public void sendArgs(Object... args);
	/** Gets the EventHandler. */
	public ObjectEventHandler getEventHandler();
	/** Register an object that listens to this object's events. */
	public IWindowObject registerListener(IWindowObject objIn);
	/** Unregister a listener Object. */
	public IWindowObject unregisterListener(IWindowObject objIn);
	/** Broadcasts an ObjectEvent on this object. */
	public IWindowObject postEvent(ObjectEvent e);
	/** Called on ObjectEvents. */
	public void onEvent(ObjectEvent e);
	
	//action
	
	/** Event called whenever a child IActionObject's action is triggered. */
	public void actionPerformed(IActionObject object, Object... args);
	
	//------------
	//close object
	//------------
	
	/** Returns whether this object can be closed or not. */
	public boolean isCloseable();
	/** Returns true is this object has been closed. */
	public boolean isClosed();
	/** Sets whether this object can be closed or not. */
	public IWindowObject setCloseable(boolean val);
	/** Removes this object and all of it's children from the immeadiate a parent. Removes any present focus locks on this object and returns focus back to the top parent. */
	public void close();
	/** Removes this object and all of it's children from the immeadiate a parent. Removes any present focus locks on this object and returns focus back to the top parent. */
	public void close(boolean recursive);
	/** Returns true if this object will close when the hud closes. */
	public boolean closesWithHud();
	/** Sets this object to close when the hud closes. */
	public IWindowObject setClosesWithHud(boolean val);
	/** Event fired when object is closed. */
	public void onClosed();
	/** Upon closing, this object will attempt to transfer it's focus to the specified object if possible. */
	public IWindowObject setFocusedObjectOnClose(IWindowObject objIn);
	/** Internal method used to indicate that this object will be removed soon. */
	public void setBeingRemoved();
	/** Returns true if this object is currently scheduled to be removed on the next draw cycle. */
	public boolean isBeingRemoved();
	
	//---------------
	//Default setters
	//---------------
	
	public default void setMoveable(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setMoveable(val), obj, objs); }
	public default void setResizeable(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setResizeable(val), obj, objs); }
	public default void setCloseable(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setCloseable(val), obj, objs); }
	public default void setClickable(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setClickable(val), obj, objs); }
	public default void setHidden(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setHidden(val), obj, objs); }
	public default void setEnabled(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setEnabled(val), obj, objs); }
	public default void setVisible(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setVisible(val), obj, objs); }
	public default void setPersistent(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setPersistent(val), obj, objs); }
	public default void setHoverText(String text, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setHoverText(text), obj, objs); }
	
	public static void setMoveableS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setMoveable(val), obj, objs); }
	public static void setResizeableS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setResizeable(val), obj, objs); }
	public static void setCloseableS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setCloseable(val), obj, objs); }
	public static void setClickableS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setClickable(val), obj, objs); }
	public static void setHiddenS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setHidden(val), obj, objs); }
	public static void setEnabledS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setEnabled(val), obj, objs); }
	public static void setVisibleS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setVisible(val), obj, objs); }
	public static void setPersistentS(boolean val, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setPersistent(val), obj, objs); }
	public static void setHoverTextS(String text, IWindowObject obj, IWindowObject... objs) { setVal(o -> o.setHoverText(text), obj, objs); }
	
	public static void setVal(Consumer<? super IWindowObject> action, IWindowObject obj, IWindowObject... objs) {
		EUtil.filterNullForEachA(action, EUtil.add(obj, objs));
	}
	
}
