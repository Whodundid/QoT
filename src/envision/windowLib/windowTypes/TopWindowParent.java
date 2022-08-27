package envision.windowLib.windowTypes;

import java.util.ArrayDeque;
import java.util.Deque;

import envision.inputHandlers.CursorHelper;
import envision.inputHandlers.Mouse;
import envision.renderEngine.GLSettings;
import envision.topOverlay.GameTopScreen;
import envision.windowLib.StaticTopParent;
import envision.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import envision.windowLib.windowTypes.interfaces.ITopParent;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowParent;
import envision.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.windowLib.windowUtil.windowEvents.events.EventFocus;
import envision.windowLib.windowUtil.windowEvents.events.EventObjects;
import envision.windowLib.windowUtil.windowEvents.events.EventRedraw;
import eutil.datatypes.Box2;
import eutil.math.EDimension;
import eutil.misc.ScreenLocation;
import game.QoT;

public class TopWindowParent<E> extends WindowObject<E> implements ITopParent<E> {
	
	//--------
	// Fields
	//--------
	
	protected IWindowObject<?> modifyingObject;
	protected IWindowObject<?> objectRequestingFocus, focusedObject, focusLockObject;
	protected IWindowObject<?> toFront, toBack;
	protected IWindowObject<?> hoveringTextObject;
	protected IWindowObject<?> escapeStopper;
	protected Box2<Integer, Integer> mousePos = new Box2<>();
	protected Box2<Integer, Integer> oldMousePos = new Box2<>();
	protected Deque<EventFocus> focusQueue = new ArrayDeque<>();
	protected ObjectModifyType modifyType = ObjectModifyType.NONE;
	protected ScreenLocation resizingDir = ScreenLocation.OUT;
	protected IWindowParent<?> maximizingWindow;
	protected ScreenLocation maximizingArea = ScreenLocation.OUT;
	protected boolean maximizingHeaderCenter = false;
	protected int mX = 0, mY = 0;
	protected long mouseHoverTime = 0l;
	protected long hoverRefTime = 0l;
	protected IWindowObject<?> lastClickedObject = null;
	protected long lastClickTime = 0l;
	protected long doubleClickThreshold = 500l;
	
	//---------------------------
	// Overrides : IWindowObject
	//---------------------------
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		
		//prime renderer
		GLSettings.pushMatrix();
		GLSettings.enableBlend();
		GLSettings.clearDepth();
		
		if (isVisible()) {
			if (!hasFirstDraw()) onFirstDraw();
			
			//draw debug stuff
			if (QoT.isDebugMode()) drawDebugInfo();
			
			//draw all child objects
			for (var o : getChildren()) {
				if (o.willBeDrawn() && !o.isHidden()) {
					boolean draw = true;
					
					if (o instanceof WindowParent<?> wp) {
						if (!wp.isMinimized() || wp.drawsWhileMinimized()) draw = true;
						else draw = false;
					}
					
					if (draw) {
						GLSettings.colorA(1.0f);
						GLSettings.clearDepth();
						GLSettings.disableScissor();
						
						if (!o.hasFirstDraw()) o.onFirstDraw();
						o.drawObject(mX, mY);
						
						//draw grayed out overlay over everything if a focus lock object is present
						if (focusLockObject != null && !o.equals(focusLockObject)) {
							if (o.isVisible()) {
								EDimension d = o.getDimensions();
								drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
							}
						}
					}
				}
			}
			
			//notify hover object
			if (getHoveringObject() != null) getHoveringObject().onMouseHover(mX, mY);
		}
		
		GLSettings.popMatrix();
	}
	
	//size
	@Override public boolean hasHeader() { return false; }
	@Override public boolean isResizeable() { return false; }
	@Override public WindowHeader<?> getHeader() { return null; }
	@Override public double getMinWidth() { return res.getWidth(); }
	@Override public double getMinHeight() { return res.getHeight(); }
	@Override public double getMaxWidth() { return res.getWidth(); }
	@Override public double getMaxHeight() { return res.getHeight(); }
	@Override public void setMinDims(double widthIn, double heightIn) {}
	@Override public void setMaxDims(double widthIn, double heightIn) {}
	@Override public void setMinWidth(double widthIn) {}
	@Override public void setMinHeight(double heightIn) {}
	@Override public void setMaxWidth(double widthIn) {}
	@Override public void setMaxHeight(double heightIn) {}
	@Override public void setResizeable(boolean val) {}
	@Override public void resize(double xIn, double yIn, ScreenLocation areaIn) {}
	
	//objects
	@Override public boolean isChildOf(IWindowObject<?> objIn) { return false; }
	
	//parents
	@Override public IWindowObject<?> getParent() { return null; }
	@Override public void setParent(IWindowObject<?> parentIn) {}
	@Override public ITopParent<E> getTopParent() { return this; }
	@Override public IWindowParent<?> getWindowParent() { return null; }
	
	//focus
	@Override public boolean hasFocus() { return this.equals(getFocusedObject()); }
	@Override public boolean relinquishFocus() {
		boolean isLock = this.equals(getFocusLockObject());
		if (isLock || doesFocusLockExist()) {
			clearFocusLockObject();
		}
		else if (hasFocus()) {
			if (!isLock) {
				setObjectRequestingFocus(this, FocusType.TRANSFER);
				return true;
			}
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
	public void transferFocus(IWindowObject<?> objIn) {
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
	@Override public void drawFocusLockBorder() {}
	@Override
	public void requestFocus() {
		if (!hasFocus() && !doesFocusLockExist()) setObjectRequestingFocus(this, FocusType.TRANSFER);
	}
	
	//mouse checks
	@Override public void mouseEntered(int mX, int mY) {}
	@Override public void mouseExited(int mX, int mY) {}
	@Override public boolean isMouseInside() { return false; }
	@Override public boolean isMouseOver() { return !isMouseInsideObject(); }
	@Override public void setBoundaryEnforcer(EDimension dimIn) {}
	@Override public EDimension getBoundaryEnforcer() { return getDimensions(); }
	@Override public boolean isClickable() { return false; }
	@Override public void setClickable(boolean valIn) {}
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { getChildren().filterForEach(o -> o.isMouseInside(), o -> o.parseMousePosition(mX, mY)); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticTopParent.mousePressed(this, mX, mY, button, focusQueue); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticTopParent.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) { StaticTopParent.mouseDragged(this, mX, mY, button, timeSinceLastClick); }
	@Override public void mouseScrolled(int change) { StaticTopParent.mouseScrolled(this, mX, mY, change); }
	@Override public void onDoubleClick() {}
	@Override public void keyPressed(char typedChar, int keyCode) { StaticTopParent.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { StaticTopParent.keyReleased(this, typedChar, keyCode); }
	public void keyHeld(char typedChar, int keyCode) {}
	
	//close object
	@Override public boolean isClosable() { return false; }
	@Override public boolean isClosed() { return false; }
	@Override public void setCloseable(boolean val) {}
	@Override public void close() { removeAllObjects(); }
	@Override public void setFocusedObjectOnClose(IWindowObject<?> objIn) {}
	
	//------------------------
	// Overrides : ITopParent 
	//------------------------
	
	//drawing
	@Override public void drawDebugInfo() { StaticTopParent.drawDebugInfo(this); }
	
	//draw order
	@Override public void bringObjectToFront(IWindowParent<?> objIn) { toFront = objIn; }
	@Override public void sendObjectToBack(IWindowParent<?> objIn) { toBack = objIn; }
	
	//hovering text
	@Override public void setHoveringObject(IWindowObject<?> objIn) { hoveringTextObject = objIn;}
	@Override public IWindowObject<?> getHoveringObject() { return hoveringTextObject; }
	
	//double click
	@Override public void setLastClickedObject(IWindowObject<?> objectIn) { lastClickedObject = objectIn; }
	@Override public IWindowObject<?> getLastClickedObject() { return lastClickedObject; }
	@Override public void setLastClickTime(long timeIn) { lastClickTime = timeIn; }
	@Override public long getLastClickTime() { return lastClickTime; }
	
	//focus
	@Override public IWindowObject<?> getFocusedObject() { return focusedObject; }
	@Override public void setFocusedObject(IWindowObject<?> objIn) { focusedObject = objIn; }
	@Override public void setObjectRequestingFocus(IWindowObject<?> objIn, FocusType typeIn) { focusQueue.add(new EventFocus(this, objIn, typeIn)); }
	@Override public IWindowObject<?> getFocusLockObject() { return focusLockObject; }
	@Override public void setFocusLockObject(IWindowObject<?> objIn) { focusLockObject = objIn; transferFocus(focusLockObject); }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override public void updateFocus() { StaticTopParent.updateFocus(this, focusQueue); }
	
	//object modification
	@Override public ObjectModifyType getModifyType() { return modifyType; }
	@Override public void setModifyingObject(IWindowObject<?> objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; }
	@Override
	public void setMaximizingWindow(IWindowParent<?> objIn, ScreenLocation areaIn, boolean centerAroundHeader) {
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
	@Override public void setEscapeStopper(IWindowObject<?> obj) { if (obj != this) { escapeStopper = obj; } }
	@Override public IWindowObject getEscapeStopper() { return escapeStopper; }
	
	protected void updateCursor() {
		var highest = getHighestZObjectUnderMouse();
		if (highest != null) highest.updateCursorImage();
		else if (!CursorHelper.isArrow() && modifyType != ObjectModifyType.RESIZE) CursorHelper.reset();
	}
	
	@Override
	public void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		res = QoT.getWindowSize();
		setSize(res.getWidth(), res.getHeight());
		
		mX = mXIn;
		mY = mYIn;
		
		if (this == QoT.getTopRenderer()) {
			if (GameTopScreen.isTopFocused()) updateCursor();
		}
		else if (!GameTopScreen.isTopFocused()) updateCursor();
		
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
				o.onRemoved();
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
				o.onAdded();
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
				modifyingObject.move(mX - mousePos.getA(), mY - mousePos.getB());
				mousePos.set(mX, mY);
				break;
			case RESIZE:
				modifyingObject.resize(mX - mousePos.getA(), mY - mousePos.getB(), resizingDir);
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
							EDimension dims = maximizingWindow.getDimensions();
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
			if (mX == oldMousePos.getA() && mY == oldMousePos.getB() && o == hoveringTextObject) {
				mouseHoverTime = (System.currentTimeMillis() - hoverRefTime);
				if (mouseHoverTime >= 1000) {
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
			if (getHoveringObject() != null) setHoveringObject(null);
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
		int oldW = res.getWidth();
		int oldH = res.getHeight();
		int newW = QoT.getWidth();
		int newH = QoT.getHeight();
		
		res = QoT.getWindowSize();
		setDimensions(0, 0, res.getWidth(), res.getHeight());
		
		for (var p : getAllActiveWindows()) {
			
			EDimension oldDims = p.getDimensions();
			
			double newX = (oldDims.startX * newW) / oldW;
			double newY = (oldDims.startY * newH) / oldH;
			
			p.setPosition(newX, newY);
			p.reInitChildren();
			
			if (p.isMaximized()) p.maximize();
		}
	}
	
}