package engine.windowLib.windowTypes.interfaces;

import engine.QoT;
import engine.input.Mouse;
import engine.windowLib.StaticTopParent;
import engine.windowLib.windowTypes.OverlayWindow;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowUtil.ObjectPosition;
import engine.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

/** An interface outlining behavior for Top Level WindowObjects. Top level objects handle drawing, object focus, object manipulation, window handling and input distribution. */
public interface ITopParent<E> extends IWindowObject<E> {
	
	//-------
	//drawing
	//-------
	
	/** Event fired when degub info for the EMC hud is to be drawn. */
	public void drawDebugInfo();
	
	//----------
	//draw order
	//----------
	
	/** Specifies a window to be brought to the front on the hud. */
	public ITopParent<E> bringObjectToFront(IWindowParent<?> objIn);
	/** Specifies a window to be sent to the back on the hud. */
	public ITopParent<E> sendObjectToBack(IWindowParent<?> objIn);
	
	//-------------
	//hovering text
	//-------------
	
	/** Sets the object that the mouse is currently hovering over. */
	public ITopParent<E> setHoveringObject(IWindowObject<?> objIn);
	/** Returns the object that the mouse is currently hovering over. */
	public IWindowObject<?> getHoveringObject();
	
	//------------
	//double click
	//------------
	
	/** Specifies the child object that was clicked last by the left moused button. */
	public ITopParent<E> setLastClickedObject(IWindowObject<?> objectIn);
	/** Returns the last clicked child object. */
	public IWindowObject<?> getLastClickedObject();
	/** Sets the time the last child object was clicked. */
	public ITopParent<E> setLastClickTime(long timeIn);
	/** Returns the time the last child object was clicked. */
	public long getLastClickTime();
	
	//-------
	//objects
	//-------
	
	/** Returns the highest child object under the mouse. */
	public default IWindowObject<?> getHighestZLevelObject() { return StaticTopParent.getHighestZLevelObject(this); }
	/** Hides all child objects which are not pinned. */
	public default ITopParent<E> hideUnpinnedObjects() { StaticTopParent.hideUnpinnedObjects(this); return this; }
	/** Hides all child objects except for the specified exceptions. */
	public default ITopParent<E> hideAllExcept(IWindowObject<?> objIn) { StaticTopParent.hideAllExcept(this, objIn); return this; }
	/** Reveals all child objects that are hidden. */
	public default ITopParent<E> revealHiddenObjects() { StaticTopParent.revealHiddenObjects(this); return this; }
	/** Removes all child objects that are not pinned. */
	public default ITopParent<E> removeUnpinnedObjects() { StaticTopParent.removeUnpinnedObjects(this); return this; }
	/** Removes all child objects on this top parent. */
	public default ITopParent<E> removeAllObjects() { StaticTopParent.removeAllObjects(this); return this; }
	/** Returns true if there are any child objects that are pinned. */
	public default boolean hasPinnedObjects() { return StaticTopParent.hasPinnedObjects(this); }
	
	//-----
	//focus
	//-----
	
	/** Returns the currently focused object. */
	public IWindowObject<?> getFocusedObject();
	/** Sets the object that will gain focus. The currently focused object will first relinquish its focus and will transfer it to the new one. */
	public ITopParent<E> setFocusedObject(IWindowObject<?> objIn);
	/** Specifies the object that is wanting focus once the current focused object loses focus. */
	public ITopParent<E> setObjectRequestingFocus(IWindowObject<?> objIn, FocusType typeIn);
	/** Returns the object that is holding a focus lock on the HUD. */
	public IWindowObject<?> getFocusLockObject();
	/** Specifies an object that prevents all focus updates on anything that isn't on the current focus lock object or its children. */
	public ITopParent<E> setFocusLockObject(IWindowObject<?> objIn);
	/** Removes the current focus lock object from the HUD. */
	public default ITopParent<E> clearFocusLockObject() { StaticTopParent.clearFocusLockObject(this); return this; }
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
	/** Specifies the direction the object to be modified will be QoTized in. */
	public ITopParent<E> setResizingDir(ScreenLocation areaIn);
	/** Specifies an object to be modified, and the modify type that indicates how it will be modified. */
	public ITopParent<E> setModifyingObject(IWindowObject<?> objIn, ObjectModifyType typeIn);
	/** Specifies the window that will be maximized, what area it will be maximized to, and whether or not the window should reposition around its header's center. */
	public ITopParent<E> setMaximizingWindow(IWindowParent<?> objIn, ScreenLocation side, boolean centerAroundHeader);
	/** Specified the mouse position that the object modification will be based around. */
	public ITopParent<E> setModifyMousePos(int mX, int mY);
	/** Returns the object that is currently being modified. */
	public IWindowObject<?> getModifyingObject();
	/** Returns the window that is currently being maximized. */
	public IWindowParent<?> getMaximizingWindow();
	/** Returns the area that the modifying window's area that it is maximizing around. */
	public ScreenLocation getMaximizingArea();
	/** Returns true if the window being modified should reposition itself around the center of its header. */
	public boolean getMaximizingHeaderCenter();
	/** Clears the object being modified. */
	public ITopParent<E> clearModifyingObject();
	
	//------------
	//mouse checks
	//------------
	
	/** Returns true if the mouse is on the edge of an object. */
	public default boolean isMouseOnObjEdge() { return StaticTopParent.isMouseOnObjEdge(this); }
	/** Returns the edge type that the mouse is currently hovering over, if any. */
	public default ScreenLocation getEdgeSideMouseIsOn() { return StaticTopParent.getEdgeAreaMouseIsOn(this); }
	/** Returns true if the mouse is inside of any object. */
	public default boolean isMouseInsideObject() { return getHighestZObjectUnderMouse() != null; }
	/** Returns true if the mouse is inside of an EGuiHeader object. */
	public default boolean isMouseInsideHeader() { return StaticTopParent.isMouseInsideHeader(this); }
	/** Returns the objects with this highest z level under the mouse. */
	public default IWindowObject<?> getHighestZObjectUnderMouse() { return StaticTopParent.getHighestZObjectUnderMouse(this); }
	/** Returns a list of all objects underneath the mouse. */
	public default EArrayList<IWindowObject<?>> getAllObjectsUnderMouse() { return StaticTopParent.getAllObjectsUnderMouse(this); }
	
	//-----
	//close
	//-----
	
	/** Specifies an object that, if pQoTent, will prevent the escape key from closing the hud. (Use with caution) */
	public ITopParent<E> setEscapeStopper(IWindowObject<?> obj);
	/** Returns the current object that will prevent the escape key from closing the hud. */
	public IWindowObject<?> getEscapeStopper();
	
	//---------
	// Windows
	//---------
	
	/** Returns true if the specified window parent is open. */
	public default <T extends WindowParent> boolean isWindowOpen(Class<T> windowIn) {
		return (windowIn != null) ? getCombinedObjects().stream().anyMatch(o -> o.getClass() == windowIn) : false;
	}
	
	/** Returns a list of all actively drawn window parents. */
	public default EArrayList<WindowParent> getAllActiveWindows() {
		EArrayList<WindowParent> windows = new EArrayList();
		try {
			getCombinedObjects().filterForEach(o -> WindowParent.class.isInstance(o) && !o.isBeingRemoved(), w -> windows.add((WindowParent) w));
		}
		catch (Exception e) { e.printStackTrace(); }
		return windows;
	}
	
	/** Returns the first active instance of a specified type of window parent. If none are active, null is returned instead. */
	public default <T extends WindowParent> WindowParent getWindowInstance(Class<T> windowIn) {
		return (windowIn != null) ? (WindowParent) (getAllChildren().filter(o -> o.getClass() == windowIn).getFirst()) : null;
	}
	
	/** Returns a list of all actively drawn window parents of a given type. */
	public default <T extends WindowParent> EArrayList<T> getAllWindowInstances(Class<T> windowIn) {
		EArrayList<T> windows = new EArrayList();
		try {
			getCombinedObjects().filterForEach(o -> o.getClass() == windowIn && !o.isBeingRemoved(), w -> windows.add((T) w));
		}
		catch (Exception e) { e.printStackTrace(); }
		return windows;
	}
	
	/** Reloads all actively drawn windows. */
	public default void reloadAllWindows() { getAllActiveWindows().forEach(w -> w.sendArgs("Reload")); }
	/** Reloads all actively drawn windows and sends a set of arguments to each. */
	public default void reloadAllWindows(Object... args) { getAllActiveWindows().forEach(w -> w.sendArgs("Reload", args)); }
	/** Reloads all actively drawn windows of a specific type. */
	public default <T extends WindowParent> void reloadAllWindowInstances(Class<T> windowIn) { getAllWindowInstances(windowIn).forEach(w -> w.sendArgs("Reload")); }
	/** Reloads all actively drawn windows of a specific type and sends a set of arguments to each. */
	public default <T extends WindowParent> void reloadAllWindowInstances(Class<T> windowIn, Object... args) { getAllWindowInstances(windowIn).forEach(w -> w.sendArgs("Reload", args)); }
	
	/** Displays the specified window parent. */
	public default IWindowParent displayWindow(IWindowParent windowIn) { return displayWindow(windowIn, null, true, false, false, ObjectPosition.SCREEN_CENTER); }
	/** Displays the specified window parent around a specific location on the screen. */
	public default IWindowParent displayWindow(IWindowParent windowIn, ObjectPosition loc) { return displayWindow(windowIn, null, true, false, false, loc); }
	/** Displays the specified window parent and specifies whether focus should be transfered to it. */
	public default IWindowParent displayWindow(IWindowParent windowIn, boolean transferFocus) { return displayWindow(windowIn, null, transferFocus, false, false, ObjectPosition.SCREEN_CENTER); }
	/** Displays the specified window parent where focus transfer properties can be set along with where it is drawn. */
	public default IWindowParent displayWindow(IWindowParent windowIn, boolean transferFocus, ObjectPosition loc) { return displayWindow(windowIn, null, transferFocus, false, false, loc); }
	/** Displays the specified window parent and passes a previous window for history traversal means. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject) { return displayWindow(windowIn, oldObject, true, true, true, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent, passes a previous window, and sets where this window will be relatively positioned. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject, ObjectPosition loc) { return displayWindow(windowIn, oldObject, true, true, true, loc); }
	/** Displays the specified window parent with variable arguments. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus) { return displayWindow(windowIn, oldObject, transferFocus, true, true, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent with variable arguments. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, ObjectPosition loc) { return displayWindow(windowIn, oldObject, transferFocus, true, true, loc); }
	/** Displays the specified window parent with variable arguments. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld) { return displayWindow(windowIn, oldObject, transferFocus, closeOld, true, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent with variable arguments. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory) { return displayWindow(windowIn, oldObject, transferFocus, closeOld, transferHistory, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent with variable arguments. */
	public default IWindowParent displayWindow(IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory, ObjectPosition loc) {
		if (windowIn != null) {
			
			//import window history
			if (transferHistory && oldObject != null) {
				IWindowParent old = (IWindowParent) oldObject;
				old.getWindowHistory().add(old);
				windowIn.setWindowHistory(old.getWindowHistory());
				windowIn.setPinned(old.isPinned());
			}
			
			//initialize the window -- if it's not already
			if (!windowIn.isInit()) windowIn.initWindow();
			windowIn.setObjectID(StaticTopParent.getNextPID());
			
			//position and add the window
			if (loc != ObjectPosition.NONE) setPos(windowIn, oldObject, loc);
			addObject(windowIn);
			windowIn.bringToFront();
			if (transferFocus) windowIn.requestFocus();
		}
		return windowIn;
	}
	
	/** Helper method used in conjunction wth displayWindow that actually positions the newley created window on the screen. */
	private void setPos(IWindowParent windowIn, IWindowObject objectIn, ObjectPosition typeIn) {
		EDimension gDim = windowIn.getDimensions();
		double headerHeight = windowIn.hasHeader() ? windowIn.getHeader().height : 0;
		
		int sX = 0;
		int sY = 0;
		
		switch (typeIn) {
		case SCREEN_CENTER:
			sX = (int) ((QoT.getWidth() / 2) - (gDim.width / 2));
			sY = (int) ((QoT.getHeight() / 2) - (gDim.height / 2));
			break;
		case BOT_LEFT:
			sX = 1;
			sY = (int) (QoT.getHeight() - 2 - gDim.height);
			break;
		case TOP_LEFT:
			sX = 1;
			sY = 2;
			break;
		case BOT_RIGHT:
			sX = (int) (QoT.getWidth() - 1 - gDim.width);
			sY = (int) (QoT.getHeight() - 2 - gDim.height);
			break;
		case TOP_RIGHT:
			sX = (int) (QoT.getWidth() - 1 - gDim.width);
			sY = 2;
			break;
		case CURSOR_CENTER:
			sX = (int) (Mouse.getMx() - (gDim.width / 2));
			sY = (int) (Mouse.getMy() - (gDim.height - headerHeight) / 2 + (gDim.height / 7));
			break;
		case CURSOR_CORNER:
			sX = Mouse.getMx();
			sY = Mouse.getMy();
			break;
		case OBJECT_CENTER:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = (int) (objDim.midX - (gDim.width / 2));
				sY = (int) (objDim.midY - (gDim.height / 2));
			}
			break;
		case OBJECT_CORNER:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = (int) objDim.startX;
				sY = (int) objDim.startY;
			}
			break;
		case OBJECT_INDENT:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = (int) (objDim.startX + 25);
				sY = (int) (objDim.startY + 25);
			}
			break;
		case EXISTING_OBJECT_INDENT:
			EArrayList<WindowParent> windows = new EArrayList();
			getAllChildren().stream().filter(o -> windowIn.getClass().isInstance(o)).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((WindowParent) w));
			
			if (windows.isNotEmpty()) {
				if (windows.get(0) != null) {
					EDimension objDim = windows.get(0).getDimensions();
					sX = (int) (objDim.startX + 25);
					sY = (int) (objDim.startY + 25);
				}
			}
			
			break;
		default: break;
		}
		
		if (!(windowIn instanceof OverlayWindow)) {
			sX = sX < 0 ? 4 : sX;
			sY = (int) ((sY - headerHeight) < 2 ? 4 + headerHeight : sY);
			sX = (int) (sX + gDim.width > QoT.getWidth() ? -4 + sX - (sX + gDim.width - QoT.getWidth()) : sX);
			sY = (int) (sY + gDim.height > QoT.getHeight() ? -4 + sY - (sY + gDim.height - QoT.getHeight()) : sY);
		}
		
		windowIn.setPosition(sX, sY);
	}

}
