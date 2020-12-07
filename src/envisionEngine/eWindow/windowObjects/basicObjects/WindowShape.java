package envisionEngine.eWindow.windowObjects.basicObjects;

import envisionEngine.eWindow.windowTypes.WindowObject;
import util.renderUtil.EColors;

public abstract class WindowShape extends WindowObject {
	
	boolean filled = false;
	int color = 0xffffffff;
	
	//-----------------
	//EGuiShape Getters
	//-----------------
	
	public boolean isFilled() { return filled; }
	public int getColor() { return color; }
	
	//-----------------
	//EGuiShape Setters
	//-----------------
	
	public WindowShape setFilled(boolean val) { filled = val; return this; }
	public WindowShape setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public WindowShape setColor(int colorIn) { color = colorIn; return this; }

}
