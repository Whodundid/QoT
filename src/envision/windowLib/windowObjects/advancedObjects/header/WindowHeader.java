package envision.windowLib.windowObjects.advancedObjects.header;

import envision.debug.DebugFunctions;
import envision.inputHandlers.Mouse;
import envision.windowLib.windowObjects.actionObjects.WindowButton;
import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.WindowParent;
import envision.windowLib.windowTypes.interfaces.IActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowParent;
import envision.windowLib.windowUtil.EObjectGroup;
import envision.windowLib.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import eutil.math.ENumUtil;
import eutil.misc.ScreenLocation;
import game.QoT;
import game.assets.textures.window.WindowTextures;

//Author: Hunter Bragg

/**
 * A header that can be added to window parents allowing them to be
 * moved, closed, maximized, and cycled through.
 * 
 * @author Hunter Bragg
 *
 * @param <E>
 */
public class WindowHeader<E> extends WindowObject<E> {
	
	//---------------
	// Static Fields
	//---------------
	
	/** Default header height in pixels. */
	public static int defaultHeight = 35;
	/** Default header button width in pixels. (also button height) */
	public static int buttonWidth = 32;
	
	//--------
	// Fields
	//--------
	
	/** The parent window for which this header pertains to. */
	protected IWindowParent<?> window;
	
	/** The set of default draw header buttons. */
	public WindowButton<?> fileUpButton, closeButton, maximizeButton, pinButton, minimizeButton;
	
	/** True if using standard default drawing scheme. */
	public boolean drawDefault = true;
	/** True if the header title will be drawn. */
	public boolean drawTitle = true;
	/** True if the header title will be centered, false if drawn from the left. */
	public boolean titleCentered = false;
	/** Used to keep track of whether or not this header is actively being pressed by the mouse. */
	private boolean pressed = false;
	/** True if this header can be used to move the parent window. */
	protected boolean headerMoveable = true;
	/** True if the background should be drawn. Transparent otherwise. */
	protected boolean drawBackground = true;
	/** True if the header will be drawn at all, this includes all child parts. */
	protected boolean drawHeader = true;
	/** Darkens the header color if the parent window has focus. */
	protected boolean drawParentFocus = true;
	/** Always draws the header darker regardless of the parent window's focus status. */
	protected boolean alwaysDrawFocused = false;
	/** True if this header is currently moving the parent window. */
	protected boolean moving = false;

	/** The title of this header. */
	public String title = "";
	/** The outer boarder color. Default is black. */
	public int borderColor = 0xff000000;
	/** The interior color. Default is dark grey. */
	public int mainColor = 0xff2d2d2d;
	//public int mainColor = 0xffe5e5e5;
	/** The color of the title. Default is light grey. */
	public int titleColor = 0xffb2b2b2;
	//public int titleColor = EColors.black.intVal;
	/** A pixel amount to offset the title's draw position by. */
	public int titleOffset = 0;

	/** A position clicked on the header. Used to determine header movement if delta is large enough. */
	private Box2<Integer, Integer> clickPos = new Box2<>(-1, -1);
	/** Used to dynamically keep track of the last created header button position. */
	private int buttonPos = buttonWidth + 2;
	
	//------------
	// Tab Fields
	//------------
	
	/** Used to denote whether or not this header has tabs or not. */
	private boolean isTabHeader = false;
	/** Used to keep track of all tabs on this header. */
	private EArrayList<HeaderTab<?>> tabList;
	/** The currently selected header tab. */
	private HeaderTab<?> currentTab;
	
	//--------------
	// Constructors
	//--------------
	
	/**
	 * Parameterless constructor for custom creation.
	 */
	protected WindowHeader() {}
	
	public WindowHeader(IWindowObject<?> parentIn) { this(parentIn, true, defaultHeight, ""); }
	public WindowHeader(IWindowObject<?> parentIn, boolean drawDefaultIn, int headerHeight) { this(parentIn, drawDefaultIn, headerHeight, ""); }
	public WindowHeader(IWindowObject<?> parentIn, boolean drawDefaultIn, int headerHeight, String titleIn) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY - headerHeight, dim.width, headerHeight);
			
			if (parentIn instanceof IWindowParent<?> p) window = p;
			else window = parentIn.getWindowParent();
		}
		drawDefault = drawDefaultIn;
		
		if (drawDefault) {
			addCloseButton();
			addMaximizeButton();
			addMinimizeButton();
			addPinButton();
			addBackButton();
			
			//set header title
			title = (titleIn.isEmpty()) ? getParent().getObjectName() : titleIn;
			
			EObjectGroup group = new EObjectGroup(getParent());
			group.addObject(this, fileUpButton, pinButton, minimizeButton, maximizeButton, closeButton);
			setObjectGroup(group);
		}
		else title = titleIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject_i(int mX, int mY) {
		//preventative logic to stop windows from moving while the left mouse button is not held down
		if (moving && !Mouse.isButtonDown(0)) {
			moving = false;
			mouseReleased(mX, mY, 0);
		}
		
		//reset parent moving status if conditions not met
		if (!moving && !Mouse.isButtonDown(0) && isParentMoving()) {
			getTopParent().clearModifyingObject();
		}
		
		//check for header grabs with maximize-able windows
		if (pressed && window != null && window.isMaximized()) {
			double dist = ENumUtil.distance(mX, mY, clickPos.getA(), clickPos.getB());
			if (dist >= 5) headerGrabMaximize();
		}
		
		//only true if the header itself should be drawn
		if (drawHeader) {
			drawHeader();
			super.drawObject_i(mX, mY);
		}
		
		handleMaximizeDraw(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		clickPos.set(mX, mY);
		pressed = true;
		
		EObjectGroup group = getObjectGroup();
		
		if (group != null && group.getGroupParent() != null) {
			IWindowObject groupParent = group.getGroupParent();
			
			if (groupParent.isResizeable()) {
				if (groupParent.getEdgeSideMouseIsOn() != ScreenLocation.OUT) {
					getTopParent().setFocusedObject(getWindowParent());
					groupParent.mousePressed(mX, mY, button);
				}
				else headerClick(button);
				
				return;
			}
		}
		headerClick(button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		if (moving) moving = false;
		clickPos.set(-1, -1);
		pressed = false;
		ScreenLocation loc = ScreenLocation.OUT;
		
		if (mXIn <= 8 && mYIn <= 8) loc = ScreenLocation.TOP_LEFT; //top left
		else if (mXIn <= 8 && mYIn >= (res.getHeight() - 8)) loc = ScreenLocation.BOT_LEFT; //bot left
		else if (mXIn >= (res.getWidth() - 8) && mYIn <= 8) loc = ScreenLocation.TOP_RIGHT; //top right
		else if (mXIn >= (res.getWidth() - 8) && mYIn >= (res.getHeight() - 8)) loc = ScreenLocation.BOT_RIGHT; //bot right
		else if (mXIn <= 5) loc = ScreenLocation.LEFT; //left
		else if (mXIn >= (res.getWidth() - 8)) loc = ScreenLocation.RIGHT; //right
		else if (mYIn <= 8) loc = ScreenLocation.TOP; //top
		
		if (loc != ScreenLocation.OUT) getTopParent().setMaximizingWindow(window, loc, false);
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
		if (window != null) window.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void setEnabled(boolean val) {
		drawHeader = val;
		
		//default draw constitutes an always visible close button and
		//selectively visible fileup button.
		if (drawDefault) {
			closeButton.setVisible(val);
			closeButton.setAlwaysVisible(val);
			
			//check for fileup button visibility
			IWindowParent<?> parent = getWindowParent();
			if (parent != null) {
				var hist = parent.getWindowHistory();
				
				//if there is a fileup button, enable visibility if there is a window history
				if (fileUpButton != null) {
					fileUpButton.setVisible(val && hist != null && !hist.isEmpty());
				}
			}
		}
		
		getChildren().forEach(o -> o.setVisible(val));
	}
	
	@Override
	public void actionPerformed(IActionObject<?> object, Object... args) {
		if (object == closeButton) 		handleClose();
		if (object == maximizeButton) 	handleMaximize();
		if (object == minimizeButton) 	handleMinimize();
		if (object == pinButton) 		handlePin();
		if (object == fileUpButton) 	handleFileUp();
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void drawHeader() {
		boolean anyFocus = alwaysDrawFocused || (drawParentFocus) ? anyFocused() : false;
		
		if (drawBackground) {
			drawRect(startX, startY, startX + 1, startY + height, borderColor); //left
			drawRect(startX + 1, startY, endX - 1, startY + 1, borderColor); //top
			drawRect(endX - 1, startY, endX, startY + height, borderColor); //right
			int backColor = anyFocus ? mainColor - 0x1f1f1f : mainColor;
			//int backColor = anyFocus ? mainColor - 0x4f4f4f : mainColor;
			drawRect(startX + 1, startY + 1, endX - 1, startY + height, backColor); //mid
		}
		
		//prevent focusLockObjects from being minimized
		if (minimizeButton != null) {
			minimizeButton.setVisible(getTopParent().getFocusLockObject() != window);
		}
		
		scissor(1);
		if (isTabHeader) drawTabs();
		else if (drawTitle) drawTitle();
		endScissor();
	}
	
	/**
	 * @return true if any object of (any) parent of this header is focused.
	 */
	protected boolean anyFocused() {
		var p = (drawDefault) ? getWindowParent() : getParent();
		if (p == null) return false;
		if (p.hasFocus() || this.hasFocus()) return true;
		for (var o : p.getAllChildren())
			if (o.hasFocus()) return true;
		return false;
	}
	
	/**
	 * Organized logic for handling header title drawing.
	 */
	protected void drawTitle() {
		double tx = startX + 4 + titleOffset;
		String tempTitle = title;
		var p = getWindowParent();
		
		//state whether or not the window is pinned
		if (p != null && p.isPinned()) {
			tempTitle += "" + EColors.mc_lightpurple + "   Pinned";
		}
		
		//debug title stuff
		if (QoT.isDebugMode()) {
			if (p != null && p.isMaximized()) {
				if (DebugFunctions.drawWindowPID) tempTitle += EColors.mc_aqua + "    PID: " + EColors.yellow + p.getObjectID();
				if (DebugFunctions.drawWindowInit) {
					if (DebugFunctions.drawWindowPID) tempTitle += "  ";
					tempTitle += EColors.mc_aqua + "InitTime: " + EColors.yellow + String.valueOf(p.getInitTime());
				}
			}
		}
		
		if (fileUpButton != null && fileUpButton.isVisible()) tx += buttonWidth + 2;
		
		//update position if title should be drawn centered
		if (titleCentered) {
			double tw = getStringWidth(tempTitle);
			//determine exact x position for which to center title within
			tx = startX + (width / 2 - tw / 2) + titleOffset + 1;
		}
		
		drawString(tempTitle, tx, startY + height / 2 - 8, titleColor);
	}
	
	protected void drawTabs() {
		
	}
	
	/**
	 * An outline drawn to show the potential dimensions of a window when
	 * it is moved against a particular side of the screen.
	 * 
	 * @param mXIn Mouse X
	 * @param mYIn Mouse Y
	 */
	protected void handleMaximizeDraw(double mXIn, double mYIn) {
		if (moving && window != null && window.isMaximizable() && ((mXIn <= 5) || (mXIn >= res.getWidth() - 5) || (mYIn <= 8))) {
			//TaskBar b = GameRenderer.instance.getTaskBar();
			
			double w = res.getWidth();
			double h = res.getHeight();
			
			if (mXIn <= 5 && mYIn <= 8) { //top left
				drawHRect(4, 4, w / 2 - 2, (h / 2) - 3, 2, EColors.lgray);
			}
			else if (mXIn <= 5 && mYIn >= (h - 8)) { //bot left
				drawHRect(4, (h / 2) + 3, w / 2 - 2, h - 4, 2, EColors.lgray);
			}
			else if (mXIn >= (w - 5) && mYIn <= 8) { //top right
				drawHRect(w / 2 + 3,  4, w - 4, (h / 2) - 3, 2, EColors.lgray);
			}
			else if (mXIn >= (w - 5) && mYIn >= (h - 8)) { //bot right
				drawHRect(w / 2 + 3, (h / 2) + 3, w - 4, h - 4, 2, EColors.lgray);
			}
			else if (mXIn <= 5) { //left
				drawHRect(4, 4, w / 2 - 2, h - 4, 2, EColors.lgray);
			}
			else if (mXIn >= (w - 5)) { //right
				drawHRect(w / 2 + 3, 4, w - 4, h - 4, 2, EColors.lgray);
			}
			else if (mYIn <= 8) { //top
				drawHRect(4, 4, w - 4, h - 4, 2, EColors.lgray);
			}
			
		}
	}
	
	/**
	 * Called whenever this header is clicked. Performs logic operations
	 * on the host parent window for which this header pertains to.
	 * 
	 * @param button The mouse button pressed
	 */
	protected void headerClick(int button) {
		var topParent = getTopParent();
		if (button == 0) {
			var parent = getParent();
			if (parent instanceof WindowParent<?> p) {
				p.bringToFront();
				
				if (headerMoveable && !window.isMaximized()) {
					moving = true;
					topParent.setModifyingObject(parent, ObjectModifyType.MOVE);
					topParent.setModifyMousePos(Mouse.getMx(), Mouse.getMy());
				}
			}
			else {
				if (headerMoveable) {
					moving = true;
					topParent.setModifyingObject(parent, ObjectModifyType.MOVE);
					topParent.setModifyMousePos(Mouse.getMx(), Mouse.getMy());
				}
			}
		}
		else topParent.clearModifyingObject();
	}
	
	private void headerGrabMaximize() {
		clickPos.set(-1, -1);
		pressed = false;
		getTopParent().setMaximizingWindow(window, ScreenLocation.OUT, true);
	}
	
	//---------
	// Methods
	//---------
	
	/**
	 * Performs logic calls based on this header's host parent to
	 * determine which default header buttons should be visible.
	 * 
	 * @return This header
	 */
	public WindowHeader<E> updateButtonVisibility() {
		if (getParent() instanceof WindowParent<?> window) {
			int buttonPos = buttonWidth * 2 + 3;
			
			if (maximizeButton != null) {
				if (!window.isMaximizable()) {
					maximizeButton.setVisible(false);
				}
				else {
					maximizeButton.setVisible(true);
					maximizeButton.setDimensions(endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
					buttonPos += (buttonWidth + 1);
				}
			}
			
			if (minimizeButton != null) {
				if (!window.isMinimizable()) {
					minimizeButton.setVisible(false);
				}
				else {
					minimizeButton.setVisible(true);
					minimizeButton.setDimensions(endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
					buttonPos += (buttonWidth + 1);
				}
			}
			
			if (pinButton != null) {
				if (!window.isPinnable()) {
					pinButton.setVisible(false);
				}
				else {
					pinButton.setVisible(true);
					pinButton.setDimensions(endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
					buttonPos += (buttonWidth + 1);
				}
			}
			
			if (fileUpButton != null) {
				if (window.getWindowHistory() != null && window.getWindowHistory().isEmpty()) {
					fileUpButton.setVisible(false);
				}
				else {
					fileUpButton.setVisible(true);
					fileUpButton.setDimensions(startX + 2, startY + 2, buttonWidth, buttonWidth);
				}
			}
		}
		else {
			var parent = getWindowParent();
			if (parent != null) {
				var hist = parent.getWindowHistory();
				if (hist != null && hist.isEmpty() && fileUpButton != null) {
					fileUpButton.setVisible(true);
				}
			}
		}
		return this;
	}
	
	protected void handleClose() {
		getParent().close();
	}
	
	protected void handlePin() {
		var p = getWindowParent();
		if (p != null) {
			if (pinButton.getPressedButton() == 0) p.setPinned(!p.isPinned());
		}
	}
	
	protected void handleMaximize() {
		var p = getWindowParent();
		if (p != null) {
			if (p.isMaximizable()) {
				if (p.getMaximizedPosition() == ScreenLocation.TOP) {
					p.setMaximized(ScreenLocation.OUT);
					p.miniaturize();
					getTopParent().setFocusedObject(p);
				}
				else {
					if (p.getMaximizedPosition() == ScreenLocation.OUT) p.setPreMax(p.getDimensions());
					p.setMaximized(ScreenLocation.TOP);
					p.maximize();
					getTopParent().setFocusedObject(p);
				}
				
				//maximizeButton.setButtonTexture(p.getMaximizedPosition() == ScreenLocation.center ? EMCResources.guiMinButton : EMCResources.guiMaxButton);
				//maximizeButton.setButtonSelTexture(p.getMaximizedPosition() == ScreenLocation.center ? EMCResources.guiMinButtonSel : EMCResources.guiMaxButtonSel);
			}
		}
	}
	
	protected void handleMinimize() {
		if (window != null) {
			if (window.isMinimizable()) {
				if (!window.isMinimized()) { window.setMinimized(true); }
			}
		}
	}
	
	protected void handleFileUp() {
		if (getParent() instanceof WindowParent<?> wp) {
			wp.fileUpAndClose();
		}
		else if (getTopParent() != null) getTopParent().close(true);
	}
	
	//------------------
	// Function Buttons
	//------------------
	
	protected void addCloseButton() {
		closeButton = new WindowButton(this, endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
		closeButton.setTextures(WindowTextures.close, WindowTextures.close_sel);
		closeButton.setHoverText("Close");
		closeButton.setObjectName("close button");
		addObject(closeButton);
		buttonPos += buttonWidth;
	}
	
	protected void addMaximizeButton() {
		maximizeButton = new WindowButton(this, endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
		
		if (window != null && window.isMaximizable()) {
			maximizeButton.setButtonTexture(window.getMaximizedPosition() == ScreenLocation.TOP ? WindowTextures.min : WindowTextures.max);
			maximizeButton.setButtonSelTexture(window.getMaximizedPosition() == ScreenLocation.TOP ? WindowTextures.min_sel : WindowTextures.max_sel);
			
			maximizeButton.setHoverText(window.isMaximized() ? "Miniaturize" : "Maximize");
			maximizeButton.setObjectName("maximize button");
			
			addObject(maximizeButton);
			buttonPos += (buttonWidth + 1);
		}
	}
	
	protected void addMinimizeButton() {
		minimizeButton = new WindowButton(this, endX - buttonPos, startY + 2, buttonWidth, buttonWidth);
		minimizeButton.setTextures(WindowTextures.minimize, WindowTextures.minimize_sel);
		minimizeButton.setHoverText("Minimize");
		minimizeButton.setObjectName("minimize button");
		
		if (window != null && window.isMinimizable()) {
			addObject(minimizeButton);
			buttonPos += (buttonWidth + 1);
		}
	}
	
	protected void addPinButton() {
		pinButton = new WindowButton(this, endX - buttonPos, startY + 2, buttonWidth, buttonWidth) {
			@Override
			public void drawObject(int mXIn, int mYIn) {
				if (window != null && window.isPinnable()) {
					setBackgroundColor(window.isPinned() ? 0xffbb0000 : EColors.dgray.c());
				}
				super.drawObject(mXIn, mYIn);
			}
		};
		if (window != null && window.isPinnable()) {
			//pinButton.setTextures(EMCResources.guiPinButtonOpen, EMCResources.guiPinButtonOpenSel);
			pinButton.setDrawBackground(true);
			pinButton.setBackgroundColor(0xffbb0000);
			pinButton.setHoverText("Pin to Hud");
			pinButton.setObjectName("pin button");
			addObject(pinButton);
			buttonPos += (buttonWidth + 1);
		}
	}
	
	protected void addBackButton() {
		fileUpButton = new WindowButton(this, startX + 2, startY + 2, buttonWidth, buttonWidth);
		//fileUpButton.setTextures(EMCResources.backButton, EMCResources.backButtonSel).setVisible(false);
		fileUpButton.setHoverText("Go Back");
		fileUpButton.setObjectName("back button");
		addObject(fileUpButton);
		//buttonPos += (buttonWidth + 1);
	}
	
	//---------
	// Getters
	//---------
	
	public int getTitleColor() { return titleColor; }
	public String getTitle() { return title; }
	public boolean isParentFocusDrawn() { return drawParentFocus; }
	public boolean isHeaderMoveable() { return headerMoveable; }
	public boolean isHeaderMoving() { return moving; }
	
	//---------
	// Setters
	//---------
	
	public void setDrawButtons(boolean val) {
		if (minimizeButton != null) minimizeButton.setVisible(val);
		if (maximizeButton != null) maximizeButton.setVisible(val);
		if (fileUpButton != null) fileUpButton.setVisible(val);
		if (pinButton != null) pinButton.setVisible(val);
		if (closeButton != null) closeButton.setVisible(val);
	}
	
	public void setMoveable(boolean val) { headerMoveable = val; }
	public void setAlwaysDrawFocused(boolean val) { alwaysDrawFocused = val; }
	public void setTitleColor(int colorIn) { titleColor = colorIn; }
	public void setBorderColor(int colorIn) { borderColor = colorIn; }
	public void setBackgroundColor(int colorIn) { mainColor = colorIn; }
	public void setTitle(String stringIn) { title = stringIn; }
	public void setTitleOffset(int offsetIn) { titleOffset = offsetIn; }
	public void setDrawTitleCentered(boolean val) { titleCentered = val; }
	public void setDrawTitle(boolean val) { drawTitle = val; }
	public void setDrawBackground(boolean val) { drawBackground = val; }
	public void setDrawHeader(boolean val) { drawHeader = val; }
	public void setDrawParentFocus(boolean val) { drawParentFocus = val; }
	public void setHeaderMoving(boolean val) { moving = val; pressed = true; }
	
}
