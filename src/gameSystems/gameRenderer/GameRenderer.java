package gameSystems.gameRenderer;

import eWindow.StaticTopParent;
import eWindow.WindowObjectS;
import eWindow.windowObjects.advancedObjects.header.WindowHeader;
import eWindow.windowTypes.WindowObject;
import eWindow.windowTypes.WindowParent;
import eWindow.windowTypes.interfaces.IActionObject;
import eWindow.windowTypes.interfaces.ITopParent;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowTypes.interfaces.IWindowParent;
import eWindow.windowUtil.EObjectGroup;
import eWindow.windowUtil.windowEvents.ObjectEvent;
import eWindow.windowUtil.windowEvents.ObjectEventHandler;
import eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import eWindow.windowUtil.windowEvents.events.EventAction;
import eWindow.windowUtil.windowEvents.events.EventFocus;
import eWindow.windowUtil.windowEvents.events.EventRedraw;
import input.Mouse;
import java.util.ArrayDeque;
import java.util.Deque;
import main.Game;
import org.lwjgl.opengl.GL11;
import util.renderUtil.ScreenLocation;
import util.renderUtil.WindowSize;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;
import util.storageUtil.StorageBox;

//Author: Hunter Bragg

public class GameRenderer extends WindowObject implements ITopParent {
	
	public static GameRenderer instance;
	protected IWindowObject modifyingObject;
	protected IWindowObject objectRequestingFocus, focusedObject, focusLockObject;
	protected IWindowObject defaultFocusObject;
	protected IWindowObject toFront, toBack;
	protected IWindowObject hoveringTextObject;
	protected IWindowObject escapeStopper;
	//protected IRendererProxy proxy;
	protected EArrayList<IWindowObject> WindowObjects = new EArrayList();
	protected EArrayList<IWindowObject> objsToBeAdded = new EArrayList();
	protected EArrayList<IWindowObject> objsToBeRemoved = new EArrayList();
	protected ObjectEventHandler eventHandler = new ObjectEventHandler(this);
	protected StorageBox<Integer, Integer> mousePos = new StorageBox();
	protected StorageBox<Integer, Integer> oldMousePos = new StorageBox();
	protected Deque<EventFocus> focusQueue = new ArrayDeque();
	protected ObjectModifyType modifyType = ObjectModifyType.None;
	protected ScreenLocation resizingDir = ScreenLocation.out;
	protected IWindowParent maximizingWindow;
	protected ScreenLocation maximizingArea = ScreenLocation.out;
	protected boolean maximizingHeaderCenter = false;
	protected boolean objectInit = false;
	protected boolean hasProxy = false;
	protected int mX = 0, mY = 0;
	protected long mouseHoverTime = 0l;
	protected long hoverRefTime = 0l;
	protected IWindowObject lastClickedObject = null;
	protected long lastClickTime = 0l;
	protected long doubleClickThreshold = 500l;
	
	public static GameRenderer getInstance() {
		return instance == null ? instance = new GameRenderer() : instance;
	}
	
	private GameRenderer() {
		res = Game.getWindowSize();
		initObjects();
	}
	
	public void onRenderTick() {
		drawObject(Mouse.getMx(), Mouse.getMy());
	}
	
	/*
	public void onTextRender(RenderGameOverlayEvent.Text e) {
		TaskBar b = getTaskBar();
		if (b != null && !b.isHidden() && e.left != null && e.right != null) {
			e.setCanceled(true);
			ArrayList<String> l = e.left;
			ArrayList<String> r = e.right;
			int width = res.getWidth();
			
			double top = b.endY + 2;
			for (String msg : l) {
				if (msg == null) { continue; }
				drawRect(1, top - 1, 2 + getStringWidth(msg) + 1, top + FONT_HEIGHT - 1, -1873784752);
				drawStringS(msg, 2.0, top, 14737632);
				top += FONT_HEIGHT;
			}
			
			top = b.endY + 2;
			for (String msg : r) {
				if (msg == null) { continue; }
				int w = getStringWidth(msg);
				int left = width - 2 - w;
				drawRect(left - 1, top - 1, left + w + 1, top + FONT_HEIGHT - 1, -1873784752);
				drawStringS(msg, left, top, 14737632);
				top += FONT_HEIGHT;
			}
		}
	}
	*/
	
	//----------------
	//Object Overrides
	//----------------
	
	@Override public String toString() { return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()); }
	
	//----------------------------
	//IEnhancedGuiObject Overrides
	//----------------------------
	
	//init
	@Override public boolean isInit() { return true; }
	@Override public boolean isObjectInit() { return objectInit; }
	/** Effectively does nothing in the renderer. */
	@Override public void completeInit() {}
	@Override public void initObjects() { objectInit = true; }
	@Override
	public void reInitObjects() {
		objectInit = false;
		WindowObjects.clear();
		initObjects();
	}
	@Override public void onAdded() {}
	
	//main draw
	@Override
	public void drawObject(int mXIn, int mYIn) {
		checkForProxy();
		updateBeforeNextDraw(mXIn, mYIn);
		
		//prime renderer
		//GlStateManager.pushMatrix();
		//GlStateManager.enableBlend();
		//GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		
		//EnhancedMC.getBlockDrawer().clearBlocks();
		/*
		if (EnhancedMC.isDevMode() && CoreApp.drawHiddenPlayers.get()) {
			if (mc.theWorld != null) {
				List<EntityPlayer> list = mc.theWorld.playerEntities;
				if (list != null) {
					for (EntityPlayer p : list) {
						if (p.isInvisible()) {
							EnhancedMC.getBlockDrawer().addPlayer(p, EColors.lred.intVal, true, EColors.seafoam.intVal);
						}
					}
				}
			}
		}
		*/
		
		if (visible) {
			
			//draw debug stuff
			//if (EnhancedMC.isDebugMode() && !mc.gameSettings.showDebugInfo && (mc.currentScreen instanceof IRendererProxy)) { drawDebugInfo(); }
			
			//draw Hud Border
			/*
			if (hasProxy && CoreApp.drawHudBorder.get()) {
				boolean hasOverlay = false;
				for (IWindowObject o : WindowObjects) {
					if (o instanceof OverlayWindow) { hasOverlay = true; }
				}
				
				if (!hasOverlay) {
					int borderColor = 0x88ff0000;
					TaskBar b = getTaskBar();
					if (b != null && !b.isHidden()) {
						drawRect(0, 24, 1, res.getHeight(), borderColor); //left
						drawRect(1, 24, res.getWidth() - 1, 25, borderColor); //top
						drawRect(res.getWidth() - 1, 24, res.getWidth(), res.getHeight(), borderColor); //right
						drawRect(1, res.getHeight() - 2, res.getWidth() - 1, res.getHeight(), borderColor); //bottom
					}
					else {
						drawRect(0, 0, 1, res.getHeight(), borderColor); //left
						drawRect(1, 0, res.getWidth() - 1, 2, borderColor); //top
						drawRect(res.getWidth() - 1, 0, res.getWidth(), res.getHeight(), borderColor); //right
						drawRect(1, res.getHeight() - 2, res.getWidth() - 1, res.getHeight(), borderColor); //bottom
					}
				}
			}
			*/
			
			//draw all child objects
			for (IWindowObject o : WindowObjects) {
				if (o.checkDraw() && !o.isHidden()) {
					boolean draw = true;
					
					if (o instanceof WindowParent) {
						WindowParent wp = (WindowParent) o;
						if (!wp.isMinimized() || wp.drawsWhileMinimized()) { draw = true; }
						else { draw = false; }
					}
					
					if (draw) {
						//GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
						//GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
						GL11.glDisable(GL11.GL_SCISSOR_TEST);
						
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
		
		//GlStateManager.popMatrix();
	}
	@Override public void onFirstDraw() {}
	@Override public boolean hasFirstDraw() { return firstDraw; }
	@Override public void updateCursorImage() {}
	@Override public void onMouseHover(int mX, int mY) {}
	@Override public GameRenderer setHoverText(String textIn) { return this; }
	@Override public GameRenderer setHoverTextColor(int colorIn) { return this; }
	
	//obj ids;
	@Override public long getObjectID() { return -1; }
	@Override public GameRenderer setObjectID(long idIn) { return this; }
	@Override public String getObjectName() { return "EMC Renderer"; }
	@Override public GameRenderer setObjectName(String nameIn) { return this; }
	
	//drawing checks
	@Override public boolean checkDraw() { return visible; }
	@Override public boolean isEnabled() { return enabled; }
	@Override public boolean isVisible() { return visible; }
	@Override public boolean isPersistent() { return true; }
	@Override public boolean isBoundaryEnforced() { return false; }
	@Override public boolean isResizing() { return false; }
	@Override public boolean isMoving() { return false; }
	@Override public GameRenderer setEnabled(boolean val) { return this; }
	@Override public GameRenderer setVisible(boolean val) { return this; }
	@Override public GameRenderer setPersistent(boolean val) { return this; }
	
	//size
	@Override public boolean hasHeader() { return false; }
	@Override public boolean isResizeable() { return false; }
	@Override public WindowHeader getHeader() { return null; }
	@Override public double getMinWidth() { return res.getWidth(); }
	@Override public double getMinHeight() { return res.getHeight(); }
	@Override public double getMaxWidth() { return res.getWidth(); }
	@Override public double getMaxHeight() { return res.getHeight(); }
	@Override public GameRenderer setMinDims(double widthIn, double heightIn) { return this; }
	@Override public GameRenderer setMaxDims(double widthIn, double heightIn) { return this; }
	@Override public GameRenderer setMinWidth(double widthIn) { return this; }
	@Override public GameRenderer setMinHeight(double heightIn) { return this; }
	@Override public GameRenderer setMaxWidth(double widthIn) { return this; }
	@Override public GameRenderer setMaxHeight(double heightIn) { return this; }
	@Override public GameRenderer setResizeable(boolean val) { return this; }
	@Override public GameRenderer resize(double xIn, double yIn, ScreenLocation areaIn) { return this; }
	
	//position
	@Override public void move(double newX, double newY) {}
	@Override public boolean isMoveable() { return false; }
	@Override public GameRenderer resetPosition() { return this; }
	@Override public GameRenderer setPosition(double xIn, double yIn) { return this; }
	@Override public GameRenderer setMoveable(boolean val) { return this; }
	@Override public GameRenderer setDimensions(EDimension dimIn) { return this; }
	@Override public GameRenderer setDimensions(double widthIn, double heightIn) { return this; }
	@Override public GameRenderer setDimensions(double startXIn, double startYIn, double widthIn, double heightIn) { return this; }
	@Override public StorageBox<Double, Double> getInitialPosition() { return new StorageBox<Double, Double>(); }
	@Override public GameRenderer setInitialPosition(double startXIn, double startYIn) { return this; }
	@Override public GameRenderer centerObjectWithSize(double widthIn, double heightIn) { return this; }
	@Override public EDimension getDimensions() { return new EDimension(0, 0, res.getWidth(), res.getHeight()); }
	
	//objects
	@Override public boolean isChild(IWindowObject objIn) { return false; }
	@Override public GameRenderer addObject(IWindowObject obj, IWindowObject... additional) { WindowObjectS.addObject(this, obj, additional); return this; }
	@Override public GameRenderer removeObject(IWindowObject obj, IWindowObject... additional) { WindowObjectS.removeObject(this, obj, additional); return this; }
	@Override public EObjectGroup getObjectGroup() { return objectGroup; }
	@Override public GameRenderer setObjectGroup(EObjectGroup groupIn) { objectGroup = groupIn; return this; }
	@Override public void onGroupNotification(ObjectEvent e) {}
	@Override public EArrayList<IWindowObject> getObjects() { return WindowObjects; }
	@Override public EArrayList<IWindowObject> getAddingObjects() { return objsToBeAdded; }
	@Override public EArrayList<IWindowObject> getRemovingObjects() { return objsToBeRemoved; }
	@Override public EArrayList<IWindowObject> getAllChildren() { return WindowObjectS.getAllChildren(this); }
	@Override public EArrayList<IWindowObject> getAllChildrenUnderMouse() { return WindowObjectS.getAllChildrenUnderMouse(this, mX, mY); }
	
	//parents
	@Override public IWindowObject getParent() { return null; }
	@Override public GameRenderer setParent(IWindowObject parentIn) { return this; }
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
	public GameRenderer requestFocus() {
		if (!hasFocus() && !doesFocusLockExist()) { setObjectRequestingFocus(this, FocusType.Transfer); }
		return this;
	}
	@Override public IWindowObject getDefaultFocusObject() { return defaultFocusObject; }
	@Override public GameRenderer setDefaultFocusObject(IWindowObject objIn) { defaultFocusObject = objIn; return this; }
	
	//mouse checks
	@Override public void mouseEntered(int mX, int mY) {}
	@Override public void mouseExited(int mX, int mY) {}
	@Override public boolean isMouseInside(int mX, int mY) { return false; }
	@Override public boolean isMouseOver(int mX, int mY) { return false; }
	@Override public GameRenderer setBoundaryEnforcer(EDimension dimIn) { return this; }
	@Override public EDimension getBoundaryEnforcer() { return getDimensions(); }
	@Override public boolean isClickable() { return true; }
	@Override public GameRenderer setClickable(boolean valIn) { return this; }
	//@Override public IEnhancedGuiObject setClickableArea(EDimension dimIn) { return this; }
	//@Override public IEnhancedGuiObject setClickableArea(int startX, int startY, int width, int height) { return this; }
	//@Override public EDimension getClickableArea() { return getDimensions(); }
	
	//basic inputs
	@Override public void parseMousePosition(int mX, int mY) { WindowObjects.stream().filter(o -> o.isMouseInside(mX, mY)).forEach(o -> o.parseMousePosition(mX, mY)); }
	@Override public void mousePressed(int mX, int mY, int button) { StaticTopParent.mousePressed(this, mX, mY, button, focusQueue); }
	@Override public void mouseReleased(int mX, int mY, int button) { StaticTopParent.mouseReleased(this, mX, mY, button); }
	@Override public void mouseDragged(int mX, int mY, int button, long timeSinceLastClick) { StaticTopParent.mouseDragged(this, mX, mY, button, timeSinceLastClick); }
	@Override public void mouseScrolled(int change) { StaticTopParent.mouseScrolled(this, mX, mY, change); }
	@Override public void onDoubleClick() {}
	@Override public void keyPressed(char typedChar, int keyCode) { StaticTopParent.keyPressed(this, typedChar, keyCode); }
	@Override public void keyReleased(char typedChar, int keyCode) { StaticTopParent.keyReleased(this, typedChar, keyCode); }
	public void keyHeld(char typedChar, int keyCode) {}
	
	//events
	@Override public ObjectEventHandler getEventHandler() { return eventHandler; }
	@Override public GameRenderer registerListener(IWindowObject objIn) { if (eventHandler != null) { eventHandler.registerObject(objIn); } return this; }
	@Override public GameRenderer unregisterListener(IWindowObject objIn) { if (eventHandler != null) { eventHandler.unregisterObject(objIn); } return this; }
	@Override public GameRenderer postEvent(ObjectEvent e) { if (eventHandler != null) { eventHandler.processEvent(e); } return this; }
	@Override public void onEvent(ObjectEvent e) {}
	
	//action object
	@Override public void actionPerformed(IActionObject object, Object... args) { postEvent(new EventAction(this, object, args)); }
	
	//close object
	@Override public boolean isCloseable() { return false; }
	@Override public boolean isClosed() { return closed; }
	@Override public GameRenderer setCloseable(boolean val) { return this; }
	@Override public void close() { System.out.println("FOOL! Dagoth Ur cannot be closed, I am a god!"); }
	@Override public void onClosed() {}
	@Override public GameRenderer setFocusedObjectOnClose(IWindowObject objIn) { return this; }
	
	//--------------------
	//ITopParent Overrides
	//--------------------
	
	//drawing
	@Override public void drawDebugInfo() { StaticTopParent.drawDebugInfo(this); }
	
	//draw order
	@Override public GameRenderer bringObjectToFront(IWindowParent objIn) { toFront = objIn; return this; }
	@Override public GameRenderer sendObjectToBack(IWindowParent objIn) { toBack = objIn; return this; }
	
	//hovering text
	@Override public GameRenderer setHoveringObject(IWindowObject objIn) { hoveringTextObject = objIn; return this; }
	@Override public IWindowObject getHoveringObject() { return hoveringTextObject; }
	
	//double click
	@Override public ITopParent setLastClickedObject(IWindowObject objectIn) { lastClickedObject = objectIn; return this; }
	@Override public IWindowObject getLastClickedObject() { return lastClickedObject; }
	@Override public ITopParent setLastClickTime(long timeIn) { lastClickTime = timeIn; return this; }
	@Override public long getLastClickTime() { return lastClickTime; }
	
	//objects
	@Override public IWindowObject getHighestZLevelObject() { return StaticTopParent.getHighestZLevelObject(this); }
	@Override public ITopParent hideUnpinnedObjects() { return StaticTopParent.hideUnpinnedObjects(this); }
	@Override public ITopParent hideAllExcept(IWindowObject objIn) { return StaticTopParent.hideAllExcept(this, objIn); }
	@Override public ITopParent revealHiddenObjects() { return StaticTopParent.revealHiddenObjects(this); }
	@Override public ITopParent removeUnpinnedObjects() { return StaticTopParent.removeUnpinnedWindows(this); }
	@Override public ITopParent removeAllObjects() { return StaticTopParent.removeAllObjects(this); }
	@Override public boolean hasPinnedObjects() { return StaticTopParent.hasPinnedWindows(this); }
	
	//focus
	@Override public IWindowObject getFocusedObject() { return focusedObject; }
	@Override public GameRenderer setFocusedObject(IWindowObject objIn) { focusedObject = objIn; return this; }
	@Override public GameRenderer setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn) { focusQueue.add(new EventFocus(this, objIn, typeIn)); return this; }
	@Override public IWindowObject getFocusLockObject() { return focusLockObject; }
	@Override public GameRenderer setFocusLockObject(IWindowObject objIn) { focusLockObject = objIn; transferFocus(focusLockObject); return this; }
	@Override public GameRenderer clearFocusLockObject() { StaticTopParent.clearFocusLockObject(this); return this; }
	@Override public boolean doesFocusLockExist() { return focusLockObject != null; }
	@Override public void clearFocusedObject() { StaticTopParent.clearFocusedObject(this);}
	@Override public void updateFocus() { StaticTopParent.updateFocus(this, focusQueue); }
	
	//object modification
	@Override public ObjectModifyType getModifyType() { return modifyType; }
	@Override public GameRenderer setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn) { modifyingObject = objIn; modifyType = typeIn; return this; }
	@Override
	public GameRenderer setMaximizingWindow(IWindowParent objIn, ScreenLocation areaIn, boolean centerAroundHeader) {
		if (objIn != null && objIn.isMaximizable()) { maximizingWindow = objIn; maximizingArea = areaIn; maximizingHeaderCenter = centerAroundHeader; }
		return this;
	}
	@Override public GameRenderer setResizingDir(ScreenLocation areaIn) { resizingDir = areaIn; return this; }
	@Override public GameRenderer setModifyMousePos(int mX, int mY) { mousePos.setValues(mX, mY); return this; }
	@Override public IWindowObject getModifyingObject() { return modifyingObject; }
	@Override public IWindowParent getMaximizingWindow() { return maximizingWindow; }
	@Override public ScreenLocation getMaximizingArea() { return maximizingArea; }
	@Override public boolean getMaximizingHeaderCenter() { return maximizingHeaderCenter; }
	@Override public GameRenderer clearModifyingObject() { modifyingObject = null; modifyType = ObjectModifyType.None; return this; }
	
	//mouse checks
	@Override public boolean isMouseOnObjEdge(int mX, int mY) { return StaticTopParent.isMouseOnObjEdge(this, mX, mY); }
	@Override public ScreenLocation getEdgeAreaMouseIsOn() { return StaticTopParent.getEdgeAreaMouseIsOn(this, mX, mY); }
	@Override public boolean isMouseInsideObject(int mX, int mY) { return getHighestZObjectUnderMouse() != null; }
	@Override public boolean isMouseInsideHeader(int mX, int mY) { return StaticTopParent.isMouseInsideHeader(this, mX, mY); }
	@Override public IWindowObject getHighestZObjectUnderMouse() { return StaticTopParent.getHighestZObjectUnderMouse(this); }
	@Override public EArrayList<IWindowObject> getAllObjectsUnderMouse() { return StaticTopParent.getAllObjectsUnderMouse(this, mX, mY); }
	
	//close
	@Override public GameRenderer setEscapeStopper(IWindowObject obj) { if (obj != this) { escapeStopper = obj; } return this; }
	@Override public IWindowObject getEscapeStopper() { return escapeStopper; }
	
	//--------------------------
	//EnhancedMCRenderer methods
	//--------------------------
	
	protected void updateBeforeNextDraw(int mXIn, int mYIn) {
		postEvent(new EventRedraw(this));
		res = Game.getWindowSize();
		
		mX = mXIn;
		mY = mYIn;
		
		//update mouse stuff based on proxy
		//if (proxy != null) { mX = proxy.getMX(); mY = proxy.getMY(); }
		//else if (mc.currentScreen instanceof EnhancedGui) { 
		//	EnhancedGui gui = (EnhancedGui) mc.currentScreen;
		//	mX = gui.mX; mY = gui.mY;
		//}
		//else { mX = -1; mY = -1; }
		
		//handle cursor stuff for highest obj
		//if (CoreApp.customCursors.get()) {
		//	if (getHighestZObjectUnderMouse() != null) { getHighestZObjectUnderMouse().updateCursorImage(); }
		//	else { updateCursorImage(); }
		//}
		//if (!CursorHelper.isNormalCursor() && getHighestZObjectUnderMouse() == null && modifyType != ObjectModifyType.Resize) { CursorHelper.reset(); }
		
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
	
	//public GuiScreen getProxyGuiScreen() { return proxy instanceof GuiScreen ? (GuiScreen) proxy : null; }
	//public IRendererProxy getProxy() { return proxy; }
	
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
	
	protected void checkForProxy() {
		/*
		hasProxy = (mc.currentScreen instanceof IRendererProxy);
		if (hasProxy) { proxy = (IRendererProxy) mc.currentScreen; }
		else { proxy = null; }
		*/
	}
	
	protected void updateZLayers() {
		if (toFront != null) {
			
			//move the 'toFront' object to the front
			if (WindowObjects.contains(toFront)) {
				WindowObjects.remove(toFront);
				WindowObjects.add(toFront);
			}
			
			//move things that should always be at the top to the top
			EArrayList<IWindowObject> atTop = WindowObjects.stream().filter(o -> o.isAlwaysOnTop()).collect(EArrayList.toEArrayList());
			for (IWindowObject o : atTop) {
				WindowObjects.remove(o);
				WindowObjects.add(o);
			}
			
			toFront = null;
		}
		
		if (toBack != null) {
			
			//move the 'toBack' object to the back
			if (WindowObjects.contains(toBack)) {
				EArrayList<IWindowObject> objects = new EArrayList();
				WindowObjects.remove(toBack);
				//objects.addAll(guiObjects);
				//guiObjects.clear();
				WindowObjects.add(WindowObjects.size() - 1, toBack);
				//guiObjects.addAll(objects);
			}
			
			//move things that should always be at the top to the top
			EArrayList<IWindowObject> atTop = WindowObjects.stream().filter(o -> o.isAlwaysOnTop()).collect(EArrayList.toEArrayList());
			for (IWindowObject o : atTop) {
				WindowObjects.remove(o);
				WindowObjects.add(o);
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
		
		System.out.println(res + " " + newRes);
		
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
