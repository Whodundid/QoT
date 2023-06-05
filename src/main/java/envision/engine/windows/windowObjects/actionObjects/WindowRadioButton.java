package envision.engine.windows.windowObjects.actionObjects;

import envision.engine.windows.windowTypes.ActionObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class WindowRadioButton<E> extends ActionObject<E> {

	//--------
	// Fields
	//--------
	
	private boolean checked = false;
	private int color = EColors.green.intVal;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowRadioButton(IWindowObject<?> objIn, double xIn, double yIn, double widthIn, double heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public WindowRadioButton(IWindowObject<?> objIn, double xIn, double yIn, double widthIn, double heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}

	//-----------
	// Overrides
	//-----------
	
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
	
	//---------
	// Getters
	//---------
	
	public boolean getIsChecked() { return checked; }
	public int getColor() { return color; }
	
	//---------
	// Setters
	//---------
	
	public void setIsChecked(boolean val) { checked = val; }
	public void setColor(EColors colorIn) { color = colorIn.intVal;  }
	public void setColor(int colorIn) { color = colorIn; }
	
}
