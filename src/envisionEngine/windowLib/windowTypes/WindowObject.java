package envisionEngine.windowLib.windowTypes;

import envisionEngine.inputHandlers.Mouse;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowUtil.EGui;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.KeyboardType;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.MouseType;
import envisionEngine.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import envisionEngine.windowLib.windowUtil.windowEvents.events.EventKeyboard;
import envisionEngine.windowLib.windowUtil.windowEvents.events.EventMouse;
import eutil.misc.ScreenLocation;
import qot.QoT;

//Author: Hunter Bragg

public abstract class WindowObject<E> extends EGui implements IWindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	protected WindowObjectProperties<E> properties = new WindowObjectProperties(this);
	
	//---------------------
	// Initializer Methods
	//---------------------
	
	/** Initializes this WindowObject with the specified parent. */
	public void init(IWindowObject<?> objIn) {
		properties.parent = objIn;
		res = QoT.getWindowSize();
	}
	
	/** Initializes this WindowObject with the specified parent and starting coordinates. */
	public void init(IWindowObject<?> objIn, Number xIn, Number yIn) {
		properties.parent = objIn;
		startX = xIn.doubleValue();
		startY = yIn.doubleValue();
		startXPos = startX;
		startYPos = startY;
		res = QoT.getWindowSize();
	}
	
	/** Initializes this WindowObject with the specified parent and dimensions. */
	public void init(IWindowObject<?> objIn, Number xIn, Number yIn, Number widthIn, Number heightIn) {
		properties.parent = objIn;
		startXPos = xIn.doubleValue();
		startYPos = yIn.doubleValue();
		startWidth = widthIn.doubleValue();
		startHeight = heightIn.doubleValue();
		setDimensions(xIn.doubleValue(), yIn.doubleValue(), widthIn.doubleValue(), heightIn.doubleValue());
		res = QoT.getWindowSize();
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
	public WindowObjectProperties<E> properties() { return properties; }
	
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
