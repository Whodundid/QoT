package engine.windowLib.windowObjects.basicObjects;

import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowTypes.WindowObject;
import eutil.colors.EColors;
import eutil.math.NumberUtil;

public class WindowStatusBar extends WindowObject {
	
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
	
	public WindowStatusBar(WindowObject parent, double xIn, double yIn, double width, double height, double min, double max) {
		this(parent, xIn, yIn, width, height, min, max, 0xffffffff);
	}
	public WindowStatusBar(WindowObject parent, double xIn, double yIn, double width, double height, double min, double max, EColors color) {
		this(parent, xIn, yIn, width, height, min, max, color.intVal);
	}
	public WindowStatusBar(WindowObject parent, double xIn, double yIn, double width, double height, double minIn, double maxIn, int colorIn) {
		init(parent, xIn, yIn, width, height);
		min = minIn;
		max = maxIn;
		color = colorIn;
		current = max;
		updateDrawing();
	}
	
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
	
	private void updateDrawing() {
		x = startX + 5;
		y = startY + 5;
		w = width - 10;
		percent = current / max;
		pw = w * percent;
		ex = x + pw;
		ey = endY - 5;
	}
	
	public void decrementVal(double amount) { setBarValue(current - amount); }
	public void incrementVal(double amount) { setBarValue(current + amount); }
	
	public WindowStatusBar setBarValue(double val) {
		current = NumberUtil.clamp(val, min, max);
		updateDrawing();
		return this;
	}
	
	public double getCurrentVal() { return current; }
	
}
