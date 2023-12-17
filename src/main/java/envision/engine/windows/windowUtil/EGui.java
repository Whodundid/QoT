package envision.engine.windows.windowUtil;

import envision.Envision;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.resourceLoaders.Sprite;
import envision.engine.windows.windowUtil.input.KeyboardInputAcceptor;
import envision.engine.windows.windowUtil.input.MouseInputAcceptor;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2d;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_d;
import eutil.math.dimensions.Dimension_i;
import eutil.misc.Rotation;
import eutil.misc.ScreenLocation;

//Author: Hunter Bragg

public abstract class EGui extends RenderingManager implements KeyboardInputAcceptor, MouseInputAcceptor {

	//--------
	// Fields
	//--------
	
	public FontRenderer fontRenderer = FontRenderer.getInstance();
	public Dimension_i res = Envision.getWindowDims();
	public double startXPos, startYPos, startWidth, startHeight;
	public double unboundedWidth, unboundedHeight;
	public double startX, startY, endX, endY;
	public double width, height;
	public double midX, midY;
	public double minWidth = 0;
	public double minHeight = 0;
	public double maxWidth = Double.MAX_VALUE;
	public double maxHeight = Double.MAX_VALUE;
	
	//-----------------
	// Drawing Helpers
	//-----------------
	
	public static void drawRect(EGui o, EColors color) { drawRect(o.startX, o.startY, o.endX, o.endY, color.intVal); }
	public static void drawRect(EGui o, int color) { drawRect(o.startX, o.startY, o.endX, o.endY, color); }
	
	public static void drawRect(EGui o, EColors color, double offset) {
		drawRect(o.startX + offset,
			     		  		  o.startY + offset,
			     		  		  o.endX - offset,
			     		  		  o.endY - offset,
			     		  		  color.intVal);
	}
	
	public static void drawRect(EGui o, int color, double offset) {
		drawRect(o.startX + offset,
			     		  		  o.startY + offset,
			     		  		  o.endX - offset,
			     		  		  o.endY - offset,
			     		  		  color);
	}
	
	public void drawRect(EColors color) { drawRect(color.intVal); }
	public void drawRect(int color) { drawRect(startX, startY, endX, endY, color); }
	
	public void drawHRect(EColors color) { drawHRect(color.intVal); }
	public void drawHRect(int color) { drawHRect(startX, startY, endX, endY, 1, color); }
	
	public void drawRect(EColors color, double offset) { drawRect(color.intVal, offset); }
	public void drawRect(int color, double offset) { drawRect(startX + offset, startY + offset, endX - offset, endY - offset, color); }
	
	public void drawHRect(EColors color, double offset) { drawHRect(color.intVal, offset); }
	public void drawHRect(EColors color, int size, double offset) { drawHRect(color.intVal, size, offset); }
	public void drawHRect(int color, double offset) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, 1, color); }
	public void drawHRect(int color, int size, double offset) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, size, color); }
	
	public void drawSprite(Sprite sprite) { drawSprite(sprite, startX, startY, width, height, false, Rotation.UP); }
	public void drawSprite(Sprite sprite, boolean flip) { drawSprite(sprite, startX, startY, width, height, flip, Rotation.UP); }
	public void drawSprite(Sprite sprite, double offset) { drawSprite(sprite, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), false, Rotation.UP); }
	public void drawSprite(Sprite sprite, Rotation rotation) { drawSprite(sprite, startX, startY, width, height, false, rotation); }
	public void drawSprite(Sprite sprite, double offset, boolean flip) { drawSprite(sprite, startX + offset, startY + offset, width - (offset * 2), height * (offset * 2), flip, Rotation.UP); }
    public void drawSprite(Sprite sprite, double offset, Rotation rotation) { drawSprite(sprite, startX + offset, startY + offset, width - (offset * 2), height * (offset * 2), false, rotation); }
    public void drawSprite(Sprite sprite, double offset, boolean flip, Rotation rotation) { drawSprite(sprite, startX + offset, startY + offset, width - (offset * 2), height * (offset * 2), flip, rotation); }

    
	public void drawTexture(GameTexture texture) { drawTexture(texture, startX, startY, width, height, false, Rotation.UP); }
	public void drawTexture(GameTexture texture, Rotation rotation) { drawTexture(texture, startX, startY, width, height, false, rotation); }
	public void drawTexture(GameTexture texture, boolean flip) { drawTexture(texture, startX, startY, width, height, flip, Rotation.UP); }
	public void drawTexture(GameTexture texture, Rotation rotation, boolean flip) { drawTexture(texture, startX, startY, width, height, flip, rotation); }
	public void drawTexture(GameTexture texture, double offset) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), false, Rotation.UP); }
	public void drawTexture(GameTexture texture, double offset, Rotation rotation) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), false, rotation); }
	public void drawTexture(GameTexture texture, double offset, boolean flip) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), flip); }
	public void drawTexture(GameTexture texture, double offset, Rotation rotation, boolean flip) { drawTexture(texture, startX + offset, startY + offset, width - (offset * 2), height - (offset * 2), flip, rotation); }
	
	public double drawString(Object obj) { return drawString(obj, midX, midY, 0xffffffff); }
	public double drawString(String str) { return drawString(str, midX, midY, 0xffffffff); }
	public double drawString(Object obj, EColors color) { return drawString(obj, midX, midY, color); }
	public double drawString(String str, EColors color) { return drawString(str, midX, midY, color); }
	public double drawString(Object obj, int color) { return drawString(obj, midX, midY, color); }
	public double drawString(String str, int color) { return drawString(str, midX, midY, color); }
	public double drawStringC(Object obj) { return drawStringC(obj, midX, midY - FontRenderer.FONT_HEIGHT / 2, 0xffffffff); }
	public double drawStringC(String str) { return drawStringC(str, midX, midY - FontRenderer.FONT_HEIGHT / 2, 0xffffffff); }
	public double drawStringC(Object obj, EColors color) { return drawStringC(obj, midX, midY - FontRenderer.FONT_HEIGHT / 2, color); }
	public double drawStringC(String str, EColors color) { return drawStringC(str, midX, midY - FontRenderer.FONT_HEIGHT / 2, color); }
	public double drawStringC(Object obj, int color) { return drawStringC(obj, midX, midY - FontRenderer.FONT_HEIGHT / 2, color); }
	public double drawStringC(String str, int color) { return drawStringC(str, midX, midY - FontRenderer.FONT_HEIGHT / 2, color); }
	
	public void scissor() { scissor(startX, startY, endX, endY); }
	public void scissor(double offset) { scissor(startX + offset, startY + offset, endX - offset, endY - offset); }
	
	public static double strWidth(Object text) { return FontRenderer.strWidth(String.valueOf(text)); }
	
	//---------
	// Methods
	//---------
	
	/** Centers the gui in the middle of the screen with the specified dimensions. */
	public void centerGuiWithSize(double widthIn, double heightIn) {
		double sWidth = Envision.getWidth();
		double sHeight = Envision.getHeight();
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
	
	/** Returns the ScreenLocation area the mouse is currently on for an object. */
	public ScreenLocation getEdgeAreaMouseIsOnGui(int mX, int mY) {
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
	
	public boolean isMouseInsideGui(int mX, int mY) {
		return mX >= startX && mX <= endX && mY >= startY && mY <= endY;
	}
	
	public boolean isMouseInsideGui(int mX, int mY, double threshold) {
	    double sx = startX - threshold;
	    double sy = startY - threshold;
	    double ex = endX + threshold;
	    double ey = endY + threshold;
	    return mX >= sx && mX <= ex && mY >= sy && mY <= ey;
	}
	
	//=========
	// Getters
	//=========
	
	public Point2d getGuiPosition() { return new Point2d(startX, startY); }
	public Point2d getGuiInitialPosition() { return new Point2d(startXPos, startYPos); }
	public Dimension_d getDimensions() { return new Dimension_d(startX, startY, endX, endY); }
	public Dimension_d getUnboundedDimensions() { return new Dimension_d(startX, startY, startX + unboundedWidth, startY + unboundedHeight); }
	
	public Point2d getMinDims() { return new Point2d(minWidth, minHeight); }
	public Point2d getMaxDims() { return new Point2d(maxWidth, maxHeight); }
	public double getMinWidth() { return minWidth; }
	public double getMinHeight() { return minHeight; }
	public double getMaxWidth() { return maxWidth; }
	public double getMaxHeight() { return maxHeight; }
	
	//---------
	// Setters
	//---------
	
	public void setGuiPosition(double newX, double newY) { setDimensions(newX, newY, width, height); }
	public void setGuiInitialPosition(double xIn, double yIn) { startXPos = xIn; startYPos = yIn; }
	public void setGuiSize(double widthIn, double heightIn) { setDimensions(startX, startY, widthIn, heightIn); }
	
	public void setDimensions(Dimension_d dimIn) { setDimensions(dimIn.startX, dimIn.startY, dimIn.width, dimIn.height); }
	public void setDimensions(double startXIn, double startYIn, double widthIn, double heightIn) {
		startX = startXIn;
		startY = startYIn;
		unboundedWidth = widthIn;
		unboundedHeight = heightIn;
		width = ENumUtil.clamp(widthIn, minWidth, maxWidth);
		height = ENumUtil.clamp(heightIn, minHeight, maxHeight);
		endX = startX + width;
		endY = startY + height;
		midX = startX + width / 2.0;
		midY = startY + height / 2.0;
	}
	
	public void setMinDims(double widthIn, double heightIn) { minWidth = widthIn; minHeight = heightIn; }
	public void setMaxDims(double widthIn, double heightIn) { maxWidth = widthIn; maxHeight = heightIn; }
	public void setMinWidth(double widthIn) { minWidth = widthIn; }
	public void setMinHeight(double heightIn) { minHeight = heightIn; }
	public void setMaxWidth(double widthIn) { maxWidth = widthIn; }
	public void setMaxHeight(double heightIn) { maxHeight = heightIn; }
	
}
