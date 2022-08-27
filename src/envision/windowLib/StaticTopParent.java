package envision.windowLib;

import java.util.Deque;

import envision.inputHandlers.Mouse;
import envision.topOverlay.desktopOverlay.TaskBar;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import envision.windowLib.windowTypes.interfaces.ITopParent;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowParent;
import envision.windowLib.windowUtil.EGui;
import envision.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.KeyboardType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.windowLib.windowUtil.windowEvents.events.EventFocus;
import envision.windowLib.windowUtil.windowEvents.events.EventKeyboard;
import envision.windowLib.windowUtil.windowEvents.events.EventMouse;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.BoxList;
import eutil.datatypes.EArrayList;
import eutil.misc.ScreenLocation;
import game.QoT;

//Author: Hunter Bragg

public class StaticTopParent extends EGui {
	
	//---------------------
	// Basic Input Methods
	//---------------------
	
	/** Notify the focused object that a mouse button was just pressed. */
	public static void mousePressed(ITopParent<?> objIn, int mX, int mY, int button, Deque<EventFocus> focusQueue) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.PRESSED)); //post an event
		var underMouse = objIn.getHighestZObjectUnderMouse(); //get the highest object under the mouse
		
		if (objIn.getFocusLockObject() != null) { //first check if there is a focusLock
			if (underMouse != null) { //if there is, then check if there is actually anything under the cursor
				//allow focus to be passed to the object under the cursor if it is the focusLockObject, a child of the focusLockObject or an EGuiHeader
				if (underMouse.equals(objIn.getFocusLockObject()) || underMouse.isChildOf(objIn.getFocusLockObject()) || underMouse instanceof WindowHeader) {
					focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MOUSE_PRESS, button, mX, mY));
				}
				else { //otherwise, annoy the user
					if (objIn.getFocusLockObject() instanceof IWindowParent<?> p) p.bringToFront();
					objIn.getFocusLockObject().drawFocusLockBorder();
				}
			}
			else { //otherwise, annoy the user
				if (objIn.getFocusLockObject() instanceof IWindowParent<?> p) p.bringToFront();
				objIn.getFocusLockObject().drawFocusLockBorder();
			}
		}
		else if (underMouse != null) { //if there is no lock, check if there was actually an object under the cursor
			//check if the object is the focused object, if it is, pass the event to it, otherwise, start a focus request
			if (underMouse.equals(objIn.getFocusedObject())) {
				
				var focused = objIn.getFocusedObject();
				focused.mousePressed(mX, mY, button);
				
				//check if the object was recently pressed and is eligible for a double click event
				if (button == 0) {
					var lastClicked = objIn.getLastClickedObject();
					if (lastClicked == focused) {
						long clickTime = objIn.getLastClickTime();
						
						if (System.currentTimeMillis() - clickTime <= 400) {
							focused.onDoubleClick();
						}
					}
				}
				
				objIn.setLastClickedObject(focused);
				objIn.setLastClickTime(System.currentTimeMillis());
			}
			else focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MOUSE_PRESS, button, mX, mY));
		}
		else { //there was no lock and there was nothing under the cursor
			objIn.clearFocusedObject();
			if (button == 1) { //open a right click menu if the right mouse button was pressed
				//Envision.displayWindow(new RendererRCM(), CenterType.cursorCorner);
			}
		}
	}
	
	/** Notify the focused object that a mouse button was just released. */
	public static void mouseReleased(ITopParent<?> objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.RELEASED)); //post an event
		
		//clear the modifying object if it the modify type was MoveAlreadyClicked -- mainly used for headers to stop moving when the mouse is released
		if (objIn.getModifyingObject() != null && objIn.getModifyType() == ObjectModifyType.MOVE_ALREADY_CLICKED) {
			objIn.setModifyingObject(objIn.getModifyingObject(), ObjectModifyType.NONE);
		}
		
		//pass the event to the focused object, if it exists and if it isn't the parent itself
		if (objIn.getFocusedObject() != null && objIn.getFocusedObject() != objIn) objIn.getFocusedObject().mouseReleased(mX, mY, button);
		if (objIn.getModifyType() == ObjectModifyType.RESIZE) objIn.clearModifyingObject(); //stop resizing windows when the mouse isn't pressed
		if (objIn.getDefaultFocusObject() != null) objIn.getDefaultFocusObject().requestFocus(); //transfer focus back to the defaultFocusObject, if it exists
	}
	
	/** Notify the focused object that the mouse was just dragged. */
	public static void mouseDragged(ITopParent<?> objIn, int mX, int mY, int button, long timeSinceLastClick) {
		var fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) fo.mouseDragged(mX, mY, button, timeSinceLastClick);
	}
	
	/** Notify any objects under the cursor that the mouse was just scrolled. */
	public static void mouseScrolled(ITopParent<?> objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.SCROLLED));
		
		var highest = objIn.getHighestZObjectUnderMouse();
		//if there are actually any objects under the mouse
		if (highest != null) {
			var obj = highest;
			var p = obj.getWindowParent();
			
			//only scroll the top most window under the mouse
			if (p != null) p.mouseScrolled(change);
			else obj.mouseScrolled(change);
		}
	}
	
	/** Notify the focused object that the keyboard just had a key pressed. */
	public static void keyPressed(ITopParent<?> objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.PRESSED));
		//post a new event too
		var fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) fo.keyPressed(typedChar, keyCode);
	}
	
	/** Notify the focused object that the keyboard just had a key released. */
	public static void keyReleased(ITopParent<?> objIn, char typedChar, int keyCode) {
		var fo = objIn.getFocusedObject();
		//post a new event too
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.RELEASED));
		if (fo != null && fo != objIn) fo.keyReleased(typedChar, keyCode);
	}
	
	//-----------------
	// Drawing Methods
	//-----------------

	/** Debug method used to display topParent information in the top left corner of the screen. */
	public static void drawDebugInfo(ITopParent<?> objIn) {
		if (QoT.isDebugMode() && objIn == QoT.getTopRenderer()) {
			var top = QoT.getActiveTopParent();
			
			double yPos = 40;
			double xPos = 3;
			
			TaskBar bar = QoT.getTopRenderer().getTaskBar();
			if (bar != null && !bar.isHidden() && bar.willBeDrawn()) {
				yPos = bar.endY + 6;
			}
			
			var modObj = (top != null) ? top.getModifyingObject() : null;
			var modType = (top != null) ? top.getModifyType() : null;
			var lastClickObj = (top != null) ? top.getLastClickedObject() : null;
			var lastClickTime = (top != null) ? top.getLastClickTime() : null;
			var focusedObj = (top != null) ? top.getFocusedObject() : null;
			var ho = (top != null) ? top.getHighestZObjectUnderMouse() : null;
			var focusLockObj = (top != null) ? top.getFocusLockObject() : null;
			
			String out = "null";
			int zLevel = (ho instanceof IWindowParent<?> wp) ? wp.getZLevel() : -1;
			
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
			
			int longestX = getStringWidth(topParent);
			
			if (getStringWidth(focusedObject) > longestX) longestX = getStringWidth(focusedObject);
			if (getStringWidth(focusLockObject) > longestX) longestX = getStringWidth(focusLockObject);
			//if (stringWidth(objects) > longestX) { longestX = stringWidth(objects); }
			if (getStringWidth(modifyType) > longestX) longestX = getStringWidth(modifyType);
			if (getStringWidth(underMouse) > longestX) longestX = getStringWidth(underMouse);
			if (getStringWidth(lastClicked) > longestX) longestX = getStringWidth(lastClicked);
			if (getStringWidth(mousePos) > longestX) longestX = getStringWidth(mousePos);
			
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
			
			//--------------------------------------------------------------------------------
			
		}
	}
	
	//----------------
	// Object Methods
	//----------------
	
	/** Returns the object with the highest z level, this could even be the topParent itself. */
	public static IWindowObject<?> getHighestZLevelObject(ITopParent<?> objIn) {
		var objs = objIn.getChildren();
		return (objs.isNotEmpty()) ? objs.getLast() : objIn;
	}
	
	/** Hides all objects that are not pinned to the hud. */
	public static void hideUnpinnedObjects(ITopParent<?> objIn) {
		for (var o : objIn.getCombinedChildren()) {
			if (o.closesWithHud()) o.close();
			else if (o instanceof IWindowParent<?> wp) {
				if (!wp.isPinned()) o.setHidden(true);
			}
			else o.setHidden(true);
		}
	}
	
	/** Hides all objects except for the specified ones. */
	public static void hideAllExcept(ITopParent<?> topIn, IWindowObject<?> exception, IWindowObject<?>... additional) {
		for (var o : topIn.getCombinedChildren()) {
			for (var e : EUtil.add(exception, additional)) {
				if (o != e || o.isChildOf(e)) {
					o.setHidden(true);
				}
			}
		}
	}
	
	/** Makes all objects that were hidden now visible. */
	public static void revealHiddenObjects(ITopParent<?> objIn) {
		objIn.getCombinedChildren().filter(o -> o.isHidden()).forEach(o -> o.setHidden(false));
	}
	
	/** Removes all windows from the topParent that are not pinned. */
	public static void removeUnpinnedObjects(ITopParent<?> objIn) {
		//check in both the current objects and the objects that will be added
		EArrayList<IWindowParent<?>> windows = new EArrayList<>();
		
		for (var o : objIn.getCombinedChildren()) {
			//only windows can be pinned
			if (o instanceof IWindowParent<?> wp) {
				if (!wp.isPinned()) windows.add(wp);
			}
			else {
				var childObjects = objIn.getAllChildren();
				for (int i = childObjects.size() - 1; i >= 0; i--) {
					IWindowObject<?> u = childObjects.get(i);
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
	
	public static void removeAllObjects(ITopParent<?> objIn) {
		//check in both the current objects and the objects that will be added
		for (var p : objIn.getCombinedChildren()) {
			var childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
	}
	
	/** Returns true if there are any pinned windows within the specified topParent. */
	public static boolean hasPinnedObjects(ITopParent<?> objIn) {
		//check in both the current objects and the objects that will be added
		for (var o : objIn.getCombinedChildren()) {
			//only windows can be pinned
			if (o instanceof IWindowParent<?> wp && wp.isPinned()) return true;
		}
		return false;
	}
	
	//---------------
	// Focus Methods
	//---------------
	
	/** Removes any object that possesses a focus lock on the specified topParent. */
	public static void clearFocusLockObject(ITopParent<?> objIn) {
		var flo = objIn.getFocusLockObject();
		if (flo != null) flo.onFocusLost(new EventFocus(objIn, flo, FocusType.LOST));
		objIn.setFocusLockObject(null); 
	}
	
	/** Forces the currently focused object, if there is one, to return focus to either the defaultFocusObject, if there is one, or the topParent. */
	public static void clearFocusedObject(ITopParent<?> objIn) {
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
	public static void updateFocus(ITopParent<?> objIn, Deque<EventFocus> focusQueue) {
		//remove any lingering focused objects if they are no longer within in the parent
		var children = objIn.getAllChildren();
		if (objIn.getFocusedObject() != null && !children.contains(objIn.getFocusedObject())) objIn.clearFocusedObject();
		if (objIn.getFocusLockObject() != null && !children.contains(objIn.getFocusLockObject())) objIn.clearFocusLockObject();
		if (objIn.getDefaultFocusObject() != null && !children.contains(objIn.getDefaultFocusObject())) objIn.setDefaultFocusObject(null);
		
		//don't process any events if there aren't any to process
		if (!focusQueue.isEmpty()) {
			EventFocus event = focusQueue.pop();
			
			if (event.getFocusObject() != null) {
				IWindowObject<?> obj = event.getFocusObject();
				if (children.contains(obj)) { //only allow object which are a part of the parent to request focus from the parent
					
					if (objIn.doesFocusLockExist()) { //check for a focus lock and, if it exists, only allow focus to transfer to headers or the focusLockObject
						if (obj.equals(objIn.getFocusLockObject()) || obj.isChildOf(objIn.getFocusLockObject()) || obj instanceof WindowHeader) {
							passFocus(objIn, objIn.getFocusedObject(), obj, event);
						}
						else if (objIn.getFocusedObject() != objIn.getFocusLockObject()) {
							passFocus(objIn, objIn.getFocusedObject(), objIn.getFocusLockObject(), new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.DEFAULT_FOCUS_OBJECT));
						}
					}
					else {
						//if there is already an object in focus, transfer the focus to the requesting object
						if (objIn.getFocusedObject() != null) passFocus(objIn, objIn.getFocusedObject(), obj, event);
						else {
							//otherwise, pass focus to a defaultFocusObject, if there is one, else pass it back to the topParent
							if (objIn.getDefaultFocusObject() != null) passFocus(objIn, objIn.getFocusedObject(), objIn.getDefaultFocusObject(), event);
							else passFocus(objIn, objIn.getFocusedObject(), objIn, event);
						}
					}
				}
				else { //the object is the topParent itself
					passFocus(objIn, objIn.getFocusedObject(), objIn, event);
				}
			}
		}
		else if (objIn.getFocusedObject() == null) {
			passFocus(objIn, objIn.getFocusedObject(), objIn, new EventFocus(objIn, objIn, FocusType.DEFAULT_FOCUS_OBJECT));
		}
	}
	
	/** Internal method used to quickly transfer focus from old object to new object. */
	private static void passFocus(ITopParent<?> par, IWindowObject<?> from, IWindowObject<?> to, EventFocus event) {
		if (from != null) from.onFocusLost(event);
		par.setFocusedObject(to);
		to.onFocusGained(event);
	}
	
	//--------------
	// Mouse Checks
	//--------------
	
	//mouse checks
	/** Returns true if the mouse is over any object edge. */
	public static boolean isMouseOnObjEdge(ITopParent<?> objIn) {
		return getEdgeAreaMouseIsOn(objIn) != ScreenLocation.OUT;
	}
	
	/** Returns the ScreenLocation type of any object the mouse is currently over. */
	public static ScreenLocation getEdgeAreaMouseIsOn(ITopParent<?> objIn) {
		//check in both the objects on screen and the objects being added
		for (var o : objIn.getCombinedChildren()) {
			ScreenLocation loc = o.getEdgeSideMouseIsOn();
			if (loc != ScreenLocation.OUT) return loc;
		}
		return ScreenLocation.OUT; //otherwise, return out becuse it wasn't under an object edge
	}
	
	/** Returns true if the cursor is currently inside an EGuiHeader object. */
	public static boolean isMouseInsideHeader(ITopParent<?> objIn) {
		for (var obj : objIn.getAllChildrenUnderMouse()) {
			if (obj instanceof WindowHeader) return true;
		}
		return false;
	}
	
	public static WindowHeader getHeaderUnderMouse(ITopParent<?> objIn) {
		return (WindowHeader) EUtil.getFirst(objIn.getAllChildrenUnderMouse(), o -> (o instanceof WindowHeader));
	}
	
	/** Returns the object that has the highest z level under the cursor. */
	public static IWindowObject<?> getHighestZObjectUnderMouse(ITopParent<?> objIn) {
		try {
			BoxList<IWindowObject<?>, EArrayList<IWindowObject<?>>> sortedByParent = new BoxList<>();
			var underMouse = objIn.getAllObjectsUnderMouse();
			
			//first setup the sorted list
			for (int i = objIn.getChildren().size() - 1; i >= 0; i--) {
				sortedByParent.add(objIn.getChildren().get(i), new EArrayList());
			}
			
			//next iterate through each of the objects found under the mouse and add them to the corresponding parents
			for (var o : underMouse) {
				for (int i = 0; i < sortedByParent.size(); i++) {
					var parent = sortedByParent.getA(i);
					if (o.equals(parent) || parent.getAllChildren().contains(o)) sortedByParent.getB(i).add(o);
				}
			}
			
			//next iterate through each of the sorted parent's found objects to see if they are the highest object
			for (var box : sortedByParent) {
				if (box.getB().isEmpty()) continue;
				
				//get the last object (which should be the highest object)
				var objects = box.getB();
				return objects.getLast();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Returns a list of all objects that currently under the cursor. */
	public static EArrayList<IWindowObject<?>> getAllObjectsUnderMouse(ITopParent<?> objIn) {
		EArrayList<IWindowObject<?>> underMouse = new EArrayList<>();
		EArrayList<IWindowObject<?>> children = objIn.getAllChildren();
		
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		
		//don't check if an object is being resized
		if (objIn.getModifyType() != ObjectModifyType.RESIZE) {
			
			for (var o : children) {
				//check if the object can even be selected
				if (o.willBeDrawn() && (o.isClickable() || o.getHoverText() != null)) {
					if (o instanceof IWindowParent<?> wp) {
						//then check if the mouse is in or around the object if it's resizeable
						if (o.isMouseInside() || ((o.isResizeable() && o.isMouseOnEdge(mX, mY)) && !(wp.isMinimized() || wp.getMaximizedPosition() == ScreenLocation.TOP))) {
							underMouse.add(o);
						}
					}
					//then check if the mouse is in or around the object if it's resizeable
					else if (o.isMouseInside() || (o.isResizeable() && o.isMouseOnEdge(mX, mY))) {
						var wp = o.getWindowParent();
						if (wp != null) {
							if (!wp.isMinimized()) underMouse.add(o);
						}
						else underMouse.add(o);
					}
				}
			} //for
		}
		
		return underMouse;
	}
	
 }