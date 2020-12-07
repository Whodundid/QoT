package envisionEngine.eWindow.windowObjects.basicObjects;

import envisionEngine.eWindow.windowTypes.WindowObject;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import util.renderUtil.EColors;

public class WindowLine extends WindowObject {
	
	double x1, y1, x2, y2;
	int color;
	
	public WindowLine(IWindowObject parentIn, double x1, double y1, double x2, double y2) { this(parentIn, x1, y1, x2, y2, 0xff000000); }
	public WindowLine(IWindowObject parentIn, double x1, double y1, double x2, double y2, EColors colorIn) { this(parentIn, x1, y1, x2, y2, colorIn.intVal); }
	public WindowLine(IWindowObject parentIn, double x1, double y1, double x2, double y2, int colorIn) {
		
		if (x1 > x2) {
			double temp = x1;
			x1 = x2;
			x2 = temp;
		}
		if (y1 > y2) {
			double temp = y1;
			y1 = y2;
			y2 = temp;
		}
		
		
		double w = x2 - x1;
		double h = y2 - y1;
		
		color = colorIn;
		
		init(parentIn, x1, y1, w, h);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawLine(startX, startY, endX, endY, color);
		
		super.drawObject(mXIn, mYIn);
	}
	
}
