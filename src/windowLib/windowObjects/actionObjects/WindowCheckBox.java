package windowLib.windowObjects.actionObjects;

import eutil.colors.EColors;
import windowLib.windowTypes.ActionObject;
import windowLib.windowTypes.interfaces.IWindowObject;


//Author: Hunter Bragg

public class WindowCheckBox<E> extends ActionObject<E> {
	
	boolean checked = false;
	boolean drawX = false;
	
	public WindowCheckBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn) { this(objIn, xIn, yIn, widthIn, heightIn, false); }
	public WindowCheckBox(IWindowObject objIn, double xIn, double yIn, double widthIn, double heightIn, boolean checkedIn) {
		init(objIn, xIn, yIn, widthIn, heightIn);
		checked = checkedIn;
	}
	
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
	
	public boolean drawsX() { return drawX; }
	public boolean isChecked() { return checked; }
	
	public WindowCheckBox setDrawX(boolean val) { drawX = val; return this; }
	public WindowCheckBox setChecked(boolean val) { checked = val; return this; }
	
}
