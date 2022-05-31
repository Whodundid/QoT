package engine.windowLib.windowUtil;

import engine.WindowSize;
import engine.renderEngine.GLObject;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.renderEngine.textureSystem.GameTexture;
import engine.windowLib.windowUtil.input.KeyboardInputAcceptor;
import engine.windowLib.windowUtil.input.MouseInputAcceptor;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import eutil.math.EDimension;
import eutil.math.NumberUtil;
import eutil.misc.Rotation;
import eutil.misc.ScreenLocation;
import main.QoT;

//Author: Hunter Bragg

public abstract class EGui extends GLObject implements KeyboardInputAcceptor, MouseInputAcceptor {

	public FontRenderer fontRenderer = QoT.getFontRenderer();
	public WindowSize res = QoT.getWindowSize();
	public double startXPos, startYPos, startWidth, startHeight;
	public double startX, startY, endX, endY;
	public double width, height;
	public double midX, midY;
	/** Temporarily cached X mouse coordinate. */
	public int mX;
	/** Temporarily cached Y mouse coordinate. */
	public int mY;
	public double minWidth = 0;
	public double minHeight = 0;
	public double maxWidth = Double.MAX_VALUE;
	public double maxHeight = Double.MAX_VALUE;
	
	//-----------------
	// Drawing Helpers
	//-----------------
	
	public void drawRect(EColors color) { drawRect(color.c()); }
	public void drawRect(int color) { drawRect(startX, startY, endX, endY, color); }
	
	public void drawHRect(EColors color) { drawHRect(color.c()); }
	public void drawHRect(int color) { drawHRect(startX, startY, endX, endY, 1, color); }
	
	public void drawRect(EColors color, int offset) { drawRect(color.c(), offset); }
	public void drawRect(int color, int offset) { drawRect(startX + offset, startY + offset, endX - offset, endY - offset, color); }
	
	public void drawHRect(EColors color, int offset) { drawHRect(color.c(), offset); }
	public void drawHRect(EColors color, int size, int offset) { drawHRect(color.c(), size, offset); }
	public void drawHRect(int color, int offset) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, 1, color); }
	public void drawHRect(int color, int size, int offset) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, size, color); }
	
	public void drawTexture(GameTexture texture) { drawTexture(texture, startX, startY, width, height, false, Rotation.UP); }
	public void drawTexture(GameTexture texture, Rotation rotation) { drawTexture(texture, startX, startY, width, height, false, rotation); }
	public void drawTexture(GameTexture texture, boolean flip) { drawTexture(texture, startX, startY, width, height, flip, Rotation.UP); }
	public void drawTexture(GameTexture texture, Rotation rotation, boolean flip) { drawTexture(texture, startX, startY, width, height, flip, rotation); }
	public void drawTexture(GameTexture texture, int offset) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), false, Rotation.UP); }
	public void drawTexture(GameTexture texture, int offset, Rotation rotation) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), false, rotation); }
	public void drawTexture(GameTexture texture, int offset, boolean flip) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), flip); }
	public void drawTexture(GameTexture texture, int offset, Rotation rotation, boolean flip) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), flip, rotation); }
	
	public double drawString(Object obj) { return drawString(obj, midX, midY, 0xffffffff); }
	public double drawString(String str) { return drawString(str, midX, midY, 0xffffffff); }
	public double drawString(Object obj, EColors color) { return drawString(obj, midX, midY, color); }
	public double drawString(String str, EColors color) { return drawString(str, midX, midY, color); }
	public double drawString(Object obj, int color) { return drawString(obj, midX, midY, color); }
	public double drawString(String str, int color) { return drawString(str, midX, midY, color); }
	public double drawStringC(Object obj) { return drawStringC(obj, midX, midY, 0xffffffff); }
	public double drawStringC(String str) { return drawStringC(str, midX, midY - FontRenderer.FONT_HEIGHT / 2, 0xffffffff); }
	public double drawStringC(Object obj, EColors color) { return drawStringC(obj, midX, midY, color); }
	public double drawStringC(String str, EColors color) { return drawStringC(str, midX, midY, color); }
	public double drawStringC(Object obj, int color) { return drawStringC(obj, midX, midY, color); }
	public double drawStringC(String str, int color) { return drawStringC(str, midX, midY, color); }
	
	public void scissor() { scissor(startX, startY, endX, endY); }
	public void scissor(double offset) { scissor(startX + offset, startY + offset, endX - offset, endY - offset); }
	
	//----------------------
	
	public double getMinWidth() { return minWidth; }
	public double getMinHeight() { return minHeight; }
	public double getMaxWidth() { return maxWidth; }
	public double getMaxHeight() { return maxHeight; }
	public EGui setMinDims(double widthIn, double heightIn) { setMinWidth(widthIn).setMinHeight(heightIn); return this; }
	public EGui setMaxDims(double widthIn, double heightIn) { setMaxWidth(widthIn).setMaxHeight(heightIn); return this; }
	public EGui setMinWidth(double widthIn) { minWidth = widthIn; return this; }
	public EGui setMinHeight(double heightIn) { minHeight = heightIn; return this; }
	public EGui setMaxWidth(double widthIn) { maxWidth = widthIn; return this; }
	public EGui setMaxHeight(double heightIn) { maxHeight = heightIn; return this; }
	
	public void setPosition(double newX, double newY) { setDimensions(newX, newY, width, height); }
	public void setDimensions(EDimension dimIn) { setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	public void setSize(double widthIn, double heightIn) { setDimensions(startX, startY, widthIn, heightIn); }
	public void setDimensions(double startXIn, double startYIn, double widthIn, double heightIn) {
		startX = startXIn;
		startY = startYIn;
		width = NumberUtil.clamp(widthIn, minWidth, maxWidth);
		height = NumberUtil.clamp(heightIn, minHeight, maxHeight);
		endX = startX + width;
		endY = startY + height;
		midX = startX + width / 2.0;
		midY = startY + height / 2.0;
	}
	public void setInitialPosition(double xIn, double yIn) { startXPos = xIn; startYPos = yIn; }
	public Box2<Double, Double> getInitialPosition() { return new Box2<Double, Double>(startXPos, startYPos); }
	/** Centers the gui in the middle of the screen with the specified dimensions. */
	public void centerGuiWithSize(double widthIn, double heightIn) {
		double sWidth = QoT.getWidth();
		double sHeight = QoT.getHeight();
		double startX, startY, width, height;
		
		if (sWidth >= widthIn) { //check if the screen width is larger than the desired gui width
			//if it is, set the xPos so that it will be in the middle of the screen
			startX = (sWidth - widthIn) / 2;
			width = widthIn;
		}
		else { //otherwise, restrict the gui's width to the screen's width
			startX = 0;
			width = sWidth;
		}
		
		if (sHeight >= heightIn) { //check if the screen height is larger than the desired gui height
			//if it is, set the yPos so that it will be in the middle of the screen
			startY = (sHeight - heightIn) / 2;
			height = heightIn;
		}
		else { //otherwise, restrict the gui's width to the screen's height
			startY = 0;
			height = sHeight;
		}
		
		setDimensions(startX, startY, width, height); //apply the dimensions to the gui
	}
	public EDimension getDimensions() { return new EDimension(startX, startY, endX, endY); }
	
	//--------------
	// Mouse Checks
	//--------------
	
	/** Returns the ScreenLocation area the mouse is currently on for an object. */
	public ScreenLocation getEdgeAreaMouseIsOn(int mX, int mY) {
		boolean left = false, right = false, top = false, bottom = false;
		if (mX >= startX - 5 && mX <= endX + 5 && mY >= startY - 5 && mY <= endY + 4) {
			if (mX >= startX - 5 && mX <= startX) left = true;
			if (mX >= endX - 4 && mX <= endX + 5) right = true;
			if (mY >= startY - 6 && mY <= startY) top = true;
			if (mY >= endY - 4 && mY <= endY + 4) bottom = true;
			if (left) {
				if (top) return ScreenLocation.TOP_LEFT;
				else if (bottom) return ScreenLocation.BOT_LEFT;
				else return ScreenLocation.LEFT;
			}
			else if (right) {
				if (top) return ScreenLocation.TOP_RIGHT;
				else if (bottom) return ScreenLocation.BOT_RIGHT;
				else return ScreenLocation.RIGHT;
			} 
			else if (top) return ScreenLocation.TOP;
			else if (bottom) return ScreenLocation.BOT;
		}
		return ScreenLocation.OUT;
	}
	
	public boolean isMouseInside(int mX, int mY) {
		return mX >= startX && mX <= endX && mY >= startY && mY <= endY;
	}
	
}
