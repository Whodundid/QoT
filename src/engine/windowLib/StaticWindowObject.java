package engine.windowLib;

import assets.textures.CursorTextures;
import engine.QoT;
import engine.input.Mouse;
import engine.util.CursorHelper;
import engine.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.ITopParent;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowTypes.interfaces.IWindowParent;
import engine.windowLib.windowUtil.EGui;
import engine.windowLib.windowUtil.windowEvents.eventUtil.KeyboardType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import engine.windowLib.windowUtil.windowEvents.events.EventKeyboard;
import engine.windowLib.windowUtil.windowEvents.events.EventModify;
import engine.windowLib.windowUtil.windowEvents.events.EventMouse;
import engine.windowLib.windowUtil.windowEvents.events.EventObjects;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.BoxList;
import eutil.math.EDimension;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public class StaticWindowObject extends EGui {
	
	private static volatile long windowPID = 0l;
	
	/** Returns the next pid that will be assigned to a requesting object. */
	public static synchronized long getPID() { return windowPID++; }
	
	/**
	 * Returns true if the object is moving in regards to its top parent.
	 * 
	 * @param obj The object to check for movement on
	 * @return True if moving
	 */
	public static boolean isObjectMoving(IWindowObject<?> obj) {
		if (obj == null) return false;
		var top = obj.getTopParent();
		return top.getModifyingObject() == obj && top.getModifyType() == ObjectModifyType.Move;
	}
	
	/**
	 * Returns true if the parent of this object is moving in regards to its top parent
	 * 
	 * @param obj The base object
	 * @return True if base object's parent is moving
	 */
	public static boolean isParentMoving(IWindowObject<?> obj) {
		if (obj == null) return false;
		return isObjectMoving(obj.getParent());
	}
	
	//main draw
	public static void updateCursorImage(IWindowObject<?> obj) {
		//make sure that the window isn't maximized
		if (obj instanceof IWindowParent && ((IWindowParent) obj).getMaximizedPosition() == ScreenLocation.CENTER) {
			CursorHelper.reset();
			return;
		}
		
		if (obj != null && obj.isResizeable() && obj.getTopParent().getModifyType() != ObjectModifyType.Resize) {
			//double rStartY = obj.hasHeader() ? obj.getHeader().startY : obj.getDimensions().startY;
			if (!Mouse.isButtonDown(0)) {
				
				switch (obj.getEdgeSideMouseIsOn()) {
				case TOP: case BOT: CursorHelper.updateCursor(CursorHelper.vresize); break;
				case LEFT: case RIGHT: CursorHelper.updateCursor(CursorHelper.hresize); break;
				case TOP_RIGHT: case BOT_LEFT: CursorHelper.updateCursor(CursorTextures.cursor_resize_dr); break;
				case TOP_LEFT: case BOT_RIGHT: CursorHelper.updateCursor(CursorTextures.cursor_resize_dl); break;
				default: CursorHelper.reset(); break;
				}
			}
		}
		else if (!CursorHelper.isArrow()) CursorHelper.reset();
		
	}
	
	public static void onMouseHover(IWindowObject<?> obj, int mX, int mY, String hoverText, int textColor) {
		if (hoverText != null && !hoverText.isEmpty()) {
			int strWidth = getStringWidth(hoverText);
			int sX = mX + 8;
			int sY = mY - 7;
			
			sX = sX < 0 ? 1 : sX;
			sY = (sY - 7) < 2 ? 2 + 7 : sY;
			if (sX + strWidth + 10 > QoT.getWidth()) {
				sX = -1 + sX - (sX + strWidth + 10 - QoT.getWidth() + 6);
				sY -= 10;
			}
			sY = sY + 16 > QoT.getHeight() ? -2 + sY - (sY + 16 - QoT.getHeight() + 6) : sY;
			
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
			//boolean e = false, s = false;
			//perform resizing on different sides depending on the side that's being resized
			switch (areaIn) {
			case TOP:
				x = d.startX; y = d.startY + yIn; w = d.width; h = d.height - yIn;
				//obj.setClickableArea(c.startX, c.startY + yIn, c.width, c.height - yIn);
				break;
			case BOT:
				x = d.startX; y = d.startY; w = d.width; h = d.height + yIn;
				//obj.setClickableArea(c.startX, c.startY, c.width, c.height + yIn);
				break;
			case RIGHT:
				x = d.startX; y = d.startY; w = d.width + xIn; h = d.height;
				//obj.setClickableArea(c.startX, c.startY, c.width + xIn, c.height);
				break;
			case LEFT:
				x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height;
				//obj.setClickableArea(c.startX + xIn, c.startY, c.width - xIn, c.height);
				break;
			case TOP_RIGHT:
				x = d.startX; y = d.startY + yIn; w = d.width + xIn; h = d.height - yIn;
				//obj.setClickableArea(c.startX, c.startY + yIn, c.width + xIn, c.height - yIn);
				break;
			case BOT_RIGHT:
				x = d.startX; y = d.startY; w = d.width + xIn; h = d.height + yIn;
				//obj.setClickableArea(c.startX, c.startY, c.width + xIn, c.height + yIn);
				break;
			case TOP_LEFT:
				x = d.startX + xIn; y = d.startY + yIn; w = d.width - xIn; h = d.height - yIn;
				//obj.setClickableArea(c.startX + xIn, c.startY + yIn, c.width - xIn, c.height - yIn);
				break;
			case BOT_LEFT:
				x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height + yIn;
				//obj.setClickableArea(c.startX + xIn, c.startY, c.width - xIn, c.height + yIn);
				break;
			default: break;
			}
			//restrict the object to its allowed minimum width
			if (w < obj.getMinWidth()) {
				w = obj.getMinWidth();
				switch (areaIn) {
				case RIGHT: case TOP_RIGHT: case BOT_RIGHT: x = d.startX; break;
				case LEFT: case TOP_LEFT: case BOT_LEFT: x = d.endX - w; break;
				default: break;
				}
			}
			//restrict the object to its allowed maximum width
			if (w > obj.getMaxWidth()) {
				w = obj.getMaxWidth();
				switch (areaIn) {
				case RIGHT: case TOP_RIGHT: case BOT_RIGHT: x = d.startX; break;
				case LEFT: case TOP_LEFT: case BOT_LEFT: x = d.endX - w; break;
				default: break;
				}
			}
			//restrict the object to its allowed minimum height
			if (h < obj.getMinHeight()) {
				h = obj.getMinHeight();
				switch (areaIn) {
				case TOP: case TOP_RIGHT: case TOP_LEFT: y = d.endY - h; break;
				case BOT: case BOT_RIGHT: case BOT_LEFT: y = d.startY; break;
				default: break;
				}
			}
			//restrict the object to its allowed maximum height
			if (h > obj.getMaxHeight()) {
				h = obj.getMaxHeight();
				switch (areaIn) {
				case TOP: case TOP_RIGHT: case TOP_LEFT: y = d.endY - h; break;
				case BOT: case BOT_RIGHT: case BOT_LEFT: y = d.startY; break;
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
			Box2<Double, Double> loc = new Box2(d.startX, d.startY);
			//StorageBox<Integer, Integer> relLoc = new StorageBox(d.startX - c.startX, d.startY - c.startY);
			
			//holder to store each object and their relative child locations
			BoxList<IWindowObject<?>, Box2<Double, Double>> previousLocations = new BoxList();
			
			//grab all immediate objects
			EArrayList<IWindowObject<?>> objs = EArrayList.combineLists(obj.getObjects(), obj.getAddingObjects());
			
			//get each of the object's children's relative positions and clickable areas relative to each child
			for (IWindowObject o : objs) {
				previousLocations.add(o, new Box2(o.getDimensions().startX - loc.getA(), o.getDimensions().startY - loc.getB()));
				//new StorageBox(o.getClickableArea().startX - o.getDimensions().startX, o.getClickableArea().startY - o.getDimensions().startY))
			}
			
			//apply the new location to parent
			obj.setDimensions(newX, newY, d.width, d.height); //move the object to the new position
			//obj.setClickableArea(newX + relLoc.getObject(), newY + relLoc.getValue(), c.width, c.height);
			
			//apply the new location to each child
			for (IWindowObject<?> o : objs) {
				//don't move the child if its position is locked
				if (o.isMoveable()) {
					Box2<Double, Double> oldLoc = previousLocations.getBoxWithA(o).getB();
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
	public static void addObject(IWindowObject<?> parent, IWindowObject<?>... objs) {
		for (IWindowObject<?> o : objs) {
			if (o == null) continue;
			try {
				//don't let the object be added to itself, or if the object is already in the object - this only goes 1 layer deep however
				if (o != parent && (parent.getRemovingObjects().contains(o) || (parent.getObjects().notContains(o) && parent.getAddingObjects().notContains(o)))) {
					
					//prevent multiple headers being added
					if (o instanceof WindowHeader && parent.hasHeader()) continue;
					//if it's a window, do it's init
					if (o instanceof WindowParent p && !o.isInit()) p.initWindow();
					
					//initialize all of the children's children
					o.setParent(parent).initObjects();
					
					//o.setZLevel(parent.getZLevel() + o.getZLevel() + 1); //increment the child's z layer based off of the parent
					//if the parent has a boundary enforcer, apply it to the child as well
					if (parent.isBoundaryEnforced()) o.setBoundaryEnforcer(parent.getBoundaryEnforcer());
					
					//tell the child that it has been fully initialized and that it is ready to be added on the next draw cycle
					o.completeInit();
					//give the processed child to the parent so that it will be added
					parent.getAddingObjects().add(o);
				}
				else {
					System.out.println(parent + " already contains " + o + "!");
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Start the process of removing a child from this object. Children are fully removed on the next draw cycle. */
	public static void removeObject(IWindowObject<?> parent, IWindowObject<?>... objs) {
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
			
			//break if the parent is itself
			if (parentObj == parentObj.getParent()) {
				//System.out.println("breaking getTopParent chain");
				break;
			}
			
			parentObj = parentObj.getParent();
		}
		
		return obj instanceof ITopParent<?> ? (ITopParent<?>) obj : null;
	}
	
	/** Returns the parent window for the specified object, if there is one. */
	public static IWindowParent<?> getWindowParent(IWindowObject<?> obj) {
		IWindowObject<?> parentObj = obj.getParent();
		//recursively check through the object's parent lineage to see if that parent is a window
		while (parentObj != null && !(parentObj instanceof ITopParent<?>)) {
			if (parentObj instanceof IWindowParent<?>) { return (IWindowParent<?>) parentObj; }
			
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
				if (top) { return ScreenLocation.TOP_LEFT; }
				else if (bottom) { return ScreenLocation.BOT_LEFT; }
				else { return ScreenLocation.LEFT; }
			}
			else if (right) {
				if (top) { return ScreenLocation.TOP_RIGHT; }
				else if (bottom) { return ScreenLocation.BOT_RIGHT; }
				else { return ScreenLocation.RIGHT; }
			} 
			else if (top) { return ScreenLocation.TOP; }
			else if (bottom) { return ScreenLocation.BOT; }
		}
		return ScreenLocation.OUT;
	}
	
	public static boolean isMouseInside(IWindowObject<?> obj, int mX, int mY) {
		if (obj != null && (!QoT.getTopRenderer().hasFocus() || obj.isChildOf(QoT.getTopRenderer()))) {
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
		if (button == 0 && objIn.isResizeable() && !objIn.getEdgeSideMouseIsOn().equals(ScreenLocation.OUT)) {
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
			if (o != null && o != objIn) {
				objIn.getObjects().add(o);
				o.onAdded();
				objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectAdded));
			}
		}
		toBeAdded.clear();
	}

	public static void removeObjects(IWindowObject<?> objIn, EArrayList<IWindowObject<?>> toBeRemoved) {
		for (IWindowObject<?> o : toBeRemoved) {
			if (o != null && o != objIn && objIn.getObjects().contains(o)) {
				objIn.onClosed();
				objIn.getObjects().remove(o);
				objIn.postEvent(new EventObjects(objIn, o, ObjectEventType.ObjectRemoved));
			}
		}
		toBeRemoved.clear();
	}
	
}
