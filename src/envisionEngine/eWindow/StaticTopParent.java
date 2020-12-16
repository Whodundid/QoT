package envisionEngine.eWindow;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowObjects.advancedObjects.header.WindowHeader;
import envisionEngine.eWindow.windowTypes.interfaces.ITopParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowParent;
import envisionEngine.eWindow.windowUtil.EGui;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.KeyboardType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.MouseType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventFocus;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventKeyboard;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventMouse;
import envisionEngine.input.Keyboard;
import envisionEngine.input.Mouse;
import java.util.Deque;
import main.Game;
import util.EUtil;
import util.renderUtil.EColors;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EArrayList;
import util.storageUtil.StorageBox;
import util.storageUtil.StorageBoxHolder;

//Author: Hunter Bragg

public class StaticTopParent extends EGui {

	//-----------------------------------
	//StaticTopParent Basic Input Methods
	//-----------------------------------
	
	/** Notify the focused object that a mouse button was just pressed. */
	public static void mousePressed(ITopParent<?> objIn, int mX, int mY, int button, Deque<EventFocus> focusQueue) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.PRESSED)); //post an event
		IWindowObject<?> underMouse = objIn.getHighestZObjectUnderMouse(); //get the highest object under the mouse
		
		if (objIn.getFocusLockObject() != null) { //first check if there is a focusLock
			if (underMouse != null) { //if there is, then check if there is actually anything under the cursor
				//allow focus to be passed to the object under the cursor if it is the focusLockObject, a child of the focusLockObject or an EGuiHeader
				if (underMouse.equals(objIn.getFocusLockObject()) || underMouse.isChildOf(objIn.getFocusLockObject()) || underMouse instanceof WindowHeader) {
					focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY));
				}
				else { //otherwise, annoy the user
					if (objIn.getFocusLockObject() instanceof IWindowParent<?>) { ((IWindowParent<?>) objIn.getFocusLockObject()).bringToFront(); }
					objIn.getFocusLockObject().drawFocusLockBorder();
				}
			}
			else { //otherwise, annoy the user
				if (objIn.getFocusLockObject() instanceof IWindowParent<?>) { ((IWindowParent<?>) objIn.getFocusLockObject()).bringToFront(); }
				objIn.getFocusLockObject().drawFocusLockBorder();
			}
		}
		else if (underMouse != null) { //if there is no lock, check if there was actually an object under the cursor
			
			//check if the object is the focused object, if it is, pass the event to it, otherwise, start a focus request
			if (underMouse.equals(objIn.getFocusedObject())) {
				
				IWindowObject<?> focused = objIn.getFocusedObject();
				focused.mousePressed(mX, mY, button);
				
				//check if the object was recently pressed and is elligible for a double click event
				if (button == 0) {
					IWindowObject<?> lastClicked = objIn.getLastClickedObject();
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
			else { focusQueue.add(new EventFocus(objIn, underMouse, FocusType.MousePress, button, mX, mY)); }
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
		if (objIn.getModifyingObject() != null && objIn.getModifyType() == ObjectModifyType.MoveAlreadyClicked) {
			objIn.setModifyingObject(objIn.getModifyingObject(), ObjectModifyType.None);
		}
		
		//pass the event to the focused object, if it exists and if it isn't the parent itself
		if (objIn.getFocusedObject() != null && objIn.getFocusedObject() != objIn) { objIn.getFocusedObject().mouseReleased(mX, mY, button); }
		if (objIn.getModifyType() == ObjectModifyType.Resize) { objIn.clearModifyingObject(); } //stop resizing windows when the mouse isn't pressed
		if (objIn.getDefaultFocusObject() != null) { objIn.getDefaultFocusObject().requestFocus(); } //transfer focus back to the defaultFocusObject, if it exists
	}
	
	/** Notify the focused object that the mouse was just dragged. */
	public static void mouseDragged(ITopParent<?> objIn, int mX, int mY, int button, long timeSinceLastClick) {
		IWindowObject<?> fo = objIn.getFocusedObject();
		if (fo != null && fo != objIn) { fo.mouseDragged(mX, mY, button, timeSinceLastClick); }
	}
	
	/** Notify any objects under the cursor that the mouse was just scrolled. */
	public static void mouseScrolled(ITopParent<?> objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.SCROLLED));
		
		if (objIn.getHighestZObjectUnderMouse() != null) { //if there are actually any objects under the mouse
			IWindowObject<?> obj = objIn.getHighestZObjectUnderMouse();
			IWindowParent<?> p = obj.getWindowParent();
			
			//only scroll the top most window under the mouse
			if (p != null) { p.mouseScrolled(change); }
			else { obj.mouseScrolled(change); }
		}
		else { //if there were no objects under the mouse, scroll the chat
			
		}
	}
	
	/** Notify the focused object that the keyboard just had a key pressed. */
	public static void keyPressed(ITopParent<?> objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed)); //post a new event
		IWindowObject<?> fo = objIn.getFocusedObject();
			
		if (fo != null && fo != objIn) { fo.keyPressed(typedChar, keyCode); }
		if (fo == null || fo == objIn) {
			
		}
	}
	
	/** Notify the focused object that the keyboard just had a key released. */
	public static void keyReleased(ITopParent<?> objIn, char typedChar, int keyCode) {
		IWindowObject<?> fo = objIn.getFocusedObject();
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released)); //post a new event too
		if (fo != null && fo != objIn) { fo.keyReleased(Keyboard.getLastChar(), Keyboard.getLastKey()); }
	}
	
	//-------------------------------
	//StaticTopParent Drawing Methods
	//-------------------------------

	/** Debug method used to display topParent information in the top left corner of the screen. */
	public static void drawDebugInfo(ITopParent<?> objIn) {
		if (Game.isDebugMode()) {
			double yPos = 3;
			double xPos = 0;
			
			//TaskBar b = EnvisionRenderer.instance.getTaskBar();
			//if (b != null && !b.isHidden() && b.checkDraw()) {
			//	yPos = b.endY + 4;
			//}
			
			IWindowObject<?> ho = objIn.getHighestZObjectUnderMouse();
			String out = "null";
			int zLevel = (ho instanceof IWindowParent<?>) ? ((IWindowParent<?>) ho).getZLevel() : -1;
			
			String topParent = "TopParent: " + objIn.getTopParent();
			String focusedObject = "";
			String focusLockObject = "";
			//String objects = "objs: " + objIn.getObjects();
			String modifyType = "ModifyingObject & type: (" + objIn.getModifyingObject() + " : " + objIn.getModifyType() + ")";
			String underMouse = "Object under mouse: " + (ho != null ? ho : out) + " " + zLevel;
			String lastClicked = "Last clicked object: " + objIn.getLastClickedObject() + " : " + objIn.getLastClickTime();
			String mousePos = "Mouse pos: (" + Mouse.getMx() + ", " + Mouse.getMy() + ")";
			
			if (objIn.getFocusedObject() instanceof WindowButton) {
				focusedObject = "FocuedObject: " + (((WindowButton) objIn.getFocusedObject()).getString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
						   		((WindowButton) objIn.getFocusedObject()).getString());
			}
			else { focusedObject = "FocuedObject: " + objIn.getFocusedObject(); }
			
			if (objIn.getFocusLockObject() instanceof WindowButton) {
				focusLockObject = "FocusLockObject: " + (((WindowButton) objIn.getFocusLockObject()).getString().isEmpty() ? objIn.getFocusedObject() : "EGuiButton: " +
								  ((WindowButton) objIn.getFocusLockObject()).getString());
			}
			else { focusLockObject = "FocusLockObject: " + objIn.getFocusLockObject(); }
			
			if (ho != null) { out = ho.getClass().getName(); }
			
			int longestX = getStringWidth(topParent);
			
			if (getStringWidth(focusedObject) > longestX) { longestX = getStringWidth(focusedObject); }
			if (getStringWidth(focusLockObject) > longestX) { longestX = getStringWidth(focusLockObject); }
			//if (stringWidth(objects) > longestX) { longestX = stringWidth(objects); }
			if (getStringWidth(modifyType) > longestX) { longestX = getStringWidth(modifyType); }
			if (getStringWidth(underMouse) > longestX) { longestX = getStringWidth(underMouse); }
			if (getStringWidth(lastClicked) > longestX) { longestX = getStringWidth(lastClicked); }
			if (getStringWidth(mousePos) > longestX) { longestX = getStringWidth(mousePos); }
			
			longestX += 5;
			
			//draw background
			drawRect(xPos, yPos - 3, longestX + 1, yPos + 71, EColors.black);
			drawRect(xPos + 1, yPos - 2, longestX, yPos + 70, EColors.dgray);
			
			//--------------------------------------------------------------------------------
			
			//draw what the topParent is
			drawString(topParent, xPos + 3, yPos, EColors.pink);
			
			//draw the currently focused object - if it's a button, show that too
			drawString(focusedObject, xPos + 3, yPos + 10, EColors.cyan);
			
			//draw the current focusLockObject - if it's a button, show that too
			drawString(focusLockObject, xPos + 3, yPos + 20, EColors.yellow);
			
			//draw the topParent's current immediate children
			//drawStringWithShadow(objects, 2, yPos + 30, 0x70f3ff);
			
			//draw the topParent's current modifying object and type
			drawString(modifyType, xPos + 3, yPos + 30, EColors.lime);
			
			//draw the highest object currently under the mouse
			drawString(underMouse, xPos + 3, yPos + 40, 0xffbb00);
			
			//draw the last clicked object
			drawString(lastClicked, xPos + 3, yPos + 50, EColors.seafoam);
			
			//draw the current mouse position
			drawString(mousePos, xPos + 3, yPos + 60, EColors.lgray);
			
			//draw escape stopper
			//drawStringWithShadow("EscapeStopper: " + objIn.getEscapeStopper(), 2, 72, 0x70f3ff);
			
			//--------------------------------------------------------------------------------
			
		}
	}
	
	//------------------------------
	//StaticTopParent Object Methods
	//------------------------------
	
	/** Returns the object with the highest z level, this could even be the topParent itself. */
	public static IWindowObject<?> getHighestZLevelObject(ITopParent<?> objIn) {
		return (objIn.getObjects().isNotEmpty()) ? objIn.getObjects().getLast() : objIn;
	}
	
	/** Hides all objects that are not pinned to the hud. */
	public static void hideUnpinnedObjects(ITopParent<?> objIn) {
		for (IWindowObject<?> o : objIn.getCombinedObjects()) {
			if (o.closesWithHud()) { o.close(); }
			else if (o instanceof IWindowParent<?>) {
				if (!((IWindowParent<?>) o).isPinned()) {
					o.setHidden(true);
				}
			}
			else { o.setHidden(true); }
		}
	}
	
	/** Hides all objects except for the specified ones. */
	public static void hideAllExcept(ITopParent<?> topIn, IWindowObject<?> exception, IWindowObject<?>... additional) {
		for (IWindowObject<?> o : topIn.getCombinedObjects()) {
			for (IWindowObject<?> e : EUtil.add(exception, additional)) {
				if (o != e || o.isChildOf(e)) {
					o.setHidden(true);
				}
			}
		}
	}
	
	/** Makes all objects that were hidden now visible. */
	public static void revealHiddenObjects(ITopParent<?> objIn) {
		objIn.getCombinedObjects().filter(o -> o.isHidden()).forEach(o -> o.setHidden(false));
	}
	
	/** Removes all windows from the topParent that are not pinned. */
	public static void removeUnpinnedObjects(ITopParent<?> objIn) {
		
		//check in both the current objects and the objects that will be added
		EArrayList<IWindowParent<?>> windows = new EArrayList();
		
		for (IWindowObject<?> o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o instanceof IWindowParent<?>) { //only windows can be pinned
				if (!((IWindowParent<?>) o).isPinned()) {
					windows.add((IWindowParent<?>) o);
				}
			}
			else {
				EArrayList<IWindowObject<?>> childObjects = objIn.getAllChildren();
				for (int i = childObjects.size() - 1; i >= 0; i--) {
					IWindowObject<?> u = childObjects.get(i);
					u.close();
				}
			}
		}
		
		for (IWindowParent<?> p : windows) {
			EArrayList<IWindowObject<?>> childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
	}
	
	public static void removeAllObjects(ITopParent<?> objIn) {
		
		//check in both the current objects and the objects that will be added
		EArrayList<IWindowObject<?>> objects = objIn.getCombinedObjects();
		
		for (IWindowObject<?> p : objects) {
			EArrayList<IWindowObject<?>> childObjects = p.getAllChildren();
			for (int i = childObjects.size() - 1; i >= 0; i--) {
				childObjects.get(i).close();
			}
			p.close();
		}
	}
	
	/** Returns true if there are any pinned windows within the specified topParent. */
	public static boolean hasPinnedObjects(ITopParent<?> objIn) {
		//check in both the current objects and the objects that will be added
		for (IWindowObject<?> o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			if (o instanceof IWindowParent<?>) { //only windows can be pinned
				if (((IWindowParent<?>) o).isPinned()) { return true; }
			}
		}
		return false;
	}
	
	//-----------------------------
	//StaticTopParent Focus Methods
	//-----------------------------
	
	/** Removes any object that possesses a focus lock on the specified topParent. */
	public static void clearFocusLockObject(ITopParent<?> objIn) {
		if (objIn.getFocusLockObject() != null) { objIn.getFocusLockObject().onFocusLost(new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.Lost)); }
		objIn.setFocusLockObject(null); 
	}
	
	/** Forces the currently focused object, if there is one, to return focus to either the defaultFocusObject, if there is one, or the topParent. */
	public static void clearFocusedObject(ITopParent<?> objIn) {
		IWindowObject<?> fo = objIn.getFocusedObject();
		
		if (fo != null && fo != objIn) {
			fo.onFocusLost(new EventFocus(objIn, fo, FocusType.Lost));
			//transfer focus back to the defaultFocusObject or the topParent
			if (objIn.getDefaultFocusObject() != null) { objIn.setFocusedObject(objIn.getDefaultFocusObject()); }
			else { objIn.setFocusedObject(objIn); }
		}
		else {
			//if there is not a focused object, transfer focus to either the defaultFocusObject or the topParent
			if (objIn.getDefaultFocusObject() != null) { objIn.setFocusedObject(objIn.getDefaultFocusObject()); }
			else if (fo != objIn) { objIn.setFocusedObject(objIn); }
		}
	}
	
	/** Method used to process focus events that are present in the specified topParent. */
	public static void updateFocus(ITopParent<?> objIn, Deque<EventFocus> focusQueue) {
		IWindowObject<?> mod = objIn.getModifyingObject();
		ObjectModifyType mType = objIn.getModifyType();
		
		//remove any lingering focused objects if they are no longer within in the parent
		EArrayList<IWindowObject<?>> children = objIn.getAllChildren();
		if (objIn.getFocusedObject() != null && !children.contains(objIn.getFocusedObject())) { objIn.clearFocusedObject(); }
		if (objIn.getFocusLockObject() != null && !children.contains(objIn.getFocusLockObject())) { objIn.clearFocusLockObject(); }
		if (objIn.getDefaultFocusObject() != null && !children.contains(objIn.getDefaultFocusObject())) { objIn.setDefaultFocusObject(null); }
		
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
							passFocus(objIn, objIn.getFocusedObject(), objIn.getFocusLockObject(), new EventFocus(objIn, objIn.getFocusLockObject(), FocusType.DefaultFocusObject));
						}
					}
					else {
						//if there is already an object in focus, transfer the focus to the requesting object
						if (objIn.getFocusedObject() != null) { passFocus(objIn, objIn.getFocusedObject(), obj, event); }
						else {
							//otherwise, pass focus to a defaultFocusObject, if there is one, else pass it back to the topParent
							if (objIn.getDefaultFocusObject() != null) { passFocus(objIn, objIn.getFocusedObject(), objIn.getDefaultFocusObject(), event); }
							else { passFocus(objIn, objIn.getFocusedObject(), objIn, event); }
						}
					}
				}
				else { //the object is the topParent itself
					passFocus(objIn, objIn.getFocusedObject(), objIn, event);
				}
			}
		}
		else if (objIn.getFocusedObject() == null) {
			passFocus(objIn, objIn.getFocusedObject(), objIn, new EventFocus(objIn, objIn, FocusType.DefaultFocusObject));
		}
	}
	
	/** Internal method used to quickly transfer focus from old object to new object. */
	private static void passFocus(ITopParent<?> par, IWindowObject<?> from, IWindowObject<?> to, EventFocus event) {
		if (from != null) { from.onFocusLost(event); }
		par.setFocusedObject(to);
		to.onFocusGained(event);
	}
	
	//----------------------------
	//StaticTopParent Mouse Checks
	//----------------------------
	
	//mouse checks
	/** Returns true if the mouse is over any object edge. */
	public static boolean isMouseOnObjEdge(ITopParent<?> objIn) {
		return getEdgeAreaMouseIsOn(objIn) != ScreenLocation.out;
	}
	
	/** Returns the ScreenLocation type of any object the mouse is currently over. */
	public static ScreenLocation getEdgeAreaMouseIsOn(ITopParent<?> objIn) {
		//check in both the objects on screen and the objects being added
		for (IWindowObject<?> o : EArrayList.combineLists(objIn.getObjects(), objIn.getAddingObjects())) {
			ScreenLocation loc = o.getEdgeSideMouseIsOn();
			if (loc != ScreenLocation.out) { return loc; }
		}
		return ScreenLocation.out; //otherwise, return out becuse it wasn't under an object edge
	}
	
	/** Returns true if the cursor is currently inside an EGuiHeader object. */
	public static boolean isMouseInsideHeader(ITopParent<?> objIn) {
		return EUtil.forEachTest(objIn.getAllChildrenUnderMouse(), o -> (o instanceof WindowHeader), true, false);
	}
	
	public static WindowHeader getHeaderUnderMouse(ITopParent<?> objIn) {
		return (WindowHeader) EUtil.getFirst(objIn.getAllChildrenUnderMouse(), o -> (o instanceof WindowHeader));
	}
	
	/** Returns the object that has the highest z level under the cursor. */
	public static IWindowObject<?> getHighestZObjectUnderMouse(ITopParent<?> objIn) {
		try {
			EArrayList<IWindowObject<?>> underMouse = objIn.getAllObjectsUnderMouse();
			StorageBoxHolder<IWindowObject<?>, EArrayList<IWindowObject<?>>> sortedByParent = new StorageBoxHolder();
			
			//first setup the sorted list
			for (int i = objIn.getObjects().size() - 1; i >= 0; i--) {
				sortedByParent.add(objIn.getObjects().get(i), new EArrayList());
			}
			
			//next iterate through each of the objects found under the mouse and add them to the corresponding parents
			for (IWindowObject<?> o : underMouse) {
				for (int i = 0; i < sortedByParent.size(); i++) {
					IWindowObject<?> parent = sortedByParent.getObject(i);
					if (o.equals(parent) || parent.getAllChildren().contains(o)) { sortedByParent.getValue(i).add(o); }
				}
			}
			
			//next iterate through each of the sorted parent's found objects to see if they are the highest object
			for (StorageBox<IWindowObject<?>, EArrayList<IWindowObject<?>>> box : sortedByParent) {
				if (box.getB().isEmpty()) { continue; }
				
				//get the last object (which should be the highest object)
				EArrayList<IWindowObject<?>> objects = box.getB();
				return objects.getLast();
			}
		}
		catch (Exception e) { e.printStackTrace(); }
		return null;
	}
	
	/** Returns a list of all objects that currently under the cursor. */
	public static EArrayList<IWindowObject<?>> getAllObjectsUnderMouse(ITopParent<?> objIn) {
		EArrayList<IWindowObject<?>> underMouse = new EArrayList();
		EArrayList<IWindowObject<?>> children = objIn.getAllChildren();
		
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		
		//don't check if an object is being resized
		if (objIn.getModifyType() != ObjectModifyType.Resize) {
			
			for (IWindowObject<?> o : objIn.getAllChildren()) {
				//check if the object can even be selected
				if (o.isVisible() && (o.isClickable() || o.getHoverText() != null)) {
					if (o instanceof IWindowParent<?>) {
						IWindowParent<?> wp = (IWindowParent<?>) o;
						
						//then check if the mouse is in or around the object if it's resizeable
						if (o.isMouseInside() || ((o.isResizeable() && o.isMouseOnEdge(mX, mY)) && !(wp.isMinimized() || wp.getMaximizedPosition() == ScreenLocation.center))) {
							underMouse.add(o);
						}
					}
					else {
						//then check if the mouse is in or around the object if it's resizeable
						if (o.isMouseInside() || (o.isResizeable() && o.isMouseOnEdge(mX, mY))) {
							IWindowParent<?> wp = o.getWindowParent();
							if (wp != null) {
								if (!wp.isMinimized()) { underMouse.add(o); }
							}
							else { underMouse.add(o); }
						}
					}
				}
			} //for
		}
		
		return underMouse;
	}
	
 }
