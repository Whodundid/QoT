package windowLib.windowObjects.basicObjects;

import eutil.colors.EColors;
import windowLib.windowTypes.WindowObject;

public abstract class WindowShape<E> extends WindowObject<E> {
	
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
	
	public WindowShape<E> setFilled(boolean val) { filled = val; return this; }
	public WindowShape<E> setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public WindowShape<E> setColor(int colorIn) { color = colorIn; return this; }

}
