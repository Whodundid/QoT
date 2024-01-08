package envision.engine.windows.windowObjects.basicObjects;

import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
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
	
	public WindowStatusBar(IWindowObject parent, double xIn, double yIn, double width, double height, double min, double max) {
		this(parent, xIn, yIn, width, height, min, max, 0xffffffff);
	}
	
	public WindowStatusBar(IWindowObject parent, double xIn, double yIn, double width, double height, double min, double max, EColors color) {
		this(parent, xIn, yIn, width, height, min, max, color.intVal);
	}
	
	public WindowStatusBar(IWindowObject parent, double xIn, double yIn, double width, double height, double minIn, double maxIn, int colorIn) {
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
	public void drawObject(float dt, int mXIn, int mYIn) {
		//drawRect(EColors.black);
		drawRect(EColors.dsteel);
		
		drawRect(x, y, ex, ey, color);
		if (isMouseInsideGui(mXIn, mYIn)) {
			drawStringC((int) current + "/" + (int) max, midX, midY - FontRenderer.FONT_HEIGHT / 2 + 2.5);
		}
	}

	//------------------
	// Internal Methods
	//------------------
	
	private void updateDrawing() {
		x = startX;
		y = startY;
		w = width;
		percent = current / max;
		pw = w * percent;
		ex = x + pw;
		ey = endY;
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
	
	public void setColor(EColors colorIn) { color = colorIn.intVal; }
	public void setColor(int colorIn) { color = colorIn; }
	
}
