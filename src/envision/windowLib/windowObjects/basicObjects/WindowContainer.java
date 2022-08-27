package envision.windowLib.windowObjects.basicObjects;

import envision.windowLib.windowTypes.WindowObject;
import envision.windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowContainer<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	protected String title = "";
	protected int titleColor = 0xffffffff; //0xff47ff96;
	protected int titleBorderColor = 0xff000000;
	protected int titleBackgroundColor = 0xff222222;
	protected int borderColor = 0xff000000;
	protected int backgroundColor = 0xff4c4c4c;
	protected boolean drawTitle = true;
	protected boolean drawBackground = true;
	protected boolean drawBorder = true;
	protected boolean useCustomWidth = false;
	protected boolean drawTitleFullWidth = true;
	protected boolean center = false;
	protected double titleWidth = 0;
	protected double titleAreaWidth = 0;
	protected double titleAreaHeight = 0;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowContainer(IWindowObject<?> parentIn, double xIn, double yIn, double widthIn, double heightIn) {
		this(parentIn, xIn, yIn, widthIn, heightIn, true);
	}
	
	public WindowContainer(IWindowObject<?> parentIn, double xIn, double yIn, double widthIn, double heightIn, boolean drawTitleIn) {
		init(parentIn, xIn, yIn, widthIn, heightIn);
		drawTitle = drawTitleIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		if (drawBorder) drawRect(startX, startY, endX, endY, borderColor); //border
		if (drawBackground) drawRect(startX + 1, startY + 1, endX - 1, endY - 1, backgroundColor); //inner
		if (drawTitle) {
			titleAreaHeight = height >= 28 ? 28 : height;
			double drawWidth = titleWidth + 6;
			if (useCustomWidth) drawWidth = titleAreaWidth;
			if (drawTitleFullWidth) drawWidth = width - 1;
			drawRect(startX + 1, startY + 1, startX + drawWidth + 1, startY + titleAreaHeight, titleBorderColor);
			drawRect(startX + 1, startY + 1, startX + drawWidth, startY + titleAreaHeight - 1, titleBackgroundColor);
			if (center) drawStringCS(title, midX, startY + 5, titleColor);
			else drawStringS(title, startX + 4, startY + 5, titleColor);
			
		}
		
		scissor(startX + 1, startY + titleAreaHeight + 1, endX - 1, endY - 1);
		super.drawObject(mXIn, mYIn);
		endScissor();
	}
	
	//---------
	// Getters
	//---------
	
	public double getTitleWidth() { return titleWidth; }
	public double getTitleAreaWidth() { return titleAreaWidth; }
	public double getTitleAreaHeight() { return titleAreaHeight; }
	
	//---------
	// Setters
	//---------
	
	public void setTitle(String stringIn) { title = stringIn; titleWidth = getStringWidth(stringIn); }
	public void setTitleColor(int colorIn) { titleColor = colorIn; }
	public void setBorderColor(int colorIn) { borderColor = colorIn; }
	public void setBackgroundColor(int colorIn) { backgroundColor = colorIn; }
	public void setTitleBorderColor(int colorIn) { titleBorderColor = colorIn; }
	public void setTitleBackgroundColor(int colorIn) { titleBackgroundColor = colorIn; }
	public void setDrawTitle(boolean val) { drawTitle = val; }
	public void setDrawBackground(boolean val) { drawBackground = val; }
	public void setDrawBorder(boolean val) { drawBorder = val; }
	public void setTitleWidth(int widthIn) { if (widthIn > 0) { useCustomWidth = true; titleAreaWidth = widthIn; } }
	public void setTitleFullWidth(boolean val) { drawTitleFullWidth = val; }
	public void setTitleCentered(boolean val) { center = val; }
	
}
