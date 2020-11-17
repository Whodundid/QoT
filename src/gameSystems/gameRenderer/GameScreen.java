package gameSystems.gameRenderer;

import eWindow.StaticTopParent;
import eWindow.windowTypes.interfaces.ITopParent;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowTypes.interfaces.IWindowParent;
import eWindow.windowUtil.windowEvents.eventUtil.FocusType;
import eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import main.Game;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EArrayList;

public class GameScreen extends AbstractScreen implements IRendererProxy {
	
	GameRenderer renderer;
	int mX = 0;
	int mY = 0;
	
	public GameScreen() {
		renderer = Game.getGameRenderer();
		init(renderer, 0, 0, game.getWidth(), game.getHeight());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		mX = mXIn;
		mY = mYIn;
		drawScreen(mX, mY);
		super.drawObject(mXIn, mYIn);
	}
	
	protected void drawScreen(int mXIn, int mYIn) {}

	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
	
	@Override public ITopParent getTopParent() { return this; }
	@Override public IWindowObject getParent() { return this; }

	@Override
	public void drawDebugInfo() {
	}

	@Override
	public ITopParent bringObjectToFront(IWindowParent objIn) {
		return null;
	}

	@Override
	public ITopParent sendObjectToBack(IWindowParent objIn) {
		return null;
	}

	@Override
	public ITopParent setHoveringObject(IWindowObject objIn) {
		return null;
	}

	@Override
	public IWindowObject getHoveringObject() { return null; }

	@Override
	public ITopParent setLastClickedObject(IWindowObject objectIn) {
		return null;
	}

	@Override
	public IWindowObject getLastClickedObject() { return null; }

	@Override
	public ITopParent setLastClickTime(long timeIn) {
		return null;
	}

	@Override
	public long getLastClickTime() { return 0; }

	@Override
	public IWindowObject getHighestZLevelObject() { return null; }

	@Override
	public ITopParent hideUnpinnedObjects() {
		return null;
	}

	@Override
	public ITopParent hideAllExcept(IWindowObject objIn) {
		return null;
	}

	@Override
	public ITopParent revealHiddenObjects() {
		return null;
	}

	@Override
	public ITopParent removeUnpinnedObjects() {
		return null;
	}

	@Override
	public ITopParent removeAllObjects() {
		StaticTopParent.removeAllObjects(this);
		return this;
	}

	@Override
	public boolean hasPinnedObjects() {
		return false;
	}

	@Override
	public IWindowObject getFocusedObject() { return null; }

	@Override
	public ITopParent setFocusedObject(IWindowObject objIn) {
		return null;
	}

	@Override
	public ITopParent setObjectRequestingFocus(IWindowObject objIn, FocusType typeIn) {
		return null;
	}

	@Override
	public IWindowObject getFocusLockObject() { return null; }

	@Override
	public ITopParent setFocusLockObject(IWindowObject objIn) {
		return null;
	}

	@Override
	public ITopParent clearFocusLockObject() {
		return null;
	}

	@Override
	public boolean doesFocusLockExist() {
		return false;
	}

	@Override
	public void clearFocusedObject() {
	}

	@Override
	public void updateFocus() {
	}

	@Override
	public ObjectModifyType getModifyType() { return null; }

	@Override
	public ITopParent setResizingDir(ScreenLocation areaIn) {
		return null;
	}

	@Override
	public ITopParent setModifyingObject(IWindowObject objIn, ObjectModifyType typeIn) {
		return null;
	}

	@Override
	public ITopParent setMaximizingWindow(IWindowParent objIn, ScreenLocation side, boolean centerAroundHeader) {
		return null;
	}

	@Override
	public ITopParent setModifyMousePos(int mX, int mY) {
		return null;
	}

	@Override
	public IWindowObject getModifyingObject() { return null; }

	@Override
	public IWindowParent getMaximizingWindow() { return null; }

	@Override
	public ScreenLocation getMaximizingArea() { return null; }

	@Override
	public boolean getMaximizingHeaderCenter() { return false; }

	@Override
	public ITopParent clearModifyingObject() {
		return null;
	}

	@Override
	public boolean isMouseInsideObject(int mX, int mY) {
		return false;
	}

	@Override
	public boolean isMouseInsideHeader(int mX, int mY) {
		return false;
	}

	@Override
	public IWindowObject getHighestZObjectUnderMouse() { return null; }

	@Override
	public EArrayList<IWindowObject> getAllObjectsUnderMouse() { return null; }

	@Override
	public ITopParent setEscapeStopper(IWindowObject obj) {
		return null;
	}

	@Override
	public IWindowObject getEscapeStopper() { return null; }
	
}
