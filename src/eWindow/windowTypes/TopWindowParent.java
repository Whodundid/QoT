package eWindow.windowTypes;

import eWindow.StaticTopParent;
import eWindow.WindowObjectS;
import eWindow.windowObjects.advancedObjects.header.WindowHeader;
import eWindow.windowTypes.interfaces.ITopParent;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowTypes.interfaces.IWindowParent;
import eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import eWindow.windowUtil.windowEvents.events.EventFocus;
import eWindow.windowUtil.windowEvents.events.EventRedraw;
import input.Mouse;
import java.util.ArrayDeque;
import java.util.Deque;
import main.Game;
import openGL_Util.GLSettings;
import util.renderUtil.ScreenLocation;
import util.renderUtil.WindowSize;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;
import util.storageUtil.StorageBox;

public class TopWindowParent extends WindowObject implements ITopParent {
	
	protected IWindowObject modifyingObject;
	protected IWindowObject objectRequestingFocus, focusedObject, focusLockObject;
	protected IWindowObject toFront, toBack;
	protected IWindowObject hoveringTextObject;
	protected IWindowObject escapeStopper;
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	protected StorageBox<Integer, Integer> oldMousePos = new StorageBox();
	protected Deque<EventFocus> focusQueue = new ArrayDeque();
	protected ObjectModifyType modifyType = ObjectModifyType.None;
	protected ScreenLocation resizingDir = ScreenLocation.out;
	protected IWindowParent maximizingWindow;
	protected ScreenLocation maximizingArea = ScreenLocation.out;
	protected boolean maximizingHeaderCenter = false;
	protected int mX = 0, mY = 0;
	protected long mouseHoverTime = 0l;
	protected long hoverRefTime = 0l;
	protected IWindowObject lastClickedObject = null;
	protected long lastClickTime = 0l;
	protected long doubleClickThreshold = 500l;
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		
		//prime renderer
		GLSettings.pushMatrix();
		GLSettings.enableBlend();
		GLSettings.clearDepth();
		
		if (visible) {
			
			//draw debug stuff
			if (Game.isDebugMode()) { drawDebugInfo(); }
			
			//draw all child objects
			for (IWindowObject o : windowObjects) {
				if (o.checkDraw() && !o.isHidden()) {
					boolean draw = true;
					
					if (o instanceof WindowParent) {
						WindowParent wp = (WindowParent) o;
						if (!wp.isMinimized() || wp.drawsWhileMinimized()) { draw = true; }
						else { draw = false; }
					}
					
					if (draw) {
						GLSettings.colorA(1.0f);
						GLSettings.clearDepth();
						GLSettings.disableScissor();
						
						if (!o.hasFirstDraw()) { o.onFirstDraw(); }
						o.drawObject(mX, mY);
						
						//draw greyed out overlay over everything if a focus lock object is present
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
			if (getHoveringObject() != null) { getHoveringObject().onMouseHover(mX, mY); }
		}
		
		GLSettings.popMatrix();
	}
	
	//size
	@Override public boolean hasHeader() { return false; }
	@Override public boolean isResizeable() { return false; }
	@Override public WindowHeader getHeader() { return null; }
	@Override public double getMinWidth() { return res.getWidth(); }
	@Override public double getMinHeight() { return res.getHeight(); }
	@Override public double getMaxWidth() { return res.getWidth(); }
	@Override public double getMaxHeight() { return res.getHeight(); }
	@Override public TopWindowParent setMinDims(double widthIn, double heightIn) { return this; }
	@Override public TopWindowParent setMaxDims(double widthIn, double heightIn) { return this; }
	@Override public TopWindowParent setMinWidth(double widthIn) { return this; }
	@Override public TopWindowParent setMinHeight(double heightIn) { return this; }
	@Override public TopWindowParent setMaxWidth(double widthIn) { return this; }
	@Override public TopWindowParent setMaxHeight(double heightIn) { return this; }
	@Override public TopWindowParent setResizeable(boolean val) { return this; }
	@Override public TopWindowParent resize(double xIn, double yIn, ScreenLocation areaIn) { return this; }
	
	//objects
	@Override public boolean isChildOf(IWindowObject objIn) { return false; }
	
	//parents
	@Override public IWindowObject getParent() { return null; }
	@Override public TopWindowParent setParent(IWindowObject parentIn) { return this; }
	@Override public ITopParent getTopParent() { return this; }
	@Override public IWindowParent getWindowParent() { return null; }
	
	//focus
	@Override public boolean hasFocus() { return getFocusedObject().equals(this); }
	@Override public boolean relinquishFocus() {
		if (doesFocusLockExist() && getFocusLockObject().equals(this)) { clearFocusLockObject(); }
		else if (hasFocus()) {
			if (!getFocusLockObject().equals(this)) { setObjectRequestingFocus(this, FocusType.Transfer); return true; }
		}
		return false;
	}
	@Override
	public void onFocusGained(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.Gained));
		if (eventIn.getFocusType().equals(FocusType.MousePress)) { mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode()); }
		if (defaultFocusObject != null) { defaultFocusObject.requestFocus(); }
	}
	@Override public void onFocusLost(EventFocus eventIn) { postEvent(new EventFocus(this, this, FocusType.Lost)); }
	@Override
	public void transferFocus(IWindowObject objIn) {
		if (!doesFocusLockExist()) {
			if (objIn != null) {
				relinquishFocus();
				setObjectRequestingFocus(objIn, FocusType.Transfer);
			}
			else { setObjectRequestingFocus(this, FocusType.Transfer); }
		}
	}
	@Override public void drawFocusLockBorder() {}
	@Override
	public TopWindowParent requestFocus() {
		if (!hasFocus() && !doesFocusLockExist()) { setObjectRequestingFocus(this, FocusType.Transfer); }
		return this;
	}
	
	//mouse checks
	@Override public void mouseEntered(int mX, int mY) {}
	@Override public void mouseExited(int mX, int mY) {}
	@Override public boolean isMouseInside(int mX, int mY) { return false; }
	@Override public boolean isMouseOver(int mX, int mY) { return !isMouseInsideObject(); }
	@Override public TopWindowParent setBoundaryEnforcer(EDimension dimIn) { return this; }
	@Override public EDimension getBoundaryEnforcer() { return getDimensions(); }
	@Override public boolean isClickable() { return false; }
	@Override public TopWindowParent setClickable(boolean valIn) { return this; }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { windowObjects.filterForEach(o -> o.isMouseInside(mX, mY), o -> o.parseMousePosition(mX, mY)); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticTopParent.mousePressed(this, mX, mY, button, focusQueue); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticTopParent.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) { StaticTopParent.mouseDragged(this, mX, mY, button, timeSinceLastClick); }
	@Override public void mouseScrolled(int change) { StaticTopParent.mouseScrolled(this, mX, mY, change); }
	@Override public void onDoubleClick() {}
	@Override public void keyPressed(char typedChar, int keyCode) { StaticTopParent.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { StaticTopParent.keyReleased(this, typedChar, keyCode); }
	public void keyHeld(char typedChar, int keyCode) {}
	
	//close object
	@Override public boolean isCloseable() { return false; }
	@Override public boolean isClosed() { return false; }
	@Override public TopWindowParent setCloseable(boolean val) { return this; }
	@Override public void close() { removeAllObjects(); }
	@Override public void onClosed() {}
	@Override public TopWindowParent setFocusedObjectOnClose(IWindowObject objIn) { return this; }
	
	//--------------------
	//ITopParent Overrides
	//--------------------
	
	//drawing
	@Override public void drawDebugInfo() { StaticTopParent.drawDebugInfo(this); }
	
	//draw order
	@Override public TopWindowParent bringObjectToFront(IWindowParent objIn) { toFront = objIn; return this; }
	@Override public TopWindowParent sendObjectToBack(IWindowParent objIn) { toBack = objIn; return this; }
	
	//hovering text
	@Override public TopWindowParent setHoveringObject(IWindowObject objIn) { hoveringTextObject = objIn; return this; }
	@Override public IWindowObject getHoveringObject() { return hoveringTextObject; }
	
	//double click
	@Override public ITopParent setLastClickedObject(IWindowObject objectIn) { lastClickedObject = objectIn; return this; }
	@Override public IWindowObject getLastClickedObject() { return lastClickedObject; }
	@Override public ITopParent setLastClickTime(long timeIn) { lastClickTime = timeIn; return this; }
	@Override public long getLastClickTime() { return lastClickTime; }
	
	//focus
	@Override public IWindowObject getFocusedObject() { return focusedObject; }
	@Override public TopWindowParent setFocusedObject(IWindowObject objIn) { focusedObject = objIn; return this; }
	@Override public TopWindowParent setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn) { focusQueue.add(new EventFocus(this, objIn, typeIn)); return this; }
	@Override public IWindowObject getFocusLockObject() { return focusLockObject; }
	@Override public TopWindowParent setFocusLockObject(IWindowObject objIn) { focusLockObject = objIn; transferFocus(focusLockObject); return this; }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override public void updateFocus() { StaticTopParent.updateFocus(this, focusQueue); }
	
	//object modification
	@Override public ObjectModifyType getModifyType() { return modifyType; }
	@Override public TopWindowParent setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override
	public TopWindowParent setMaximizingWindow(IWindowParent objIn, ScreenLocation areaIn, boolean centerAroundHeader) {
		if (objIn != null && objIn.isMaximizable()) { maximizingWindow = objIn; maximizingArea = areaIn; maximizingHeaderCenter = centerAroundHeader; }
		return this;
	}
	@Override public TopWindowParent setResizingDir(ScreenLocation areaIn) { resizingDir = areaIn; return this; }
	@Override public TopWindowParent setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IWindowObject getModifyingObject() { return modifyingObject; }
	@Override public IWindowParent getMaximizingWindow() { return maximizingWindow; }
	@Override public ScreenLocation getMaximizingArea() { return maximizingArea; }
	@Override public boolean getMaximizingHeaderCenter() { return maximizingHeaderCenter; }
	@Override public TopWindowParent clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyType.None; return this; }
	
	//close
	@Override public TopWindowParent setEscapeStopper(IWindowObject obj) { if (obj != this) { escapeStopper = obj; } return this; }
	@Override public IWindowObject getEscapeStopper() { return escapeStopper; }
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		res = Game.getWindowSize();
		
		mX = mXIn;
		mY = mYIn;
		
		//handle mouse hover stuff
		checkMouseHover();
		oldMousePos.setValues(mX, mY);
		
		//update objects
		if (!objsToBeRemoved.isEmpty()) { WindowObjectS.removeObjects(this, objsToBeRemoved); }
		if (!objsToBeAdded.isEmpty()) { WindowObjectS.addObjects(this, objsToBeAdded); }
		if (escapeStopper != null && getAllChildren().notContains(escapeStopper)) { escapeStopper = null; }
		
		//update object states
		updateZLayers();
		updateFocus();
		
		if (modifyingObject != null) {
			switch (modifyType) {
			case Move:
			case MoveAlreadyClicked: modifyingObject.move(mX - mousePos.getA(), mY - mousePos.getB()); mousePos.setValues(mX, mY); break;
			case Resize: modifyingObject.resize(mX - mousePos.getA(), mY - mousePos.getB(), resizingDir); mousePos.setValues(mX, mY); break;
			default: break;
			}
		}
		
		if (maximizingWindow != null) {
			if (maximizingWindow.isMaximizable()) {
				switch (maximizingArea) {
				case topLeft:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.topLeft);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case botLeft:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.botLeft);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case topRight:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.topRight);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case botRight:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.botRight);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case left:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.left);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case right:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.right);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case center:
					maximizingWindow.setPreMax(maximizingWindow.getDimensions());
					maximizingWindow.setMaximized(ScreenLocation.center);
					maximizingWindow.maximize();
					setFocusedObject(maximizingWindow);
					break;
				case out:
					if (maximizingWindow.isMaximized()) {
						maximizingWindow.setMaximized(ScreenLocation.out);
						maximizingWindow.miniaturize();
						
						if (maximizingHeaderCenter) {
							EDimension dims = maximizingWindow.getDimensions();
							WindowHeader header = maximizingWindow.getHeader();
							double yPos = header != null ? header.height / 2 : 0;
							
							maximizingWindow.setPosition(Mouse.getMx() - dims.width / 2, Mouse.getMy() + yPos);
							maximizingWindow.reInitObjects();
							
							WindowHeader newHeader = maximizingWindow.getHeader();
							newHeader.setHeaderMoving(true);
							setModifyingObject(maximizingWindow, ObjectModifyType.Move);
							setModifyMousePos((int) newHeader.midX, (int) newHeader.midY);
						}
						
						setFocusedObject(maximizingWindow);
					}
				default: break;
				}
			}
			
			maximizingWindow = null;
			maximizingArea = ScreenLocation.out;
			maximizingHeaderCenter = false;
		}
	}
	
	public void checkMouseHover() {
		IWindowObject o = getHighestZObjectUnderMouse();
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
			if (getHoveringObject() != null) { setHoveringObject(null); }
		}
	}
	
	protected void updateZLayers() {
		if (toFront != null) {
			
			//move the 'toFront' object to the front
			if (windowObjects.contains(toFront)) {
				windowObjects.remove(toFront);
				windowObjects.add(toFront);
			}
			
			//move things that should always be at the top to the top
			EArrayList<IWindowObject> atTop = windowObjects.stream().filter(o -> o.isAlwaysOnTop()).collect(EArrayList.toEArrayList());
			for (IWindowObject o : atTop) {
				windowObjects.remove(o);
				windowObjects.add(o);
			}
			
			toFront = null;
		}
		
		if (toBack != null) {
			
			//move the 'toBack' object to the back
			if (windowObjects.contains(toBack)) {
				EArrayList<IWindowObject> objects = new EArrayList();
				windowObjects.remove(toBack);
				//objects.addAll(guiObjects);
				//guiObjects.clear();
				windowObjects.add(windowObjects.size() - 1, toBack);
				//guiObjects.addAll(objects);
			}
			
			//move things that should always be at the top to the top
			EArrayList<IWindowObject> atTop = windowObjects.stream().filter(o -> o.isAlwaysOnTop()).collect(EArrayList.toEArrayList());
			for (IWindowObject o : atTop) {
				windowObjects.remove(o);
				windowObjects.add(o);
			}
			
			toBack = null;
		}
	}
	
	public void onWindowResized() {
		//notify apps
		//MinecraftForge.EVENT_BUS.post(new GameWindowResizedEvent(newWidth, newHeight));
		
		WindowSize newRes = Game.getWindowSize();
		
		//handle windows
		EArrayList<WindowParent> windows = Game.getAllActiveWindows();
		
		int oldW = res.getWidth();
		int oldH = res.getHeight();
		int newW = newRes.getWidth();
		int newH = newRes.getHeight();
		
		res = Game.getWindowSize();
		setDimensions(0, 0, res.getWidth(), res.getHeight());
		
		for (WindowParent p : windows) {
			
			EDimension oldDims = p.getDimensions();
			
			double newX = (oldDims.startX * newW) / oldW;
			double newY = (oldDims.startY * newH) / oldH;
			
			p.setPosition(newX, newY);
			p.reInitObjects();
			
			if (p.isMaximized()) { p.maximize(); }
		}
	}
	
}
