package envisionEngine.eWindow.windowTypes.interfaces;

import envisionEngine.eWindow.StaticTopParent;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EArrayList;

//Author: Hunter Bragg

/** An interface outlining behavior for Top Level WindowObjects. Top level objects handle drawing, object focus, object manipulation, and inputs. */
public interface ITopParent extends IWindowObject {
	
	//-------
	//drawing
	//-------
	
	/** Event fired when degub info for the EMC hud is to be drawn. */
	public void drawDebugInfo();
	
	//----------
	//draw order
	//----------
	
	/** Specifies a window to be brought to the front on the hud. */
	public ITopParent bringObjectToFront(IWindowParent objIn);
	/** Specifies a window to be sent to the back on the hud. */
	public ITopParent sendObjectToBack(IWindowParent objIn);
	
	//-------------
	//hovering text
	//-------------
	
	/** Sets the object that the mouse is currently hovering over. */
	public ITopParent setHoveringObject(IWindowObject objIn);
	/** Returns the object that the mouse is currently hovering over. */
	public IWindowObject getHoveringObject();
	
	//------------
	//double click
	//------------
	
	/** Specifies the child object that was clicked last by the left moused button. */
	public ITopParent setLastClickedObject(IWindowObject objectIn);
	/** Returns the last clicked child object. */
	public IWindowObject getLastClickedObject();
	/** Sets the time the last child object was clicked. */
	public ITopParent setLastClickTime(long timeIn);
	/** Returns the time the last child object was clicked. */
	public long getLastClickTime();
	
	//-------
	//objects
	//-------
	
	/** Returns the highest child object under the mouse. */
	public default IWindowObject getHighestZLevelObject() { return StaticTopParent.getHighestZLevelObject(this); }
	/** Hides all child objects which are not pinned. */
	public default ITopParent hideUnpinnedObjects() { return StaticTopParent.hideUnpinnedObjects(this); }
	/** Hides all child objects except for the specified exceptions. */
	public default ITopParent hideAllExcept(IWindowObject objIn) { return StaticTopParent.hideAllExcept(this, objIn); }
	/** Reveals all child objects that are hidden. */
	public default ITopParent revealHiddenObjects() { return StaticTopParent.revealHiddenObjects(this); }
	/** Removes all child objects that are not pinned. */
	public default ITopParent removeUnpinnedObjects() { return StaticTopParent.removeUnpinnedObjects(this); }
	/** Removes all child objects on this top parent. */
	public default ITopParent removeAllObjects() { return StaticTopParent.removeAllObjects(this); }
	/** Returns true if there are any child objects that are pinned. */
	public default boolean hasPinnedObjects() { return StaticTopParent.hasPinnedObjects(this); }
	
	//-----
	//focus
	//-----
	
	/** Returns the currently focused object. */
	public IWindowObject getFocusedObject();
	/** Sets the object that will gain focus. The currently focused object will first relinquish its focus and will transfer it to the new one. */
	public ITopParent setFocusedObject(IWindowObject objIn);
	/** Specifies the object that is wanting focus once the current focused object loses focus. */
	public ITopParent setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn);
	/** Returns the object that is holding a focus lock on the HUD. */
	public IWindowObject getFocusLockObject();
	/** Specifies an object that prevents all focus updates on anything that isn't on the current focus lock object or its children. */
	public ITopParent setFocusLockObject(IWindowObject objIn);
	/** Removes the current focus lock object from the HUD. */
	public default ITopParent clearFocusLockObject() { StaticTopParent.clearFocusLockObject(this); return this; }
	/** Returns true if there is a focus lock object. */
	public boolean doesFocusLockExist();
	/** Forces the currently focused object to relinquish it's focus back to the renderer. */
	public default void clearFocusedObject() { StaticTopParent.clearFocusedObject(this); }
	/** Event handler that manages all focus updates for itself as well as all child objects for the top parent. */
	public void updateFocus();
	
	//-------------------
	//object modification
	//-------------------
	
	/** Returns the modify type that the modifying object will be affected by. */
	public ObjectModifyType getModifyType();
	/** Specifies the direction the object to be modified will be resized in. */
	public ITopParent setResizingDir(ScreenLocation areaIn);
	/** Specifies an object to be modified, and the modify type that indicates how it will be modified. */
	public ITopParent setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn);
	/** Specifies the window that will be maximized, what area it will be maximized to, and whether or not the window should reposition around its header's center. */
	public ITopParent setMaximizingWindow(IWindowParent objIn, ScreenLocation side, boolean centerAroundHeader);
	/** Specified the mouse position that the object modification will be based around. */
	public ITopParent setModifyMousePos(int mX, int mY);
	/** Returns the object that is currently being modified. */
	public IWindowObject getModifyingObject();
	/** Returns the window that is currently being maximized. */
	public IWindowParent getMaximizingWindow();
	/** Returns the area that the modifying window's area that it is maximizing around. */
	public ScreenLocation getMaximizingArea();
	/** Returns true if the window being modified should reposition itself around the center of its header. */
	public boolean getMaximizingHeaderCenter();
	/** Clears the object being modified. */
	public ITopParent clearModifyingObject();
	
	//------------
	//mouse checks
	//------------
	
	/** Returns true if the mouse is on the edge of an object. */
	public default boolean isMouseOnObjEdge() { return StaticTopParent.isMouseOnObjEdge(this); }
	/** Returns the edge type that the mouse is currently hovering over, if any. */
	public default ScreenLocation getEdgeAreaMouseIsOn() { return StaticTopParent.getEdgeAreaMouseIsOn(this); }
	/** Returns true if the mouse is inside of any object. */
	public default boolean isMouseInsideObject() { return getHighestZObjectUnderMouse() != null; }
	/** Returns true if the mouse is inside of an EGuiHeader object. */
	public default boolean isMouseInsideHeader() { return StaticTopParent.isMouseInsideHeader(this); }
	/** Returns the objects with this highest z level under the mouse. */
	public default IWindowObject getHighestZObjectUnderMouse() { return StaticTopParent.getHighestZObjectUnderMouse(this); }
	/** Returns a list of all objects underneath the mouse. */
	public default EArrayList<IWindowObject> getAllObjectsUnderMouse() { return StaticTopParent.getAllObjectsUnderMouse(this); }
	
	//-----
	//close
	//-----
	
	/** Specifies an object that, if present, will prevent the escape key from closing the hud. (Use with caution) */
	public ITopParent setEscapeStopper(IWindowObject obj);
	/** Returns the current object that will prevent the escape key from closing the hud. */
	public IWindowObject getEscapeStopper();

}
