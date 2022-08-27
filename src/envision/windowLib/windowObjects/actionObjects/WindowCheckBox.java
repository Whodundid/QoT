package envision.windowLib.windowObjects.actionObjects;

import envision.windowLib.windowTypes.ActionObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class WindowCheckBox<E> extends ActionObject<E> {
	
	//--------
	// Fields
	//--------
	
	private boolean checked = false;
	private boolean drawX = false;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowCheckBox(IWindowObject<?> objIn, double xIn, double yIn, double widthIn, double heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public WindowCheckBox(IWindowObject<?> objIn, double xIn, double yIn, double widthIn, double heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawRect(startX, startY, endX, endY, EColors.black);
		drawRect(startX + 1, startY + 1, endX - 1, endY - 1, EColors.steel);
		
		if (checked) {
			drawRect(EColors.green, 10);
			//drawTexture(startX - 2, startY - 5, width + 5, height + 6, EMCResources.guiCheck);
		}
		else if (drawX) {
			//drawTexture(startX - 2, startY - 2, width + 4, height + 4, EMCResources.guiX);
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
	// Methods
	//---------
	
	public boolean getDrawsX() { return drawX; }
	public boolean getIsChecked() { return checked; }
	
	//---------
	// Setters
	//---------
	
	public void setDrawX(boolean val) { drawX = val; }
	public void setIsChecked(boolean val) { checked = val; }
	
}
