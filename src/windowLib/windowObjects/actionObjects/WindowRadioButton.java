package windowLib.windowObjects.actionObjects;

import renderUtil.EColors;
import windowLib.windowTypes.ActionObject;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowRadioButton<E> extends ActionObject<E> {

	boolean checked = false;
	int color = EColors.green.intVal;
	
	public WindowRadioButton(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public WindowRadioButton(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}

	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawFilledEllipse(midX, midY, width / 2, height / 2, 30, EColors.black);
		drawFilledEllipse(midX, midY, width / 2 - 1, height / 2 - 1, 30, EColors.steel);
		
		if (checked) {
			drawFilledEllipse(midX, midY, width / 2 - 3, height / 2 - 3, 30, EColors.green);
		}
	}
	
	@Override
	public void mousePressed(int mXIn, int mYIn, int button) {
		if (button == 0) {
			WindowButton.playPressSound();
			checked = !checked;
			performAction();
		}
	}
	
	public boolean isChecked() { return checked; }
	public int getColor() { return color; }
	
	public WindowRadioButton setChecked(boolean val) { checked = val; return this; }
	public WindowRadioButton setColor(EColors colorIn) { color = colorIn.c(); return this; }
	public WindowRadioButton setColor(int colorIn) { color = colorIn; return this; }
	
}
