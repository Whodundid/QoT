package envision.engine.windows.windowObjects.actionObjects;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.settings.config.ConfigSetting;
import envision.engine.windows.windowTypes.ActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;
import qot.settings.QoTSettings;

//Author: Hunter Bragg

public class WindowButton<E> extends ActionObject {
	
	//--------
	// Fields
	//--------
	
	public static int defaultColor = 0xffababab;
	public int color = EColors.white.intVal;
	public int disabledColor = EColors.mgray.intVal;
	public int textHoverColor = 0xffffffa0;
	public int backgroundColor = 0xff525252;
	public int backgroundHoverColor = 0x886666bb;
	public int selectedColor = 0xffffff00;
	public int borderColor = 0xff000000;
	public int borderWidth = 1;
	protected boolean forceDrawHover = false;
	protected boolean drawBorder = false;
	protected boolean custom = false;
	protected boolean usingBaseTextures = true;
	protected boolean stretchTextures = false;
	protected boolean drawTextures = true;
	protected int pressedButton = -1;
	protected int textOffset = 0;
	protected boolean drawBackground = true;
	protected boolean drawBackgroundHover = true;
	protected boolean trueFalseButton = false;
	protected boolean drawString = true;
	protected boolean drawDisabledColor = true;
	protected boolean drawCentered = true;
	protected boolean drawShadowed = true;
	protected boolean drawSelected = false;
	protected boolean acceptRightClicks = false;
	protected GameTexture btnTexture = null;
	protected GameTexture btnSelTexture = null;
	private GameTexture curTexture;
	protected String displayString;
	protected E genericObject;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowButton() {
		
	}
	
	public WindowButton(IWindowObject parentIn) {
		super(parentIn);
		custom = true;
	}
	
	public WindowButton(IWindowObject parentIn, String displayStringIn) {
		this(parentIn, 0, 0, 0, 0, displayStringIn);
	}
	
	public WindowButton(IWindowObject parentIn, double posX, double posY, double width, double height) {
		this(parentIn, posX, posY, width, height, "");
	}
	
	public WindowButton(IWindowObject parentIn, double posX, double posY, double width, double height, String displayStringIn) {
		init(parentIn, posX, posY, width, height);
		displayString = displayStringIn;
	}
	
    public WindowButton(IWindowObject parentIn, double posX, double posY, double width, double height, ConfigSetting<Boolean> settingIn) {
        this(parentIn, posX, posY, width, height, "");
        setTrueFalseButton(true, settingIn);
    }
	
	//-----------
	// Overrides
	//-----------

	@Override
	public void drawObject(int mX, int mY) {
		boolean mouseHover = isClickable() && isMouseOver();
		boolean mouseCheck = /*!Mouse.isButtonDown(0) && */mouseHover;
		int stringColor = isEnabled() ? (mouseCheck ? textHoverColor : color) : (drawDisabledColor ? disabledColor : color + 0xbbbbbb);
		if (drawSelected) stringColor = selectedColor;
		
		if (drawBackground && btnTexture == null && btnSelTexture == null) {
			drawRect(borderColor);
			drawRect(backgroundColor, 1);
		}
		
		//reset the color buffer to prepare for texture drawing
//		GLSettings.fullBright();
		
		//only draw textures if specified
		if (drawTextures) {
			
			//determine textures to draw
			if (usingBaseTextures) {
				if (mouseHover) bindSel();
				else bindBase();
			}
			else if (btnTexture != null && btnSelTexture == null) bindBase();
			else if (mouseHover) bindSel();
			else bindBase();
			
			//draw the textures
			if (usingBaseTextures) {
				if (stretchTextures) drawTexture(curTexture, startX, startY, width, height);
				else drawBaseTexture(mouseHover);
			}
			else if (btnTexture != null) {
				
				//if (stretchTextures) {
				drawTexture(curTexture, startX, startY, width, height);
				//}
				/*
				else {
					int tW = btnTexture.getWidth();
					int tH = btnTexture.getHeight();
					
					double sX = NumUtil.clamp(midX - tW / 2, startX, endX);
					double sY = NumUtil.clamp(midY - tH / 2, startY, endY);
					double w = NumUtil.clamp(tW, 0, tW);
					double h = NumUtil.clamp(tH, 0, tH);
					
					drawTexture(sX, sY, w, h);
				}
				*/
				
				if (btnSelTexture == null && mouseHover) {
					//drawRect(startX + 1, startY + 1, endX - 1, endY - 1, 0xaa8B97D3);
					drawRect(startX, startY, startX + 1, endY, EColors.lgray); //left
					drawRect(startX, startY, endX, startY + 1, EColors.lgray); //top
					drawRect(endX - 1, startY + 1, endX, endY, EColors.lgray); //right
					drawRect(startX + 1, endY - 1, endX - 1, endY, EColors.lgray); //bot
				}
			}
			
			if (drawBackgroundHover && (forceDrawHover || mouseHover)) {
				//drawRect(backgroundHoverColor, 1);
			}
			
			//draw disabled overlay
			if (!isEnabled()) drawRect(startX, startY, endX, endY, 0x77000000);
		}
		
		//------------------------------------
		//------------------------------------
		
		if (drawString && displayString != null && !displayString.isEmpty()) {
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			
			if (drawCentered) {
				if (drawShadowed) drawStringCS(displayString, midX, midY - FontRenderer.HALF_FH + 3, stringColor);
				else drawStringC(displayString, midX, midY - FontRenderer.HALF_FH + 3, stringColor);
			}
			else if (drawShadowed) drawStringS(displayString, startX + textOffset, midY - FontRenderer.HALF_FH + 3, stringColor);
			else drawString(displayString, startX + textOffset, midY - FontRenderer.HALF_FH + 3, stringColor);
			
			endScissor();
		}
		
		forceDrawHover = false;
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
	    super.mousePressed(mX, mY, button);
		if (isClickable()) {
			EUtil.nullDo(getWindowParent(), w -> w.bringToFront());
			pressButton(button);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == GLFW.GLFW_KEY_ENTER) {
			if (isEnabled() && willBeDrawn() && isClickable()) performAction();
		}
		super.keyPressed(typedChar, keyCode);
	}
	
	//---------
	// Methods
	//---------
	
    public void toggleTrueFalseDisplay() {
        if (trueFalseButton) {
            String cur = getString();
            boolean val = cur.equals("False");
            setString(val ? "True" : "False");
            setStringColor(val ? 0xff55ff55 : 0xffff5555);
        }
    }
    
    public void toggleTrueFalseDisplay(boolean val) {
        if (trueFalseButton) {
            setString(val ? "True" : "False");
            setStringColor(val ? 0xff55ff55 : 0xffff5555);
        }
    }
    
    public void toggleTrueFalseDisplay(boolean val, boolean updateConfig) {
        if (trueFalseButton) {
            setString(val ? "True" : "False");
            setStringColor(val ? 0xff55ff55 : 0xffff5555);
            if (updateConfig) QoTSettings.saveConfig();
        }
    }
    
    public void toggleTrueFalseDisplay(ConfigSetting<Boolean> setting, boolean updateConfig) {
        if (trueFalseButton) {
            boolean val = setting.toggle();
            setString(val ? "True" : "False");
            setStringColor(val ? 0xff55ff55 : 0xffff5555);
            if (updateConfig) QoTSettings.saveConfig();
        }
    }
	
	public void setDrawStringCentered(boolean val) { drawCentered = val; }
	public void setDrawStringShadowed(boolean val) { drawShadowed = val; }
	public void setDisplayStringOffset(int offsetIn) { textOffset = offsetIn; }
	
	//------------------
	// Internal Methods
	//------------------
	
	protected void pressButton(int button) {
		if (isEnabled() && willBeDrawn()) {
			pressedButton = button;
			if (runActionOnPress) press(button);
			else if (button == 0 || (button == 1 && acceptRightClicks)) {
				playPressSound();
				performAction();
			}
		}
	}
	
	//---------------------------------
	// Internal Helper Drawing Methods
	//---------------------------------
	
	//texture binding methods
	private void bindBase() { curTexture = btnTexture; }
	private void bindSel() { curTexture = btnSelTexture; }
	
	//draw method
	private void drawBaseTexture(boolean mouseHover) {
		double i = height > 20 ? 20 : height;
		//int offset = mouseHover ? 20 : 0;
		//if (!isEnabled()) { offset = 0; }
		if (height < 20) {
			i = i >= 3 ? i - 2 : i;
			//drawTexture(startX, startY, 0, 66 + offset, width - 2, i);
			//drawTexture(startX + width - 2, startY, 198, 66 + offset, 2, i);
			//drawTexture(startX, startY + height - 2, 0, 84 + offset, width - 2, 2);
			//drawTexture(startX + width - 2, startY + height - 2, 198, 84 + offset, 2, 2);
		}
		else {
			//drawTexture(startX, startY, 0, 66 + offset, width - 2, i);
			//drawTexture(startX + width - 2, startY, 198, 66 + offset, 2, i);
		}
	}
	
	private void checkForBaseTextures() {
		usingBaseTextures = false;
		//usingBaseTextures = EMCResources.guiButtonBase.equals(btnTexture) && EMCResources.guiButtonSel.equals(btnSelTexture);
	}
	
	//---------
	// Getters
	//---------
	
	public int getPressedButton() { return pressedButton; }
	public int getBackgroundColor() { return backgroundColor; }
	public int getStringColor() { return color; }
	public int getStringHoverColor() { return textHoverColor; }
	public String getString() { return displayString; }
	public boolean getAcceptsRightClicks() { return acceptRightClicks; }
	
	//---------
	// Setters
	//---------
	
	public void setForceDrawHover(boolean val) { forceDrawHover = val; }
	public void setTextures(GameTexture base, GameTexture sel) { setButtonTexture(base); setButtonSelTexture(sel); }
	public void setButtonTexture(GameTexture loc) { btnTexture = loc; checkForBaseTextures(); }
	public void setButtonSelTexture(GameTexture loc) { btnSelTexture = loc; checkForBaseTextures(); }
	public void setString(String stringIn) { displayString = stringIn; }
	public void setStringColor(int colorIn) { color = colorIn; }
	public void setStringColor(EColors colorIn) { if (colorIn != null) { color = colorIn.c(); } }
	public void setStringDisabledColor(int colorIn) { disabledColor = colorIn; }
	public void setStringDisabledColor(EColors colorIn) { if (colorIn != null) { disabledColor = colorIn.intVal; } }
	public void setStringHoverColor(int colorIn) { textHoverColor = colorIn; }
	public void setStringHoverColor(EColors colorIn) { if (colorIn != null) { textHoverColor = colorIn.intVal; } }
	public void setBackgroundColor(int colorIn) { backgroundColor = colorIn; }
	public void setBackgroundColor(EColors colorIn) { if (colorIn != null) { backgroundColor = colorIn.intVal; } }
	public void setBackgroundHoverColor(int colorIn) { backgroundHoverColor = colorIn; }
	public void setBackgroundHoverColor(EColors colorIn) { if (colorIn != null) { backgroundHoverColor = colorIn.intVal; } }
	public void setTrueFalseButton(boolean val) { setTrueFalseButton(val, false); }
	public void setTrueFalseButton(boolean val, boolean initial) { trueFalseButton = val; toggleTrueFalseDisplay(initial); }
	public void setTrueFalseButton(boolean val, ConfigSetting<Boolean> settingIn) { setTrueFalseButton(val, settingIn != null ? settingIn.get() : false); }
	public void setDrawString(boolean val) { drawString = val; }
	public void setDrawDisabledColor(boolean val) { drawDisabledColor = val; }
	public void setDrawStretched(boolean val) { stretchTextures = val; }
	public void setDrawBackgroundHover(boolean val) { drawBackgroundHover = val; }
	public void setDrawSelected(boolean val) { drawSelected = val; }
	public void setSelectedColor(EColors colorIn) { setSelectedColor(colorIn.intVal); }
	public void setSelectedColor(int colorIn) { selectedColor = colorIn; }
	public void setAcceptRightClicks(boolean val) { acceptRightClicks = val; }
	
	public void setDrawTextures(boolean val) {
		drawTextures = val;
	}
	
	public void setDrawBackground(boolean val) {
		drawBackground = val;
	}
	
	//================
    // Generic Object
    //================
    
    public void setGenericObject(E objectIn) { genericObject = objectIn; }
    public E getGenericObject() { return genericObject; }
	
	//----------------
	// Static Methods
	//----------------
	
	public static void playPressSound() {
		
	}
	
	public static void setTextures(GameTexture base, GameTexture sel, WindowButton... buttons) { setButtonVal(b -> b.setTextures(base, sel), buttons); }
	public static void setButtonTexture(GameTexture base, WindowButton... buttons) { setButtonVal(b -> b.setButtonTexture(base), buttons); }
	public static void setButtonSelTexture(GameTexture sel, WindowButton... buttons) { setButtonVal(b -> b.setButtonSelTexture(sel), buttons); }
	public static void setString(String textIn, WindowButton... buttons) { setButtonVal(b -> b.setString(textIn), buttons); }
	public static void setStringColor(EColors colorIn, WindowButton... buttons) { setButtonVal(b -> b.setStringColor(colorIn), buttons); }
	public static void setStringColor(int colorIn, WindowButton... buttons) { setButtonVal(b -> b.setStringColor(colorIn), buttons); }
	public static void setStringHoverColor(EColors colorIn, WindowButton... buttons) { setButtonVal(b -> b.setStringHoverColor(colorIn), buttons); }
	public static void setStringHoverColor(int colorIn, WindowButton... buttons) { setButtonVal(b -> b.setStringHoverColor(colorIn), buttons); }
	public static void setStringDisabledColor(EColors colorIn, WindowButton... buttons) { setButtonVal(b -> b.setStringDisabledColor(colorIn), buttons); }
	public static void setStringDisabledColor(int colorIn, WindowButton... buttons) { setButtonVal(b -> b.setStringDisabledColor(colorIn), buttons); }
	public static void setBackgroundColor(EColors colorIn, WindowButton... buttons) { setButtonVal(b -> b.setBackgroundColor(colorIn), buttons); }
	public static void setBackgroundColor(int colorIn, WindowButton... buttons) { setButtonVal(b -> b.setBackgroundColor(colorIn), buttons); }
	public static void setBackgroundHoverColor(EColors colorIn, WindowButton... buttons) { setButtonVal(b -> b.setBackgroundHoverColor(colorIn), buttons); }
	public static void setBackgroundHoverColor(int colorIn, WindowButton... buttons) { setButtonVal(b -> b.setBackgroundHoverColor(colorIn), buttons); }
	public static void setDrawString(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawString(val), buttons); }
	public static void setDrawDisabledColor(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawDisabledColor(val), buttons); }
	public static void setDrawStretched(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawStretched(val), buttons); }
	public static void setDrawTextures(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawTextures(val), buttons); }
	public static void setDrawBackground(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawBackground(val), buttons); }
	public static void setDrawBackgroundHover(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawBackgroundHover(val), buttons); }
	public static void setDrawSelected(boolean val, WindowButton... buttons) { setButtonVal(b -> b.setDrawSelected(val)); }
	public static void setSelectedColor(EColors colorIn, WindowButton... buttons) { setButtonVal(b -> b.setSelectedColor(colorIn)); }
	public static void setSelectedColor(int colorIn, WindowButton... buttons) { setButtonVal(b -> b.setSelectedColor(colorIn)); }
	
	private static void setButtonVal(Consumer<? super WindowButton> action, WindowButton... buttons) {
		EUtil.filterNullForEachA(action, buttons);
	}
	
}
