package envision.engine.rendering.fontRenderer;

import org.lwjgl.opengl.GL11;

import envision.debug.testStuff.testing.OpenGLTestingEnvironment;
import envision.engine.rendering.GLObject;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.colors.EColors;
import eutil.datatypes.boxes.Box2;
import qot.QoT;
import qot.settings.QoTSettings;

public class FontRenderer {
	
	
	
	public static final double FONT_HEIGHT = 24;
	public static final double FH = FONT_HEIGHT;
	public static final double HALF_FH = FONT_HEIGHT / 2;
	
	public static final char COPYRIGHT = '\u00A9';
	public static final char ERROR_CHAR = '\u0000';
	
	public static GameFont defaultFont = GameFont.createFont("font_map.txt", "font.png");
	public static GameFont newFont = GameFont.createFont("control_map.txt", "font_test_fix.png");
	public static GameFont font8 = GameFont.createFont("font_map_8x8.txt", "font_8x8.png");
	public static GameFont courier = GameFont.createFont("courier_map.txt", "courier.png");
	
	public static GameFont smooth = GameFont.createFont("control_map.txt", "font_test_fix.png", GL11.GL_LINEAR, GL11.GL_LINEAR);
	
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
		TextureSystem.getInstance().registerTexture(defaultFont.getFontTexture());
		TextureSystem.getInstance().registerTexture(newFont.getFontTexture());
		TextureSystem.getInstance().registerTexture(font8.getFontTexture());
		TextureSystem.getInstance().registerTexture(courier.getFontTexture());
		TextureSystem.getInstance().registerTexture(smooth.getFontTexture());
		currentFont = newFont;
	}
	
	//---------
	// Methods
	//---------
	
	public static double drawString(Object in, double xIn, double yIn) { return drawString(in != null ? in.toString() : "null", xIn, yIn, 0xffffffff); }
	public static double drawString(String in, double xIn, double yIn) { return drawString(in, xIn, yIn, 0xffffffff); }
	
	public static double drawString(Object in, double xIn, double yIn, EColors colorIn) { return drawString(in, xIn, yIn, colorIn.intVal); }
	public static double drawString(Object in, double xIn, double yIn, int colorIn) { return drawString(in != null ? in.toString() : "null", xIn, yIn, colorIn); }
	
	public static double drawString(String in, double xIn, double yIn, EColors colorIn) { return drawString(in, xIn, yIn, colorIn.intVal); }
	public static double drawString(String in, double xIn, double yIn, int colorIn) {
		return instance.createString(in, xIn, yIn, colorIn, 1.0, 1.0);
	}
	
	public static double drawString(Object in, double xIn, double yIn, double scaleX, double scaleY) { return drawString(in != null ? in.toString() : "null", xIn, yIn, 0xffffffff, scaleX, scaleY); }
	public static double drawString(String in, double xIn, double yIn, double scaleX, double scaleY) { return drawString(in, xIn, yIn, 0xffffffff, scaleX, scaleY); }
	
	public static double drawString(Object in, double xIn, double yIn, EColors colorIn, double scaleX, double scaleY) { return drawString(in, xIn, yIn, colorIn.intVal, scaleX, scaleY); }
	public static double drawString(Object in, double xIn, double yIn, int colorIn, double scaleX, double scaleY) { return drawString(in != null ? in.toString() : "null", xIn, yIn, colorIn, scaleX, scaleY); }
	
	public static double drawString(String in, double xIn, double yIn, EColors colorIn, double scaleX, double scaleY) { return drawString(in, xIn, yIn, colorIn.intVal, scaleX, scaleY); }
	public static double drawString(String in, double xIn, double yIn, int colorIn, double scaleX, double scaleY) {
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
	
	private double createString(String in, double xIn, double yIn, int colorIn, double scaleX, double scaleY) {
		if (in == null) return 0;
		
		double sX = xIn;
		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			Box2<Integer, Integer> loc = currentFont.getCharImage(c);
			
			int w = currentFont.getWidth();
			int h = currentFont.getHeight();
			int xPos = loc.getA() * w;
			int yPos = loc.getB() * h;
			
			drawChar(sX, yIn, xPos, yPos, colorIn, scaleX, scaleY);
			//drawCharBetter(sX, yIn, xPos, yPos, colorIn, scaleX, scaleY);
			sX += (w * currentFont.getScaleSpace() / QoT.getGameScale()) * scaleX;
		}
		return sX;
	}
	
	private void drawChar(double posX, double posY, int tX, int tY, int color, double scaleX, double scaleY) {
		double w = currentFont.getWidth();
		double h = currentFont.getHeight();
		double sw = currentFont.getScaleW() * scaleX;
		double sh = currentFont.getScaleH() * scaleY;
		GLObject.drawTexture(currentFont.getFontTexture(), posX, posY, w * sw, h * sh, tX, tY, w, h, color);
	}
	
	private void drawCharBetter(double posX, double posY, int tX, int tY, int color, double scaleX, double scaleY) {
		double w = currentFont.getWidth();
		double h = currentFont.getHeight();
		double sw = currentFont.getScaleW() * scaleX;
		double sh = currentFont.getScaleH() * scaleY;
		OpenGLTestingEnvironment.drawTexture(currentFont.getFontTexture(), posX, posY, w * sw, h * sh, tX, tY, w, h, color, false);
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
