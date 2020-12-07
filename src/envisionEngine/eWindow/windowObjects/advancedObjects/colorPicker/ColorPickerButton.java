package envisionEngine.eWindow.windowObjects.advancedObjects.colorPicker;

import envisionEngine.eWindow.windowObjects.actionObjects.WindowButton;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import util.renderUtil.EColors;

//Author: Hunter Bragg

public class ColorPickerButton<E> extends WindowButton<E> {

	public ColorPickerButton(IWindowObject parentIn, double posX, double posY, double width, double height) { this(parentIn, posX, posY, width, height, "White", EColors.white); }
	public ColorPickerButton(IWindowObject parentIn, double posX, double posY, double width, double height, EColors colorIn) { this(parentIn, posX, posY, width, height, colorIn.name, colorIn.intVal); }
	public ColorPickerButton(IWindowObject parentIn, double posX, double posY, double width, double height, String textIn, EColors colorIn) { this(parentIn, posX, posY, width, height, textIn, colorIn.c()); }
	public ColorPickerButton(IWindowObject parentIn, double posX, double posY, double width, double height, String textIn, int colorIn) {
		super(parentIn, posX, posY, width, height);
		setTextures(null, null);
		setDrawBackground(true);
		setParams(textIn, colorIn);
	}
	
	@Override
	public void onDoubleClick() {
		performAction("dc");
	}
	
	public ColorPickerButton setParams(String textIn, EColors colorIn) { return setParams(textIn, colorIn.c()); }
	public ColorPickerButton setParams(String textIn, int colorIn) { setColor(colorIn); setHover(textIn, EColors.seafoam); return this; }
	
	public ColorPickerButton setHover(String textIn, EColors colorIn) { return setHover(textIn, colorIn.c()); }
	public ColorPickerButton setHover(String textIn, int colorIn) { setHoverText(textIn).setHoverTextColor(colorIn); return this; }
	
	public ColorPickerButton setColor(EColors colorIn) { setBackgroundColor(colorIn); return this; }
	public ColorPickerButton setColor(int colorIn) { setBackgroundColor(colorIn); return this; }
	
	public int getColor() { return getBackgroundColor(); }
	
}
