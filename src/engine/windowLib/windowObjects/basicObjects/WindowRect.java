package engine.windowLib.windowObjects.basicObjects;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.math.EDimension;

//Author: Hunter Bragg

public class WindowRect<E> extends WindowShape<E> {
	
	//--------------
	// Constructors
	//--------------
	
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
	
	public WindowRect(IWindowObject parentIn, EDimension dims) { this(parentIn, dims, true, EColors.black.intVal); }
	public WindowRect(IWindowObject parentIn, EDimension dims, boolean filledIn) { this(parentIn, dims, filledIn, EColors.black.intVal); }
	public WindowRect(IWindowObject parentIn, EDimension dims, boolean filledIn, EColors colorIn) { this(parentIn, dims, filledIn, colorIn.intVal); }
	public WindowRect(IWindowObject parentIn, EDimension dims, boolean filledIn, int colorIn) {
		init(parentIn, dims.startX, dims.startY, dims.endX, dims.endY);
		filled = filledIn;
		color = colorIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (filled) drawRect(startX, startY, endX, endY, color);
		else drawHRect(startX, startY, endX, endY, 1, color);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void setSize(double newWidth, double newHeight) {
		width = Math.abs(newWidth);
		height = Math.abs(newHeight);
		if (newWidth < 0) { startX += newWidth; }
		if (newHeight < 0) { startY += newHeight; }
		endX = startX + width;
		endY = startY + height;
		midX = startX + (width / 2);
		midY = startY + (height / 2);
	}
	
	//---------
	// Getters
	//---------
	
	public int getColor() { return color; }

	//---------
	// Setters
	//---------
	
	public WindowRect<E> setColor(EColors colorIn) { return setColor(colorIn.c()); }
	public WindowRect<E> setColor(int colorIn) { color = colorIn; return this; }
	
}
