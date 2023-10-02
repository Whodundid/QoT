package envision.engine.windows.windowObjects.utilityObjects;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.ActionWindowParent;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.engine.windows.windowUtil.windowEvents.ObjectEvent;
import envision.engine.windows.windowUtil.windowEvents.eventUtil.MouseType;
import envision.engine.windows.windowUtil.windowEvents.events.EventMouse;
import eutil.colors.EColors;
import eutil.datatypes.boxes.BoxList;

//Author: Hunter Bragg

public class RightClickMenu<E> extends ActionWindowParent {
	
	//--------
	// Fields
	//--------
	
	protected BoxList<String, WindowButton> options = new BoxList<>();
	//public WindowLabel title;
	public String title = "";
	public boolean useTitle = false;
	protected boolean dontCloseOnPress = false;
	public int optionHeight = 40;
	public int titleHeight = 30;
	public int backgroundColor = 0xff4b4b4b;
	public int titleBackgroundColor = 0xff1f1f1f;
	public int separatorLineColor = 0xff000000;
	public int borderColor = 0xff000000;
	protected E genericObject;
	
	//--------------
	// Constructors
	//--------------
    
    public RightClickMenu() { this(Envision.getActiveTopParent()); }
    public RightClickMenu(String titleIn) { this(Envision.getActiveTopParent(), titleIn); }
    public RightClickMenu(IWindowObject parent) { this(parent, ""); }
    public RightClickMenu(IWindowObject parent, String titleIn) {
        super(parent);
        setDimensions(Mouse.getMx(), Mouse.getMy(), 260, 60);
        getTopParent().registerListener(this);
        
        setTitle(titleIn);
        setUseTitle(true);
        showInTaskBar = false;
    }
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mX, int mY) {
		drawRect(startX + 1, useTitle ? startY + titleHeight : startY + 1, endX - 1, endY, backgroundColor); //background
		drawRect(startX, startY, startX + 1, endY, borderColor); //left
		drawRect(startX, startY, endX, startY + 1, borderColor); //top
		drawRect(endX - 1, startY, endX, endY, borderColor); //right
		drawRect(startX, endY - 1, endX, endY, borderColor); //bottom
		drawRect(startX + 41, useTitle ? startY + titleHeight : startY + 1, startX + 42, endY - 1, separatorLineColor); //separator line
		
		if (useTitle) {
			drawRect(startX + 1, startY + 1, endX - 1, startY + titleHeight, titleBackgroundColor);
			drawRect(startX + 1, startY + titleHeight, endX - 1, startY + titleHeight + 1, separatorLineColor);
			drawStringC(title, midX, startY + titleHeight / 2 - FontRenderer.FONT_HEIGHT / 2 + 3, EColors.mc_gold);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 1) close();
		performAction();
		super.mousePressed(mXIn, mYIn, button);
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == 1) getTopParent().removeObject();
	}
	
	@Override
	public void onEvent(ObjectEvent e) {
		if (e instanceof EventMouse em) {
			if (em.getMouseType() != MouseType.PRESSED) return;
			if (isMouseInside()) return;
			if (dontCloseOnPress) return;
			
			getTopParent().unregisterListener(this);
			close();
		}
	}
	
	@Override public void showOnCurrent() { showOnCurrent(ObjectPosition.CURSOR_CORNER); }
	@Override public void showOnTop() { showOnTop(ObjectPosition.CURSOR_CORNER); }
	
	//---------
	// Methods
	//---------
	
	public void display() { displayOnCurrent(); }
	public void displayOnCurrent() {
		Envision.getCurrentScreen().displayWindow(this, ObjectPosition.CURSOR_CORNER);
	}
	
	public void displayOnTop() {
		Envision.getDeveloperDesktop().displayWindow(this, ObjectPosition.CURSOR_CORNER);
	}
	
	public void addOption(String... optionNames) { for (String s : optionNames) { addOption(s); } }
	public void addOption(String optionName) { addOption(optionName, (GameTexture) null); }
	public void removeOption(String... optionNames) { for (String s : optionNames) { removeOption(s); } }
	
	public void addOptionIf(boolean cond, String optionName) { addOptionIf(cond, optionName, (GameTexture) null); }
	public void addOptionIf(boolean cond, String optionName, GameTexture icon) {
	    if (!cond) addOption(optionName, icon);
	}
	
	public void addOptionIf(boolean cond, String optionName, Runnable action) { addOptionIf(cond, optionName, null, action); }
	public void addOptionIf(boolean cond, String optionName, GameTexture icon, Runnable action) {
	    if (cond) addOption(optionName, icon, action);
	}
	
	public WindowButton addOption(String optionName, Runnable action) { return addOption(optionName, null, action); }
	public WindowButton addOption(String optionName, GameTexture optionIcon, Runnable action) {
		if (optionName == null || options.containsA(optionName)) return null;
		
		var b = new WindowButton(this, optionName) {
			@Override
			public void drawObject(int mX, int mY) {
				if (isMouseInside()) {
					drawRect(startX + 41, startY, endX, endY + 1, 0x33adadad);
				}
				if (optionIcon != null) {
					int color = (isMouseInside()) ? 0xffffffff : 0xdddddddd;
					drawTexture(optionIcon, startX + 3, midY - 32 / 2, 32, 32, color);
				}
				super.drawObject(mX, mY);
			}
			@Override
			public void press(int button) {
				if (getPressedButton() == 0 && isEnabled()) {
					playPressSound();
					action.run();
					RightClickMenu.this.performAction(getString());
					getTopParent().unregisterListener(RightClickMenu.this);
					RightClickMenu.this.close();
				}
				else RightClickMenu.this.close();
			}
		};
		
		b.setDrawStringCentered(false);
		b.setDisplayStringOffset(46);
		b.setDrawTextures(false);
		b.setDrawBackground(false);
		b.setRunActionOnPress(true);
		options.add(optionName, b);
		addObject(b);
		resize();
		
		return b;
	}
	
	public WindowButton addOption(String optionName, GameTexture optionIcon) {
		if (optionName == null || options.containsA(optionName)) return null;
		
		var b = new WindowButton(this, optionName) {
			@Override
			public void drawObject(int mX, int mY) {
				if (isMouseInside()) {
					drawRect(startX + 41, startY, endX, endY + 1, 0x33adadad);
				}
				if (optionIcon != null) {
					int color = (isMouseInside()) ? 0xffffffff : 0xdddddddd;
					drawTexture(optionIcon, startX + 3, midY - 32 / 2, 32, 32, color);
				}
				super.drawObject(mX, mY);
			}
			@Override
			public void press(int button) {
				if (getPressedButton() == 0 && isEnabled()) {
					playPressSound();
					RightClickMenu.this.performAction(getString());
					getTopParent().unregisterListener(RightClickMenu.this);
					RightClickMenu.this.close();
				}
				else RightClickMenu.this.close();
			}
		};
		
		b.setDrawStringCentered(false);
		b.setDisplayStringOffset(46);
		b.setDrawTextures(false);
		b.setDrawBackground(false);
		b.setRunActionOnPress(true);
		options.add(optionName, b);
		addObject(b);
		resize();
		
		return b;
	}
	
	public void addOptionAtPos(String optionName, GameTexture optionIcon, int posIn) {
		var b = new WindowButton(this, optionName) {
			@Override
			public void drawObject(int mX, int mY) {
				if (isMouseInside()) {
					drawRect(startX + 41, startY, endX, endY + 1, 0x33adadad);
				}
				if (optionIcon != null) {
					int color = (isMouseInside()) ? 0xffffffff : 0xdddddddd;
					drawTexture(optionIcon, startX + 3, midY - 32 / 2, 32, 32, color);
				}
				super.drawObject(mX, mY);
			}
			@Override
			public void press(int button) {
				if (getPressedButton() == 0 && isEnabled()) {
					playPressSound();
					RightClickMenu.this.performAction(getString());
					getTopParent().unregisterListener(RightClickMenu.this);
					RightClickMenu.this.close();
				}
				else RightClickMenu.this.close();
			}
		};
		b.setDrawStringCentered(false);
		b.setDisplayStringOffset(46);
		b.setDrawTextures(false);
		b.setDrawBackground(false);
		b.setRunActionOnPress(true);
		options.add(posIn, optionName, b);
		addObject(b);
		resize();
	}
	
	public void removeOption(String optionName) {
		if (optionName != null && options.containsA(optionName)) {
			var l = options.removeBoxesContainingA(optionName);
			if (!l.isEmpty()) {
				var s = l.get(0);
				if (s != null) {
					var b = s.getB();
					removeObject(b);
				}
			}
			resize();
		}
	}
	
	public void setOptionEnabled(String optionName, boolean enabledVal) {
		var boxes = options.getAllBoxesWithA(optionName);
		if (boxes.size() > 0) {
			for (var b : boxes) {
				if (b.getA().equals(optionName)) b.getB().setEnabled(enabledVal);
			}
		}
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private void resize() {
		int longestOption = 0;
		double newWidth = 0;
		double newHeight = options.size() * optionHeight + options.size() + 4 + (useTitle ? titleHeight : 0);
		double sX = startX;
		double sY = startY;
		
		for (var s : options.getAVals()) {
			int w = FontRenderer.strWidth(s);
			if (w > longestOption) longestOption = w;
		}
		
		if (useTitle) {
			int len = FontRenderer.strWidth(title);
			if (len > longestOption) longestOption = len;
		}
		
		newWidth = longestOption + 60;
		
		double testHeight = startY + newHeight;
		if (testHeight > res.height) {
			double diff = testHeight - res.height;
			sY -= diff;
		}
		
		setDimensions(sX, sY, newWidth, newHeight);
		
		//title.setDimensions(midX, startY + titleHeight / 2 - 3, 0, 0);
		
		for (int i = 0; i < options.size(); i++) {
			var b = options.getB(i);
			b.setDimensions(sX + 2, sY + (useTitle ? titleHeight : 0) + 2 + (optionHeight * i + i), newWidth - 4, optionHeight);
		}
	}
	
	//================
    // Generic Object
    //================
    
    public void setGenericObject(E objectIn) { genericObject = objectIn; }
    public E getGenericObject() { return genericObject; }
	
	//---------
	// Getters
	//---------
	
	public int getBackgroundColor() { return backgroundColor; }
	public int getTitleBackgroundColor() { return titleBackgroundColor; }
	public int getLineSepartorColor() { return separatorLineColor; }
	public int getBorderColor() { return borderColor; }
	public int getTitleHeight() { return titleHeight; }
	//public WindowLabel getTitle() { return title; }
	public String getTitle() { return title; }
	public boolean hasTitle() { return useTitle; }
	
	//---------
	// Setters
	//---------
	
	public void setDontCloseOnPress(boolean val) { dontCloseOnPress = val; }
	public void setBackgroundColor(int colorIn) { backgroundColor = colorIn; }
	public void setTitleBackgroundColor(int colorIn) { titleBackgroundColor = colorIn; }
	public void setSeparatorLineColor(int colorIn) { separatorLineColor = colorIn; }
	public void setBorderColor(int colorIn) { borderColor = colorIn; }
	public void setTitleHeight(int heightIn) { titleHeight = heightIn; resize(); }
	//public void setTitle(String titleIn) { title.setString(titleIn); }
	//public void setUseTitle(boolean val) { useTitle = val; title.setVisible(val); resize(); }
	public void setTitle(String titleIn) { title = titleIn; }
	public void setUseTitle(boolean val) { useTitle = val; resize(); }
	
}
