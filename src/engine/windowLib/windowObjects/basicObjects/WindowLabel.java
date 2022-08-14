package engine.windowLib.windowObjects.basicObjects;

import engine.renderEngine.fontRenderer.EStringBuilder;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.windowLib.windowTypes.WindowObject;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EList;

//Author: Hunter Bragg

public class WindowLabel<E> extends WindowObject<E> {
	
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
	
	public WindowLabel(IWindowObject<?> parentIn, double xPos, double yPos) { this(parentIn, xPos, yPos, "", 0xffffffff); }
	public WindowLabel(IWindowObject<?> parentIn, double xPos, double yPos, String stringIn) { this(parentIn, xPos, yPos, stringIn, 0xffffffff); }
	public WindowLabel(IWindowObject<?> parentIn, double xPos, double yPos, String stringIn, EColors colorIn) { this(parentIn, xPos, yPos, stringIn, colorIn.c()); }
	public WindowLabel(IWindowObject<?> parentIn, double xPos, double yPos, String stringIn, int colorIn) {
		init(parentIn, xPos, yPos, getStringWidth(stringIn), FontRenderer.FONT_HEIGHT);
		displayString = stringIn;
		displayStringColor = colorIn;
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mX, int mY) {
		if (wordWrap && wordWrappedLines != null) {
			int i = 0;
			for (String s : wordWrappedLines) {
				if (centered) {
					//if (shadow) { drawCenteredStringWithShadow(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					//else { drawCenteredString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
				}
				else {
					//if (shadow) { drawStringWithShadow(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					//else { drawString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0), displayStringColor); }
					drawString(s, startX, startY + (i * 9) + (i > 0 ? i * gapSize : 0));
				}
				i++;
			}
		}
		else {
			if (centered) {
				drawStringC(displayString, startX, startY, displayStringColor);
				//if (shadow) { drawCenteredStringWithShadow(displayString, startX, startY, displayStringColor); }
				//else { drawCenteredString(displayString, startX, startY, displayStringColor); }
			}
			else {
				//if (shadow) { drawStringWithShadow(displayString, startX, startY, displayStringColor); }
				//else { drawString(displayString, startX, startY, displayStringColor); }
				drawString(displayString, startX, startY, displayStringColor);
				
			}
		}
		super.drawObject(mX, mY);
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
			wordWrappedLines = EStringBuilder.createWordWrapString(displayString, widthMax);
			
			int longest = 0;
			for (String s : wordWrappedLines) {
				int len = getStringWidth(s);
				if (len > longest) longest = len;
			}
			
			double w = longest;
			double h = getTextHeight();
			
			setSize(w, h);
		}
		else {
			setSize(getStringWidth(displayString), FontRenderer.FONT_HEIGHT);
		}
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static void enableShadow(boolean val, WindowLabel<?> label, WindowLabel<?>... additional) {
		EUtil.filterNullForEachA(l -> l.enableShadow(val), EUtil.add(label, additional));
	}
	
	public static void setColor(EColors colorIn, WindowLabel<?> label, WindowLabel<?>... additional) { setColor(colorIn.intVal, label, additional); }
	public static void setColor(int colorIn, WindowLabel<?> label, WindowLabel<?>... additional) {
		EUtil.filterNullForEachA(l -> l.setColor(colorIn), EUtil.add(label, additional));
	}
	
}
