package envision.engine.windows.windowTypes.interfaces;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.terminal.window.ETerminalWindow;
import envision.engine.windows.StaticTopParent;
import envision.engine.windows.developerDesktop.taskbar.TaskBar;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.OverlayWindow;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import eutil.datatypes.util.EList;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

/**
 * An interface outlining behavior for Top Level WindowObjects. Top
 * level objects handle drawing, object focus, object manipulation,
 * window handling and input distribution.
 */
public interface ITopParent extends IWindowObject {
	
	//=========
	// Drawing
	//=========
	
	/** Event fired when debug info for the QoT top overlay is to be drawn. */
	void drawDebugInfo();
	
	//============
	// Draw Order
	//============
	
	/** Specifies a window to be brought to the front on the hud. */
	void bringObjectToFront(IWindowParent objIn);
	/** Specifies a window to be sent to the back on the hud. */
	void sendObjectToBack(IWindowParent objIn);
	
	//===============
	// Hovering Text
	//===============
	
	/** Sets the object that the mouse is currently hovering over. */
	void setHoveringObject(IWindowObject objIn);
	/** Returns the object that the mouse is currently hovering over. */
	IWindowObject getHoveringObject();
	
	//==============
	// Double Click
	//==============
	
	/** Specifies the child object that was clicked last by the left moused button. */
	void setLastClickedChild(IWindowObject objectIn);
	/** Returns the last clicked child object. */
	IWindowObject getLastClickedChild();
	/** Sets the time the last child object was clicked. */
	void setLastChildClickTime(long timeIn);
	/** Returns the time the last child object was clicked. */
	long getLastChildClickTime();
	
	//=========
	// Objects
	//=========
	
	/** Returns the highest child object under the mouse. */
	default IWindowObject getHighestZLevelObject() { return StaticTopParent.getHighestZLevelObject(this); }
	/** Hides all child objects which are not pinned. */
	default void hideUnpinnedObjects() { StaticTopParent.hideUnpinnedObjects(this); }
	/** Hides all child objects except for the specified exceptions. */
	default void hideAllExcept(IWindowObject objIn) { StaticTopParent.hideAllExcept(this, objIn); }
	/** Reveals all child objects that are hidden. */
	default void revealHiddenObjects() { StaticTopParent.revealHiddenObjects(this); }
	/** Removes all child objects that are not pinned. */
	default void removeUnpinnedObjects() { StaticTopParent.removeUnpinnedObjects(this); }
	/** Removes all child objects on this top parent. */
	default void removeAllObjects() { StaticTopParent.removeAllObjects(this); }
	/** Returns true if there are any child objects that are pinned. */
	default boolean hasPinnedObjects() { return StaticTopParent.hasPinnedObjects(this); }
	
	//=======
	// Focus
	//=======
	
	/** Returns the currently focused object. */
	IWindowObject getFocusedObject();
	/** Sets the object that will gain focus. The currently focused object will first relinquish its focus and will transfer it to the new one. */
	void setFocusedObject(IWindowObject objIn);
	/** Specifies the object that is wanting focus once the current focused object loses focus. */
	void setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn);
	/** Returns the object that is holding a focus lock on the HUD. */
	IWindowObject getFocusLockObject();
	/** Specifies an object that prevents all focus updates on anything that isn't on the current focus lock object or its children. */
	void setFocusLockObject(IWindowObject objIn);
	/** Removes the current focus lock object from the HUD. */
	default void clearFocusLockObject() { StaticTopParent.clearFocusLockObject(this); }
	/** Returns true if there is a focus lock object. */
	boolean doesFocusLockExist();
	/** Forces the currently focused object to relinquish it's focus back to the renderer. */
	default void clearFocusedObject() { StaticTopParent.clearFocusedObject(this); }
	/** Event handler that manages all focus updates for itself as well as all child objects for the top parent. */
	void updateFocus();
	
	//=====================
	// Object modification
	//=====================
	
	/** Returns the modify type that the modifying object will be affected by. */
	ObjectModifyType getModifyType();
	/** Specifies the direction the object to be modified will be QoTized in. */
	void setResizingDir(ScreenLocation areaIn);
	/** Specifies an object to be modified, and the modify type that indicates how it will be modified. */
	void setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn);
	/** Specifies the window that will be maximized, what area it will be maximized to, and whether or not the window should reposition around its header's center. */
	void setMaximizingWindow(IWindowParent objIn, ScreenLocation side, boolean centerAroundHeader);
	/** Specified the mouse position that the object modification will be based around. */
	void setModifyMousePos(int mX, int mY);
	/** Returns the object that is currently being modified. */
	IWindowObject getModifyingObject();
	/** Returns the window that is currently being maximized. */
	IWindowParent getMaximizingWindow();
	/** Returns the area that the modifying window's area that it is maximizing around. */
	ScreenLocation getMaximizingArea();
	/** Returns true if the window being modified should reposition itself around the center of its header. */
	boolean getMaximizingHeaderCenter();
	/** Clears the object being modified. */
	void clearModifyingObject();
	
	//==============
	// Mouse Checks
	//==============
	
	/** Returns true if the mouse is on the edge of an object. */
	default boolean isMouseOnObjEdge() { return StaticTopParent.isMouseOnObjEdge(this); }
	/** Returns the edge type that the mouse is currently hovering over, if any. */
	@Override default ScreenLocation getEdgeSideMouseIsOn() { return StaticTopParent.getEdgeAreaMouseIsOn(this); }
	/** Returns true if the mouse is inside of any object. */
	default boolean isMouseInsideObject() { return getHighestZObjectUnderMouse() != null; }
	/** Returns true if the mouse is inside of an EGuiHeader object. */
	default boolean isMouseInsideHeader() { return StaticTopParent.isMouseInsideHeader(this); }
	/** Returns the objects with this highest z level under the mouse. */
	default IWindowObject getHighestZObjectUnderMouse() { return StaticTopParent.getHighestZObjectUnderMouse(this); }
	/** Returns a list of all objects underneath the mouse. */
	default EList<IWindowObject> getAllObjectsUnderMouse() { return StaticTopParent.getAllObjectsUnderMouse(this); }
	
	//=======
	// Close
	//=======
	
	/** Specifies an object that, if pQoTent, will prevent the escape key from closing the hud. (Use with caution) */
	void setEscapeStopper(IWindowObject obj);
	/** Returns the current object that will prevent the escape key from closing the hud. */
	IWindowObject getEscapeStopper();
	
	//=========
	// Windows
	//=========
	
	default boolean isTerminalOpen() {
		return isWindowOpen(ETerminalWindow.class);
	}
	
	/** Returns true if the specified window parent is open. */
	default <T extends IWindowParent> boolean isWindowOpen(Class<T> windowIn) {
		return (windowIn != null) && getCombinedChildren().stream().anyMatch(o -> o.getClass() == windowIn);
	}
	
	default boolean isWindowOpen(IWindowParent windowIn) {
		return (windowIn != null) && getCombinedChildren().contains(windowIn);
	}
	
	/** Returns a list of all actively drawn window parents. */
	default EList<IWindowParent> getAllActiveWindows() {
		EList<IWindowParent> windows = EList.newList();
		try {
			getCombinedChildren().filterForEach(o -> o instanceof IWindowParent && !o.isBeingRemoved(), w -> windows.add((IWindowParent) w));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return windows;
	}
	
	default ETerminalWindow getTerminalInstance() {
		return getWindowInstance(ETerminalWindow.class);
	}
	
	/** Returns the first active instance of a specified type of window parent. If none are active, null is returned instead. */
	default <T extends IWindowParent> T getWindowInstance(Class<T> windowIn) {
		return (windowIn != null) ? (T) (getAllChildren().filter(o -> o.getClass() == windowIn).getFirst()) : null;
	}
	
	/** Returns a list of all actively drawn window parents of a given type. */
	default <T extends IWindowParent> EList<T> getAllWindowInstances(Class<T> windowIn) {
		EList<T> windows = EList.newList();
		try {
			getCombinedChildren().filterForEach(o -> o.getClass() == windowIn && !o.isBeingRemoved(), w -> windows.add((T) w));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return windows;
	}
	
	/** Reloads all actively drawn windows. */
	default void reloadAllWindows() { getAllActiveWindows().forEach(w -> w.sendArgs("reload")); }
	/** Reloads all actively drawn windows and sends a set of arguments to each. */
	default void reloadAllWindows(Object... args) { getAllActiveWindows().forEach(w -> w.sendArgs("reload", args)); }
	/** Reloads all actively drawn windows of a specific type. */
	default <T extends IWindowParent> void reloadAllWindowInstances(Class<T> windowIn) { getAllWindowInstances(windowIn).forEach(w -> w.sendArgs("reload")); }
	/** Reloads all actively drawn windows of a specific type and sends a set of arguments to each. */
	default <T extends IWindowParent> void reloadAllWindowInstances(Class<T> windowIn, Object... args) { getAllWindowInstances(windowIn).forEach(w -> w.sendArgs("reload", args)); }
	
	/** Displays the specified window parent. */
	default <T extends IWindowParent> T displayWindow(T windowIn) { return displayWindow(windowIn, null, true, false, false, ObjectPosition.SCREEN_CENTER); }
	/** Displays the specified window parent around a specific location on the screen. */
	default <T extends IWindowParent> T displayWindow(T windowIn, ObjectPosition loc) { return displayWindow(windowIn, null, true, false, false, loc); }
	/** Displays the specified window parent and specifies whether focus should be transfered to it. */
	default <T extends IWindowParent> T displayWindow(T windowIn, boolean transferFocus) { return displayWindow(windowIn, null, transferFocus, false, false, ObjectPosition.SCREEN_CENTER); }
	/** Displays the specified window parent where focus transfer properties can be set along with where it is drawn. */
	default <T extends IWindowParent> T displayWindow(T windowIn, boolean transferFocus, ObjectPosition loc) { return displayWindow(windowIn, null, transferFocus, false, false, loc); }
	/** Displays the specified window parent and passes a previous window for history traversal means. */
	default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject) { return displayWindow(windowIn, oldObject, true, true, true, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent, passes a previous window, and sets where this window will be relatively positioned. */
	default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject, ObjectPosition loc) { return displayWindow(windowIn, oldObject, true, true, true, loc); }
	/** Displays the specified window parent with variable arguments. */
	default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject, boolean transferFocus) { return displayWindow(windowIn, oldObject, transferFocus, true, true, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent with variable arguments. */
	default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject, boolean transferFocus, ObjectPosition loc) { return displayWindow(windowIn, oldObject, transferFocus, true, true, loc); }
	/** Displays the specified window parent with variable arguments. */
	default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld) { return displayWindow(windowIn, oldObject, transferFocus, closeOld, true, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent with variable arguments. */
	default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory) { return displayWindow(windowIn, oldObject, transferFocus, closeOld, transferHistory, ObjectPosition.OBJECT_CENTER); }
	/** Displays the specified window parent with variable arguments. */
    default <T extends IWindowParent> T displayWindow(T windowIn, IWindowParent oldObject, boolean transferFocus,
        boolean closeOld, boolean transferHistory, ObjectPosition loc) {
        if (windowIn == null) return null;
        
        // import window history
        if (transferHistory && oldObject != null) {
            oldObject.getWindowHistory().add(oldObject);
            windowIn.setWindowHistory(oldObject.getWindowHistory());
            windowIn.setPinned(oldObject.isPinned());
        }
        
        // initialize the window -- if it's not already
        //        if (!windowIn.isInitialized()) windowIn.initWindow();
        //        addObject(windowIn);
        //        
        //        windowIn.bringToFront();
        //        if (transferFocus) windowIn.requestFocus();
        //        
        //        // position the window
        //        if (loc != ObjectPosition.NONE) setPos(windowIn, oldObject, loc);
        //        if (this == Envision.getTopScreen()) TaskBar.windowOpened(windowIn);
        
        if (!windowIn.isInitialized()) windowIn.initWindow();
        
        // position the window
        if (loc != ObjectPosition.NONE) setPos(windowIn, oldObject, loc);
        if (this == Envision.getDeveloperDesktop()) TaskBar.windowOpened(windowIn);
        
        addObject(windowIn);
        if (transferFocus) windowIn.requestFocus();
        windowIn.bringToFront();
        
        return windowIn;
    }
	
	/**
	 * Helper method used in conjunction with displayWindow that actually
	 * positions the newly created window on the screen.
	 */
	private void setPos(IWindowParent windowIn, IWindowObject objectIn, ObjectPosition typeIn) {
		Dimension_d gDim = windowIn.getDimensions();
		double headerHeight = WindowHeader.defaultHeight;
		
		double tbH = 0.0;
		boolean drawTB = false;
		TaskBar taskbar = null;
		
		if (this == Envision.getDeveloperDesktop()) {
		    boolean hasTB = Envision.getDeveloperDesktop().getTaskBar() != null;
	        taskbar = (hasTB) ? Envision.getDeveloperDesktop().getTaskBar() : null;
	        drawTB = (hasTB && Envision.getDeveloperDesktop().getTaskBar().willBeDrawn());
	        tbH = (drawTB && taskbar != null) ? taskbar.height : 0.0;
		}
		
		double sX = 0.0;
		double sY = 0.0;
		
		switch (typeIn) {
		case SCREEN_CENTER:
			sX = (Envision.getWidth() / 2.0) - (gDim.width / 2.0);
			sY = (Envision.getHeight() / 2.0) - (gDim.height / 2.0);
			break;
		case BOT_LEFT:
			sX = 1;
			sY = Envision.getHeight() - 2.0 - gDim.height;
			break;
		case TOP_LEFT:
			sX = 1.0;
			sY = 2.0 + headerHeight + tbH;
			break;
		case BOT_RIGHT:
			sX = Envision.getWidth() - 1.0 - gDim.width;
			sY = Envision.getHeight() - 2.0 - gDim.height;
			break;
		case TOP_RIGHT:
			sX = Envision.getWidth() - 1.0 - gDim.width;
			sY = 2.0 + headerHeight + tbH;
			break;
		case CURSOR_CENTER:
			sX = (int) (Mouse.getMx() - (gDim.width / 2));
			sY = (int) (Mouse.getMy() - (gDim.height - headerHeight) / 2 + (gDim.height / 7));
			break;
		case CURSOR_CORNER:
			sX = Mouse.getMx();
			sY = ENumUtil.clamp(Mouse.getMy(), 2.0 + headerHeight, Mouse.getMy());
			break;
		case OBJECT_CENTER:
			if (objectIn != null) {
				Dimension_d objDim = objectIn.getDimensions();
				sX = objDim.midX - (gDim.width / 2.0);
				sY = objDim.midY - (gDim.height / 2.0);
			}
			break;
		case OBJECT_CORNER:
			if (objectIn != null) {
				Dimension_d objDim = objectIn.getDimensions();
				sX = objDim.startX;
				sY = objDim.startY;
			}
			break;
		case OBJECT_INDENT:
			if (objectIn != null) {
				Dimension_d objDim = objectIn.getDimensions();
				sX = objDim.startX + 25.0;
				sY = objDim.startY + 25.0;
			}
			break;
		case EXISTING_OBJECT_INDENT:
			EList<? extends IWindowParent> windows = getAllWindowInstances(windowIn.getClass());
//			getCombinedChildren().stream()
//			                     .filter(o -> windowIn.getClass().isInstance(o))
//			                     .filter(o -> !o.isBeingRemoved())
//			                     .forEach(windows::add);
			
			var last = windows.getLast();
			
			if (windows.isEmpty()) {
	            sX = (Envision.getWidth() / 2.0) - (gDim.width / 2.0);
	            sY = (Envision.getHeight() / 2.0) - (gDim.height / 2.0);
			}
			else if (last != null) {
				Dimension_d objDim = last.getDimensions();
				sX = objDim.startX + headerHeight + headerHeight * 0.35;
				sY = objDim.startY + headerHeight + headerHeight * 0.35;
			}
			
			break;
		default: break;
		}
		
		if (!(windowIn instanceof OverlayWindow)) {
			sX = sX < 0.0 ? 4.0 : sX;
			sY = (sY - headerHeight) < 2.0 ? 4.0 + headerHeight : sY;
			sX = sX + gDim.width > Envision.getWidth() ? -4.0 + sX - (sX + gDim.width - Envision.getWidth()) : sX;
			sY = sY + gDim.height > Envision.getHeight() ? -4.0 + sY - (sY + gDim.height - Envision.getHeight()) : sY;
		}
		
		windowIn.setPosition(sX, sY, true);
	}
	
	//===============
	// Drag and Drop
	//===============
	
	/** Sets the object that is being 'dragged and dropped'. */
	void setDragAndDropObject(DragAndDropObject object);
	/** Removes the object being dragged and dropped. */
	void clearDragAndDropObject();
	/** Returns the object currently being dragged and dropped. */
	DragAndDropObject getDragAndDropObject();

}
