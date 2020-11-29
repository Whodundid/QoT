package eWindow.windowObjects.utilityObjects;

import eWindow.windowObjects.actionObjects.WindowButton;
import eWindow.windowTypes.interfaces.IWindowObject;
import util.renderUtil.EColors;

public class ColorButton<E> extends WindowButton<E> {
	
	private int color = 0xffffffff;
	private boolean drawBorder = true;
	
	//------------------------
	//ColorButton Constructors
	//------------------------
	
	public ColorButton(IWindowObject parent, double xIn, double yIn, double widthIn, double heightIn) { this(parent, xIn, yIn, widthIn, heightIn, 0xffffffff); }
	public ColorButton(IWindowObject parent, double xIn, double yIn, double widthIn, double heightIn, int colorIn) {
		super(parent, xIn, yIn, widthIn, heightIn);
		color = colorIn;
		setDrawTextures(false);
	}
	
	//----------------------
	//WindowButton Overrides
	//----------------------
	
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
	
	//-------------------
	//ColorButton Getters
	//-------------------
	
	public int getColor() { return color; }
	
	//-------------------
	//ColorButton Setters
	//-------------------
	
	public ColorButton setColor(EColors colorIn) { if (colorIn != null) { color = colorIn.intVal; } return this; }
	public ColorButton setColor(int colorIn) { color = colorIn; return this; }
	public ColorButton setDrawBorder(boolean val) { drawBorder = val; return this; }
	
}