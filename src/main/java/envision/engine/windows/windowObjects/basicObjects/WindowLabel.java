package envision.engine.windows.windowObjects.basicObjects;

import envision.engine.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;

//Author: Hunter Bragg

public class WindowLabel extends WindowObject {
	
	//--------
	// Fields
	//--------
	
	public String displayString = "";
	public int displayStringColor = 0xffffffff;
	protected EList<String> wordWrappedLines;
	protected double widthMax = 0;
	protected boolean wordWrap = false;
	protected boolean centered = false;
	protected boolean shadow = true;
	protected double gapSize = 0;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos) { this(parentIn, xPos, yPos, "", 0xffffffff); }
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos, String stringIn) { this(parentIn, xPos, yPos, stringIn, 0xffffffff); }
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos, String stringIn, EColors colorIn) { this(parentIn, xPos, yPos, stringIn, colorIn.c()); }
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos, String stringIn, int colorIn) {
		init(parentIn, xPos, yPos, FontRenderer.strWidth(stringIn), FontRenderer.FONT_HEIGHT);
		displayString = stringIn;
		displayStringColor = colorIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(long dt, int mX, int mY) {
		if (wordWrap && wordWrappedLines != null) {
			int i = 0;
			
			double textHeight = getTextHeight();
			double yPos = startY - textHeight * 0.5;
			
			for (String s : wordWrappedLines) {
			    
			    double drawX = startX;
			    double drawY = yPos + (i * FontRenderer.FONT_HEIGHT) + (i > 0 ? i * gapSize : 0);
			    
				if (centered) {
					if (shadow) drawStringCS(s, drawX, drawY, displayStringColor);
					else drawStringC(s, drawX, drawY, displayStringColor);
				}
				else {
					if (shadow) drawStringS(s, drawX, drawY, displayStringColor);
					else drawString(s, drawX, drawY, displayStringColor);
				}
				i++;
			}
		}
		else if (centered) {
            if (shadow) drawStringCS(displayString, startX, startY, displayStringColor);
            else drawStringC(displayString, startX, startY, displayStringColor);
        }
        else if (shadow) drawStringS(displayString, startX, startY, displayStringColor);
        else drawString(displayString, startX, startY, displayStringColor);
	}
	
	//---------
	// Methods
	//---------
	
	public boolean isEmpty() { return (displayString != null) ? displayString.isEmpty() : true; }
	public void clear() { if (displayString != null) displayString = ""; }
	
	public void enableWordWrap(boolean val, double widthMaxIn) {
		widthMax = widthMaxIn;
		wordWrap = val;
		setString(displayString);
	}
	
	//---------
	// Getters
	//---------

	public String getString() { return displayString; }
	public int getColor() { return displayStringColor; }
	
	public double getTextHeight() {
		var size = wordWrappedLines.size();
		return wordWrap ? (size * FontRenderer.FONT_HEIGHT + size * gapSize) : FontRenderer.FONT_HEIGHT;
	}
	
	//---------
	// Setters
	//---------
	
	public void setLineGapHeight(int heightIn) { gapSize = heightIn; }
	public void enableShadow(boolean val) { shadow = val; }
	public void setDrawCentered(boolean val) { centered = val; }
	
	public void setColor(int colorIn) { displayStringColor = colorIn; }
	public void setColor(EColors colorIn) {
		if (colorIn != null) displayStringColor = colorIn.intVal;
	}
	
	public void setString(String stringIn) {
		displayString = stringIn;
		if (wordWrap) {
			wordWrappedLines = EStringOutputFormatter.createWordWrapString(displayString, widthMax);
			
			double longest = 0;
			for (String s : wordWrappedLines) {
				double len = FontRenderer.strWidth(s);
				if (len > longest) longest = len;
			}
			
			double w = longest;
			double h = getTextHeight();
			
			setGuiSize(w, h);
		}
		else {
			setGuiSize(FontRenderer.strWidth(displayString), FontRenderer.FONT_HEIGHT);
		}
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static void enableShadow(boolean val, WindowLabel label, WindowLabel... additional) {
		EUtil.filterNullForEachA(l -> l.enableShadow(val), EUtil.add(label, additional));
	}
	
	public static void setColor(EColors colorIn, WindowLabel label, WindowLabel... additional) { setColor(colorIn.intVal, label, additional); }
	public static void setColor(int colorIn, WindowLabel label, WindowLabel... additional) {
		EUtil.filterNullForEachA(l -> l.setColor(colorIn), EUtil.add(label, additional));
	}
	
}
