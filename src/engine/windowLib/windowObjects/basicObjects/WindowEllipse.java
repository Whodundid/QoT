package engine.windowLib.windowObjects.basicObjects;

import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.Box2;

public class WindowEllipse<E> extends WindowShape<E> {
	
	double cX, cY;
	
	public WindowEllipse(IWindowObject parentIn, double x, double y, double radiusX, double radiusY) { this(parentIn, x, y, radiusX, radiusY, true, 0xffffffff); }
	public WindowEllipse(IWindowObject parentIn, double x, double y, double radiusX, double radiusY, EColors colorIn) { this(parentIn, x, y, radiusX, radiusY, true, colorIn.intVal); }
	public WindowEllipse(IWindowObject parentIn, double x, double y, double radiusX, double radiusY, int colorIn) { this(parentIn, x, y, radiusX, radiusY, true, colorIn); }
	public WindowEllipse(IWindowObject parentIn, double x, double y, double radiusX, double radiusY, boolean filledIn) { this(parentIn, x, y, radiusX, radiusY, filledIn, 0xffffffff); }
	public WindowEllipse(IWindowObject parentIn, double x, double y, double radiusX, double radiusY, boolean filledIn, EColors colorIn) { this(parentIn, x, y, radiusX, radiusY, filledIn, colorIn.intVal); }
	public WindowEllipse(IWindowObject parentIn, double x, double y, double radiusX, double radiusY, boolean filledIn, int colorIn) {
		init(parentIn, x, y, radiusX * 2, radiusY * 2);
		cX = x;
		cY = y;
		filled = filledIn;
		color = colorIn;
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		
		if (filled) { drawFilledEllipse(startX + width / 2, startY + height / 2, width, height, 50, color); }
		else { drawEllipse(startX + width / 2, startY + height / 2, width, height, 50, color); }
		
		super.drawObject(mXIn, mYIn);
	}
	
	//-------------------
	//EGuiEllipse Getters
	//-------------------
	
	public Box2<Integer, Integer> getCenter() { return new Box2(cX, cY); }
	
	//-------------------
	//EGuiEllipse Setters
	//-------------------
	
	public WindowEllipse<E> setCenter(Box2<Integer, Integer> in) { return setCenter(in.getA(), in.getB()); }
	public WindowEllipse<E> setCenter(int x, int y) { cX = x; cY = y; return this; }

}