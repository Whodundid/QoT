package envision.engine.windows.windowObjects.basicObjects;

import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.datatypes.points.Point2d;

public class WindowEllipse<E> extends WindowShape<E> {
	
	//--------
	// Fields
	//--------
	
	private double cX, cY;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowEllipse(IWindowObject<?> parentIn, double x, double y, double radiusX, double radiusY) { this(parentIn, x, y, radiusX, radiusY, true, 0xffffffff); }
	public WindowEllipse(IWindowObject<?> parentIn, double x, double y, double radiusX, double radiusY, EColors colorIn) { this(parentIn, x, y, radiusX, radiusY, true, colorIn.intVal); }
	public WindowEllipse(IWindowObject<?> parentIn, double x, double y, double radiusX, double radiusY, int colorIn) { this(parentIn, x, y, radiusX, radiusY, true, colorIn); }
	public WindowEllipse(IWindowObject<?> parentIn, double x, double y, double radiusX, double radiusY, boolean filledIn) { this(parentIn, x, y, radiusX, radiusY, filledIn, 0xffffffff); }
	public WindowEllipse(IWindowObject<?> parentIn, double x, double y, double radiusX, double radiusY, boolean filledIn, EColors colorIn) { this(parentIn, x, y, radiusX, radiusY, filledIn, colorIn.intVal); }
	public WindowEllipse(IWindowObject<?> parentIn, double x, double y, double radiusX, double radiusY, boolean filledIn, int colorIn) {
		init(parentIn, x, y, radiusX * 2, radiusY * 2);
		cX = x;
		cY = y;
		filled = filledIn;
		color = colorIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (filled) drawFilledEllipse(startX + width / 2, startY + height / 2, width, height, 50, color);
		else drawEllipse(startX + width / 2, startY + height / 2, width, height, 50, color);
	}
	
	//---------
	// Getters
	//---------
	
	public Point2d getCenter() { return new Point2d(cX, cY); }
	
	//---------
	// Setters
	//---------
	
	public void setCenter(Point2d in) {
		setCenter(in.x, in.y);
	}
	
	public void setCenter(Number x, Number y) {
		cX = x.doubleValue();
		cY = y.doubleValue();
	}

}
