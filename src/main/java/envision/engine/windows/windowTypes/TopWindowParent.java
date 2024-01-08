package envision.engine.windows.windowTypes;

import java.util.ArrayDeque;
import java.util.Deque;

import envision.Envision;
import envision.engine.inputHandlers.CursorHelper;
import envision.engine.inputHandlers.Mouse;
import envision.engine.windows.StaticTopParent;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowObjects.advancedObjects.header.WindowHeader;
import envision.engine.windows.windowTypes.interfaces.ITopParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.FocusType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectEventType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.engine.windows.windowUtil.windowEvents.events.EventFocus;
import envision.engine.windows.windowUtil.windowEvents.events.EventObjects;
import envision.engine.windows.windowUtil.windowEvents.events.EventRedraw;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.Dimension_d;
import eutil.misc.ScreenLocation;

public class TopWindowParent extends WindowObject implements ITopParent {
	
	//========
	// Fields
	//========
	
	protected IWindowObject modifyingObject;
	protected IWindowObject objectRequestingFocus, focusedObject, focusLockObject;
	protected IWindowObject toFront, toBack;
	protected IWindowObject hoveringTextObject;
	protected IWindowObject escapeStopper;
	protected DragAndDropObject dragAndDropObject;
	protected Point2i mousePos = new Point2i();
	protected Point2i oldMousePos = new Point2i();
	protected Deque<EventFocus> focusQueue = new ArrayDeque<>();
	protected ObjectModifyType modifyType = ObjectModifyType.NONE;
	protected ScreenLocation resizingDir = ScreenLocation.OUT;
	protected IWindowParent maximizingWindow;
	protected ScreenLocation maximizingArea = ScreenLocation.OUT;
	protected boolean maximizingHeaderCenter = false;
	protected int mX = 0, mY = 0;
	protected long mouseHoverTime = 0l;
	protected long hoverRefTime = 0l;
	protected IWindowObject lastClickedObject = null;
	protected long lastClickTime = 0l;
	protected long doubleClickThreshold = 500l;
	
	private final EList<IWindowParent> highlightedWindows = EList.newList();
	
	//===========================
	// Overrides : IWindowObject
	//===========================
	
	//main draw
	@Override
	public void drawObject_i(float dt, int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		if (!willBeDrawn()) return;
		
		//reset highlighted windows
		highlightedWindows.clear();
		
		//update first draw state
		if (!hasFirstDraw()) onFirstDraw_i();
		
		//draw this object first
		drawObject(dt, mX, mY);
		
		//now draw all child objects on top of parent
		for (var o : getChildren()) {
		    drawWindowObject(dt, o);
		}
		
		//draw highlighted window borders
		for (IWindowParent p : highlightedWindows) {
			if (p == null) continue;
			p.drawHighlightBorder();
		}
		
		// draw object being dragged and dropped
		if (dragAndDropObject != null) {
		    if (!dragAndDropObject.hasFirstDraw()) dragAndDropObject.onFirstDraw();
		    dragAndDropObject.drawObject_i(dt, mX, mY);
		}
		
		//notify hover object
		if (getHoveringObject() != null) getHoveringObject().onMouseHover(mX, mY);
		
	    //draw debug stuff on top of object
        //if (Envision.isDebugMode()) drawDebugInfo();
	}
	
	//size
	@Override public boolean hasHeader() { return false; }
	@Override public boolean isResizeable() { return false; }
	@Override public WindowHeader getHeader() { return null; }
	@Override public double getMinWidth() { return res.width; }
	@Override public double getMinHeight() { return res.height; }
	@Override public double getMaxWidth() { return res.width; }
	@Override public double getMaxHeight() { return res.height; }
	@Override public void setMinDims(double widthIn, double heightIn) { /* Empty because irrelevant. */ }
	@Override public void setMaxDims(double widthIn, double heightIn) { /* Empty because irrelevant. */ }
	@Override public void setMinWidth(double widthIn) { /* Empty because irrelevant. */ }
	@Override public void setMinHeight(double heightIn) { /* Empty because irrelevant. */ }
	@Override public void setMaxWidth(double widthIn) { /* Empty because irrelevant. */ }
	@Override public void setMaxHeight(double heightIn) { /* Empty because irrelevant. */ }
	@Override public void setResizeable(boolean val) { /* Empty because irrelevant. */ }
	@Override public void resize(double xIn, double yIn, ScreenLocation areaIn) { /* Empty because irrelevant. */ }
	
	//objects
	@Override public boolean isChildOf(IWindowObject objIn) { return false; }
	
	//parents
	@Override public IWindowObject getParent() { return null; }
	@Override public void setParent(IWindowObject parentIn) { /* Empty because irrelevant. */ }
	@Override public ITopParent getTopParent() { return this; }
	@Override public IWindowParent getWindowParent() { return null; }
	
	//focus
	@Override public boolean hasFocus() { return this.equals(getFocusedObject()); }
	@Override public boolean relinquishFocus() {
		boolean isLock = this.equals(getFocusLockObject());
		if (isLock || doesFocusLockExist()) {
			clearFocusLockObject();
		}
		else if (hasFocus()) {
		    setObjectRequestingFocus(this, FocusType.TRANSFER);
            return true;
		}
		return false;
	}
	@Override
	public void onFocusGained(EventFocus e) {
		postEvent(new EventFocus(this, this, FocusType.GAINED));
		if (e.getFocusType().equals(FocusType.MOUSE_PRESS)) mousePressed(e.getMX(), e.getMY(), e.getActionCode());
		if (getDefaultFocusObject() != null) getDefaultFocusObject().requestFocus();
	}
	@Override public void onFocusLost(EventFocus eventIn) { postEvent(new EventFocus(this, this, FocusType.LOST)); }
	@Override
	public void transferFocus(IWindowObject objIn) {
		if (objIn == getFocusLockObject() || !doesFocusLockExist()) {
			if (objIn != null) {
				if (!objIn.hasFocus()) {
					relinquishFocus();
					//setObjectRequestingFocus(objIn, FocusType.Transfer);
				}
			}
			else setObjectRequestingFocus(this, FocusType.TRANSFER);
		}
	}
	@Override public void drawFocusLockBorder() { /* Empty because irrelevant. */ }
	@Override
	public void requestFocus() {
		if (!hasFocus() && !doesFocusLockExist()) setObjectRequestingFocus(this, FocusType.TRANSFER);
	}
	
	//mouse checks
	@Override public void mouseEntered(int mX, int mY) { /* Empty because irrelevant. */ }
	@Override public void mouseExited(int mX, int mY) { /* Empty because irrelevant. */ }
	@Override public boolean isMouseInside() { return false; }
	@Override public boolean isMouseOver() { return !isMouseInsideObject(); }
	@Override public void setBoundaryEnforcer(Dimension_d dimIn) { /* Empty because irrelevant. */ }
	@Override public Dimension_d getBoundaryEnforcer() { return getDimensions(); }
	@Override public boolean isClickable() { return false; }
	@Override public void setClickable(boolean valIn) { /* Empty because irrelevant. */ }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { getChildren().filterForEach(o -> o.isMouseInside(), o -> o.parseMousePosition(mX, mY)); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticTopParent.mousePressed(this, mX, mY, button, focusQueue); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticTopParent.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) { StaticTopParent.mouseDragged(this, mX, mY, button, timeSinceLastClick); }
	@Override public void mouseScrolled(int change) { StaticTopParent.mouseScrolled(this, mX, mY, change); }
	@Override public void onDoubleClick() { /* Empty because irrelevant. */ }
	@Override public void keyPressed(char typedChar, int keyCode) { StaticTopParent.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { StaticTopParent.keyReleased(this, typedChar, keyCode); }
	public void keyHeld(char typedChar, int keyCode) { /* Empty because irrelevant. */ }
	
	//close object
	@Override public boolean isClosable() { return false; }
	@Override public boolean isClosed() { return false; }
	@Override public void setCloseable(boolean val) { /* Empty because irrelevant. */ }
	//@Override public void close() { removeAllObjects(); }
	@Override public void setFocusedObjectOnClose(IWindowObject objIn) { /* Empty because irrelevant. */ }
	
	//========================
	// Overrides : ITopParent 
	//========================
	
	//drawing
	@Override public void drawDebugInfo() { StaticTopParent.drawDebugInfo(this); }
	
	//draw order
	@Override public void bringObjectToFront(IWindowParent objIn) { toFront = objIn; }
	@Override public void sendObjectToBack(IWindowParent objIn) { toBack = objIn; }
	
	//hovering text
	@Override public void setHoveringObject(IWindowObject objIn) { hoveringTextObject = objIn;}
	@Override public IWindowObject getHoveringObject() { return hoveringTextObject; }
	
	//double click
	@Override public void setLastClickedChild(IWindowObject objectIn) { lastClickedObject = objectIn; }
	@Override public IWindowObject getLastClickedChild() { return lastClickedObject; }
	@Override public void setLastChildClickTime(long timeIn) { lastClickTime = timeIn; }
	@Override public long getLastChildClickTime() { return lastClickTime; }
	
	//focus
	@Override public IWindowObject getFocusedObject() { return focusedObject; }
	@Override public void setFocusedObject(IWindowObject objIn) { focusedObject = objIn; }
	@Override public void setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn) { focusQueue.add(new EventFocus(this, objIn, typeIn)); }
	@Override public IWindowObject getFocusLockObject() { return focusLockObject; }
	@Override public void setFocusLockObject(IWindowObject objIn) { focusLockObject = objIn; transferFocus(focusLockObject); }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override public void updateFocus() { StaticTopParent.updateFocus(this, focusQueue); }
	
	//object modification
	@Override public ObjectModifyType getModifyType() { return modifyType; }
	@Override public void setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; }
	@Override
	public void setMaximizingWindow(IWindowParent objIn, ScreenLocation areaIn, boolean centerAroundHeader) {
		if (objIn != null && objIn.isMaximizable()) {
			maximizingWindow = objIn;
			maximizingArea = areaIn;
			maximizingHeaderCenter = centerAroundHeader;
		}
	}
	@Override public void setResizingDir(ScreenLocation areaIn) { resizingDir = areaIn; }
	@Override public void setModifyMousePos(int mX, int mY) { mousePos.set(mX, mY); }
	@Override public IWindowObject getModifyingObject() { return modifyingObject; }
	@Override public IWindowParent getMaximizingWindow() { return maximizingWindow; }
	@Override public ScreenLocation getMaximizingArea() { return maximizingArea; }
	@Override public boolean getMaximizingHeaderCenter() { return maximizingHeaderCenter; }
	@Override public void clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyType.NONE; }
	
	//close
	@Override public void setEscapeStopper(IWindowObject obj) { if (obj != this) { escapeStopper = obj; } }
	@Override public IWindowObject getEscapeStopper() { return escapeStopper; }
	
	protected void updateCursor() {
		var highest = getHighestZObjectUnderMouse();
		if (highest != null) highest.updateCursorImage();
		else if (!CursorHelper.isArrow() && modifyType != ObjectModifyType.RESIZE) CursorHelper.reset();
	}
	
	@Override
	public void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		res = Envision.getWindowDims();
		setGuiSize(res.width, res.height);
		
		mX = mXIn;
		mY = mYIn;
		
		if (this == Envision.getDeveloperDesktop()) {
			if (DeveloperDesktop.isOpen()) updateCursor();
		}
		else if (!DeveloperDesktop.isOpen()) updateCursor();
		
		//handle mouse hover stuff
		checkMouseHover();
		oldMousePos.set(mX, mY);
		
		//update objects
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
		
		if (escapeStopper != null && getAllChildren().notContains(escapeStopper)) escapeStopper = null;
		
		//update object states
		updateZLayers();
		updateFocus();
		
		if (modifyingObject != null) {
			switch (modifyType) {
			case MOVE:
			case MOVE_ALREADY_CLICKED:
				modifyingObject.move((double) mX - mousePos.x, (double) mY - mousePos.y);
				mousePos.set(mX, mY);
				break;
			case RESIZE:
				modifyingObject.resize((double) mX - mousePos.x, (double) mY - mousePos.y, resizingDir);
				mousePos.set(mX, mY);
				break;
			default: break;
			}
		}
		
		if (maximizingWindow == null) return;
		if (maximizingWindow != null) {
			if (maximizingWindow.isMaximizable()) {
				if (maximizingArea == ScreenLocation.OUT) {
					if (maximizingWindow.isMaximized()) {
						maximizingWindow.setMaximized(maximizingArea);
						maximizingWindow.miniaturize();
						
						if (maximizingHeaderCenter) {
							Dimension_d dims = maximizingWindow.getDimensions();
							var header = maximizingWindow.getHeader();
							double yPos = header != null ? header.height / 2 : 0;
							
							maximizingWindow.setPosition(Mouse.getMx() - dims.width / 2, Mouse.getMy() + yPos);
							maximizingWindow.reInitChildren();
							
							var newHeader = maximizingWindow.getHeader();
							newHeader.setHeaderMoving(true);
							setModifyingObject(maximizingWindow, ObjectModifyType.MOVE);
							setModifyMousePos((int) newHeader.midX, (int) newHeader.midY);
						}
						
						setFocusedObject(maximizingWindow);
					}
				}
				else {
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(maximizingArea);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
				}
			}
			
			maximizingWindow = null;
			maximizingArea = ScreenLocation.OUT;
			maximizingHeaderCenter = false;
		}
	}
	
	public void checkMouseHover() {
		var o = getHighestZObjectUnderMouse();
		if (o != null) {
			if (oldMousePos.compare(mX, mY)) {
				mouseHoverTime = (System.currentTimeMillis() - hoverRefTime);
				if (mouseHoverTime >= 300) {
					setHoveringObject(o);
				}
			}
			else {
				mouseHoverTime = 0l;
				hoverRefTime = System.currentTimeMillis();
				setHoveringObject(null);
			}
		}
		else if (mouseHoverTime > 0l) {
			mouseHoverTime = 0l;
			if (getHoveringObject() != null) {
				setHoveringObject(null);
			}
		}
	}
	
	protected void updateZLayers() {
		if (toFront != null) {
			//move the 'toFront' object to the front
			if (getChildren().contains(toFront)) {
				getChildren().remove(toFront);
				getChildren().add(toFront);
			}
			
			//move things that should always be at the top to the top
			var atTop = getChildren().filter(o -> o.isAlwaysOnTop());
			for (var o : atTop) {
				getChildren().remove(o);
				getChildren().add(o);
			}
			
			toFront = null;
		}
		
		if (toBack != null) {
			//move the 'toBack' object to the back
			if (getChildren().contains(toBack)) {
				//EArrayList<IWindowObject> objects = new EArrayList();
				getChildren().remove(toBack);
				//objects.addAll(guiObjects);
				//guiObjects.clear();
				getChildren().add(getChildren().size() - 1, toBack);
				//guiObjects.addAll(objects);
			}
			
			//move things that should always be at the top to the top
			var atTop = getChildren().filter(o -> o.isAlwaysOnTop());
			for (var o : atTop) {
				getChildren().remove(o);
				getChildren().add(o);
			}
			
			toBack = null;
		}
	}
	
	public void onScreenResized() {
		//handle windows
		var oldW = res.width;
		var oldH = res.height;
		var newW = Envision.getWidth();
		var newH = Envision.getHeight();
		
		res = Envision.getWindowDims();
		setDimensions(0, 0, res.width, res.height);
		
		for (var p : getAllActiveWindows()) {
			
			Dimension_d oldDims = p.getUnboundedDimensions();
			
			double newX = (oldDims.startX * newW) / oldW;
			double newY = (oldDims.startY * newH) / oldH;
			double windowW = (oldDims.width * newW) / oldW;
			double windowH = (oldDims.height * newH) / oldH;
			
			p.setPosition(newX, newY);
			p.setSize(windowW, windowH);
			p.reInitChildren();
			
			if (p.isMaximized()) p.maximize();
		}
	}
	
   protected void drawWindowObject(float dt, IWindowObject o) {
        //only draw if the object is actually visible
        if (!o.willBeDrawn() || o.isHidden()) return;
        boolean draw = true;
        
        if (o instanceof IWindowParent wp) {
            draw = (wp.drawsWhileMinimized() || !wp.isMinimized());
            if (wp.isHighlighted()) highlightedWindows.add(wp);
        }
        
        if (!draw) return;
        
        //notify object on first draw
        if (!o.hasFirstDraw()) o.onFirstDraw_i();
        //actually draw the child object
        o.drawObject_i(dt, mX, mY);
        
        //draw grayed out overlay over everything if a focus lock object is present
        if (focusLockObject != null && !o.equals(focusLockObject)) {
            if (o.isVisible()) {
                drawRect(o.getDimensions(), 0x77000000);
                Dimension_d d = o.getDimensions();
                drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
            }
        }
    }
	
	//===============
	// Drag and Drop
	//===============
	
    /** Sets the list of objects that are being 'dragged and dropped'. */
	@Override public void setDragAndDropObject(DragAndDropObject objects) { dragAndDropObject = objects; }
    /** Removes all drag and drop objects. */
	@Override public void clearDragAndDropObject() { dragAndDropObject = null; }
    /** Returns all objects currently being dragged and dropped. */
	@Override public DragAndDropObject getDragAndDropObject() { return dragAndDropObject; }
	
}