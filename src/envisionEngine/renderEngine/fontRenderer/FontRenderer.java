package envisionEngine.renderEngine.fontRenderer;

import eutil.colors.EColors;
import eutil.datatypes.Box2;
import qot.QoT;
import qot.settings.QoTSettings;

import org.lwjgl.opengl.GL11;

import envisionEngine.renderEngine.GLObject;

public class FontRenderer {
	
	private static final String fontDir = QoTSettings.getResourcesDir().toString() + "\\font\\";
	
	public static final double FONT_HEIGHT = 24;
	public static final double FH = FONT_HEIGHT;
	public static final double HALF_FH = FONT_HEIGHT / 2;
	
	public static final char COPYRIGHT = '\u00A9';
	public static final char ERROR_CHAR = '\u0000';
	
	public static GameFont defaultFont = GameFont.createFont(fontDir + "font_map.txt", fontDir + "font.png");
	public static GameFont newFont = GameFont.createFont(fontDir + "control_map.txt", fontDir + "font_test_fix.png");
	public static GameFont font8 = GameFont.createFont(fontDir + "font_map_8x8.txt", fontDir + "font_8x8.png");
	public static GameFont courier = GameFont.createFont(fontDir + "courier_map.txt", fontDir + "courier.png");
	
	private GameFont currentFont;
	
	//-----------------
	// Static Instance
	//-----------------
	
	private static FontRenderer instance;
	
	public static FontRenderer getInstance() {
		return instance = (instance != null) ? instance : new FontRenderer();
	}

	//--------------
	// Constructors
	//--------------
	
	private FontRenderer() {
		QoT.getTextureSystem().registerTexture(defaultFont.getFontTexture());
		QoT.getTextureSystem().registerTexture(newFont.getFontTexture());
		QoT.getTextureSystem().registerTexture(font8.getFontTexture());
		QoT.getTextureSystem().registerTexture(courier.getFontTexture());
		currentFont = newFont;
	}
	
	//---------
	// Methods
	//---------
	
	public static double drawString(Object in, Number xIn, Number yIn) { return drawString(in != null ? in.toString() : "null", xIn, yIn, 0xffffffff); }
	public static double drawString(String in, Number xIn, Number yIn) { return drawString(in, xIn, yIn, 0xffffffff); }
	
	public static double drawString(Object in, Number xIn, Number yIn, EColors colorIn) { return drawString(in, xIn, yIn, colorIn.intVal); }
	public static double drawString(Object in, Number xIn, Number yIn, int colorIn) { return drawString(in != null ? in.toString() : "null", xIn, yIn, colorIn); }
	
	public static double drawString(String in, Number xIn, Number yIn, EColors colorIn) { return drawString(in, xIn, yIn, colorIn.intVal); }
	public static double drawString(String in, Number xIn, Number yIn, int colorIn) {
		return instance.createString(in, xIn, yIn, colorIn, 1.0, 1.0);
	}
	
	public static double drawString(Object in, Number xIn, Number yIn, double scaleX, double scaleY) { return drawString(in != null ? in.toString() : "null", xIn, yIn, 0xffffffff, scaleX, scaleY); }
	public static double drawString(String in, Number xIn, Number yIn, double scaleX, double scaleY) { return drawString(in, xIn, yIn, 0xffffffff, scaleX, scaleY); }
	
	public static double drawString(Object in, Number xIn, Number yIn, EColors colorIn, double scaleX, double scaleY) { return drawString(in, xIn, yIn, colorIn.intVal, scaleX, scaleY); }
	public static double drawString(Object in, Number xIn, Number yIn, int colorIn, double scaleX, double scaleY) { return drawString(in != null ? in.toString() : "null", xIn, yIn, colorIn, scaleX, scaleY); }
	
	public static double drawString(String in, Number xIn, Number yIn, EColors colorIn, double scaleX, double scaleY) { return drawString(in, xIn, yIn, colorIn.intVal, scaleX, scaleY); }
	public static double drawString(String in, Number xIn, Number yIn, int colorIn, double scaleX, double scaleY) {
		return instance.createString(in, xIn, yIn, colorIn, scaleX, scaleY);
	}
	
	//---------
	// Getters
	//---------
	
	public static GameFont getCurrentFont() { return getInstance().currentFont; }
	
	public static int getStringWidth(String in) { return (int) (in.length() * (getCharWidth() * getScaleSpace() / QoT.getGameScale())); }
	public static int getCharWidth() { return instance.currentFont.getWidth(); }
	public static double getScaleW() { return instance.currentFont.getScaleW(); }
	public static double getScaleH() { return instance.currentFont.getScaleH(); }
	public static double getScaleSpace() { return instance.currentFont.getScaleSpace(); }
	
	//---------
	// Setters
	//---------
	
	public static void setCurrentFont(GameFont fontIn) {
		if (fontIn != null) {
			if (fontIn.created()) instance.currentFont = fontIn;
			else QoT.error("Font '" + fontIn.getFontFile() + "' failed to load!");
		}
	}
	
	//------------------
	// Internal Methods
	//------------------
	
	private double createString(String in, Number xIn, Number yIn, int colorIn, double scaleX, double scaleY) {
		if (in == null) return 0;
		
		double sX = xIn.doubleValue();
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			Box2<Integer, Integer> loc = currentFont.getCharImage(c);
			
			int w = currentFont.getWidth();
			int h = currentFont.getHeight();
			int xPos = loc.getA() * w;
			int yPos = loc.getB() * h;
			
			drawChar(sX, yIn.doubleValue(), xPos, yPos, colorIn, scaleX, scaleY);
			sX += (w * currentFont.getScaleSpace() / QoT.getGameScale()) * scaleX;
		}
		return sX;
	}
	
	private void drawChar(double posX, double posY, int tX, int tY, int color, double scaleX, double scaleY) {
		GL11.glPushMatrix();
		double w = currentFont.getWidth();
		double h = currentFont.getHeight();
		double sw = currentFont.getScaleW() * scaleX;
		double sh = currentFont.getScaleH() * scaleY;
		GLObject.drawTexture(currentFont.getFontTexture(), posX, posY, w * sw, h * sh, tX, tY, w, h, color);
		GL11.glPopMatrix();
	}
	
	public String trimToWidth(String in, int width) { return trimToWidth(in, width, true); }
	public String trimToWidth(String in, int width, boolean reverse) {
		String s = "";
		int curLen = 0;
		for (int i = (reverse) ? in.length() - 1 : 0; i < in.length() && curLen < width; i += (reverse) ? -1 : 1) {
			char c = in.charAt(i);
			int w = getCharWidth();
			if (reverse) { s = c + s; }
			else { s += c; }
			curLen += w;
		}
		return s;
	}
	
}
