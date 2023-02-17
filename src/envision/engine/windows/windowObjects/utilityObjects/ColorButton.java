package envision.engine.windows.windowObjects.utilityObjects;

import envision.engine.windows.windowObjects.actionObjects.WindowButton;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.colors.EColors;

public class ColorButton<E> extends WindowButton<E> {
	
	//--------
	// Fields
	//--------
	
	private int color = 0xffffffff;
	private boolean drawBorder = true;
	
	//--------------
	// Constructors
	//--------------
	
	public ColorButton(IWindowObject<?> parent, double xIn, double yIn, double widthIn, double heightIn) { this(parent, xIn, yIn, widthIn, heightIn, 0xffffffff); }
	public ColorButton(IWindowObject<?> parent, double xIn, double yIn, double widthIn, double heightIn, int colorIn) {
		super(parent, xIn, yIn, widthIn, heightIn);
		color = colorIn;
		setDrawTextures(false);
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		super.drawObject(mXIn, mYIn);
		
		if (drawBorder) {
			drawRect(EColors.black);
			drawRect(color, 1);
		}
		else {
			drawRect(color);
		}
	}
	
	//---------
	// Getters
	//---------
	
	public int getColor() { return color; }
	
	//---------
	// Setters
	//---------
	
	public void setColor(EColors colorIn) { if (colorIn != null) { color = colorIn.intVal; } }
	public void setColor(int colorIn) { color = colorIn; }
	public void setDrawBorder(boolean val) { drawBorder = val; }
	
}