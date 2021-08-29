package windowLib.windowObjects.basicObjects;

import eutil.EUtil;
import eutil.colors.EColors;
import eutil.storage.EArrayList;
import renderEngine.fontRenderer.EStringBuilder;
import renderEngine.fontRenderer.FontRenderer;
import windowLib.windowTypes.WindowObject;
import windowLib.windowTypes.interfaces.IWindowObject;

//Author: Hunter Bragg

public class WindowLabel<E> extends WindowObject<E> {
	
	public String displayString = "";
	public int displayStringColor = 0xffffffff;
	protected EArrayList<String> wordWrappedLines;
	protected double widthMax = 0;
	protected boolean wordWrap = false;
	protected boolean centered = false;
	protected boolean shadow = true;
	protected double gapSize = 0;
	
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos) { this(parentIn, xPos, yPos, "", 0xffffffff); }
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos, String stringIn) { this(parentIn, xPos, yPos, stringIn, 0xffffffff); }
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos, String stringIn, EColors colorIn) { this(parentIn, xPos, yPos, stringIn, colorIn.c()); }
	public WindowLabel(IWindowObject parentIn, double xPos, double yPos, String stringIn, int colorIn) {
		init(parentIn, xPos, yPos, getStringWidth(stringIn), FontRenderer.FONT_HEIGHT);
		displayString = stringIn;
		displayStringColor = colorIn;
	}
	
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
		} else {
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
	
	public WindowLabel<E> setString(String stringIn) {
		displayString = stringIn;
		if (wordWrap) {
			wordWrappedLines = EStringBuilder.createWordWrapString(displayString, widthMax);
			
			int longest = 0;
			for (String s : wordWrappedLines) {
				int len = getStringWidth(s);
				if (len > longest) { longest = len; }
			}
			
			double w = longest;
			double h = getTextHeight();
			
			setDimensions(w, h);
		}
		else {
			setDimensions(getStringWidth(displayString), FontRenderer.FONT_HEIGHT);
		}
		return this;
	}
	
	public WindowLabel<E> enableWordWrap(boolean val, double widthMaxIn) {
		boolean oldVal = wordWrap;
		widthMax = widthMaxIn;
		wordWrap = val;
		setString(displayString);
		return this;
	}
	
	public double getTextHeight() { return wordWrap ? (wordWrappedLines.size() * FontRenderer.FONT_HEIGHT + wordWrappedLines.size() * gapSize) : FontRenderer.FONT_HEIGHT; }
	public String getString() { return displayString; }
	public int getColor() { return displayStringColor; }
	
	public boolean isEmpty() { return (displayString != null) ? displayString.isEmpty() : true; }
	
	public WindowLabel<E> clear() { if (displayString != null) { displayString = ""; } return this; }
	public WindowLabel<E> setLineGapHeight(int heightIn) { gapSize = heightIn; return this; }
	public WindowLabel<E> setColor(int colorIn) { displayStringColor = colorIn; return this; }
	public WindowLabel<E> setColor(EColors colorIn) { if (colorIn != null) { displayStringColor = colorIn.c(); } return this; }
	public WindowLabel<E> enableShadow(boolean val) { shadow = val; return this; }
	public WindowLabel<E> setDrawCentered(boolean val) { centered = val; return this; }
	
	public static void setColor(EColors colorIn, WindowLabel label, WindowLabel... additional) { setColor(colorIn.intVal, label, additional); }
	public static void setColor(int colorIn, WindowLabel label, WindowLabel... additional) {
		EUtil.filterNullForEachA(l -> l.setColor(colorIn), EUtil.add(label, additional));
	}
	
	public static void enableShadow(boolean val, WindowLabel label, WindowLabel... additional) {
		EUtil.filterNullForEachA(l -> l.enableShadow(val), EUtil.add(label, additional));
	}
	
}
