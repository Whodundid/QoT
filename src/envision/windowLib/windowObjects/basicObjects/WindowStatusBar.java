package envision.windowLib.windowObjects.basicObjects;

import envision.renderEngine.fontRenderer.FontRenderer;
import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;
import eutil.math.ENumUtil;

public class WindowStatusBar extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	private double min;
	private double max;
	private double current;
	private int color;
	
	// draw stuff
	double x;
	double y;
	double w;
	double percent;
	double pw;
	double ex;
	double ey;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowStatusBar(IWindowObject<?> parent, double xIn, double yIn, double width, double height, double min, double max) {
		this(parent, xIn, yIn, width, height, min, max, 0xffffffff);
	}
	
	public WindowStatusBar(IWindowObject<?> parent, double xIn, double yIn, double width, double height, double min, double max, EColors color) {
		this(parent, xIn, yIn, width, height, min, max, color.intVal);
	}
	
	public WindowStatusBar(IWindowObject<?> parent, double xIn, double yIn, double width, double height, double minIn, double maxIn, int colorIn) {
		init(parent, xIn, yIn, width, height);
		min = minIn;
		max = maxIn;
		color = colorIn;
		current = max;
		updateDrawing();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.dsteel, 5);
		
		drawRect(x, y, ex, ey, color);
		if (isMouseInside(mXIn, mYIn)) {
			drawStringC((int) current + "/" + (int) max, midX, midY - FontRenderer.FONT_HEIGHT / 2 + 2.5);
		}
		
		super.drawObject(mXIn, mYIn);
	}

	//------------------
	// Internal Methods
	//------------------
	
	private void updateDrawing() {
		x = startX + 5;
		y = startY + 5;
		w = width - 10;
		percent = current / max;
		pw = w * percent;
		ex = x + pw;
		ey = endY - 5;
	}
	
	//---------
	// Methods
	//---------
	
	public void decrementVal(double amount) { setBarValue(current - amount); }
	public void incrementVal(double amount) { setBarValue(current + amount); }
	
	//---------
	// Getters
	//---------
	
	public double getCurrentVal() { return current; }
	
	//---------
	// Setters
	//---------
	
	public void setBarValue(double val) {
		current = ENumUtil.clamp(val, min, max);
		updateDrawing();
	}
	
}
