package eWindow.windowObjects.basicObjects;

import eWindow.windowTypes.interfaces.IWindowObject;
import util.renderUtil.EColors;

//Author: Hunter Bragg

public class WindowRect extends WindowShape {
	
	public WindowRect(IWindowObject parentIn, double startX, double startY, double endX, double endY) { this(parentIn, startX, startY, endX, endY, true, EColors.black); }
	public WindowRect(IWindowObject parentIn, double startX, double startY, double endX, double endY, EColors colorIn) { this(parentIn, startX, startY, endX, endY, true, colorIn.intVal); }
	public WindowRect(IWindowObject parentIn, double startX, double startY, double endX, double endY, int colorIn) { this(parentIn, startX, startY, endX, endY, true, colorIn); }
	public WindowRect(IWindowObject parentIn, double startX, double startY, double endX, double endY, boolean filledIn) { this(parentIn, startX, startY, endX, endY, filledIn, EColors.black.intVal); }
	public WindowRect(IWindowObject parentIn, double startX, double startY, double endX, double endY, boolean filledIn, EColors colorIn) { this(parentIn, startX, startY, endX, endY, filledIn, EColors.black.intVal); }
	public WindowRect(IWindowObject parentIn, double startX, double startY, double endX, double endY, boolean filledIn, int colorIn) {
		init(parentIn, startX, startY, Math.abs(endX - startX), Math.abs(endY - startY));
		filled = filledIn;
		color = colorIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		if (filled) { drawRect(startX, startY, endX, endY, color); }
		else { drawHRect(startX, startY, endX, endY, 1, color); }
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public WindowRect setDimensions(double newWidth, double newHeight) {
		width = Math.abs(newWidth);
		height = Math.abs(newHeight);
		if (newWidth < 0) { startX += newWidth; }
		if (newHeight < 0) { startY += newHeight; }
		endX = startX + width;
		endY = startY + height;
		midX = startX + (width / 2);
		midY = startY + (height / 2);
		return this;
	}
	
	//----------------
	//EGuiRect Getters
	//----------------
	
	public int getColor() { return color; }

	//----------------
	//EGuiRect Setters
	//----------------
	
	public WindowRect setColor(EColors colorIn) { return setColor(colorIn.c()); }
	public WindowRect setColor(int colorIn) { color = colorIn; return this; }
	
}
