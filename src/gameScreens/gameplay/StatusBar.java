package gameScreens.gameplay;

import eutil.colors.EColors;
import eutil.math.NumberUtil;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IActionObject;

public class StatusBar extends WindowObject {
	
	private double min;
	private double max;
	private double current;
	private int color;
	
	public StatusBar(WindowObject parent, double xIn, double yIn, double width, double height, double min, double max) {
		this(parent, xIn, yIn, width, height, min, max, 0xffffffff);
	}
	public StatusBar(WindowObject parent, double xIn, double yIn, double width, double height, double min, double max, EColors color) {
		this(parent, xIn, yIn, width, height, min, max, color.intVal);
	}
	public StatusBar(WindowObject parent, double xIn, double yIn, double width, double height, double minIn, double maxIn, int colorIn) {
		init(parent, xIn, yIn, width, height);
		min = minIn;
		max = maxIn;
		color = colorIn;
		current = max;
	}
	
	@Override
	public void initObjects() {
		
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(EColors.black);
		drawRect(EColors.dsteel, 5);
		
		double x = startX + 5;
		double y = startY + 5;
		double w = width - 10;
		double percent = current / max;
		double pw = w * percent;
		double ex = x + pw;
		drawRect(x, y, ex, endY - 5, color);
		
		super.drawObject(mXIn, mYIn);
	}
	
	@Override
	public void actionPerformed(IActionObject object, Object... args) {
		
	}
	
	public void decrementVal(double amount) {
		current -= amount;
		current = NumberUtil.clamp(current, min, max);
	}
	
	public void incrementVal(double amount) {
		current += amount;
		current = NumberUtil.clamp(current, min, max);
	}
	
	public StatusBar setCurrentValue(double val) { current = NumberUtil.clamp(val, min, max); return this; }
	public double getCurrentVal() { return current; }
	
}
