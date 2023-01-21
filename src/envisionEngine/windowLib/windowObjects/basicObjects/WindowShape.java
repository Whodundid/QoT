package envisionEngine.windowLib.windowObjects.basicObjects;

import envisionEngine.windowLib.windowTypes.WindowObject;
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
	
	public void setFilled(boolean val) { filled = val; }
	public void setColor(EColors colorIn) { setColor(colorIn.intVal); }
	public void setColor(int colorIn) { color = colorIn; }

}
