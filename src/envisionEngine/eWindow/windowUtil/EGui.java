package envisionEngine.eWindow.windowUtil;

import envisionEngine.eWindow.windowUtil.input.KeyboardInputAcceptor;
import envisionEngine.eWindow.windowUtil.input.MouseInputAcceptor;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.textureSystem.GameTexture;
import main.Game;
import mathUtil.NumberUtil;
import miscUtil.Rotation;
import openGL_Util.GLObject;
import renderUtil.EColors;
import renderUtil.ScreenLocation;
import storageUtil.EDimension;
import storageUtil.StorageBox;
import tempUtil.WindowSize;

//Author: Hunter Bragg

public abstract class EGui extends GLObject implements KeyboardInputAcceptor, MouseInputAcceptor {

	public Game game = Game.getGame();
	public FontRenderer fontRenderer = game.getFontRenderer();
	public WindowSize res = game.getWindowSize();
	public double startXPos, startYPos, startWidth, startHeight;
	public double startX, startY, endX, endY;
	public double width, height;
	public double midX, midY;
	public int mX, mY;
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
	public void drawHRect(int color, int offset) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, 1, color); }
	
	public void drawTexture(GameTexture texture) { drawTexture(startX, startY, width, height, texture, false, Rotation.N); }
	public void drawTexture(GameTexture texture, Rotation rotation) { drawTexture(startX, startY, width, height, texture, false, rotation); }
	public void drawTexture(GameTexture texture, boolean flip) { drawTexture(startX, startY, width, height, texture, flip, Rotation.N); }
	public void drawTexture(GameTexture texture, Rotation rotation, boolean flip) { drawTexture(startX, startY, width, height, texture, flip, rotation); }
	public void drawTexture(GameTexture texture, int offset) { drawTexture(startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), texture, false, Rotation.N); }
	public void drawTexture(GameTexture texture, int offset, Rotation rotation) { drawTexture(startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), texture, false, rotation); }
	public void drawTexture(GameTexture texture, int offset, boolean flip) { drawTexture(startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), texture, flip); }
	public void drawTexture(GameTexture texture, int offset, Rotation rotation, boolean flip) { drawTexture(startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), texture, flip, rotation); }
	
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
	
	public EGui setPosition(double newX, double newY) { setDimensions(newX, newY, width, height); return this; }
	public EGui setDimensions(EDimension dimIn) { return setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	public EGui setDimensions(double widthIn, double heightIn) { return setDimensions(startX, startY, widthIn, heightIn); }
	public EGui setDimensions(double startXIn, double startYIn, double widthIn, double heightIn) {
		startX = startXIn;
		startY = startYIn;
		width = NumberUtil.clamp(widthIn, minWidth, maxWidth);
		height = NumberUtil.clamp(heightIn, minHeight, maxHeight);
		endX = startX + width;
		endY = startY + height;
		midX = startX + width / 2.0;
		midY = startY + height / 2.0;
		return this;
	}
	public EGui setInitialPosition(double xIn, double yIn) { startXPos = xIn; startYPos = yIn; return this; }
	public StorageBox<Double, Double> getInitialPosition() { return new StorageBox<Double, Double>(startXPos, startYPos); }
	/** Centers the gui in the middle of the screen with the specified dimensions. */
	public void centerGuiWithSize(double widthIn, double heightIn) {
		WindowSize res = Game.getWindowSize(); //get the screen size
		double sWidth = res.getWidth();
		double sHeight = res.getHeight();
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
			if (mX >= startX - 5 && mX <= startX) { left = true; }
			if (mX >= endX - 4 && mX <= endX + 5) { right = true; }
			if (mY >= startY - 6 && mY <= startY) { top = true; }
			if (mY >= endY - 4 && mY <= endY + 4) { bottom = true; }
			if (left) {
				if (top) { return ScreenLocation.topLeft; }
				else if (bottom) { return ScreenLocation.botLeft; }
				else { return ScreenLocation.left; }
			}
			else if (right) {
				if (top) { return ScreenLocation.topRight; }
				else if (bottom) { return ScreenLocation.botRight; }
				else { return ScreenLocation.right; }
			} 
			else if (top) { return ScreenLocation.top; }
			else if (bottom) { return ScreenLocation.bot; }
		}
		return ScreenLocation.out;
	}
	
	public boolean isMouseInside(int mX, int mY) { return mX >= startX && mX <= endX && mY >= startY && mY <= endY; }
	
}
