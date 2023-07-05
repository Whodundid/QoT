package envision.engine.windows.windowTypes;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.EGui;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.KeyboardType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envision.engine.windows.windowUtil.windowEvents.events.EventKeyboard;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public abstract class WindowObject extends EGui implements IWindowObject {
	
	//--------
	// Fields
	//--------
	
	protected WindowObjectProperties properties = new WindowObjectProperties(this);
	
	//---------------------
	// Initializer Methods
	//---------------------
	
	/** Initializes this WindowObject with the specified parent. */
	public void init(IWindowObject objIn) {
		properties.parent = objIn;
		res = Envision.getWindowDims();
	}
	
	/** Initializes this WindowObject with the specified parent and starting coordinates. */
	public void init(IWindowObject objIn, double xIn, double yIn) {
		properties.parent = objIn;
		startX = xIn;
		startY = yIn;
		startXPos = startX;
		startYPos = startY;
		res = Envision.getWindowDims();
	}
	
	/** Initializes this WindowObject with the specified parent and dimensions. */
	public void init(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) {
		properties.parent = objIn;
		startXPos = xIn;
		startYPos = yIn;
		startWidth = widthIn;
		startHeight = heightIn;
		setDimensions(startXPos, startYPos, startWidth, startHeight);
		res = Envision.getWindowDims();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
	}
	
	//-------------------------------
	// Overrides : Object Properties
	//-------------------------------
	
	/** Returns the current properties of this object. */
	public WindowObjectProperties properties() { return properties; }
	
	//--------------------------------
	// Overrides : MouseInputAcceptor
	//--------------------------------
	
	@Override
	public void parseMousePosition(int mX, int mY) {
		getChildren().filterForEach(o -> o.isMouseInside(), o -> o.parseMousePosition(mX, mY));
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		postEvent(new EventMouse(this, mX, mY, button, MouseType.PRESSED));
		var p = getWindowParent();
		if (p != null) p.bringToFront();
		if (!hasFocus() && isMouseOver()) requestFocus();
		if (button == 0 && isResizeable() && !getEdgeSideMouseIsOn().equals(ScreenLocation.OUT)) {
			getTopParent().setResizingDir(getEdgeSideMouseIsOn());
			getTopParent().setModifyMousePos(mX, mY);
			getTopParent().setModifyingObject(this, ObjectModifyType.RESIZE);
		}
	}
	
	@Override
	public void mouseReleased(int mX, int mY, int button) {
		postEvent(new EventMouse(this, mX, mY, button, MouseType.RELEASED));
		var top = getTopParent();
		if (top != null) {
			if (top.getDefaultFocusObject() != null) top.getDefaultFocusObject().requestFocus();
			if (top.getModifyType() == ObjectModifyType.RESIZE) top.clearModifyingObject();
		}
	}
	
	@Override
	public void mouseScrolled(int change) {
		postEvent(new EventMouse(this, Mouse.getMx(), Mouse.getMy(), -1, MouseType.SCROLLED));
		getChildren().filterForEach(o -> o.isMouseInside() && o.willBeDrawn(), o -> o.mouseScrolled(change));
	}
	
	//-----------------------------------
	// Overrides : KeyboardInputAcceptor
	//-----------------------------------
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		postEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.PRESSED));
	}
	
	@Override
	public void keyReleased(char typedChar, int keyCode) {
		postEvent(new EventKeyboard(this, typedChar, keyCode, KeyboardType.RELEASED));
	}
	
}
