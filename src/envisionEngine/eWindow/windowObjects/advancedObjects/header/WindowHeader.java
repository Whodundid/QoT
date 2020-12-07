package envisionEngine.eWindow.windowObjects.advancedObjects.header;

import assets.textures.WindowTextures;
import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IActionObject;
import envisionEngine.eWindow.windowTypes.interfaces.ITopParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowParent;
import envisionEngine.eWindow.windowUtil.EObjectGroup;
import envisionEngine.eWindow.windowUtil.windowEvents.eventUtil.ObjectModifyType;
import gameSystems.gameRenderer.GameRenderer;
import gameSystems.input.Mouse;
import util.mathUtil.NumUtil;
import util.renderUtil.EColors;
import util.renderUtil.ScreenLocation;
import util.storageUtil.EDimension;
import util.storageUtil.StorageBox;

//Author: Hunter Bragg

public class WindowHeader extends WindowObject {
	
	public static int defaultHeight = 35;
	public static int buttonWidth = 32;
	public WindowButton fileUpButton, closeButton, maximizeButton, pinButton, minimizeButton;
	public boolean fullClose = false;
	public boolean drawDefault = true;
	public boolean drawTitle = true;
	public boolean titleCentered = false;
	public String title = "";
	public int borderColor = 0xff000000;
	public int mainColor = 0xff2d2d2d;
	public int titleColor = 0xb2b2b2;
	public int titleOffset = 0;
	protected boolean headerMoveable = true;
	protected boolean drawBackground = true;
	protected boolean drawHeader = true;
	protected boolean drawParentFocus = true;
	protected boolean alwaysDrawFocused = false;
	protected boolean moving = false;
	private StorageBox<Integer, Integer> clickPos = new StorageBox(-1, -1);
	protected IWindowParent window;
	private int buttonPos = buttonWidth + 2;
	private boolean pressed = false;
	
	//-------------------------
	//WindowHeader Constructors
	//-------------------------
	
	protected WindowHeader() {}
	public WindowHeader(IWindowObject parentIn) { this(parentIn, true, defaultHeight, ""); }
	public WindowHeader(IWindowObject parentIn, boolean drawDefaultIn, int headerHeight) { this(parentIn, drawDefaultIn, headerHeight, ""); }
	public WindowHeader(IWindowObject parentIn, boolean drawDefaultIn, int headerHeight, String titleIn) {
		if (parentIn != null) {
			EDimension dim = parentIn.getDimensions();
			init(parentIn, dim.startX, dim.startY - headerHeight, dim.width, headerHeight);
			
			if (parentIn instanceof IWindowParent) { window = (IWindowParent) parentIn; }
			else { window = parentIn.getWindowParent(); }
		}
		drawDefault = drawDefaultIn;
		
		if (drawDefault) {
			addCloseButton();
			addMaximizeButton();
			addMinimizeButton();
			addPinButton();
			addBackButton();
			
			if (titleIn.isEmpty()) {
				title = getParent().getObjectName();
			}
			else { title = titleIn; }
			
			EObjectGroup group = new EObjectGroup(getParent());
			group.addObject(this, fileUpButton, pinButton, minimizeButton, maximizeButton, closeButton);
			setObjectGroup(group);
		}
		else { title = titleIn; }
	}
	
	//-----------------------
	//IWindowObject Overrides
	//-----------------------
	
	@Override
	public void drawObject(int mX, int mY) {
		ITopParent top = getTopParent();
		
		//preventative logic to stop windows from moving while the left mouse button is not held down
		if (moving && !Mouse.isButtonDown(0)) { moving = false; mouseReleased(mX, mY, 0); }
		
		if (!moving && top.getModifyingObject() == parent && top.getModifyType() == ObjectModifyType.Move && !Mouse.isButtonDown(0)) {
			top.clearModifyingObject();
		}
		
		//check for header grabs with maximizable windows
		if (pressed && window != null && window.isMaximized()) {
			double dist = NumUtil.distance(mX, mY, clickPos.getA(), clickPos.getB());
			if (dist >= 5) {
				headerGrabMaximize();
			}
		}
		
		if (drawHeader) {
			
			boolean anyFocus = alwaysDrawFocused;
			if (drawParentFocus) {
				IWindowObject p = drawDefault ? getWindowParent() : getParent();
				if (p != null) {
					if (p.hasFocus()) { anyFocus = true; }
					else {
						for (IWindowObject o : p.getAllChildren()) {
							if (o.hasFocus()) { anyFocus = true; break; }
						}
					}
				}
			}
			
			if (drawBackground) {
				drawRect(startX, startY, startX + 1, startY + height, borderColor); //left
				drawRect(startX + 1, startY, endX - 1, startY + 1, borderColor); //top
				drawRect(endX - 1, startY, endX, startY + height, borderColor); //right
				drawRect(startX + 1, startY + 1, endX - 1, startY + height, anyFocus ? mainColor - 0x1f1f1f : mainColor); //mid
			}
			
			//prevent focusLockObjects from being minimizable
			if (minimizeButton != null) {
				minimizeButton.setVisible(getTopParent().getFocusLockObject() != window);
			}
			
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			if (drawTitle) {
				double tx = startX + 4 + titleOffset;
				String tempTitle = title;
				IWindowParent p = getWindowParent();
				
				if (p != null && p.isPinned()) {
					tempTitle += "" + EColors.mc_lightpurple + "   Pinned";
				}
				
				/*
				if (EnhancedMC.isDebugMode()) {
					if (p != null && p.isMaximized()) {
						if (DebugFunctions.drawWindowPID) { tempTitle += EColors.mc_aqua + "    PID: " + EColors.yellow + p.getObjectID(); }
						if (DebugFunctions.drawWindowInit) {
							if (DebugFunctions.drawWindowPID) { tempTitle += "  "; }
							tempTitle += EColors.mc_aqua + "InitTime: " + EColors.yellow + String.valueOf(p.getInitTime());
						}
					}
				}
				*/
				
				if (fileUpButton != null && fileUpButton.isVisible()) { tx += buttonWidth + 2; }
				
				if (titleCentered) {
					double tw = getStringWidth(tempTitle);
					tx = startX + (width / 2 - tw / 2) + titleOffset + 1;
				}
				
				drawString(tempTitle, tx, startY + height / 2 - 8, titleColor);
			}
			
			super.drawObject(mX, mY);
			endScissor();
		}
		
		handleMaximizeDraw(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
		super.mousePressed(mX, mY, button);
		clickPos.setValues(mX, mY);
		pressed = true;
		
		if (getObjectGroup() != null && getObjectGroup().getGroupParent() != null) {
			IWindowObject groupParent = getObjectGroup().getGroupParent();
			if (groupParent.isResizeable()) {
				if (groupParent.getEdgeAreaMouseIsOn() != ScreenLocation.out) {
					getTopParent().setFocusedObject(getWindowParent());
					groupParent.mousePressed(mX, mY, button);
				}
				else { headerClick(button); }
				return;
			}
		}
		headerClick(button);
	}
	
	@Override
	public void mouseReleased(int mXIn, int mYIn, int button) {
		if (moving) { moving = false; }
		clickPos.setValues(-1, -1);
		pressed = false;
		
		WindowParent p = (WindowParent) window;
		
		if (mXIn <= 5 && mYIn <= 8) { //top left
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.topLeft, false);
		}
		else if (mXIn <= 5 && mYIn >= (res.getHeight() - 8)) { //bot left
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.botLeft, false);
		}
		else if (mXIn >= (res.getWidth() - 5) && mYIn <= 8) { //top right
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.topRight, false);
		}
		else if (mXIn >= (res.getWidth() - 5) && mYIn >= (res.getHeight() - 8)) { //bot right
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.botRight, false);
		}
		else if (mXIn <= 5) { //left
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.left, false);
		}
		else if (mXIn >= (res.getWidth() - 5)) { //right
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.right, false);
		}
		else if (mYIn <= 8) { //top
			GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.center, false);
		}
		
		super.mouseReleased(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		super.keyPressed(typedChar, keyCode);
		if (window != null) { window.keyPressed(typedChar, keyCode); }
	}
	
	@Override
	public WindowHeader setEnabled(boolean val) {
		drawHeader = val;
		if (drawDefault) {
			closeButton.setVisible(val).setPersistent(val);
			if (getWindowParent() != null) {
				if (fileUpButton != null) { fileUpButton.setVisible(val && getWindowParent().getWindowHistory() != null && !getWindowParent().getWindowHistory().isEmpty()); }
			}
		}
		for (IWindowObject o : getObjects()) { o.setVisible(val); }
		return this;
	}
	
	protected void handleMaximizeDraw(double mXIn, double mYIn) {
		if (moving && window != null && window.isMaximizable() && ((mXIn <= 5) || (mXIn >= res.getWidth() - 5) || (mYIn <= 8))) {
			//TaskBar b = GameRenderer.instance.getTaskBar();
			
			if (mXIn <= 5 && mYIn <= 8) { //top left
				drawHRect(4, 4, res.getWidth() / 2 - 2, (res.getHeight() / 2) - 3, 2, EColors.lgray);
			}
			else if (mXIn <= 5 && mYIn >= (res.getHeight() - 8)) { //bot left
				drawHRect(4, (res.getHeight() / 2) + 3, res.getWidth() / 2 - 2, res.getHeight() - 4, 2, EColors.lgray);
			}
			else if (mXIn >= (res.getWidth() - 5) && mYIn <= 8) { //top right
				drawHRect(res.getWidth() / 2 + 3,  4, res.getWidth() - 4, (res.getHeight() / 2) - 3, 2, EColors.lgray);
			}
			else if (mXIn >= (res.getWidth() - 5) && mYIn >= (res.getHeight() - 8)) { //bot right
				drawHRect(res.getWidth() / 2 + 3, (res.getHeight() / 2) + 3, res.getWidth() - 4, res.getHeight() - 4, 2, EColors.lgray);
			}
			else if (mXIn <= 5) { //left
				drawHRect(4, 4, res.getWidth() / 2 - 2, res.getHeight() - 4, 2, EColors.lgray);
			}
			else if (mXIn >= (res.getWidth() - 5)) { //right
				drawHRect(res.getWidth() / 2 + 3, 4, res.getWidth() - 4, res.getHeight() - 4, 2, EColors.lgray);
			}
			else if (mYIn <= 8) { //top
				drawHRect(4, 4, res.getWidth() - 4, res.getHeight() - 4, 2, EColors.lgray);
			}
			
		}
	}
	
	protected void headerClick(int button) {
		ITopParent topParent = getTopParent();
		if (button == 0) {
			IWindowObject parent = getParent();
			if (parent instanceof WindowParent p) {
				p.bringToFront();
				
				if (headerMoveable && !window.isMaximized()) {
					moving = true;
					topParent.setModifyingObject(parent, ObjectModifyType.Move);
					topParent.setModifyMousePos(mX, mY);
				}
			}
			else {
				if (headerMoveable) {
					moving = true;
					topParent.setModifyingObject(parent, ObjectModifyType.Move);
					topParent.setModifyMousePos(mX, mY);
				}
			}
		}
		else { topParent.clearModifyingObject(); }
	}
	
	private void headerGrabMaximize() {
		clickPos.setValues(-1, -1);
		pressed = false;
		GameRenderer.instance.setMaximizingWindow(window, ScreenLocation.out, true);
	}
	
	public WindowHeader updateButtonVisibility() {
		if (getParent() instanceof WindowParent) {
			WindowParent window = (WindowParent) getParent();
			if (window != null) {
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
		}
		else if (getWindowParent() != null && getWindowParent().getWindowHistory() != null && !getWindowParent().getWindowHistory().isEmpty() && fileUpButton != null) { fileUpButton.setVisible(true); }
		return this;
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		if (object == closeButton) { handleClose(); }
		if (object == maximizeButton) { handleMaximize(); }
		if (object == minimizeButton) { handleMinimize(); }
		if (object == pinButton) { handlePin(); }
		if (object == fileUpButton) { handleFileUp(); }
	}
	
	protected void handleClose() {
		parent.close();
	}
	
	protected void handlePin() {
		IWindowParent p = getWindowParent();
		if (p != null) {
			if (pinButton.getPressedButton() == 0) { p.setPinned(!p.isPinned()); }
		}
	}
	
	protected void handleMaximize() {
		IWindowParent p = getWindowParent();
		if (p != null) {
			if (p.isMaximizable()) {
				if (p.getMaximizedPosition() == ScreenLocation.center) {
					p.setMaximized(ScreenLocation.out);
					p.miniaturize();
					GameRenderer.instance.setFocusedObject(p);
				}
				else {
					if (p.getMaximizedPosition() == ScreenLocation.out) { p.setPreMax(p.getDimensions()); }
					p.setMaximized(ScreenLocation.center);
					p.maximize();
					GameRenderer.instance.setFocusedObject(p);
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
		if (getParent() instanceof WindowParent) {
			((WindowParent) getParent()).fileUpAndClose();
		}
		else if (getTopParent() != null) { getTopParent().close(true); }
	}
	
	//-----------------------------
	//WindowHeader Function Buttons
	//-----------------------------
	
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
			maximizeButton.setButtonTexture(window.getMaximizedPosition() == ScreenLocation.center ? WindowTextures.min : WindowTextures.max);
			maximizeButton.setButtonSelTexture(window.getMaximizedPosition() == ScreenLocation.center ? WindowTextures.min_sel : WindowTextures.max_sel);
			
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
		
		//if (CoreApp.enableTaskBar.get() && window != null && window.isMinimizable()) {
		//	addObject(minimizeButton);
		//	buttonPos += (buttonWidth + 1);
		//}
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
			pinButton.setDrawBackground(true).setBackgroundColor(0xffbb0000);
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
	
	//--------------------
	//WindowHeader Getters
	//--------------------
	
	public int getTitleColor() { return titleColor; }
	public String getTitle() { return title; }
	public boolean isParentFocusDrawn() { return drawParentFocus; }
	public boolean isHeaderMoveable() { return headerMoveable; }
	public boolean isHeaderMoving() { return moving; }
	
	//--------------------
	//WindowHeader Setters
	//--------------------
	
	public WindowHeader setDrawButtons(boolean val) {
		if (minimizeButton != null) { minimizeButton.setVisible(val); }
		if (maximizeButton != null) { maximizeButton.setVisible(val); }
		if (fileUpButton != null) { fileUpButton.setVisible(val); }
		if (pinButton != null) { pinButton.setVisible(val); }
		if (closeButton != null) { closeButton.setVisible(val); }
		return this;
	}
	
	public WindowHeader setAlwaysDrawFocused(boolean val) { alwaysDrawFocused = val; return this; }
	public WindowHeader setMoveable(boolean val) { headerMoveable = val; return this; }
	public WindowHeader setTitleColor(int colorIn) { titleColor = colorIn; return this; }
	public WindowHeader setBorderColor(int colorIn) { borderColor = colorIn; return this; }
	public WindowHeader setBackgroundColor(int colorIn) { mainColor = colorIn; return this; }
	public WindowHeader setTitle(String stringIn) { title = stringIn; return this; }
	public WindowHeader setTitleOffset(int offsetIn) { titleOffset = offsetIn; return this; }
	public WindowHeader setDrawTitleCentered(boolean val) { titleCentered = val; return this; }
	public WindowHeader setDrawTitle(boolean val) { drawTitle = val; return this; }
	public WindowHeader setDrawBackground(boolean val) { drawBackground = val; return this; }
	public WindowHeader setDrawHeader(boolean val) { drawHeader = val; return this; }
	public WindowHeader setDrawParentFocus(boolean val) { drawParentFocus = val; return this; }
	public WindowHeader setHeaderMoving(boolean val) { moving = val; pressed = true; return this; }
	
}
