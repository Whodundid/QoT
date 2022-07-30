package engine.windowLib.windowTypes;

import engine.renderEngine.GLSettings;
import engine.windowLib.StaticWindowObject;
import engine.windowLib.windowObjects.advancedObjects.header.WindowHeader;
import engine.windowLib.windowObjects.utilityObjects.FocusLockBorder;
import engine.windowLib.windowTypes.interfaces.IActionObject;
import engine.windowLib.windowTypes.interfaces.ITopParent;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import engine.windowLib.windowUtil.EGui;
import engine.windowLib.windowUtil.EObjectGroup;
import engine.windowLib.windowUtil.windowEvents.ObjectEvent;
import engine.windowLib.windowUtil.windowEvents.ObjectEventHandler;
import engine.windowLib.windowUtil.windowEvents.eventUtil.FocusType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectEventType;
import engine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import engine.windowLib.windowUtil.windowEvents.events.EventAction;
import engine.windowLib.windowUtil.windowEvents.events.EventFirstDraw;
import engine.windowLib.windowUtil.windowEvents.events.EventFocus;
import engine.windowLib.windowUtil.windowEvents.events.EventMouse;
import engine.windowLib.windowUtil.windowEvents.events.EventObjects;
import engine.windowLib.windowUtil.windowEvents.events.EventRedraw;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.misc.ScreenLocation;
import main.QoT;

//Author: Hunter Bragg

public abstract class WindowObject<E> extends EGui implements IWindowObject<E> {
	
	protected WindowObject<E> instance;
	protected IWindowObject<?> parent, focusObjectOnClose, defaultFocusObject;
	protected EDimension boundaryDimension;
	protected EArrayList<IWindowObject<?>> windowObjects = new EArrayList();
	protected EArrayList<IWindowObject<?>> objsToBeRemoved = new EArrayList();
	protected EArrayList<IWindowObject<?>> objsToBeAdded = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected ScreenLocation oldArea = ScreenLocation.OUT;
	protected EObjectGroup objectGroup;
	protected String objectName = "noname";
	protected String hoverText = null;
	protected long objectId = -1;
	protected boolean enabled = true;
	protected boolean visible = true;
	protected boolean hidden = false;
	protected boolean mouseEntered = false;
	protected boolean moveable = true;
	protected boolean hasFocus = false;
	protected boolean focusLock = false;
	protected boolean persistent = false;
	protected boolean alwaysOnTop = false;
	protected boolean resizeable = false;
	protected boolean clickable = true;
	protected boolean firstDraw = false;
	protected boolean closeable = true;
	protected boolean closed = false;
	protected boolean closesWithHud = false;
	protected boolean hasCustomHoverColor = false;
	protected int hoverTextColor = EColors.lime.intVal;
	private boolean hasBeenInitialized = false;
	private boolean objectInit = false;
	private boolean beingRemoved = false;
	protected E genericObject = null;
	
	/** Initializes this WindowObject with the specified parent. */
	public void init(IWindowObject<?> objIn) {
		parent = objIn;
		instance = this;
		res = QoT.getWindowSize();
	}
	
	public void init(IWindowObject<?> objIn, int xIn, int yIn) { init(objIn, (double) xIn, (double) yIn); }
	
	/** Initializes this WindowObject with the specified parent and starting coordinates. */
	public void init(IWindowObject<?> objIn, double xIn, double yIn) {
		parent = objIn;
		startX = xIn;
		startY = yIn;
		startXPos = startX;
		startYPos = startY;
		instance = this;
		res = QoT.getWindowSize();
	}
	
	public void init(IWindowObject<?> objIn, int xIn, int yIn, int widthIn, int heightIn) { init(objIn, (double) xIn, (double) yIn, (double) widthIn, (double) heightIn); }
	/** Initializes this WindowObject with the specified parent and dimensions. */
	public void init(IWindowObject<?> objIn, double xIn, double yIn, double widthIn, double heightIn) { init(objIn, xIn, yIn, widthIn, heightIn, -1); }
	
	public void init(IWindowObject<?> objIn, int xIn, int yIn, int widthIn, int heightIn, int objectIdIn) { init(objIn, (double) xIn, (double) yIn, (double) widthIn, (double) heightIn, objectIdIn); }
	/** Initializes this WindowObject with the specified parent, dimensions, as well as object id. */
	public void init(IWindowObject<?> objIn, double xIn, double yIn, double widthIn, double heightIn, int objectIdIn) {
		parent = objIn;
		objectId = objectIdIn;
		startXPos = xIn;
		startYPos = yIn;
		startWidth = widthIn;
		startHeight = heightIn;
		setDimensions(xIn, yIn, widthIn, heightIn);
		instance = this;
		res = QoT.getWindowSize();
	}
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean isInit() { return hasBeenInitialized; }
	@Override public boolean isObjectInit() { return objectInit; }
	@Override public void completeInit() { hasBeenInitialized = true; objectInit = true; }
	@Override public void initObjects() {}
	@Override
	public void reInitObjects() {
		objectInit = false;
		ITopParent p = getTopParent();
		EArrayList<IWindowObject<?>> children = getAllChildren();
		if (!(p.getModifyType() == ObjectModifyType.Resize)) {
			if (children.contains(p.getFocusedObject())) p.clearFocusedObject();
			if (children.contains(p.getFocusLockObject())) p.clearFocusLockObject();
			if (children.contains(p.getModifyingObject())) p.clearModifyingObject();
		}
		windowObjects.clear();
		objsToBeAdded.clear();
		
		preReInit();
		initObjects();
		postReInit();
		objectInit = true;
	}
	@Override public void preReInit() {}
	@Override public void postReInit() {}
	@Override public void onAdded() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn) {
		updateBeforeNextDraw(mXIn, mYIn);
		try {
			if (willBeDrawn()) {
				GLSettings.pushMatrix();
				GLSettings.enableBlend();
				
				//draw all child objects
				for (IWindowObject o : windowObjects) {
					//only draw if the object is actually visible
					if (!o.willBeDrawn() || o.isHidden()) continue;
					GLSettings.fullBright();
					
					//notify object on first draw
					if (!o.hasFirstDraw()) o.onFirstDraw();
					//actually draw the object
					o.drawObject(mX, mY);
					
					//draw grayed out overlay over everything if a focus lock object is present
					IWindowObject f = getTopParent().getFocusLockObject();
					if (f != null && o instanceof WindowHeader && (!o.equals(f) && !f.getAllChildren().contains(o))) {
						if (o.isVisible()) {
							EDimension d = o.getDimensions();
							drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
						}
					}
				}
				
				GLSettings.popMatrix();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override public void onFirstDraw() { postEvent(new EventFirstDraw(this)); firstDraw = true; }
	@Override public boolean hasFirstDraw() { return firstDraw; }
	@Override public void updateCursorImage() { StaticWindowObject.updateCursorImage(this); }
	@Override public void onMouseHover(int mX, int mY) { StaticWindowObject.onMouseHover(this, mX, mY, hoverText, hoverTextColor); }
	@Override public boolean isDrawingHover() { return getTopParent() != null && this.equals(getTopParent().getHoveringObject()); }
	@Override public WindowObject<E> setHoverText(String textIn) { hoverText = textIn; return this; }
	@Override public WindowObject<E> setHoverTextColor(int colorIn) { hoverTextColor = colorIn; hasCustomHoverColor = true; return this; }
	@Override public String getHoverText() { return hoverText; }
	@Override public int getHoverTextColor() { return hoverTextColor; }
	
	//obj ids
	@Override public long getObjectID() { return objectId; }
	@Override public WindowObject<E> setObjectID(long idIn) { objectId = idIn; return this; }
	@Override public String getObjectName() { return objectName; }
	@Override public WindowObject<E> setObjectName(String nameIn) { objectName = nameIn; return this; }
	
	//drawing checks
	@Override public boolean willBeDrawn() { return persistent || visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isHidden() { return hidden; }
	@Override public boolean isAlwaysVisible() { return persistent; }
	@Override public boolean isBoundaryEnforced() { return boundaryDimension != null; }
	@Override public boolean isAlwaysOnTop() { return alwaysOnTop; }
	@Override public WindowObject<E> setEnabled(boolean val) { enabled = val; return this; }
	@Override public WindowObject<E> setVisible(boolean val) { visible = val; return this; }
	@Override public WindowObject<E> setAlwaysVisible(boolean val) { persistent = val; return this; }
	@Override public WindowObject<E> setAlwaysOnTop(boolean val) { alwaysOnTop = val; return this; }
	@Override public WindowObject<E> setHidden(boolean val) { hidden = val; return this; }
	
	//size and position
	@Override public boolean isResizeable() { return resizeable; }
	@Override public boolean isMoveable() { return moveable; }
	@Override public double getMinWidth() { return minWidth; }
	@Override public double getMinHeight() { return minHeight; }
	@Override public double getMaxWidth() { return maxWidth; }
	@Override public double getMaxHeight() { return maxHeight; }
	@Override public WindowObject<E> setMinDims(double widthIn, double heightIn) { super.setMinDims(widthIn, heightIn); return this; }
	@Override public WindowObject<E> setMaxDims(double widthIn, double heightIn) { super.setMaxDims(widthIn, heightIn); return this; }
	@Override public WindowObject<E> setMinWidth(double widthIn) { minWidth = widthIn; return this; }
	@Override public WindowObject<E> setMinHeight(double heightIn) { minHeight = heightIn; return this; }
	@Override public WindowObject<E> setMaxWidth(double widthIn) { maxWidth = widthIn; return this; }
	@Override public WindowObject<E> setMaxHeight(double heightIn) { maxHeight = heightIn; return this; }
	@Override public WindowObject<E> setMoveable(boolean val) { moveable = val; return this; }
	@Override public WindowObject<E> setResizeable(boolean val) { resizeable = val; return this; }
	@Override public Box2<Double, Double> getPosition() { return new Box2<Double, Double>(startX, startY); }
	@Override public Box2<Double, Double> getInitialPosition() { return super.getInitialPosition(); }
	@Override public void setInitialPosition(double startXIn, double startYIn) { super.setInitialPosition(startXIn, startYIn); }
	@Override public EDimension getDimensions() { return super.getDimensions(); }
	@Override public void setDimensions(double startXIn, double startYIn, double widthIn, double heightIn) { super.setDimensions(startXIn, startYIn, widthIn, heightIn); }
	@Override public WindowObject<E> centerObjectWithSize(double widthIn, double heightIn) { centerGuiWithSize(widthIn, heightIn); return this; }
	
	@Override
	public WindowObject<E> resetPosition() {
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		windowObjects.forEach(o -> o.resetPosition());
		return this;
	}
	
	//groups
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public WindowObject<E> setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	
	//objects
	@Override public EArrayList<IWindowObject<?>> getObjects() { return windowObjects; }
	@Override public EArrayList<IWindowObject<?>> getAddingObjects() { return objsToBeAdded; }
	@Override public EArrayList<IWindowObject<?>> getRemovingObjects() { return objsToBeRemoved; }
	@Override public boolean containsObject(IWindowObject<?> object) { return getCombinedObjects().contains(object); }
	@Override public boolean containsObject(Class objIn) { return objIn != null ? getAllChildren().stream().anyMatch(o -> objIn.isInstance(o)) : false; }
	
	//parents
	@Override public IWindowObject<?> getParent() { return parent; }
	@Override public WindowObject<E> setParent(IWindowObject<?> parentIn) { parent = parentIn; return this; }
	
	//focus

	@Override
	public boolean relinquishFocus() {
		ITopParent<?> t = getTopParent();
		if (t.doesFocusLockExist()) {
			if (t.getFocusLockObject().equals(this)) {
				t.setObjectRequestingFocus(t, FocusType.Transfer);
				return true;
			}
			return false;
		}
		t.setObjectRequestingFocus(t, FocusType.Transfer);
		return true;
	}
	
	@Override
	public void onFocusGained(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.Gained));
		
		if (eventIn.getFocusType().equals(FocusType.MousePress)) {
			mousePressed(eventIn.getMX(), eventIn.getMY(), eventIn.getActionCode());
			
			ITopParent<?> t = getTopParent();
			
			if (t != null) {
				
				//check if elligible for a double click event
				if (eventIn.getActionCode() == 0) {
					IWindowObject<?> lastClicked = t.getLastClickedObject();
					if (lastClicked == this) {
						long clickTime = t.getLastClickTime();
						
						if (System.currentTimeMillis() - clickTime <= 400) {
							onDoubleClick();
						}
					}
				}
				
				t.setLastClickedObject(this);
				t.setLastClickTime(System.currentTimeMillis());
			}
		}
		if (defaultFocusObject != null) defaultFocusObject.requestFocus();
	}
	@Override
	public void onFocusLost(EventFocus eventIn) {
		postEvent(new EventFocus(this, this, FocusType.Lost));
	}
	
	@Override
	public void transferFocus(IWindowObject<?> objIn) {
		ITopParent<?> t = getTopParent();
		
		if (t.doesFocusLockExist() && getTopParent().getFocusLockObject().equals(this)) {
			if (objIn != null) {
				t.clearFocusLockObject();
				t.setObjectRequestingFocus(objIn, FocusType.Transfer);
			}
		}
		else if (objIn != null) t.setObjectRequestingFocus(objIn, FocusType.Transfer);
	}
	
	@Override
	public void drawFocusLockBorder() {
		if (willBeDrawn() && windowObjects.containsNoInstanceOf(FocusLockBorder.class)) {
			if (hasHeader() && getHeader().isEnabled()) {
				addObject(new FocusLockBorder(this, getHeader().startX, getHeader().startY, width, height + getHeader().height));
			}
			else addObject(new FocusLockBorder(this));
		}
	}
	
	@Override public void requestFocus() { requestFocus(FocusType.Transfer); }
	@Override public void requestFocus(FocusType typeIn) { getTopParent().setObjectRequestingFocus(this, typeIn); }
	@Override public IWindowObject<?> getDefaultFocusObject() { return defaultFocusObject; }
	@Override public WindowObject<E> setDefaultFocusObject(IWindowObject<?> objIn) { defaultFocusObject = objIn; return this; }
	
	//mouse checks
	@Override public boolean isMouseOnEdge(int mX, int mY) { return willBeDrawn() && getEdgeSideMouseIsOn() != ScreenLocation.OUT; }
	@Override public void mouseEntered(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.ENTERED)); }
	@Override public void mouseExited(int mX, int mY) { postEvent(new EventMouse(this, mX, mY, -1, MouseType.EXITED)); }
	@Override public boolean isMouseOver() { return isMouseInside() && this.equals(getTopParent().getHighestZObjectUnderMouse()); }
	@Override public void setBoundaryEnforcer(EDimension dimIn) { boundaryDimension = new EDimension(dimIn); }
	@Override public EDimension getBoundaryEnforcer() { return boundaryDimension; }
	@Override public boolean isClickable() { return clickable; }
	@Override public void setClickable(boolean valIn) { clickable = valIn; }
	@Override public void setEntiretyClickable(boolean val) { StaticWindowObject.setEntiretyClickable(this, val); }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { StaticWindowObject.parseMousePosition(this, mX, mY); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticWindowObject.mousePressed(this, mX, mY, button); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticWindowObject.mouseReleased(this, mX, mY, button); }
	@Override public void mouseScrolled(int change) { StaticWindowObject.mouseScolled(this, mX, mY, change); }
	@Override public void onDoubleClick() {}
	@Override public void keyPressed(char typedChar, int keyCode) { StaticWindowObject.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { StaticWindowObject.keyReleased(this, typedChar, keyCode); }
	
	//events
	@Override
	public void sendArgs(Object... args) {
		if (args.length == 1) {
			if (args[0] instanceof String msg) {
				if (msg.equals("Reload")) {
					boolean any = false;
					for (IWindowObject<?> o : getAllChildren()) {
						if (o.hasFocus()) {
							any = true;
							break;
						}
					}
					reInitObjects();
					if (any) requestFocus();
				}
			}
		}
	}
	
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public void registerListener(IWindowObject<?> objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } }
	@Override public void unregisterListener(IWindowObject<?> objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } }
	@Override public void postEvent(ObjectEvent e) { if (eventHandler != null) { eventHandler.processEvent(e); } }
	@Override public void onEvent(ObjectEvent e) {}
	
	//action object
	@Override public void actionPerformed(IActionObject<?> object, Object... args) { postEvent(new EventAction(this, object, args)); }
	
	//close object
	@Override public void close() { close(true); }
	@Override
	public void close(boolean recursive) {
		if (closeable) {
			postEvent(new EventObjects(this, this, ObjectEventType.Close));
			if (recursive) { for (IWindowObject<?> o : getAllChildren()) o.close(false); }
			ITopParent p = getTopParent();
			if (p.doesFocusLockExist() && p.getFocusLockObject().equals(this)) p.clearFocusLockObject();
			//if (getTopParent().getFocusedObject().equals(this)) relinquishFocus();
			if (focusObjectOnClose != null) focusObjectOnClose.requestFocus();
			parent.removeObject(this);
			closed = true;
		}
	}
	@Override public boolean closesWithHud() { return closesWithHud; }
	@Override public void setClosesWithHud(boolean val) { closesWithHud = val; }
	@Override public boolean isCloseable() { return closeable; }
	@Override public boolean isClosed() { return closed; }
	@Override public void setCloseable(boolean val) { closeable = val; }
	@Override public void onClosed() {}
	@Override public void setFocusedObjectOnClose(IWindowObject<?> objIn) { focusObjectOnClose = objIn; }
	@Override public void setBeingRemoved() { beingRemoved = true; }
	@Override public boolean isBeingRemoved() { return beingRemoved; }
	
	@Override public WindowObject<E> setGenericObject(E objIn) { genericObject = objIn; return this; }
	@Override public E getGenericObject() { return genericObject; }
	
	//----------------------
	// WindowObject methods
	//----------------------
	
	/** Internal method used to prep the next draw phase before it is drawn. */
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		res = QoT.getWindowSize();
		mX = mXIn; mY = mYIn;
		if (!mouseEntered && isMouseOver()) { mouseEntered = true; mouseEntered(mX, mY); }
		if (mouseEntered && !isMouseOver()) { mouseEntered = false; mouseExited(mX, mY); }
		if (!objsToBeRemoved.isEmpty()) StaticWindowObject.removeObjects(this, objsToBeRemoved);
		if (!objsToBeAdded.isEmpty()) StaticWindowObject.addObjects(this, objsToBeAdded);
	}
	
}
