package engine.windowLib.windowObjects.actionObjects;

import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import engine.inputHandlers.Mouse;
import engine.renderEngine.GLSettings;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.renderEngine.textureSystem.GameTexture;
import engine.windowLib.windowTypes.ActionObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class WindowButton<E> extends ActionObject<E> {
	
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
	protected boolean drawSelected = false;
	protected boolean acceptRightClicks = false;
	protected GameTexture btnTexture = null;
	protected GameTexture btnSelTexture = null;
	protected String displayString;
	
	public WindowButton(IWindowObject<?> parentIn) { super(parentIn); custom = true; }
	
	public WindowButton(IWindowObject<?> parentIn, double posX, double posY, double width, double height) { this(parentIn, posX, posY, width, height, ""); }
	public WindowButton(IWindowObject<?> parentIn, double posX, double posY, double width, double height, String displayStringIn) {
		init(parentIn, posX, posY, width, height);
		displayString = displayStringIn;
	}

	@Override
	public void drawObject(int mX, int mY) {
		boolean mouseHover = isClickable() && isMouseOver();
		boolean mouseCheck = !Mouse.isButtonDown(0) && mouseHover;
		int stringColor = isEnabled() ? (mouseCheck ? textHoverColor : color) : (drawDisabledColor ? disabledColor : color + 0xbbbbbb);
		if (drawSelected) stringColor = selectedColor;
		
		if (drawBackground && btnTexture == null && btnSelTexture == null) {
			drawRect(borderColor);
			drawRect(backgroundColor, 1);
		}
		
		//reset the color buffer to prepare for texture drawing
		GLSettings.fullBright();
		
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
			
			//prime the renderer
			GLSettings.enableBlend();
			GLSettings.blendSeparate();
			GLSettings.blendFunc();
			
			//draw the textures
			if (usingBaseTextures) {
				if (stretchTextures) drawTexture(startX, startY, width, height);
				else drawBaseTexture(mouseHover);
			}
			else if (btnTexture != null) {
				
				//if (stretchTextures) {
					drawTexture(startX, startY, width, height);
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
		
		if (drawString) {
			scissor(startX + 1, startY + 1, endX - 1, endY - 1);
			
			if (drawCentered) drawStringC(displayString, midX, midY - FontRenderer.FONT_HEIGHT / 2 + 3, stringColor);
			else drawString(displayString, midX, midY - FontRenderer.FONT_HEIGHT / 2 + 3, stringColor);
			
			endScissor();
		}
		
		forceDrawHover = false;
		
		super.drawObject(mX, mY);
	}
	
	@Override
	public void mousePressed(int mX, int mY, int button) {
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
	
	//------------------
	//EGuiButton Methods
	//------------------
	
	protected void pressButton(int button) {
		if (enabled && willBeDrawn()) {
			pressedButton = button;
			if (runActionOnPress) onPress(button);
			else if (button == 0 || (button == 1 && acceptRightClicks)) {
				playPressSound();
				performAction();
			}
		}
	}
	
	public WindowButton<E> updateTrueFalseDisplay() {
		if (trueFalseButton) {
			String cur = getString();
			boolean val = cur.equals("False");
			setString(val ? "True" : "False").setStringColor(val ? 0x55ff55 : 0xff5555);
		}
		return this;
	}
	
	public WindowButton<E> updateTrueFalseDisplay(boolean val) {
		if (trueFalseButton) { setString(val ? "True" : "False").setStringColor(val ? 0x55ff55 : 0xff5555); }
		return this;
	}
	
	public WindowButton<E> setDrawStringCentered(boolean val) { drawCentered = val; return this; }
	public WindowButton<E> setDisplayStringOffset(int offsetIn) { textOffset = offsetIn; return this; }
	
	//----------------------
	//Drawing Helper Methods
	//----------------------
	
	//texture binding methods
	private void bindBase() { if (btnTexture != null) { bindTexture(btnTexture); } }
	private void bindSel() { if (btnSelTexture != null ) { bindTexture(btnSelTexture); } }
	
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
	
	//------------------
	//EGuiButton Getters
	//------------------
	
	public int getPressedButton() { return pressedButton; }
	public int getBackgroundColor() { return backgroundColor; }
	public int getStringColor() { return color; }
	public int getStringHoverColor() { return textHoverColor; }
	public String getString() { return displayString; }
	public boolean getAcceptsRightClicks() { return acceptRightClicks; }
	
	//------------------
	//EGuiButton Setters
	//------------------
	
	public WindowButton<E> setForceDrawHover(boolean val) { forceDrawHover = val; return this; }
	public WindowButton<E> setTextures(GameTexture base, GameTexture sel) { setButtonTexture(base); setButtonSelTexture(sel); return this; }
	public WindowButton<E> setButtonTexture(GameTexture loc) { btnTexture = loc; checkForBaseTextures(); return this; }
	public WindowButton<E> setButtonSelTexture(GameTexture loc) { btnSelTexture = loc; checkForBaseTextures(); return this; }
	public WindowButton<E> setString(String stringIn) { displayString = stringIn; return this; }
	public WindowButton<E> setStringColor(int colorIn) { color = colorIn; return this; }
	public WindowButton<E> setStringColor(EColors colorIn) { if (colorIn != null) { color = colorIn.c(); } return this; }
	public WindowButton<E> setStringDisabledColor(int colorIn) { disabledColor = colorIn; return this; }
	public WindowButton<E> setStringDisabledColor(EColors colorIn) { if (colorIn != null) { disabledColor = colorIn.c(); } return this; }
	public WindowButton<E> setStringHoverColor(int colorIn) { textHoverColor = colorIn; return this; }
	public WindowButton<E> setStringHoverColor(EColors colorIn) { if (colorIn != null) { textHoverColor = colorIn.c(); } return this; }
	public WindowButton<E> setBackgroundColor(int colorIn) { backgroundColor = colorIn; return this; }
	public WindowButton<E> setBackgroundColor(EColors colorIn) { if (colorIn != null) { backgroundColor = colorIn.c(); } return this; }
	public WindowButton<E> setBackgroundHoverColor(int colorIn) { backgroundHoverColor = colorIn; return this; }
	public WindowButton<E> setBackgroundHoverColor(EColors colorIn) { if (colorIn != null) { backgroundHoverColor = colorIn.c(); } return this; }
	public WindowButton<E> setTrueFalseButton(boolean val) { return setTrueFalseButton(val, false); }
	public WindowButton<E> setTrueFalseButton(boolean val, boolean initial) { trueFalseButton = val; updateTrueFalseDisplay(initial); return this; }
	public WindowButton<E> setDrawString(boolean val) { drawString = val; return this; }
	public WindowButton<E> setDrawDisabledColor(boolean val) { drawDisabledColor = val; return this; }
	public WindowButton<E> setDrawStretched(boolean val) { stretchTextures = val; return this; }
	public WindowButton<E> setDrawBackgroundHover(boolean val) { drawBackgroundHover = val; return this; }
	public WindowButton<E> setDrawSelected(boolean val) { drawSelected = val; return this; }
	public WindowButton<E> setSelectedColor(EColors colorIn) { return setSelectedColor(colorIn.intVal); }
	public WindowButton<E> setSelectedColor(int colorIn) { selectedColor = colorIn; return this; }
	public WindowButton<E> setAcceptRightClicks(boolean val) { acceptRightClicks = val; return this; }
	
	public WindowButton<E> setDrawTextures(boolean val) {
		drawTextures = val;
		if (!custom) {
			if (drawBackground && !drawTextures) { setMaxDims(Integer.MAX_VALUE, Integer.MAX_VALUE); }
			else { setMaxDims(200, 20); }
		}
		return this;
	}
	
	public WindowButton<E> setDrawBackground(boolean val) {
		drawBackground = val;
		if (!custom) {
			if (drawBackground && !drawTextures) setMaxDims(Integer.MAX_VALUE, Integer.MAX_VALUE);
			else setMaxDims(200, 20);
		}
		return this;
	}
	
	//--------------
	//Static Methods
	//--------------
	
	public static void playPressSound() {
		
	}
	
	public static void setTextures(GameTexture base, GameTexture sel, WindowButton<?>... buttons) { setButtonVal(b -> b.setTextures(base, sel), buttons); }
	public static void setButtonTexture(GameTexture base, WindowButton<?>... buttons) { setButtonVal(b -> b.setButtonTexture(base), buttons); }
	public static void setButtonSelTexture(GameTexture sel, WindowButton<?>... buttons) { setButtonVal(b -> b.setButtonSelTexture(sel), buttons); }
	public static void setString(String textIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setString(textIn), buttons); }
	public static void setStringColor(EColors colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setStringColor(colorIn), buttons); }
	public static void setStringColor(int colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setStringColor(colorIn), buttons); }
	public static void setStringHoverColor(EColors colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setStringHoverColor(colorIn), buttons); }
	public static void setStringHoverColor(int colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setStringHoverColor(colorIn), buttons); }
	public static void setStringDisabledColor(EColors colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setStringDisabledColor(colorIn), buttons); }
	public static void setStringDisabledColor(int colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setStringDisabledColor(colorIn), buttons); }
	public static void setBackgroundColor(EColors colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setBackgroundColor(colorIn), buttons); }
	public static void setBackgroundColor(int colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setBackgroundColor(colorIn), buttons); }
	public static void setBackgroundHoverColor(EColors colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setBackgroundHoverColor(colorIn), buttons); }
	public static void setBackgroundHoverColor(int colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setBackgroundHoverColor(colorIn), buttons); }
	public static void setDrawString(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawString(val), buttons); }
	public static void setDrawDisabledColor(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawDisabledColor(val), buttons); }
	public static void setDrawStretched(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawStretched(val), buttons); }
	public static void setDrawTextures(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawTextures(val), buttons); }
	public static void setDrawBackground(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawBackground(val), buttons); }
	public static void setDrawBackgroundHover(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawBackgroundHover(val), buttons); }
	public static void setDrawSelected(boolean val, WindowButton<?>... buttons) { setButtonVal(b -> b.setDrawSelected(val)); }
	public static void setSelectedColor(EColors colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setSelectedColor(colorIn)); }
	public static void setSelectedColor(int colorIn, WindowButton<?>... buttons) { setButtonVal(b -> b.setSelectedColor(colorIn)); }
	
	private static void setButtonVal(Consumer<? super WindowButton<?>> action, WindowButton<?>... buttons) {
		EUtil.filterNullForEachA(action, buttons);
	}
	
}
