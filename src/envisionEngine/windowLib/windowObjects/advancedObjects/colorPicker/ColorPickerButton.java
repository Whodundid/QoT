package envisionEngine.windowLib.windowObjects.advancedObjects.colorPicker;

import envisionEngine.windowLib.windowObjects.actionObjects.WindowButton;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;

//Author: Hunter Bragg

public class ColorPickerButton<E> extends WindowButton<E> {

	//--------------
	// Constructors
	//--------------
	
	public ColorPickerButton(IWindowObject<?> parentIn, double posX, double posY, double width, double height) { this(parentIn, posX, posY, width, height, "White", EColors.white); }
	public ColorPickerButton(IWindowObject<?> parentIn, double posX, double posY, double width, double height, EColors colorIn) { this(parentIn, posX, posY, width, height, colorIn.name, colorIn.intVal); }
	public ColorPickerButton(IWindowObject<?> parentIn, double posX, double posY, double width, double height, String textIn, EColors colorIn) { this(parentIn, posX, posY, width, height, textIn, colorIn.c()); }
	public ColorPickerButton(IWindowObject<?> parentIn, double posX, double posY, double width, double height, String textIn, int colorIn) {
		super(parentIn, posX, posY, width, height);
		setTextures(null, null);
		setDrawBackground(true);
		setParams(textIn, colorIn);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void onDoubleClick() {
		performAction("dc");
	}
	
	//---------
	// Getters
	//---------
	
	public int getColor() { return getBackgroundColor(); }
	
	//---------
	// Setters
	//---------
	
	public void setParams(String textIn, EColors colorIn) { setParams(textIn, colorIn.intVal); }
	public void setParams(String textIn, int colorIn) {
		setColor(colorIn);
		setHover(textIn, EColors.seafoam);
	}
	
	public void setHover(String textIn, EColors colorIn) { setHover(textIn, colorIn.intVal); }
	public void setHover(String textIn, int colorIn) {
		setHoverText(textIn);
		setHoverTextColor(colorIn);
	}
	
	public void setColor(EColors colorIn) { setBackgroundColor(colorIn.intVal); }
	public void setColor(int colorIn) { setBackgroundColor(colorIn); }
	
}
