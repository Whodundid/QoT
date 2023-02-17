package envision.engine.windows.windowObjects.basicObjects;

import envision.engine.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.windows.windowTypes.WindowObject;
import envision.engine.windows.windowTypes.interfaces.IWindowObject;
import eutil.EUtil;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.datatypes.boxes.Box2;
import eutil.datatypes.boxes.BoxList;

public class WindowTextBox<E> extends WindowObject<E> {
	
	//--------
	// Fields
	//--------
	
	private BoxList<String, Integer> lines = new BoxList<>();
	private boolean centered = false;
	private boolean shadowed = false;
	private boolean drawBackground = true;
	private double maxWidth = -1;
	private double maxHeight = -1;
	private double border = 5;
	private double lineGap = 0;
	private double xPos, yPos;
	
	//--------------
	// Constructors
	//--------------
	
	public WindowTextBox(IWindowObject<?> parent, double x, double y) { this(parent, x, y, null); }
	public WindowTextBox(IWindowObject<?> parent, double x, double y, EArrayList<String> linesIn) {
		init(parent);
		xPos = x;
		yPos = y;
		EUtil.nullDo(linesIn, arr -> arr.forEach(l -> addLine(l, 0xffffff)));
		redimension();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		//draw background
		if (drawBackground) {
			drawRect(startX, startY, startX + width, startY + height, EColors.black);
			drawRect(startX + 1, startY + 1, startX + width - 1, startY + height - 1, 0xff202020);
			drawRect(startX + 2, startY + 2, startX + width - 2, startY + height - 2, 0xff282828);
			drawRect(startX + 3, startY + 3, startX + width - 3, startY + height - 2, 0xff303030);
		}
		
		//draw contents scissored
		
		double scissorX = maxWidth > 0 ? 0 : startX;
		double scissorY = maxHeight > 0 ? 0 : startY;
		double scissorW = maxWidth > 0 ? res.getWidth() : startX + maxWidth + border;
		double scissorH = maxHeight > 0 ? res.getHeight() : startY + maxHeight + border;
		
		scissor(scissorX, scissorY, scissorW, scissorH);
		
		double yPos = startY + border + 5;
		for (var line : lines) {
			drawLine(line.getA(), line.getB(), yPos);
			
			yPos += FontRenderer.FONT_HEIGHT + lineGap;
		}
		
		endScissor();
	}
	
	//---------
	// Methods
	//---------
	
	public void addLine(String lineIn) { addLine(lineIn, 0xffffff); }
	public void addLine(String lineIn, int color) {
		EUtil.nullDo(parseLine(lineIn, color), l -> lines.addAll(l));
		redimension();
	}
	
	//---------
	// Getters
	//---------
	
	public double getMaxWidth() { return maxWidth; }
	public double getMaxHeight() { return maxHeight; }
	
	//---------
	// Setters
	//---------
	
	public void setMaxWidth(double widthIn) { maxWidth = widthIn; redimension(); }
	public void setMaxHeight(double heightIn) { maxHeight = heightIn; redimension(); }
	public void setBorderSize(int sizeIn) { border = sizeIn; redimension(); }
	public void setLineGap(int gapIn) { lineGap = gapIn; redimension(); }
	public void setDrawBackground(boolean val) { drawBackground = val; }
	
	//------------------
	// Internal Methods
	//------------------
	
	private EArrayList<Box2<String, Integer>> parseLine(String lineIn, int colorIn) {
		if (lineIn != null) {
			
			EArrayList<String> newLineCheck = new EArrayList<>();
			
			//check for new line characters
			int pos = 0;
			for (int i = 0; i < lineIn.length(); i++) {
				if (lineIn.charAt(i) == '\n') {
					
					newLineCheck.add(lineIn.substring(pos, i));
					pos = i;
				}
			}
			
			EArrayList<String> widthAdjusted = new EArrayList<>();
			
			for (String s : newLineCheck) {
				widthAdjusted.addAll(EStringOutputFormatter.createWordWrapString(s, (int) maxWidth));
			}
			
			EArrayList<Box2<String, Integer>> createdLines = new EArrayList<>();
			
			for (String s : widthAdjusted) {
				createdLines.add(new Box2<String, Integer>(s, colorIn));
			}
			
			return createdLines;
		}
		
		return null;
	}
	
	private void redimension() {
		if (lines.isNotEmpty()) {
			int longest = 0;
			for (var box : lines) {
				String s = box.getA();
				int len = getStringWidth(s);
				if (len > longest) longest = len;
			}
			
			double w = longest + border;
			double h = lines.size() * FontRenderer.FONT_HEIGHT + (lines.size() * lineGap);
			double x = xPos - (w / 2);
			double y = yPos - (h / 2);
			
			setDimensions(x, y, w, h);
		}
		else {
			setDimensions(xPos - border, yPos - border, xPos + border, yPos + border);
		}
	}
	
	private void drawLine(String s, int color, double i) {
		if (s != null) {
			if (centered) {
				if (shadowed) drawStringCS(s, xPos, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color);
				else drawStringC(s, xPos, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color);
			}
			else {
				if (shadowed) drawStringS(s, startX, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color);
				else drawString(s, startX, startY + (i * 9) + (i > 0 ? i * lineGap : 0), color);
			}
		}
	}
	
	//----------------
	// Static Methods
	//----------------
	
	public static void drawBox(double x, double y, String... linesIn) {
		if (linesIn != null) {
			var lines = new Box2[linesIn.length];
			for (int i = 0; i < linesIn.length; i++) lines[i] = new Box2<>(linesIn[i], 0xffffff);
			drawBox(x, y, 8, 3, true, true, lines);
		}
	}
	
	public static void drawBox(double x, double y, BoxList<String, Integer> linesIn) {
		if (linesIn != null) { drawBox(x, y, 8, 3, true, true, linesIn.getBoxesAsArray()); }
	}
	
	public static void drawBox(double x, double y, Box2<String, Integer>[] linesIn) {
		drawBox(x, y, 8, 3, true, true, linesIn);
	}
	
	public static void drawBox(double xIn, double yIn, double borderIn, double lineGapIn, boolean centered, boolean shadowed, Box2<String, Integer>[] linesIn) {
		if (linesIn != null) {
			
			int longest = 0;
			for (var box : linesIn) {
				String s = box.getA();
				if (s != null) {
					int len = getStringWidth(s);
					if (len > longest) longest = len;
				}
			}
			
			double w = longest + (borderIn * 2);
			double h = linesIn.length * FontRenderer.FONT_HEIGHT + (linesIn.length * lineGapIn) + (borderIn * 2);
			double x = xIn - (w / 2);
			double y = yIn - (h / 2);
			
			//draw background
			drawRect(x, y, x + w, y + h, EColors.black);
			drawRect(x + 1, y + 1, x + w - 1, y + h - 1, 0xff202020);
			drawRect(x + 2, y + 2, x + w - 2, y + h - 2, 0xff282828);
			drawRect(x + 3, y + 3, x + w - 3, y + h - 2, 0xff303030);
			
			double ty = y + borderIn + (lineGapIn / 2) + (lineGapIn % 2);
			
			int i = 0;
			for (var box : linesIn) {
				if (box != null) {
					String s = box.getA();
					int c = box.getB();
					
					//draw
					if (centered) {
						if (shadowed) drawStringCS(s, xIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c);
						else drawStringC(s, xIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c);
					}
					else {
						if (shadowed) drawStringS(s, x + borderIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c);
						else drawString(s, x + borderIn, ty + (i * 9) + (i > 0 ? i * lineGapIn : 0), c);
					}
				}
				i++;
			}
		}
	}
	
}