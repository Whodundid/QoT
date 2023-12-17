package envision.engine.windows;

import java.util.Deque;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.windows.developerDesktop.taskbar.TaskBar;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowTypes.interfaces.ITopParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.EGui;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.KeyboardType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import envision.engine.windows.windowUtil.windowEvents.events.EventKeyboard;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class StaticTopParent extends EGui {
	
	public static final EList<IWindowObject> EMPTY_OBJECT_LIST = EList.emptyUnmodifiableList();
	
	//=====================
	// Basic Input Methods
	//=====================
	
	/** Notify the focused object that a mouse button was just pressed. */
	public static void mousePressed(ITopParent objIn, int mX, int mY, int button, Deque<EventFocus> focusQueue) {
	    //post an event
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.PRESSED));
		
		final IWindowObject underMouse = objIn.getHighestZObjectUnderMouse();
		final IWindowObject focusedObject = objIn.getFocusedObject();
		final IWindowObject focusLockObject = objIn.getFocusLockObject();
		
		//first check if there is a focusLock
		if (focusLockObject != null) {
			if (underMouse != null) { //if there is, then check if there is actually anything under the cursor
				//allow focus to be passed to the object under the cursor if it is the focusLockObject, a child of the focusLockObject or an EGuiHeader
				if (underMouse.equals(focusLockObject) || underMouse.isChildOf(focusLockObject) || underMouse instanceof WindowHeader) {
					focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MOUSE_PRESS, button, mX, mY));
				}
				else { //otherwise, annoy the user
					if (focusLockObject instanceof IWindowParent p) p.bringToFront();
					focusLockObject.drawFocusLockBorder();
				}
			}
			else { //otherwise, annoy the user
				if (focusLockObject instanceof IWindowParent p) p.bringToFront();
				focusLockObject.drawFocusLockBorder();
			}
		}
		//if there is no lock, check if there was actually an object under the cursor
		else if (underMouse != null) {
			// check if the object is the focused object, if it is, pass the event to it, otherwise, start a focus request
			if (underMouse.equals(focusedObject)) {
				
			    focusedObject.mousePressed(mX, mY, button);
				boolean dclicked = false;
				
				// check if the object was recently pressed and is eligible for a double click event
				if (button == 0) {
					var lastClicked = objIn.getLastClickedChild();
					if (lastClicked == focusedObject) {
						long clickTime = objIn.getLastChildClickTime();
						
						if (System.currentTimeMillis() - clickTime <= 200) {
						    focusedObject.onDoubleClick();
							dclicked = true;
						}
					}
				}
				// check if eligible for middle click event
				else if (button == 2) {
				    focusedObject.onMiddleClick();
				}
				
				objIn.setLastClickedChild(focusedObject);
				if (dclicked) objIn.setLastChildClickTime(0);
				else objIn.setLastChildClickTime(System.currentTimeMillis());
			}
			else focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MOUSE_PRESS, button, mX, mY));
		}
		//there was no lock and there was nothing under the cursor
		else {
			objIn.clearFocusedObject();
		}
	}
	
	/** Notify the focused object that a mouse button was just released. */
	public static void mouseReleased(ITopParent objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.RELEASED)); //post an event
		var underMouse = objIn.getHighestZObjectUnderMouse();
		
		//clear the modifying object if it the modify type was MoveAlreadyClicked -- mainly used for headers to stop moving when the mouse is released
		if (objIn.getModifyingObject() != null && objIn.getModifyType() == ObjectModifyType.MOVE_ALREADY_CLICKED) {
			objIn.setModifyingObject(objIn.getModifyingObject(), ObjectModifyType.NONE);
		}
		
		if (underMouse != null) {
			underMouse.mouseReleased(mX, mY, button);
		}
		
		//pass the event to the focused object, if it exists and if it isn't the parent itself
		//if (objIn.getFocusedObject() != null && objIn.getFocusedObject() != objIn) objIn.getFocusedObject().mouseReleased(mX, mY, button);
		
		//stop resizing windows when the mouse isn't pressed
		if (objIn.getModifyType() == ObjectModifyType.RESIZE) objIn.clearModifyingObject();
		
		//transfer focus back to the defaultFocusObject, if it exists
		if (objIn.getDefaultFocusObject() != null) objIn.getDefaultFocusObject().requestFocus(); 
	}
	
	/** Notify the focused object that the mouse was just dragged. */
	public static void mouseDragged(ITopParent objIn, int mX, int mY, int button, long timeSinceLastClick) {
		var fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) fo.mouseDragged(mX, mY, button, timeSinceLastClick);
	}
	
	/** Notify any objects under the cursor that the mouse was just scrolled. */
	public static void mouseScrolled(ITopParent objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.SCROLLED));
		
		var highest = objIn.getHighestZObjectUnderMouse();
		// check if there are actually any objects under the mouse
		if (highest == null) return;
		
		var obj = highest;
        var p = obj.getWindowParent();
        
        //only scroll the top most window under the mouse
        if (p != null) p.mouseScrolled(change);
        else obj.mouseScrolled(change);
	}
	
	/** Notify the focused object that the keyboard just had a key pressed. */
	public static void keyPressed(ITopParent objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.PRESSED));
		//post a new event too
		var fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) fo.keyPressed(typedChar, keyCode);
	}
	
	/** Notify the focused object that the keyboard just had a key released. */
	public static void keyReleased(ITopParent objIn, char typedChar, int keyCode) {
		var fo = objIn.getFocusedObject();
		//post a new event too
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.RELEASED));
		if (fo != null && fo != objIn) fo.keyReleased(typedChar, keyCode);
	}
	
	//=================
	// Drawing Methods
	//=================

	/** Debug method used to display topParent information in the top left corner of the screen. */
	public static void drawDebugInfo(ITopParent objIn) {
	    if (!Envision.isDebugMode()) return;
	    if (objIn != Envision.getDeveloperDesktop()) return;
	    
        var top = Envision.getActiveTopParent();
        
        double yPos = 40;
        double xPos = 3;
        
        TaskBar bar = Envision.getDeveloperDesktop().getTaskBar();
        if (bar != null && !bar.isHidden() && bar.willBeDrawn()) {
            yPos = bar.endY + 6;
        }
        
        var modObj = (top != null) ? top.getModifyingObject() : null;
        var modType = (top != null) ? top.getModifyType() : null;
        var lastClickObj = (top != null) ? top.getLastClickedChild() : null;
        var lastClickTime = (top != null) ? top.getLastChildClickTime() : null;
        var focusedObj = (top != null) ? top.getFocusedObject() : null;
        var ho = (top != null) ? top.getHighestZObjectUnderMouse() : null;
        var focusLockObj = (top != null) ? top.getFocusLockObject() : null;
        
        String out = "null";
        int zLevel = (ho instanceof IWindowParent wp) ? wp.getZLevel() : -1;
        
        String topParent = "TopParent: " + top;
        String focusedObject = "";
        String focusLockObject = "";
        //String objects = "objs: " + objIn.getObjects();
        String modifyType = "ModifyingObject & type: (" + modObj + " : " + modType + ")";
        String underMouse = "Object under mouse: " + (ho != null ? ho : out) + " " + zLevel;
        String lastClicked = "Last clicked object: " + lastClickObj + " : " + lastClickTime;
        String mousePos = "Mouse pos: (" + Mouse.getMx() + ", " + Mouse.getMy() + ")";
        
        if (objIn.getFocusedObject() instanceof WindowButton b) {
            focusedObject = "FocuedObject: " + (b.getString().isEmpty() ? b : "EGuiButton: " + b.getString());
        }
        else focusedObject = "FocuedObject: " + focusedObj;
        
        if (objIn.getFocusLockObject() instanceof WindowButton b) {
            focusLockObject = "FocusLockObject: " + (b.getString().isEmpty() ? b : "EGuiButton: " + b.getString());
        }
        else focusLockObject = "FocusLockObject: " + focusLockObj;
        
        if (ho != null) out = ho.getClass().getName();
        
        double longestX = strWidth(topParent);
        
        if (strWidth(focusedObject) > longestX)         longestX = strWidth(focusedObject);
        if (strWidth(focusLockObject) > longestX)   longestX = strWidth(focusLockObject);
        //if (stringWidth(objects) > longestX)      longestX = stringWidth(objects);
        if (strWidth(modifyType) > longestX)            longestX = strWidth(modifyType);
        if (strWidth(underMouse) > longestX)            longestX = strWidth(underMouse);
        if (strWidth(lastClicked) > longestX)       longestX = strWidth(lastClicked);
        if (strWidth(mousePos) > longestX)          longestX = strWidth(mousePos);
        
        longestX += 5;
        
        //draw background
        //drawRect(xPos, yPos - 3, longestX + 1, yPos + 141, EColors.black.opacity(100));
        drawRect(xPos + 1, yPos - 2, longestX, yPos + 140, EColors.dgray.opacity(150));
        
        //--------------------------------------------------------------------------------
        
        //draw what the topParent is
        drawString(topParent, xPos + 3, yPos, EColors.pink.opacity(190));
        
        //draw the currently focused object - if it's a button, show that too
        drawString(focusedObject, xPos + 3, yPos + 20, EColors.cyan.opacity(190));
        
        //draw the current focusLockObject - if it's a button, show that too
        drawString(focusLockObject, xPos + 3, yPos + 40, EColors.yellow.opacity(190));
        
        //draw the topParent's current immediate children
        //drawStringWithShadow(objects, 2, yPos + 30, 0x70f3ff);
        
        //draw the topParent's current modifying object and type
        drawString(modifyType, xPos + 3, yPos + 60, EColors.lime.opacity(190));
        
        //draw the highest object currently under the mouse
        drawString(underMouse, xPos + 3, yPos + 80, EColors.changeOpacity(0xffffbb00, 190));
        
        //draw the last clicked object
        drawString(lastClicked, xPos + 3, yPos + 100, EColors.seafoam.opacity(190));
        
        //draw the current mouse position
        drawString(mousePos, xPos + 3, yPos + 120, EColors.lgray.opacity(190));
        
        //draw escape stopper
        //drawStringWithShadow("EscapeStopper: " + objIn.getEscapeStopper(), 2, 72, 0x70f3ff);
	}
	
	//================
	// Object Methods
	//================
	
	/** Returns the object with the highest z level, this could even be the topParent itself. */
	public static IWindowObject getHighestZLevelObject(ITopParent objIn) {
		var objs = objIn.getChildren();
		return (objs.isNotEmpty()) ? objs.getLast() : objIn;
	}
	
	/** Hides all objects that are not pinned to the hud. */
	public static void hideUnpinnedObjects(ITopParent objIn) {
		for (var o : objIn.getCombinedChildren()) {
			if (o.closesWithHud()) o.close();
			else if (o instanceof IWindowParent wp) {
				if (!wp.isPinned()) o.setHidden(true);
			}
			else o.setHidden(true);
		}
	}
	
	/** Hides all objects except for the specified ones. */
	public static void hideAllExcept(ITopParent topIn, IWindowObject exception, IWindowObject... additional) {
		for (var o : topIn.getCombinedChildren()) {
			for (var e : EUtil.add(exception, additional)) {
				if (o != e || o.isChildOf(e)) {
					o.setHidden(true);
				}
			}
		}
	}
	
	/** Makes all objects that were hidden now visible. */
	public static void revealHiddenObjects(ITopParent objIn) {
		objIn.getCombinedChildren().filter(o -> o.isHidden()).forEach(o -> o.setHidden(false));
	}
	
	/** Removes all windows from the topParent that are not pinned. */
	public static void removeUnpinnedObjects(ITopParent objIn) {
		//check in both the current objects and the objects that will be added
		EList<IWindowParent> windows = EList.newList();
		
		for (var o : objIn.getCombinedChildren()) {
			//only windows can be pinned
			if (o instanceof IWindowParent wp) {
				if (!wp.isPinned()) windows.add(wp);
			}
			else {
				var childObjects = objIn.getAllChildren();
				for (int i = childObjects.size() - 1; i >= 0; i--) {
					IWindowObject u = childObjects.get(i);
					u.close();
				}
			}
		}
		
		for (var p : windows) {
			var childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
	}
	
	public static void removeAllObjects(ITopParent objIn) {
		// check in both the current objects and the objects that will be added
		for (var p : objIn.getCombinedChildren()) {
			var childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
	}
	
	/** Returns true if there are any pinned windows within the specified topParent. */
	public static boolean hasPinnedObjects(ITopParent objIn) {
		// check in both the current objects and the objects that will be added
		for (var o : objIn.getCombinedChildren()) {
			// only windows can be pinned
			if (o instanceof IWindowParent wp && wp.isPinned()) return true;
		}
		return false;
	}
	
	//===============
	// Focus Methods
	//===============
	
	/** Removes any object that possesses a focus lock on the specified topParent. */
	public static void clearFocusLockObject(ITopParent objIn) {
		var flo = objIn.getFocusLockObject();
		if (flo != null) flo.onFocusLost(new EventFocus(objIn, flo, FocusType.LOST));
		objIn.setFocusLockObject(null); 
	}
	
	/** Forces the currently focused object, if there is one, to return focus to either the defaultFocusObject, if there is one, or the topParent. */
	public static void clearFocusedObject(ITopParent objIn) {
		var fo = objIn.getFocusedObject();
		var dfo = objIn.getDefaultFocusObject();
		
		if (fo != null && fo != objIn) {
			fo.onFocusLost(new EventFocus(objIn, fo, FocusType.LOST));
			//transfer focus back to the defaultFocusObject or the topParent
			if (dfo != null) objIn.setFocusedObject(dfo);
			else objIn.setFocusedObject(objIn);
		}
		else {
			//if there is not a focused object, transfer focus to either the defaultFocusObject or the topParent
			if (dfo != null) objIn.setFocusedObject(dfo);
			else if (fo != objIn) objIn.setFocusedObject(objIn);
		}
	}
	
	/** Method used to process focus events that are present in the specified topParent. */
	public static void updateFocus(ITopParent objIn, Deque<EventFocus> focusQueue) {
		// remove any lingering focused objects if they are no longer within in the parent
		var children = objIn.getAllChildren();
		if (objIn.getFocusedObject() != null) {
		    if (objIn == Envision.getActiveTopParent()) {
		        //System.out.println(objIn.getFocusedObject());
		        //objIn.close();
		    }
		    if (!children.contains(objIn.getFocusedObject())) {
		        objIn.clearFocusedObject();		        
		    }
		}
		if (objIn.getFocusLockObject() != null && !children.contains(objIn.getFocusLockObject())) objIn.clearFocusLockObject();
		if (objIn.getDefaultFocusObject() != null && !children.contains(objIn.getDefaultFocusObject())) objIn.setDefaultFocusObject(null);
		
		// defining this after we have cleared out old objects
		final IWindowObject focusedObject = objIn.getFocusedObject();
		final IWindowObject focusLockObject = objIn.getFocusLockObject();
		final IWindowObject defaultFocusObject = objIn.getDefaultFocusObject();
		
		// don't process any events if there aren't any to process
		if (focusQueue.isEmpty()) {
		    if (focusedObject == null) {
		        passFocus(objIn, focusedObject, objIn, new EventFocus(objIn, objIn, FocusType.DEFAULT_FOCUS_OBJECT));
		    }
		    return;
		}
		
		EventFocus event = focusQueue.pop();
		// don't care if there's no focus event
        if (event.getFocusObject() == null) return;
		
        IWindowObject obj = event.getFocusObject();
        
        // only allow object which are a part of the parent to request focus from the parent
        if (children.notContains(obj)) {
            // the object is the topParent itself
            passFocus(objIn, focusedObject, objIn, event);
            return;
        }
        
        // check for a focus lock and, if it exists, only allow focus to transfer to headers or the focusLockObject
        if (objIn.doesFocusLockExist()) {
            if (obj.equals(focusLockObject) || obj.isChildOf(focusLockObject) || obj instanceof WindowHeader) {
                passFocus(objIn, focusedObject, obj, event);
            }
            else if (focusedObject != focusLockObject) {
                passFocus(objIn, focusedObject, focusLockObject, new EventFocus(objIn, focusLockObject, FocusType.DEFAULT_FOCUS_OBJECT));
            }
            return;
        }
        
        // if there is already an object in focus, transfer the focus to the requesting object
        if (focusedObject != null) passFocus(objIn, focusedObject, obj, event);
        // otherwise, pass focus to a defaultFocusObject, if there is one
        else if (defaultFocusObject != null) passFocus(objIn, focusedObject, defaultFocusObject, event);
        // else pass it back to the topParent
        else passFocus(objIn, focusedObject, objIn, event);
	}
	
	/** Internal method used to quickly transfer focus from previous object to new object. */
	private static void passFocus(ITopParent par, IWindowObject from, IWindowObject to, EventFocus event) {
		if (from != null) from.onFocusLost(event);
		par.setFocusedObject(to);
		to.onFocusGained(event);
	}
	
	//==============
	// Mouse Checks
	//==============
	
	/** Returns true if the mouse is over any object edge. */
	public static boolean isMouseOnObjEdge(ITopParent objIn) {
		return getEdgeAreaMouseIsOn(objIn) != ScreenLocation.OUT;
	}
	
	/** Returns the ScreenLocation type of any object the mouse is currently over. */
	public static ScreenLocation getEdgeAreaMouseIsOn(ITopParent objIn) {
		//check in both the objects on screen and the objects being added
		for (var o : objIn.getCombinedChildren()) {
			ScreenLocation loc = o.getEdgeSideMouseIsOn();
			if (loc != ScreenLocation.OUT) return loc;
		}
		//otherwise, return out because it wasn't under an object edge
		return ScreenLocation.OUT;
	}
	
	/** Returns true if the cursor is currently inside an EGuiHeader object. */
	public static boolean isMouseInsideHeader(ITopParent objIn) {
		for (var obj : objIn.getAllChildrenUnderMouse()) {
			if (obj instanceof WindowHeader) return true;
		}
		return false;
	}
	
	/** Returns the first window header found under the mouse (if there is one). */
	public static WindowHeader getHeaderUnderMouse(ITopParent objIn) {
		return (WindowHeader) objIn.getAllChildrenUnderMouse().getFirst(o -> o instanceof WindowHeader);
	}
	
	/** Returns the object that has the highest z level under the cursor. */
	public static IWindowObject getHighestZObjectUnderMouse(ITopParent objIn) {
        var objectsUnderMouse = objIn.getAllObjectsUnderMouse();
        
        if (objectsUnderMouse.isEmpty()) return null;
        IWindowObject highestObject = objectsUnderMouse.getLast();
        
        EList<IWindowObject> childrenUnderMouse = getChildrenUnderMouseForObject(highestObject);
        if (childrenUnderMouse.isEmpty()) return highestObject;
        highestObject = childrenUnderMouse.getLast();
        
        return highestObject;
	}
	
    /**
     * Returns a list of all children that are under the mouse cursor for the
     * given object.
     * 
     * @param objIn The parent object
     * 
     * @return the children of the parent object that are under the mouse
     */
	public static EList<IWindowObject> getChildrenUnderMouseForObject(IWindowObject objIn) {
	    // find all children that are visible, we need all of them because all children
	    // may not directly be within the bounds of the parent object.
        EList<IWindowObject> children = objIn.getAllVisibleChildren();
        EList<IWindowObject> underMouse = EList.newList();
        
        for (var o : children) {
            if (isObjectUnderMouse(o)) {
                underMouse.add(o);
            }
        }
	    
	    return underMouse;
	}
	
    /**
     * Returns true if the given object meets any of the possible conditions
     * that constitute it being under the mouse.
     * <p>
     * In order for an object to be under the mouse, it must meet all of the
     * following requirements:
     * <ul>
     *      <li> 1. The object is going to be drawn (not hidden)
     *      <li> 2. The object is clickable or there is at least hover text available for it
     *      <li> 3. If the object is a window, the window is not minimized
     * </ul>
     * <p>
     * In addition to the previous requirements, the possible conditions that
     * permit an object to be declared as 'under the mouse' are as follows:
     * <ul>
     *      <li> 1. The cursor's position is within the object's bounds
     *      <li> 2. The object is a non-maximized, resizable window and the cursor is
     *              on one of the window's edges
     *      <li> 3. The object has a header and the cursor is inside of the header
     *      <li> 4. The object must not be in a minimized parent window
     * </ul>
     * 
     * @param o The object to check for being under the mouse or not
     * 
     * @return true if all requirements and any condition is met for being under the mouse
     */
    public static boolean isObjectUnderMouse(IWindowObject o) {
        if (!o.willBeDrawn()) return false;
        if (!o.isClickable() && o.getHoverText() == null) return false;
        if (o instanceof IWindowParent wp && wp.isMinimized()) return false;
        
        final boolean mouseInside = o.isMouseInside();
        final boolean resizable = o.isResizeable();
        final boolean mouseOnEdge = o.isMouseOnEdge(Mouse.getMx(), Mouse.getMy());
        final boolean onResizableObjectEdge = resizable && mouseOnEdge;
        
        final WindowHeader header = o.getHeader();
        final boolean hasHeader = header != null;
        final boolean inHeader = (hasHeader) ? isObjectUnderMouse(header) : false;
        
        if (o instanceof IWindowParent wp) {
            final boolean isMaximizedTop = wp.getMaximizedPosition() == ScreenLocation.TOP;
            final boolean windowBeResized = onResizableObjectEdge && !isMaximizedTop;
            
            //then check if the mouse is in or around the object if it's resizeable
            if (mouseInside || windowBeResized || inHeader) return true;
        }
        //then check if the mouse is in or around the object if it's resizeable
        else if (mouseInside || onResizableObjectEdge || inHeader) {
            final var wp = o.getWindowParent();
            
            if (wp == null || !wp.isMinimized()) return true;
        }
        
        return false;
	}
	
	/** Returns a list of all objects that currently under the cursor. */
	public static EList<IWindowObject> getAllObjectsUnderMouse(ITopParent objIn) {
	    // if the given top object isn't going to be drawn, then nothing is under the mouse
		if (!objIn.willBeDrawn()) return EMPTY_OBJECT_LIST;
		
		// don't care if resizing -- don't check if an object is being resized
		if (objIn.getModifyType() == ObjectModifyType.RESIZE) return EMPTY_OBJECT_LIST;
		
		// The list to return
		EList<IWindowObject> underMouse = EList.newList();
		
		// find all children
		EList<IWindowObject> children = objIn.getVisibleChildren();
		
		// get all immediately visible children
		for (IWindowObject visibleChild : children) {
		    if (isObjectUnderMouse(visibleChild)) {
                underMouse.add(visibleChild);
            }
		}
		
		return underMouse;
	}
	
 }
