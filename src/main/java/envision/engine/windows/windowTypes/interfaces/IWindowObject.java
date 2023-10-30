package envision.engine.windows.windowTypes.interfaces;

import java.util.Collection;
import java.util.function.Consumer;

import envision.Envision;
import envision.engine.inputHandlers.CursorHelper;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowObjects.utilityObjects.FocusLockBorder;
import envision.engine.windows.windowTypes.DragAndDropObject;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.WindowObjectProperties;
import envision.engine.windows.windowTypes.WindowParent;
import envision.engine.windows.windowUtil.EObjectGroup;
import envision.engine.windows.windowUtil.FutureTaskEventType;
import envision.engine.windows.windowUtil.FutureTaskManager;
import envision.engine.windows.windowUtil.input.KeyboardInputAcceptor;
import envision.engine.windows.windowUtil.input.MouseInputAcceptor;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.ObjectEventHandler;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectEventType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.engine.windows.windowUtil.windowEvents.events.EventAction;
import envision.engine.windows.windowUtil.windowEvents.events.EventDragAndDrop;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import envision.engine.windows.windowUtil.windowEvents.events.EventModify;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import envision.engine.windows.windowUtil.windowEvents.events.EventObjects;
import envision.engine.windows.windowUtil.windowEvents.events.EventRedraw;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.boxes.BoxList;
import eutil.datatypes.points.Point2d;
import eutil.datatypes.util.EList;
import eutil.debug.DebugToolKit;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.ScreenLocation;
import qot.assets.textures.cursor.CursorTextures;

// Author: Hunter Bragg

/** An interface outlining the behavior for all WindowObjects. */
public interface IWindowObject extends KeyboardInputAcceptor, MouseInputAcceptor {
	
	//===================
	// Object Properties
	//===================
	
	/** Returns the current properties of this object. */
	public WindowObjectProperties properties();
	/** Returns the underlying object instance. */
	public default WindowObject instance() { return properties().instance; }
	
	//======
	// Init
	//======

	/** Internal event that happens right before initialization. */
	public default void onPreInit() {}
	/**
	 * Event fired from the top parent upon being fully added to the
	 * parent so that this object can safely initialize all of it's own
	 * children.
	 */
	public default void initChildren() {}
	/** Internal event that happens after initialization has occurred. */
	public default void onPostInit() {}
	/** Internal event that happens before children are reinitialized. */
	public default void preReInit() {}
	/** Internal event that happens after children are reinitialized. */
	public default void postReInit() {}
	
	/** Removes all children and re-runs the initChildren method. */
	public default void reInitChildren() {
		properties().areChildrenInit = false;
		var p = getTopParent();
		var children = getAllChildren();
		if (p.getModifyType() != ObjectModifyType.RESIZE) {
			if (children.contains(p.getFocusedObject())) p.clearFocusedObject();
			if (children.contains(p.getFocusLockObject())) p.clearFocusLockObject();
			if (children.contains(p.getModifyingObject())) p.clearModifyingObject();
		}
		
		preReInit();
		properties().children.clear();
		properties().childrenToBeAdded.clear();
		properties().objectHeader = null;
		initChildren();
		postReInit();
		properties().areChildrenInit = true;
	}
	
	//=========================
	// Basic Object Properties
	//=========================
	
	/** Returns true if this object is currently enabled. */
	public default boolean isEnabled() { return properties().isEnabled; }
	/** Returns true if this object is visible. */
	public default boolean isVisible() { return properties().isVisible; }
	/** Returns true if this object is hidden when the top overlay is not drawn. */
	public default boolean isHidden() { return properties().isHidden; }
	/** Returns true if this object will be drawn regardless of it being visible or enabled. */
	public default boolean isAlwaysVisible() { return properties().isAlwaysVisible; }
	/** Returns true if this object will always be drawn on top. */
	public default boolean isAlwaysOnTop() { return properties().isAlwaysOnTop; }
	/** Returns true if this object is resizeable. */
	public default boolean isResizeable() { return properties().isResizeable; }
	/** Returns true if this object's position cannot be modified. */
	public default boolean isMoveable() { return properties().isMoveable; }
	/** Returns true if this object can be clicked on. */
	public default boolean isClickable() { return properties().isClickable; }
	/** Returns whether this object can be closed or not. */
	public default boolean isClosable() { return properties().isClosable; }
	
	/** Set this object's enabled state. */
	public default void setEnabled(boolean val) { properties().isEnabled = val; }
	/** Set this object's visibility. A non-visible object can still run actions if it is still enabled. */
	public default void setVisible(boolean val) { properties().isVisible = val; }
	/** Sets this object as hidden when the top overlay is not drawn. */
	public default void setHidden(boolean val) { properties().isHidden = val; }
	/** Sets this object to be drawn regardless of it being visible or enabled. */
	public default void setAlwaysVisible(boolean val) { properties().isAlwaysVisible = val; }
	/** Sets this object to always be drawn on top. */
	public default void setAlwaysOnTop(boolean val) { properties().isAlwaysOnTop = val; }
	/** Sets whether this object can be resized or not. */
	public default void setResizeable(boolean val) { properties().isResizeable = val; }
	/** Sets this object's position as unmodifiable. */
	public default void setMoveable(boolean val) { properties().isMoveable = val; }
	/** Specifies if this object can be clicked on. */
	public default void setClickable(boolean val) { properties().isClickable = val; }
	/** Sets whether this object can be closed or not. */
	public default void setCloseable(boolean val) { properties().isClosable = val; }
	
	//=======================
	// Tracked Object States
	//=======================
	
	/** Tracked state of whether or not this object has been initialized. */
	public default boolean isInitialized() { return properties().isInitialized; }
	/** Tracked state of whether or not this object has had its children initialized. */
	public default boolean areChildrenInit() { return properties().areChildrenInit; }
	/** Tracked state of whether or not this object has been drawn at least once. */
	public default boolean hasFirstDraw() { return properties().hasFirstDraw; }
	/** Tracked state of whether or not this object has received focus at least once. */
	public default boolean hasReceivedFocus() { return properties().hasReceivedFocus; }
	/** Tracked state of whether or not this object is currently being added to some parent object. */
	public default boolean isBeingAdded() { return properties().isBeingAdded; }
	/** Tracked state of whether or not this object has been fully added to its parent. */
	public default boolean isAdded() { return properties().isAdded; }
	/** Tracked state of whether or not this object is currently being removed from its current parent object. */
	public default boolean isBeingRemoved() { return properties().isBeingRemoved; }
	/** Tracked state of whether or not this object has been fully removed from its most recent parent object. */
	public default boolean isRemoved() { return properties().isRemoved; }
	/** Tracked state of whether or not this object is in the process of being closed. */
	public default boolean isClosing() { return properties().isClosing; }
	/** Tracked state of whether or not this object has been closed. */
	public default boolean isClosed() { return properties().isClosed; }
	
	//==============
	// Future Tasks
	//==============
	
	/** Returns this object's FutureTaskManager. */
	public default FutureTaskManager getFutureTaskManager() {
		return properties().futureTaskManager;
	}
	
	/**
	 * Adds a task to be executed once the specified future task event
	 * type event is run.
	 * 
	 * @param type The type of future tasks to add to
	 * @param task The task to be eventually performed
	 */
	public default void addFutureTask(FutureTaskEventType type, Runnable task) {
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.addFutureTask(type, task);
	}
	
	/**
	 * Removes all future tasks that were scheduled to run for the given
	 * future task type.
	 * 
	 * @param type The type of future tasks to clear out
	 */
	public default void clearFutureTasks(FutureTaskEventType type) {
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.clearTasks(type);
	}
	
	/**
	 * Returns all of the tasks that are scheduled to be run once the
	 * given future task event type is fired.
	 * 
	 * @param type The type of future tasks to be executed
	 * @return A list of all tasks that will be run for the given type
	 */
	public default EList<Runnable> getFutureTasks(FutureTaskEventType type) {
		var ftm = getFutureTaskManager();
		if (ftm != null) return ftm.getFutureTasks(type);
		return EList.newList();
	}
	
	/**
	 * Internal event fired once this object has been fully initialized with
	 * all of its children.
	 * <p>
	 * Most developers will probably want to override the non-internal 'onInit'
	 * method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onInit_i() {
		properties().isInitialized = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_INIT);
		onInit();
		onPostInit();
	}
	
	/**
	 * Internal event fired once this object has initialized all of its
	 * children.
	 * <p>
	 * Most developers will probably want to override the non-internal
	 * 'onChildrenInit' method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onChildrenInit_i() {
		properties().areChildrenInit = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_CHILDREN_INIT);
		onChildrenInit();
	}
	
	/**
	 * Internal event fired from the top parent when the object is drawn for
	 * the first time.
	 * <p>
	 * Most developers will probably want to override the non-internal
	 * 'onFirstDraw' method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onFirstDraw_i() {
		properties().hasFirstDraw = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_FIRST_DRAW);
		onFirstDraw();
	}
	
	/**
	 * Internal event fired when this object first receives focus.
	 * <p>
	 * Most developers will probably want to override the non-internal
	 * 'onInitialFocusGained' method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onInitialFocusGained_i() {
		properties().hasReceivedFocus = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_INITIAL_FOCUS_GAINED);
		onInitialFocusGained();
	}
	
	/**
	 * Internal event called when this object has actually been added to its
	 * parent.
	 * <p>
	 * Most developers will probably want to override the non-internal
	 * 'onAdded' method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onAdded_i() {
		properties().isAdded = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_ADDED);
		onAdded();
	}
	
	/**
	 * Internal event fired when this object is removed from its parent.
	 * <p>
	 * Most developers will probably want to override the non-internal
	 * 'onRemoved' method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onRemoved_i() {
		properties().isRemoved = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_REMOVED);
		onRemoved();
	}
	
	/**
	 * Internal event fired when this object is closed.
	 * <p>
	 * Most developers will probably want to override the non-internal
	 * 'onClosed' method instead of this one.
	 * <p>
	 * If this method is overridden, future tasks will not be propagated and
	 * internal object properties will not be set unless manually set.
	 */
	public default void onClosed_i() {
		properties().isClosed = true;
		var ftm = getFutureTaskManager();
		if (ftm != null) ftm.runTaskType(FutureTaskEventType.ON_CLOSED);
		onClosed();
	}
	
	/** Called immediately <b>AFTER</b> an object's initialization event. */
	public default void onInit() {}
	/** Called immediately <b>AFTER</b> this object initializes its children. */
	public default void onChildrenInit() {}
	/** Called <b>RIGHT_BEFORE</b> this object is about to be drawn for the first time. */
	public default void onFirstDraw() {}
	/** Called immediately <b>AFTER</b> this object first receives focus. */
	public default void onInitialFocusGained() {}
	/** Called immediately <b>AFTER</b> this object has been added to its parent. */
	public default void onAdded() {}
	/** Called immediately <b>AFTER</b> this object has been removed from its parent. */
	public default void onRemoved() {}
	/** Called immediately <b>AFTER</b> this object has been closed. */
	public default void onClosed() {}
	
	//===========
	// Main Draw
	//===========
	
	/** Internal event fired from the top parent to draw this object. */
	public default void drawObject_i(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (!willBeDrawn()) return;
			
			//draw this object first
			drawObject(mXIn, mYIn);
			
			//now draw all child objects on top of parent
			for (var o : getChildren()) {
				//only draw if the object is actually visible
				if (!o.willBeDrawn() || o.isHidden()) continue;
				
				//notify object on first draw
				if (!o.hasFirstDraw()) o.onFirstDraw_i();
				//actually draw the child object
				o.drawObject_i(mXIn, mYIn);
				
				//draw grayed out overlay over everything if a focus lock object is present
				var top = getTopParent();
				if (top == null) continue;
				
				var f = top.getFocusLockObject();
				if (f == null) continue;
				
				if (o instanceof WindowHeader && (!o.equals(f) && !f.getAllChildren().contains(o))) {
					Dimension_d d = o.getDimensions();
					RenderingManager.drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Called when this object specifically (not children) is being drawn. */
	public default void drawObject(int mXIn, int mYIn) {}
	
	/**
	 * Event fired from this object's pre draw setup to perform
	 * cursorImage changes.
	 */
	public default void updateCursorImage() {
		//make sure that the window isn't maximized
		if (this instanceof IWindowParent p && p.getMaximizedPosition() == ScreenLocation.TOP) {
			CursorHelper.reset();
			return;
		}
		
		if (isResizeable() && getTopParent().getModifyType() != ObjectModifyType.RESIZE) {
			//double rStartY = obj.hasHeader() ? obj.getHeader().startY : obj.getDimensions().startY;
			if (!Mouse.isButtonDown(0)) {
				switch (getEdgeSideMouseIsOn()) {
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
	
	//====================
	// Mouse Hover Checks
	//====================
	
	/** Gets the hover text. */
	public default String getHoverText() { return properties().hoverText; }
	/** Gets the hover text color. */
	public default int getHoverTextColor() { return properties().hoverTextColor; }
	
	/** Sets generic mouse hovering background with specified text. */
	public default void setHoverText(String textIn) { properties().hoverText = textIn; }
	/** Sets hover text color. */
	public default void setHoverTextColor(int colorIn) { properties().hoverTextColor = colorIn; }
	
	/**
	 * Event fired from the top parent when the mouse has been hovering
	 * over this object for a short period of time.
	 */
	public default void onMouseHover(int mX, int mY) {
		if (properties().hoverText == null || properties().hoverText.isEmpty()) return;
			
		int strWidth = FontRenderer.strWidth(properties().hoverText);
		double sX = mX + 20.0;
		double sY = mY - FontRenderer.FONT_HEIGHT / 2;
		
		sX = sX < 0 ? 1 : sX;
		//sY = (sY - 7) < 2 ? 2 + 7 : sY;
		if (sX + strWidth + 10 > Envision.getWidth()) {
			sX = -1 + sX - (sX + strWidth + 10 - Envision.getWidth() + 6);
			sY -= 10;
		}
		sY = sY + 16 > Envision.getHeight() ? -2 + sY - (sY + 16 - Envision.getHeight() + 6) : sY;
		
		double eX = sX + strWidth + 10;
		double eY = sY + FontRenderer.FONT_HEIGHT + 4;
		
		RenderingManager.drawRect(sX, sY, eX, eY, EColors.black);
		RenderingManager.drawRect(sX + 1, sY + 1, eX - 1, eY - 1, EColors.vdgray);
		RenderingManager.drawString(properties().hoverText, sX + 5, sY + 4, properties().hoverTextColor);
	}

	/**
	 * Hook that indicates when the object is drawing its hovering layer.
	 */
	public default boolean isDrawingHover() {
		var tp = getTopParent();
		return tp != null && this.equals(tp.getHoveringObject());
	}
	
	/**
	 * Called right before the next draw cycle will occur so that any
	 * updates can be performed safely outside of the draw loop.
	 */
	public default void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		instance().res = Envision.getWindowDims();
		
		//check for mouse entered event
		if (!properties().mouseEntered && isMouseOver()) {
			properties().mouseEntered = true;
			mouseEntered(mXIn, mYIn);
		}
		
		//check for mouse exited event
		if (properties().mouseEntered && !isMouseOver()) {
			properties().mouseEntered = false;
			mouseExited(mXIn, mYIn);
		}
		
		//remove all children scheduled to be removed
		if (!properties().childrenToBeRemoved.isEmpty()) {
			for (var o : properties().childrenToBeRemoved) {
				//prevent null removals, self removals, and non-children from being removed
				if (o == null || o == this || !getChildren().contains(o)) continue;
				
				o.properties().isBeingRemoved = true;
				properties().children.remove(o);
				o.onRemoved_i();
				o.properties().isBeingRemoved = false;
				postEvent(new EventObjects(this, o, ObjectEventType.OBJECT_REMOVED));
			}
			properties().childrenToBeRemoved.clear();
		}
		
		//add all children scheduled to be added
		if (!properties().childrenToBeAdded.isEmpty()) {
			for (var o : properties().childrenToBeAdded) {
				//prevent null additions, self additions, and already existing children from being added
				if (o == null || o == this || getChildren().contains(o)) continue;
				
				o.properties().isBeingAdded = true;
				properties().children.add(o);
				o.onAdded_i();
				o.properties().isBeingAdded = false;
				postEvent(new EventObjects(this, o, ObjectEventType.OBJECT_ADDED));
			}
			properties().childrenToBeAdded.clear();
		}
	}
	
	//============
	// Object IDs
	//============
	
	/** Returns this object's set ID number. */
	public default long getObjectID() { return properties().objectId; }
	/** Returns the name of this object. */
	public default String getObjectName() { return properties().objectName; }
	
	/** Sets the name of this object. */
	public default void setObjectName(String nameIn) { properties().objectName = nameIn; }
	
	//================
	// Drawing Checks
	//================
	
	/** Returns true if this object will be drawn on the next draw cycle. */
	public default boolean willBeDrawn() {
		if (properties().isAlwaysVisible) return true;
		
		boolean val = true;
		
		var p = getParent();
		if (p != null) val &= p.willBeDrawn();
		
		val &= properties().isVisible;
		val &= !properties().isHidden;
		
		return val;
	}
	
	/** Returns true if this object's mouse checks are enforced by a boundary. */
	public default boolean isBoundaryEnforced() { return properties().boundaryDimension != null; }
	
	/** Returns true if this object is resizing. */
	public default boolean isResizing() {
		var t = getTopParent();
		var modObj = t.getModifyingObject();
		var modType = t.getModifyType();
		return modObj == this && modType == ObjectModifyType.RESIZE && Mouse.isLeftDown();
	}
	
	/** Returns true if this object is moving. */
	public default boolean isMoving() {
		var t = getTopParent();
		return t.getModifyingObject() == this && t.getModifyType() == ObjectModifyType.MOVE;
	}
	
	//========
	// Header
	//========
	
	/** Returns true if this object has a header. */
	public default boolean hasHeader() { return properties().objectHeader != null; }
	/** If this object has a header, returns the header object, otherwise returns null. */
	public default WindowHeader getHeader() { return properties().objectHeader; }
	
	//===================
	// Size And Position
	//===================
	
    /**
     * Returns the dimensions of this object without dimension boundary
     * limitations.
     * <p>
     * Specifically useful for maintaining object aspect ratio when screen
     * dimensions are changed.
     */
	public default Dimension_d getUnboundedDimensions() {
	    return instance().getUnboundedDimensions();
	}
	
	/** Returns the current dimensions of this object. */
	public default Dimension_d getDimensions() { return instance().getDimensions(); }
	/** Returns the current position of this object. */
	public default Point2d getPosition() { return instance().getGuiPosition(); }
	/** Returns the position this object will relocate to when reset. */
	public default Point2d getInitialPosition() { return instance().getGuiInitialPosition(); }
	/** Returns the minimum width and height that this object can have. */
	public default Point2d getMinDims() { return instance().getMinDims(); }
	/** Returns the maximum width and height that this object can have. */
	public default Point2d getMaxDims() { return instance().getMaxDims(); }
	/** Returns the minimum width that this object can have. */
	public default double getMinWidth() { return instance().getMinWidth(); }
	/** Returns the minimum height that this object can have. */
	public default double getMinHeight() { return instance().getMinHeight(); }
	/** Returns the maximum width that this object can have. */
	public default double getMaxWidth() { return instance().getMaxWidth(); }
	/** Returns the maximum height that this object can have. */
	public default double getMaxHeight() { return instance().getMaxHeight(); }
	
	/** Specifies this objects position, width, and height using an EDimension object. */
	public default void setDimensions(Dimension_d dimIn) { instance().setDimensions(dimIn); }
	/** Specifies this objects position, width, and height. (x, y, width, height) */
	public default void setDimensions(double x, double y, double w, double h) { instance().setDimensions(x, y, w, h); }
	/** Specifies the position this object will relocate to when its' position is reset. */
	public default void setInitialPosition(double x, double y) { instance().setGuiInitialPosition(x, y); }
	/** Sets both the minimum width and height for this object. */
	public default void setMinDims(double w, double h) { instance().setMinDims(w, h); }
	/** Sets both the maximum width and height for this object. */
	public default void setMaxDims(double w, double h) { instance().setMaxDims(w, h); }
	/** Sets the minimum width for this object when resizing. */
	public default void setMinWidth(double w) { instance().setMinWidth(w); }
	/** Sets the minimum height for this object when resizing. */
	public default void setMinHeight(double h) { instance().setMinHeight(h); }
	/** Sets the maximum width for this object when resizing. */
	public default void setMaxWidth(double w) { instance().setMaxWidth(w); }
	/** Sets the maximum height for this object when resizing. */
	public default void setMaxHeight(double h) { instance().setMaxHeight(h); }

	/** Specifies this object's width and height based on the current starting position. */
	public default void setSize(double widthIn, double heightIn) { instance().setGuiSize(widthIn, heightIn); }
	
	/** Centers the object around the center of the screen with proper dimensions. */
	public default void centerObjectWithSize(double w, double h) { instance().centerGuiWithSize(w, h); }
	/** Moves this object back to it's initial position that it had upon its creation. */
	public default void resetPosition() { instance().resetPosition(); }

	/**
	 * Returns true if the parent of this object is moving in regards to
	 * its top parent
	 * 
	 * @return True if base object's parent is moving
	 */
	public default boolean isParentMoving() {
		return isObjectMoving(getParent());
	}
	
	/** Moves the object by the specified x and y values. Does not move the
	 *  object to specified coordinates however. Use setPosition() instead. */
	public default void move(double newX, double newY) {
		//only move if actually move-able and there is a value to move by
		if (!isMoveable() || (newX == 0 && newY == 0)) return;
		
		//post moving event
		postEvent(new EventModify(this, this, ObjectModifyType.MOVE));
		
		//get all of the children in the object that aren't locked in place
		for (var o : getCombinedChildren().filter(o -> o.isMoveable())) {
			//only move the window if it moves with the parent
			if (o instanceof WindowParent p) {
				if (p.movesWithParent()) o.move(newX, newY);
			}
			else o.move(newX, newY);
		}
		
		Dimension_d d = getUnboundedDimensions();
		//offset the original position by the specified offset
		setDimensions(d.startX + newX, d.startY + newY, d.width, d.height);
		
		//also move the boundary enforcer, if there is one
		if (isBoundaryEnforced()) {
			Dimension_d b = getBoundaryEnforcer();
			getBoundaryEnforcer().setPosition(b.startX + newX, b.startY + newY);
		}
	}
	
	/** Resizes this object by an amount in both the x and y axis, specified
	 *  by the given Direction. */
	public default void resize(double xIn, double yIn, ScreenLocation areaIn) {
		postEvent(new EventModify(this, this, ObjectModifyType.RESIZE)); //post an event
		//make sure that there is actually a change in the cursor position
		if (xIn == 0 && yIn == 0) return;

		Dimension_d d = getUnboundedDimensions();
		double minW = getMinWidth();
		double minH = getMinHeight();
		double maxW = getMaxWidth();
		double maxH = getMaxHeight();
		double x = 0, y = 0, w = 0, h = 0;
		
		//boolean e = false, s = false;
		//perform resizing on different sides depending on the side that's being resized
		switch (areaIn) {
		case TOP: x = d.startX; y = d.startY + yIn; w = d.width; h = d.height - yIn; break;
		case BOT: x = d.startX; y = d.startY; w = d.width; h = d.height + yIn; break;
		case RIGHT: x = d.startX; y = d.startY; w = d.width + xIn; h = d.height; break;
		case LEFT: x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height; break;
		case TOP_RIGHT: x = d.startX; y = d.startY + yIn; w = d.width + xIn; h = d.height - yIn; break;
		case BOT_RIGHT: x = d.startX; y = d.startY; w = d.width + xIn; h = d.height + yIn; break;
		case TOP_LEFT: x = d.startX + xIn; y = d.startY + yIn; w = d.width - xIn; h = d.height - yIn; break;
		case BOT_LEFT: x = d.startX + xIn; y = d.startY; w = d.width - xIn; h = d.height + yIn; break;
		default: break;
		}
		
		//restrict the object to its allowed minimum width
		if (w < minW) {
			w = minW;
			switch (areaIn) {
			case RIGHT: case TOP_RIGHT: case BOT_RIGHT: x = d.startX; break;
			case LEFT: case TOP_LEFT: case BOT_LEFT: x = d.endX - w; break;
			default: break;
			}
		}
		
		//restrict the object to its allowed maximum width
		if (w > maxW) {
			w = maxW;
			switch (areaIn) {
			case RIGHT: case TOP_RIGHT: case BOT_RIGHT: x = d.startX; break;
			case LEFT: case TOP_LEFT: case BOT_LEFT: x = d.endX - w; break;
			default: break;
			}
		}
		
		//restrict the object to its allowed minimum height
		if (h < minH) {
			h = minH;
			switch (areaIn) {
			case TOP: case TOP_RIGHT: case TOP_LEFT: y = d.endY - h; break;
			case BOT: case BOT_RIGHT: case BOT_LEFT: y = d.startY; break;
			default: break;
			}
		}
		
		//restrict the object to its allowed maximum height
		if (h > maxH) {
			h = maxH;
			switch (areaIn) {
			case TOP: case TOP_RIGHT: case TOP_LEFT: y = d.endY - h; break;
			case BOT: case BOT_RIGHT: case BOT_LEFT: y = d.startY; break;
			default: break;
			}
		}
		
		//set the dimensions of the object to the resized dimensions
		setDimensions(x, y, w, h);
		
		//(lazy approach) re-make all the children based on the resized dimensions
		reInitChildren();
		var top = getTopParent();
		if (top != null) top.setFocusedObject(this);
	}
	
    /**
     * Move this object and all of its children to the specified x and y
     * coordinates. The specified position represents the top left corner of
     * this object. All children will remain in their original positions
     * relative to the parent object.
     */
	public default void setPosition(double newX, double newY) {
	    setPosition(newX, newY, true);
	}

	public default void setPosition(double newX, double newY, boolean force) {
		//only move this object and its children if it is move-able
		if (!force && !isMoveable()) return;
		
		Dimension_d d = getDimensions();
		//the object's current position and relative clickArea for shorter code
		//var loc = new Box2<>(d.startX, d.startY);
		
		//holder to store each object and their relative child locations
		BoxList<IWindowObject, Point2d> previousLocations = new BoxList<>();
		
		//grab all immediate objects
		var objs = getCombinedChildren();
		
		//get each of the object's children's relative positions and click-able areas relative to each child
		for (var o : objs) {
			var dims = o.getDimensions();
			var prev = new Point2d(dims.startX - d.startX, dims.startY - d.startY);
			previousLocations.add(o, prev);
		}
		
		//apply the new location to parent
		setDimensions(newX, newY, d.width, d.height); //move the object to the new position
		
		//apply the new location to each child
		for (var o : objs) {
			//don't move the child if its position is locked
			if (!o.isMoveable()) continue;
			
			var oldLoc = previousLocations.getBoxWithA(o).getB();
			//move the child to the new location with the parent's offset
			o.setPosition(newX + oldLoc.x, newY + oldLoc.y); 
		}
	}
	
	//===============
	// Object Groups
	//===============
	
	/** Returns this object's object group, if any. */
	public default EObjectGroup getObjectGroup() { return properties().objectGroup; }
	/** Sets this object's object group. */
	public default void setObjectGroup(EObjectGroup groupIn) { properties().objectGroup = groupIn; }
	/** Event fired when any object within the object group fires an event. */
	public default void onGroupNotification(ObjectEvent e) {}
	
	//==========
	// Children
	//==========

	/** Returns a list of all objects that are directly children of this object. */
	public default EList<IWindowObject> getChildren() { return properties().children; }
	/** Returns a list of all objects that are going to be added on the next draw cycle */
	public default EList<IWindowObject> getAddingChildren() { return properties().childrenToBeAdded; }
	/** Returns a list of all objects that are going to be removed on the next draw cycle */
	public default EList<IWindowObject> getRemovingChildren() { return properties().childrenToBeRemoved; }

	/** Returns a list of all immediate children on this object that will be drawn. */
	public default EList<IWindowObject> getVisibleChildren() {
	    return getChildren().filter(c -> c.willBeDrawn());
	}
	
	/** Returns a list of all children that descend from this parent. */
	public default EList<IWindowObject> getAllChildren() {
		EList<IWindowObject> foundObjs = EList.newList();
		EList<IWindowObject> objsWithChildren = EList.newList();
		EList<IWindowObject> workList = EList.newList();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		getChildren().forEach(o -> {
			foundObjs.add(o);
			if (!o.getCombinedChildren().isEmpty()) objsWithChildren.add(o);
		});
		
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getCombinedChildren()));
		
		//only work as long as there are still child layers to process
		while (workList.isNotEmpty()) {
			//update the foundObjs
			foundObjs.addAll(workList);
			
			//for the current layer, find all objects that have children
			objsWithChildren.clear();
			workList.filterForEach(o -> o.getCombinedChildren().isNotEmpty(), objsWithChildren::add);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getCombinedChildren()));
		}
		
		return foundObjs;
	}
	
	/** Returns a list of all children that descend from this parent. */
	public default EList<IWindowObject> getAllVisibleChildren() {
		if (!willBeDrawn()) return EList.newList();
		
		EList<IWindowObject> foundObjs = EList.newList();
		EList<IWindowObject> objsWithChildren = EList.newList();
		EList<IWindowObject> workList = EList.newList();
		
		//grab all immediate children and add them to foundObjs, then check if any have children of their own
		getChildren().forEach(o -> {
			if (o.willBeDrawn()) foundObjs.add(o);
			if (!o.getCombinedChildren().isEmpty()) objsWithChildren.add(o);
		});
		
		//load the workList with every child found on each object
		objsWithChildren.forEach(c -> workList.addAll(c.getCombinedChildren()));
		
		//only work as long as there are still child layers to process
		while (workList.isNotEmpty()) {
			//update the foundObjs
			workList.filterForEach(o -> o.willBeDrawn(), foundObjs::add);
			
			//for the current layer, find all objects that have children
			objsWithChildren.clear();
			workList.filterForEach(o -> o.getCombinedChildren().isNotEmpty(), objsWithChildren::add);
			
			//put all children on the next layer into the work list
			workList.clear();
			objsWithChildren.forEach(c -> workList.addAll(c.getCombinedChildren()));
		}
		
		return foundObjs;
	}
	
	/**
	 * Returns a list of all children from 'getAllChildren()' that are
	 * currently under the mouse.
	 */
	public default EList<IWindowObject> getAllChildrenUnderMouse() {
		//only add objects if they are visible and if the cursor is over them.
		return getAllVisibleChildren().filterNull(o -> o.isMouseInside());
	}
	
	/**
	 * Returns true if the specified object is a child of the parent or is
	 * being added to the parent.
	 */
	public default boolean containsObject(IWindowObject object) {
		return getCombinedChildren().contains(object);
	}
	
	/** Returns true if the specified object type is a child of the
	 *  parent or is being added to the parent. */
	public default boolean containsObject(Class<?> objIn) {
		return (objIn != null) && getAllChildren().anyMatch(objIn::isInstance);
	}
	
	/** Checks if this object is a child of the specified object. */
	public default boolean isChildOf(IWindowObject parent) {
		//prevent checking if there is nothing to check against
		if (parent == null) return false;
		
		//keep track of current parent
		var parentObj = getParent();
		
		//recursively check through the object's parent lineage to see if that parent is the possible parent
		while (parentObj != null) {
			if (parentObj == parent) return true;
			//check for infinite loops
			if (parentObj == parentObj.getParent()) break;
			parentObj = parentObj.getParent();
		}
		
		return false;
	}
	
	/**
	 * Adds a child IWindowObject to this object. The object is added
	 * before the next draw cycle.
	 * <p>
	 * This method starts the process of adding a child to this object.
	 * Children are fully added on the next draw cycle. There is an issue
	 * where a child of a child can be added to the parent again.
	 * 
	 * @param objs The objects to add as children
	 */
	public default void addObject(IWindowObject... objs) {
		for (var o : objs) {
			// prevent null additions
			if (o == null) continue;
			// prevent self additions
			if (o == this) continue;
			// don't add if already being removed
			if (getRemovingChildren().contains(o)) continue;
			// don't add if the object is either being added or is already in the object
			// this only goes 1 layer deep however!
			if (getCombinedChildren().contains(o)) {
				System.out.println(this + " already contains " + o + "!");
				continue;
			}
			
			try {
				o.properties().isBeingAdded = true;
				
				// prevent multiple headers being added
				if (o instanceof WindowHeader h) {
				    if (hasHeader()) continue;
				    else properties().objectHeader = h;
				}
				
				// if it's a window, do its init
				if (o instanceof WindowParent p && !o.isInitialized()) p.initWindow();
				
				// initialize all of the children's children
				o.setParent(this);
				o.onPreInit();
				o.initChildren();
				o.onChildrenInit_i();
				
				// if the parent has a boundary enforcer, apply it to the child as well
				if (isBoundaryEnforced()) o.setBoundaryEnforcer(getBoundaryEnforcer());
				
				// give the processed child to the parent so that it will be added
				getAddingChildren().add(o);
				// tell the child that it has been fully initialized and that it is ready to be added on the next draw cycle
				o.onInit_i();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Removes a child IWindowObject to this object. If this object does
	 * not contain the specified child, no action is performed. The object
	 * is removed before the next draw cycle.
	 */
	public default void removeObject(IWindowObject... objs) {
		EUtil.filterNullForEach(objs, o -> o.properties().isBeingRemoved = true);
		getRemovingChildren().add(objs);
	}
	
    /**
     * Returns a list combining the objects currently within within this
     * object as well as the ones being added.
     */
	public default EList<IWindowObject> getCurrentCombinedChildren() {
	    return EList.combineLists(getChildren(), getAddingChildren());
	}
	
	/**
	 * Returns a list combining the objects currently within within this
	 * object as well as the ones being added.
	 */
	public default EList<IWindowObject> getCombinedChildren() {
		return EList.combineLists(getChildren(), getAddingChildren());
	}
	
	//=========
	// Parents
	//=========
	
	/** Returns this object's direct parent object. */
	public default IWindowObject getParent() { return properties().parent; }
	/** Sets this object's parent. */
	public default void setParent(IWindowObject parentIn) { properties().parent = parentIn; }
	
	/** Returns the top most parent object in the parent chain. */
	public default ITopParent getTopParent() {
		var parentObj = getParent();
		//recursively check through the object's parent lineage to see if that parent is a topParentdw
		while (parentObj != null) {
			if (parentObj instanceof ITopParent top) return top;
			//break if the parent is itself
			if (parentObj == parentObj.getParent()) break;
			parentObj = parentObj.getParent();
		}
		return (this instanceof ITopParent t) ? t : null;
	}
	
	/** Returns the first instance of a WindowParent in the parent chain. */
	public default IWindowParent getWindowParent() {
		var parentObj = getParent();
		//recursively check through the object's parent lineage to see if that parent is a window
		while (parentObj != null && !(parentObj instanceof ITopParent)) {
			if (parentObj instanceof IWindowParent p) return p;
			//break if the parent is itself
			if (parentObj == parentObj.getParent()) break;
			parentObj = parentObj.getParent();
		}
		return (this instanceof IWindowParent p) ? p : null;
	}
	
	//=======
	// Focus
	//=======
	
	/**
	 * Returns the object that will receive focus by default when the base
	 * object has focus transfered to it.
	 */
	public default IWindowObject getDefaultFocusObject() {
		return properties().defaultFocusObject;
	}
	
	/**
	 * Sets a default focus object for this object. When the main object
	 * receives focus, the top parent will attempt to transfer focus to
	 * the specified default focus object.
	 */
	public default void setDefaultFocusObject(IWindowObject objectIn) {
		properties().defaultFocusObject = objectIn;
	}
	
	/**
	 * Returns true if this object is the current focus owner in it's top
	 * parent object.
	 */
	public default boolean hasFocus() {
		var fo = getTopParent().getFocusedObject();
		return (fo != null) && fo.equals(this);
	}
	
	/**
	 * Signals the top parent to transfer focus from this object to the
	 * top parent's default focus object on the next draw cycle. If this
	 * object has a focus lock set, the lock will be removed and focus
	 * will be transfered to the top parent's default focus object on the
	 * next draw cycle.
	 */
	public default boolean relinquishFocus() {
		var t = getTopParent();
		if (t.doesFocusLockExist()) {
			if (t.getFocusLockObject().equals(this)) {
				t.setObjectRequestingFocus(t, FocusType.TRANSFER);
				return true;
			}
			return false;
		}
		t.setObjectRequestingFocus(t, FocusType.TRANSFER);
		return true;
	}
	
	/**
	 * Focus event that is called when this object is given focus from its
	 * top parent.
	 */
	public default void onFocusGained(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.GAINED));
		//check if this is the first time this object has received parent focus
		if (!hasReceivedFocus()) onInitialFocusGained_i();
		
		if (eventIn.getFocusType().equals(FocusType.MOUSE_PRESS)) {
			mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode());
			var t = getTopParent();
			
			//check if eligible for a double click event
			if (eventIn.getActionCode() == 0) {
				var lastClicked = t.getLastClickedChild();
				if (lastClicked == this) {
					long clickTime = t.getLastChildClickTime();
					
					if (System.currentTimeMillis() - clickTime <= 400) {
						onDoubleClick();
					}
				}
			}
			
			t.setLastClickedChild(this);
			t.setLastChildClickTime(System.currentTimeMillis());
		}
		var default_obj = getDefaultFocusObject();
		if (default_obj != null) default_obj.requestFocus();
	}
	
	/**
	 * Focus event that is called when this object loses focus in any way.
	 */
	public default void onFocusLost(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.LOST));
	}
	
	/**
	 * Signals the top parent to transfer focus from this object to the
	 * object specified on the next draw cycle.
	 */
	public default void transferFocus(IWindowObject objIn) {
		var t = getTopParent();
		
		if (t.doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) {
			if (objIn != null) {
				t.clearFocusLockObject();
				t.setObjectRequestingFocus(objIn, FocusType.TRANSFER);
			}
		}
		else if (objIn != null) t.setObjectRequestingFocus(objIn, FocusType.TRANSFER);
	}
	
	/**
	 * Used to draw a visible border around an object whose focus is
	 * locked. A focus lock does not need to be in place in order for this
	 * to be called however.
	 */
	public default void drawFocusLockBorder() {
		if (willBeDrawn() && getChildren().containsNoInstanceOf(FocusLockBorder.class)) {
			WindowHeader h = getHeader();
			Dimension_d dims = getDimensions();
			FocusLockBorder flb;
			
			if (h != null && h.isEnabled()) {
				flb = new FocusLockBorder(this, h.startX, h.startY, dims.width, dims.height + h.height);
			}
			else flb = new FocusLockBorder(this);
			
			addObject(flb);
		}
	}
	
	/**
	 * Signals the top parent to try transferring focus to this object on
	 * the next draw cycle. If another object has a focus lock, this
	 * object will not receive focus
	 */
	public default void requestFocus() {
		requestFocus(FocusType.TRANSFER);
	}
	
	/**
	 * Same as the previous request focus but the exact type of focus
	 * event can be specified.
	 */
	public default void requestFocus(FocusType typeIn) {
		getTopParent().setObjectRequestingFocus(this, typeIn);
	}
	
	//==============
	// Mouse Checks
	//==============
	
	/** Specifies a region that this object will adhere to for mouse checks. */
	public default void setBoundaryEnforcer(Dimension_d dimIn) { properties().setBoundaryEnforcer(dimIn); }
	/** Returns the boundary for which this object is bounded by. */
	public default Dimension_d getBoundaryEnforcer() { return properties().boundaryDimension; }
	
	/** Returns true if the mouse is on the edge of an object. */
	public default boolean isMouseOnEdge(int mX, int mY) {
		return willBeDrawn() && getEdgeSideMouseIsOn() != ScreenLocation.OUT;
	}
	
	/**
	 * Returns the edge that the mouse is currently hovering over, if any.
	 */
	public default ScreenLocation getEdgeSideMouseIsOn() {
		Dimension_d d = getDimensions();
		double rStartY = hasHeader() ? getHeader().startY : d.startY;
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		
		if (mX >= d.startX - 5 && mX <= d.endX + 5 && mY >= rStartY - 5 && mY <= d.endY + 4) {
		    boolean left = false, right = false, top = false, bottom = false;
		    
			if (mX >= d.startX - 5 && mX <= d.startX) left = true;
			else if (mX >= d.endX - 4 && mX <= d.endX + 5) right = true;
			
			if (mY >= rStartY - 6 && mY <= rStartY) top = true;
			else if (mY >= d.endY - 4 && mY <= d.endY + 4) bottom = true;
			
			if (left) {
				if (top) return ScreenLocation.TOP_LEFT;
				else if (bottom) return ScreenLocation.BOT_LEFT;
				else return ScreenLocation.LEFT;
			}
			else if (right) {
				if (top) return ScreenLocation.TOP_RIGHT;
				else if (bottom) return ScreenLocation.BOT_RIGHT;
				else return ScreenLocation.RIGHT;
			} 
			else if (top) return ScreenLocation.TOP;
			else if (bottom) return ScreenLocation.BOT;
		}
		
		return ScreenLocation.OUT;
	}
	
	/** Event fired upon the mouse entering this object. */
	public default void mouseEntered(int mX, int mY) {
		postEvent(new EventMouse(this, mX, mY, -1, MouseType.ENTERED));
	}
	
	/** Event fired upon the mouse exiting this object. */
	public default void mouseExited(int mX, int mY) {
		postEvent(new EventMouse(this, mX, mY, -1, MouseType.EXITED));
	}
	
	/**
	 * Returns true if the mouse is currently inside this object
	 * regardless of z-level. If a boundary enforcer is set, this method
	 * will return true if the mouse is inside of the the specified
	 * boundary.
	 */
	public default boolean isMouseInside() { return isMouseInside(Mouse.getMx(), Mouse.getMy(), 0.0); }
	public default boolean isMouseInside(double threshold) { return isMouseInside(Mouse.getMx(), Mouse.getMy(), threshold); }
	public default boolean isMouseInside(int mX, int mY) { return isMouseInside(mX, mY, 0.0); }	
	public default boolean isMouseInside(int mX, int mY, double threshold) {
	       //if the top renderer is being drawn and this object is not a child of the top renderer -- ignore
        //if (Envision.getTopScreen().hasFocus() && !isChildOf(Envision.getTopScreen())) return false;
        
        final WindowObject instance = instance();
        final double startX = instance.startX - threshold;
        final double startY = instance.startY - threshold;
        final double endX = instance.endX + threshold;
        final double endY = instance.endY + threshold;
        
        // check if there is a boundary enforcer limiting the overall area
        if (isBoundaryEnforced()) {
            final Dimension_d b = getBoundaryEnforcer();
            return mX >= startX && mX >= b.startX &&
                   mX <= endX && mX <= b.endX &&
                   mY >= startY && mY >= b.startY &&
                   mY <= endY && mY <= b.endY;
        }
        
        // otherwise just check if the mouse is within the object's boundaries
        return mX >= startX && mX <= endX && mY >= startY && mY <= endY;
	}
	
	/**
	 * Returns true if the mouse is currently inside this object and that
	 * this is the top most object inside of the parent.
	 */
	public default boolean isMouseOver() {
	    var h = getTopParent().getHighestZObjectUnderMouse();
		return isMouseInside() && this.equals(h);
	}
	
	/** Sets this object and every child to be click-able or not. */
	public default void setEntiretyClickable(boolean val) {
		getAllChildren().forEach(o -> o.setClickable(val));
		setClickable(val);
	}
	
	//==============
	// Basic Inputs
	//==============
	
	/**
	 * Event fired when the mouse has left clicked on this object at least
	 * 2 times in quick succession.
	 */
	public default void onDoubleClick() {}
	
	//========
	// Events
	//========
	
	/** Used to send some kind of message to this object. */
	public default void sendArgs(Object... args) {
		if (args.length != 1) return;
		if (args[0] instanceof String msg) {
			if (!msg.equalsIgnoreCase("reload")) return;
			
			boolean any = false;
			for (var o : getAllChildren()) {
				if (o.hasFocus()) {
					any = true;
					break;
				}
			}
			
			reInitChildren();
			if (any) requestFocus();
		}
	}
	
	/** Gets the EventHandler. */
	public default ObjectEventHandler getEventHandler() {
		return properties().eventHandler;
	}
	
	/** Register an object that listens to this object's events. */
	public default void registerListener(IWindowObject objIn) {
		var handler = properties().eventHandler;
		if (handler != null) handler.registerObject(objIn);
	}
	
	/** Unregister a listener Object. */
	public default void unregisterListener(IWindowObject objIn) {
		var handler = properties().eventHandler;
		if (handler != null) handler.unregisterObject(objIn);
	}
	
	/** Broadcasts an ObjectEvent on this object. */
	public default boolean postEvent(ObjectEvent e) {
		var handler = properties().eventHandler;
		if (handler != null) return handler.processEvent(e);
		return false;
	}
	
	/** Called on ObjectEvents. */
	public default void onEvent(ObjectEvent e) {}
	
	//========
	// Action
	//========
	
	/** Event called whenever a child IActionObject's action is triggered. */
	public default void actionPerformed(IActionObject object, Object... args) {
		postEvent(new EventAction(this, object, args));
	}
	
	//===============
	// Drag and Drop
	//===============
	
    /** Internal event called whenever an object is being dragged-and-dropped into this object. */
    public default void onDragAndDrop_i(DragAndDropObject objectBeingDropped) {
        if (postEvent(new EventDragAndDrop(objectBeingDropped, this))) {
            onDragAndDrop(objectBeingDropped);
        }
    }
	
	/** Event called whenever an object is being dragged-and-dropped into this object. */
	public default void onDragAndDrop(DragAndDropObject objectBeingDropped) {}
	
    /**
     * If set to return true, permits this object to be receptive to drag
     * and drop events from the system's host file system.
     * <p>
     * This value returns false by default and must be explicitly overrided
     * by an implementing object to enable this functionality.
     * 
     * @return True if system drag and drops are permitted
     */
	public default boolean allowsSystemDragAndDrop() {
	    var wp = getWindowParent();
	    if (wp != null && wp != this) return wp.allowsSystemDragAndDrop();
	    return false;
	}
	
    /**
     * A special type of drag and drop event that occurs when files from
     * the host OS are dragged and dropped onto a the game window and
     * specifically onto this window object.
     * 
     * @param droppedFileNames The list of files being dragged and dropped
     */
	public default void onSystemDragAndDrop(EList<String> droppedFileNames) {
	    var wp = getWindowParent();
	    if (wp != null && wp.allowsSystemDragAndDrop()) {
	        wp.onSystemDragAndDrop(droppedFileNames);
	    }
	}
	
	//==============
	// Close Object
	//==============
	
	/** Returns true if this object will close when the hud closes. */
	public default boolean closesWithHud() { return properties().closesWithHud; }
	/** Sets this object to close when the hud closes. */
	public default void setClosesWithHud(boolean val) { properties().closesWithHud = true; }
	/** Upon closing, this object will attempt to transfer it's focus to the specified object if possible. */
	public default void setFocusedObjectOnClose(IWindowObject objIn) { properties().focusObjectOnClose = objIn; }
	
	/**
	 * Removes this object and all of it's children from the immediate a
	 * parent. Removes any present focus locks on this object and returns
	 * focus back to the top parent.
	 */
	public default void close() {
		close(true);
	}
	
	/**
	 * Removes this object and all of it's children from the immediate a
	 * parent. Removes any present focus locks on this object and returns
	 * focus back to the top parent.
	 */
	public default void close(boolean recursive) {
		//ignore if not actually closable
		if (!isClosable()) return;
		
		postEvent(new EventObjects(this, this, ObjectEventType.CLOSE));
		properties().isClosing = true;
		
		if (recursive) {
			for (var o : getAllChildren()) o.close(false);
		}
		
		var p = getTopParent();
		if (p.doesFocusLockExist() && p.getFocusLockObject().equals(this)) p.clearFocusLockObject();
		if (properties().focusObjectOnClose != null) properties().focusObjectOnClose.requestFocus();
		
		properties().parent.removeObject(this);
		properties().isClosing = false;
		onClosed_i();
	}
	
	//=============
	// Debug Stuff
	//=============
	
	public default void printf(String toPrint, Object... args) { DebugToolKit.printf(toPrint, args); }
	public default void printlnf(String toPrint, Object... args) { DebugToolKit.printlnf(toPrint, args); }
	
	public default void print(Object... toPrint) { DebugToolKit.print(toPrint); }
	public default void println(Object... toPrint) { DebugToolKit.println(toPrint); }
	
	//================
	// Static Methods
	//================
	
	public static void setMoveable(boolean val, IWindowObject... objs) { setVal(o -> o.setMoveable(val), objs); }
	public static void setResizeable(boolean val, IWindowObject... objs) { setVal(o -> o.setResizeable(val), objs); }
	public static void setCloseable(boolean val, IWindowObject... objs) { setVal(o -> o.setCloseable(val), objs); }
	public static void setClickable(boolean val, IWindowObject... objs) { setVal(o -> o.setClickable(val), objs); }
	public static void setHidden(boolean val, IWindowObject... objs) { setVal(o -> o.setHidden(val), objs); }
	public static void setEnabled(boolean val, IWindowObject... objs) { setVal(o -> o.setEnabled(val), objs); }
	public static void setVisible(boolean val, IWindowObject... objs) { setVal(o -> o.setVisible(val), objs); }
	public static void setPersistent(boolean val, IWindowObject... objs) { setVal(o -> o.setAlwaysVisible(val), objs); }
	public static void setHoverText(String text, IWindowObject... objs) { setVal(o -> o.setHoverText(text), objs); }
	
	public static void setVal(Consumer<? super IWindowObject> action, IWindowObject... objs) {
		EUtil.filterNullForEachA(action, objs);
	}

	public static void setMoveable(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setMoveable(val), objs); }
	public static void setResizeable(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setResizeable(val), objs); }
	public static void setCloseable(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setCloseable(val), objs); }
	public static void setClickable(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setClickable(val), objs); }
    public static void setHidden(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setHidden(val), objs); }
    public static void setEnabled(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setEnabled(val), objs); }
    public static void setVisible(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setVisible(val), objs); }
    public static void setPersistent(boolean val, Collection<IWindowObject> objs) { setVal(o -> o.setAlwaysVisible(val), objs); }
    public static void setHoverText(String text, Collection<IWindowObject> objs) { setVal(o -> o.setHoverText(text), objs); }
	
    public static void setVal(Consumer<? super IWindowObject> action, Collection<IWindowObject> objs) {
        EUtil.filterNullForEach(objs, action);
    }
    
	/**
	 * Returns true if the object is moving in regards to its top parent.
	 * 
	 * @param obj The object to check for movement on
	 * @return True if moving
	 */
	public static boolean isObjectMoving(IWindowObject obj) {
		if (obj == null) return false;
		var top = obj.getTopParent();
		return top.getModifyingObject() == obj && top.getModifyType() == ObjectModifyType.MOVE;
	}
	
}
