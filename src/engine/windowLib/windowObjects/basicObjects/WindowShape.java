package engine.windowLib.windowObjects.basicObjects;

import engine.windowLib.windowTypes.WindowObject;
import eutil.colors.EColors;

public abstract class WindowShape<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	protected boolean filled = false;
	protected int color = 0xffffffff;
	
	//---------
	// Getters
	//---------
	
	public boolean isFilled() { return filled; }
	public int getColor() { return color; }
	
	//---------
	// Setters
	//---------
	
	public WindowShape<E> setFilled(boolean val) { filled = val; return this; }
	public WindowShape<E> setColor(EColors colorIn) { return setColor(colorIn.intVal); }
	public WindowShape<E> setColor(int colorIn) { color = colorIn; return this; }

}
