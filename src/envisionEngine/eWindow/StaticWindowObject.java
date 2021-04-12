package envisionEngine.eWindow;

import envisionEngine.eWindow.windowObjects.advancedObjects.header.WindowHeader;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.ITopParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowParent;
import envisionEngine.eWindow.windowUtil.EGui;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.KeyboardType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.MouseType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.ObjectEventType;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventKeyboard;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventModify;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventMouse;
import envisionEngine.eWindow.windowUtil.windowEvents.events.EventObjects;
import eutil.EUtil;
import main.Game;
import renderUtil.EColors;
import renderUtil.ScreenLocation;
import storageUtil.EArrayList;
import storageUtil.EDimension;
import storageUtil.StorageBox;
import storageUtil.StorageBoxHolder;
import tempUtil.WindowSize;

//Author: Hunter Bragg

public class StaticWindowObject extends EGui {
	
	private static volatile long windowPID = 0l;
	
	/** Returns the next pid that will be assigned to a requesting object. */
	public static synchronized long getPID() { return windowPID++; }
	
	//main draw
	public static void updateCursorImage(IWindowObject<?> obj) {
		
		/*
		//make sure that the window isn't maximized
		if (obj instanceof IWindowParent && ((IWindowParent) obj).getMaximizedPosition() == ScreenLocation.center) {
			CursorHelper.updateCursor(null);
			return;
		}
		
		if (obj != null && obj.isResizeable() && obj.getTopParent().getModifyType() != ObjectModifyType.Resize) {
			double rStartY = obj.hasHeader() ? obj.getHeader().startY : obj.getDimensions().startY;
			if (!Mouse.isButtonDown(0)) {
				
				switch (obj.getEdgeAreaMouseIsOn()) {
				case top: case bot: CursorHelper.updateCursor(EMCResources.cursorResizeNS); break;
				case left: case right: CursorHelper.updateCursor(EMCResources.cursorResizeEW); break;
				case topRight: case botLeft: CursorHelper.updateCursor(EMCResources.cursorResizeDL); break;
				case topLeft: case botRight: CursorHelper.updateCursor(EMCResources.cursorResizeDR); break;
				default: CursorHelper.setCursor(null); break;
				}
			}
		}
		else { CursorHelper.updateCursor(null); }
		*/
	}
	
	public static void onMouseHover(IWindowObject<?> obj, int mX, int mY, String hoverText, int textColor) {
		if (hoverText != null && !hoverText.isEmpty()) {
			WindowSize res = Game.getWindowSize();
			int strWidth = getStringWidth(hoverText);
			int sX = mX + 8;
			int sY = mY - 7;
			
			sX = sX < 0 ? 1 : sX;
			sY = (sY - 7) < 2 ? 2 + 7 : sY;
			if (sX + strWidth + 10 > res.getWidth()) {
				sX = -1 + sX - (sX + strWidth + 10 - res.getWidth() + 6);
				sY -= 10;
			}
			sY = sY + 16 > res.getHeight() ? -2 + sY - (sY + 16 - res.getHeight() + 6) : sY;
			
			int eX = sX + strWidth + 10;
			int eY = sY + 16;
			
			drawRect(sX, sY, sX + strWidth + 10, sY + 16, EColors.black);
			drawRect(sX + 1, sY + 1, eX - 1, eY - 1, EColors.steel);
			drawStringWithShadow(hoverText, sX + 5, sY + 4, textColor);
		}
	}
	
	//size
	/** Returns true if the object has an EGuiHeader. */
	public static boolean hasHeader(IWindowObject<?> obj) { return getHeader(obj) != null; }
	
	/** Returns this objects WindowHeader, if there is one. */
	public static WindowHeader getHeader(IWindowObject<?> obj) {
		for (IWindowObject<?> o : obj.getCombinedObjects()) {
			if (o instanceof WindowHeader<?>) { return (WindowHeader<?>) o; }
		}
		return null;
	}
	
	/** Generic object resizing algorithm. */
	public static void resize(IWindowObject<?> obj, double xIn, double yIn, ScreenLocation areaIn) {
		obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Resize)); //post an event
		if (xIn != 0 || yIn != 0) { //make sure that there is actually a change in the cursor position
			EDimension d = obj.getDimensions();
			//EDimension c = obj.getClickableArea();
			double x = 0, y = 0, w = 0, h = 0;
			boolean e = false, s = false;
			//perform resizing on different sides depending on the side that's being resized
			switch (areaIn) {
			case top:
				x = d.startX; y = d.startY + yIn; w = d.width; h = d.height - yIn;
				//obj.setClickableArea(c.startX, c.startY + yIn, c.width, c.height - yIn);
				break;
			case bot:
				x = d.startX; y = d.startY; w = d.width; h = d.height + yIn;
				//obj.setClickableArea(c.startX, c.startY, c.width, c.height + yIn);
				break;
			case right:
				x = d.startX; y = d.startY; w = d.width + xIn; h = d.height;
				//obj.setClickableArea(c.startX, c.startY, c.width + xIn, c.height);
				break;
			case left:
				x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height;
				//obj.setClickableArea(c.startX + xIn, c.startY, c.width - xIn, c.height);
				break;
			case topRight:
				x = d.startX; y = d.startY + yIn; w = d.width + xIn; h = d.height - yIn;
				//obj.setClickableArea(c.startX, c.startY + yIn, c.width + xIn, c.height - yIn);
				break;
			case botRight:
				x = d.startX; y = d.startY; w = d.width + xIn; h = d.height + yIn;
				//obj.setClickableArea(c.startX, c.startY, c.width + xIn, c.height + yIn);
				break;
			case topLeft:
				x = d.startX + xIn; y = d.startY + yIn; w = d.width - xIn; h = d.height - yIn;
				//obj.setClickableArea(c.startX + xIn, c.startY + yIn, c.width - xIn, c.height - yIn);
				break;
			case botLeft:
				x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height + yIn;
				//obj.setClickableArea(c.startX + xIn, c.startY, c.width - xIn, c.height + yIn);
				break;
			default: break;
			}
			//restrict the object to its allowed minimum width
			if (w < obj.getMinWidth()) {
				w = obj.getMinWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = d.startX; break;
				case left: case topLeft: case botLeft: x = d.endX - w; break;
				default: break;
				}
			}
			//restrict the object to its allowed maximum width
			if (w > obj.getMaxWidth()) {
				w = obj.getMaxWidth();
				switch (areaIn) {
				case right: case topRight: case botRight: x = d.startX; break;
				case left: case topLeft: case botLeft: x = d.endX - w; break;
				default: break;
				}
			}
			//restrict the object to its allowed minimum height
			if (h < obj.getMinHeight()) {
				h = obj.getMinHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = d.endY - h; break;
				case bot: case botRight: case botLeft: y = d.startY; break;
				default: break;
				}
			}
			//restrict the object to its allowed maximum height
			if (h > obj.getMaxHeight()) {
				h = obj.getMaxHeight();
				switch (areaIn) {
				case top: case topRight: case topLeft: y = d.endY - h; break;
				case bot: case botRight: case botLeft: y = d.startY; break;
				default: break;
				}
			}
			
			//set the dimensions of the object to the resized dimensions
			obj.setDimensions(x, y, w, h);
			
			//(lazy approach) remake all the children based on the resized dimensions
			obj.reInitObjects();
			if (obj.getTopParent() != null) { obj.getTopParent().setFocusedObject(obj); }
		}
	}
	
	//position
	/** Translates the specified object by a given x and y amount. */
	public static void move(IWindowObject<?> obj, double newX, double newY) {
		if (newX != 0 || newY != 0) {
			obj.postEvent(new EventModify(obj, obj, ObjectModifyType.Move)); //post an event
			
			if (obj.isMoveable()) { //only allow the object to be moved if it's not locked in place
				//get all of the children in the object that aren't locked in place
				for (IWindowObject<?> o : EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects())) {
					if (o.isMoveable()) { //only move the child if it's not locked in place
						if (o instanceof WindowParent<?>) { //only move the window if it moves with the parent
							if (((WindowParent<?>) o).movesWithParent()) { o.move(newX, newY); }
						}
						else { o.move(newX, newY); }
					}
				}
				EDimension d = obj.getDimensions();
				obj.setDimensions(d.startX + newX, d.startY + newY, d.width, d.height); //offset the original position by the specified offset
				//obj.getClickableArea().move(newX, newY);
				if (obj.isBoundaryEnforced()) { //also move the boundary enforcer, if there is one
					EDimension b = obj.getBoundaryEnforcer();
					obj.getBoundaryEnforcer().setPosition(b.startX + newX, b.startY + newY);
				}
			}
		}
	}
	
	/** Moves the object to the specified x and y coordinates. */
	public static void setPosition(IWindowObject<?> obj, double newX, double newY) {
		//only move this object and its children if it is moveable
		if (obj.isMoveable()) {
			EDimension d = obj.getDimensions();
			//EDimension c = obj.getClickableArea();
			
			//the object's current position and relative clickArea for shorter code
			StorageBox<Double, Double> loc = new StorageBox(d.startX, d.startY);
			//StorageBox<Integer, Integer> relLoc = new StorageBox(d.startX - c.startX, d.startY - c.startY);
			
			//holder to store each object and their relative child locations
			StorageBoxHolder<IWindowObject<?>, StorageBox<Double, Double>> previousLocations = new StorageBoxHolder();
			
			//grab all immediate objects
			EArrayList<IWindowObject<?>> objs = EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects());
			
			//get each of the object's children's relative positions and clickable areas relative to each child
			for (IWindowObject o : objs) {
				previousLocations.add(o, new StorageBox(o.getDimensions().startX - loc.getA(), o.getDimensions().startY - loc.getB()));
				//new StorageBox(o.getClickableArea().startX - o.getDimensions().startX, o.getClickableArea().startY - o.getDimensions().startY))
			}
			
			//apply the new location to parent
			obj.setDimensions(newX, newY, d.width, d.height); //move the object to the new position
			//obj.setClickableArea(newX + relLoc.getObject(), newY + relLoc.getValue(), c.width, c.height);
			
			//apply the new location to each child
			for (IWindowObject<?> o : objs) {
				//don't move the child if its position is locked
				if (o.isMoveable()) {
					StorageBox<Double, Double> oldLoc = previousLocations.getBoxWithA(o).getB();
					//StorageBox<Integer, Integer> oldClick = previousLocations.getBoxWithObj(o).getValue().getValue();
					
					//move the child to the new location with the parent's offest
					o.setPosition(newX + oldLoc.getA(), newY + oldLoc.getB()); 
					//o.setClickableArea(o.getDimensions().startX + oldClick.getObject(), o.getDimensions().startY + oldClick.getObject(), o.getClickableArea().width, o.getClickableArea().height);
				}
			}
		}
	}
	
	//objects
	/** Returns true if the given object is a child of the specified parent. */
	public static boolean isChildOfObject(IWindowObject<?> child, IWindowObject<?> parent) {
		//prevent checking if there is nothing to check against
		if (parent == null) { return false; }
		
		IWindowObject<?> parentObj = child.getParent();
		
		//recursively check through the object's parent lineage to see if that parent is the possible parent
		while (parentObj != null) {
			if (parentObj == parent) { return true; }
			//check for infinite loops
			if (parentObj == parentObj.getParent()) { break; }
			parentObj = parentObj.getParent();
		}
		
		return false;
	}
	
	/** Start the process of adding a child to this object. Children are fully added on the next draw cycle. 
	 *  There is an issue where a child of a child can be added to the parent again. */
	public static void addObject(IWindowObject<?> parent, IWindowObject<?> obj, IWindowObject<?>... objsIn) {
		for (IWindowObject<?> o : EUtil.add(obj, objsIn)) {
			try {
				if (o != null) { //only add if the object isn't null
					//don't let the object be added to itself, or if the object is already in the object - this only goes 1 layer deep however
					if (o != parent && (parent.getRemovingObjects().contains(o) || (parent.getObjects().notContains(o) && parent.getAddingObjects().notContains(o)))) {
						
						if (o instanceof WindowHeader && parent.hasHeader()) { continue; } //prevent multiple headers being added
						if (o instanceof WindowParent) { ((WindowParent) o).initWindow(); } //if it's a window, do it's init
						
						o.setParent(parent).initObjects(); //initialize all of the children's children
						
						//o.setZLevel(parent.getZLevel() + o.getZLevel() + 1); //increment the child's z layer based off of the parent
						//if the parent has a boundary enforcer, apply it to the child as well
						if (parent.isBoundaryEnforced()) { o.setBoundaryEnforcer(parent.getBoundaryEnforcer()); }
						
						o.completeInit(); //tell the child that it has been fully initialized and that it is ready to be added on the next draw cycle
						parent.getAddingObjects().add(o); //give the processed child to the parent so that it will be added
					}
					else {
						System.out.println(parent + " already contains " + o + "!");
					}
				}
			}
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	/** Start the process of removing a child from this object. Children are fully removed on the next draw cycle. */
	public static void removeObject(IWindowObject<?> parent, IWindowObject<?> obj, IWindowObject<?>... objsIn) {
		IWindowObject<?>[] objs = EUtil.add(obj, objsIn);
		EUtil.filterNullForEachA(o -> o.setBeingRemoved(), objs);
		parent.getRemovingObjects().add(objs);
	}
	
	/** Returns a list containing every single child from every object in the specified object. */
	public static EArrayList<IWindowObject<?>> getAllChildren(IWindowObject<?> obj) {
		EArrayList<IWindowObject<?>> foundObjs = new EArrayList();
		EArrayList<IWindowObject<?>> objsWithChildren = new EArrayList();
		EArrayList<IWindowObject<?>> workList = new EArrayList();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		obj.getObjects().forEach(o -> { foundObjs.add(o); if (!o.getCombinedObjects().isEmpty()) { objsWithChildren.add(o); } });
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getCombinedObjects()));
		
		//only work as long as there are still child layers to process
		while (workList.isNotEmpty()) {
			//update the foundObjs
			foundObjs.addAll(workList);
			
			//for the current layer, find all objects that have children
			objsWithChildren.clear();
			workList.filterForEach(o -> o.getCombinedObjects().isNotEmpty(), objsWithChildren::add);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getCombinedObjects()));
		}
		
		return foundObjs;
	}
	
	/** Returns a list of all children currently under the cursor. */
	public static EArrayList<IWindowObject<?>> getAllChildrenUnderMouse(IWindowObject<?> obj, int mX, int mY) {
		//only add objects if they are visible and if the cursor is over them.
		return obj.getAllChildren().filterNull(o -> o.checkDraw() && o.isMouseInside());
	}
	
	//parents
	/* Returns the topParent for the specified object. */
	public static ITopParent<?> getTopParent(IWindowObject<?> obj) {
		IWindowObject<?> parentObj = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a topParentdw
		while (parentObj != null) {
			if (parentObj instanceof ITopParent<?>) {
				//System.out.println("returning cast of getTopParent");
				return (ITopParent<?>) parentObj;
			}
			
			//System.out.println("HERE: " + obj + " : " + parentObj);
			//break if the parent is itself
			if (parentObj == parentObj.getParent()) {
				//System.out.println("breaking getTopParent chain");
				break;
			}
			
			parentObj = parentObj.getParent();
		}
		
		//System.out.println("out of getTopParent");
		
		return obj instanceof ITopParent<?> ? (ITopParent<?>) obj : null;
	}
	
	/** Returns the parent window for the specified object, if there is one. */
	public static IWindowParent<?> getWindowParent(IWindowObject<?> obj) {
		IWindowObject<?> parentObj = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a window
		while (parentObj != null && !(parentObj instanceof ITopParent<?>)) {
			if (parentObj instanceof IWindowParent<?>) { return (IWindowParent<?>) parentObj; }
			
			//System.out.println("HERE2: " + obj + " : " + parentObj);
			//break if the parent is itself
			if (parentObj == parentObj.getParent()) { break; }
			
			parentObj = parentObj.getParent();
		}
		
		return obj instanceof IWindowParent<?> ? (IWindowParent<?>) obj : null;
	}
	
	//mouse checks
	
	/** Returns the ScreenLocation area the mouse is currently on for an object. */
	public static ScreenLocation getEdgeAreaMouseIsOn(IWindowObject<?> objIn, int mX, int mY) {
		boolean left = false, right = false, top = false, bottom = false;
		EDimension d = objIn.getDimensions();
		double rStartY = objIn.hasHeader() ? objIn.getHeader().startY : d.startY;
		if (mX >= d.startX - 5 && mX <= d.endX + 5 && mY >= rStartY - 5 && mY <= d.endY + 4) {
			if (mX >= d.startX - 5 && mX <= d.startX) { left = true; }
			if (mX >= d.endX - 4 && mX <= d.endX + 5) { right = true; }
			if (mY >= rStartY - 6 && mY <= rStartY) { top = true; }
			if (mY >= d.endY - 4 && mY <= d.endY + 4) { bottom = true; }
			if (left) {
				if (top) { return ScreenLocation.topLeft; }
				else if (bottom) { return ScreenLocation.botLeft; }
				else { return ScreenLocation.left; }
			}
			else if (right) {
				if (top) { return ScreenLocation.topRight; }
				else if (bottom) { return ScreenLocation.botRight; }
				else { return ScreenLocation.right; }
			} 
			else if (top) { return ScreenLocation.top; }
			else if (bottom) { return ScreenLocation.bot; }
		}
		return ScreenLocation.out;
	}
	
	public static boolean isMouseInside(IWindowObject<?> obj, int mX, int mY) {
		if (obj != null && (!Game.getTopRenderer().hasFocus() || obj.isChildOf(Game.getTopRenderer()))) {
			EDimension d = obj.getDimensions();
			
			// check if there is a boundary enforcer limiting the overrall area
			if (obj.isBoundaryEnforced()) {
				EDimension b = obj.getBoundaryEnforcer();
				return mX >= d.startX && mX >= b.startX &&
					   mX <= d.endX && mX <= b.endX &&
					   mY >= d.startY && mY >= b.startY &&
					   mY <= d.endY && mY <= b.endY;
			}
			
			// otherwise just check if the mouse is within the object's boundaries
			return mX >= d.startX && mX <= d.endX && mY >= d.startY && mY <= d.endY;
		}
		return false;
	}
	
	//basic inputs
	public static void parseMousePosition(IWindowObject<?> objIn, int mX, int mY) {
		objIn.getObjects().filterForEach(o -> o.isMouseInside(), o -> o.parseMousePosition(mX, mY));
	}
	
	public static void mousePressed(IWindowObject<?> objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.PRESSED));
		IWindowParent<?> p = objIn.getWindowParent();
		if (p != null) { p.bringToFront(); }
		if (!objIn.hasFocus() && objIn.isMouseOver()) { objIn.requestFocus(); }
		if (button == 0 && objIn.isResizeable() && !objIn.getEdgeSideMouseIsOn().equals(ScreenLocation.out)) {
			objIn.getTopParent().setResizingDir(objIn.getEdgeSideMouseIsOn());
			objIn.getTopParent().setModifyMousePos(mX, mY);
			objIn.getTopParent().setModifyingObject(objIn, ObjectModifyType.Resize);
		}
	}
	
	public static void mouseReleased(IWindowObject<?> objIn, int mX, int mY, int button) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, button, MouseType.RELEASED));
		if (objIn.getTopParent() != null) {
			if (objIn.getTopParent().getDefaultFocusObject() != null) { objIn.getTopParent().getDefaultFocusObject().requestFocus(); }
			if (objIn.getTopParent().getModifyType() == ObjectModifyType.Resize) { objIn.getTopParent().clearModifyingObject(); }
		}
	}
	
	public static void mouseDragged(IWindowObject<?> objIn, int mX, int mY, int button, long timeSinceLastClick) {}
	
	public static void mouseScolled(IWindowObject<?> objIn, int mX, int mY, int change) {
		objIn.postEvent(new EventMouse(objIn, mX, mY, -1, MouseType.SCROLLED));
		objIn.getObjects().filterForEach(o -> o.isMouseInside() && o.checkDraw(), o -> o.mouseScrolled(change));
	}
	
	public static void keyPressed(IWindowObject<?> objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Pressed));
	}
	
	public static void keyReleased(IWindowObject<?> objIn, char typedChar, int keyCode) {
		objIn.postEvent(new EventKeyboard(objIn, typedChar, keyCode, KeyboardType.Released));
	}
	
	public static void setEntiretyClickable(IWindowObject<?> objIn, boolean val) {
		objIn.getAllChildren().forEach(o -> o.setClickable(val));
		objIn.setClickable(val);
	}
	
	public static void addObjects(IWindowObject<?> objIn, EArrayList<IWindowObject<?>> toBeAdded) {
		for (IWindowObject<?> o : toBeAdded) {
			if (o != null) {
				if (o != objIn) {
					objIn.getObjects().add(o);
					o.onAdded();
					objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectAdded));
				}
			}
		}
		toBeAdded.clear();
	}

	public static void removeObjects(IWindowObject<?> objIn, EArrayList<IWindowObject<?>> toBeRemoved) {
		for (IWindowObject<?> o : toBeRemoved) {
			if (o != null) {
				if (o != objIn) {
					if (objIn.getObjects().contains(o)) {
						objIn.onClosed();
						objIn.getObjects().remove(o);
						objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectRemoved));
					}
				}
			}
		}
		toBeRemoved.clear();
	}
	
}
