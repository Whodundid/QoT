package engine.renderEngine.fontRenderer;

import engine.renderEngine.GLObject;
import eutil.colors.EColors;
import eutil.datatypes.Box2;
import main.QoT;

import org.lwjgl.opengl.GL11;

public class FontRenderer {
	
	public static final double FONT_HEIGHT = 24;
	public static GameFont defaultFont = GameFont.createFont("bin/font/font_map.txt", "bin/font/font.png");
	public static GameFont newFont = GameFont.createFont("bin/font/control_map.txt", "bin/font/font_test_fix.png");
	public static GameFont font8 = GameFont.createFont("bin/font/font_map_8x8.txt", "bin/font/font_8x8.png");
	public static GameFont courier = GameFont.createFont("bin/font/courier_map.txt", "bin/font/courier.png");
	
	private GameFont currentFont;
	
	//---------------
	//Static Instance
	//---------------
	
	private static FontRenderer instance;
	
	public static FontRenderer getInstance() {
		return instance = (instance != null) ? instance : new FontRenderer();
	}

	//-------------------------
	//FontRenderer Constructors
	//-------------------------
	
	private FontRenderer() {
		QoT.getTextureSystem().registerTexture(defaultFont.getFontTexture());
		QoT.getTextureSystem().registerTexture(newFont.getFontTexture());
		QoT.getTextureSystem().registerTexture(font8.getFontTexture());
		QoT.getTextureSystem().registerTexture(courier.getFontTexture());
		currentFont = newFont;
	}
	
	//--------------------
	//FontRenderer Methods
	//--------------------
	
	public static double drawString(Object in, Number xIn, Number yIn) { return drawString(in != null ? in.toString() : "null", xIn, yIn, 0xffffffff); }
	public static double drawString(String in, Number xIn, Number yIn) { return drawString(in, xIn, yIn, 0xffffffff); }
	
	public static double drawString(Object in, Number xIn, Number yIn, EColors colorIn) { return drawString(in, xIn, yIn, colorIn.intVal); }
	public static double drawString(Object in, Number xIn, Number yIn, int colorIn) { return drawString(in != null ? in.toString() : "null", xIn, yIn, colorIn); }
	
	public static double drawString(String in, Number xIn, Number yIn, EColors colorIn) { return drawString(in, xIn, yIn, colorIn.intVal); }
	public static double drawString(String in, Number xIn, Number yIn, int colorIn) {
		return instance.createString(in, xIn, yIn, colorIn);
	}
	
	//--------------------
	//FontRenderer Getters
	//--------------------
	
	public static GameFont getCurrentFont() { return getInstance().currentFont; }
	
	public static int getStringWidth(String in) { return (int) (in.length() * (getCharWidth() * getScaleSpace() / QoT.getGameScale())); }
	public static int getCharWidth() { return instance.currentFont.getWidth(); }
	public static double getScaleW() { return instance.currentFont.getScaleW(); }
	public static double getScaleH() { return instance.currentFont.getScaleH(); }
	public static double getScaleSpace() { return instance.currentFont.getScaleSpace(); }
	
	//--------------------
	//FontRenderer Setters
	//--------------------	
	
	public static void setCurrentFont(GameFont fontIn) {
		if (fontIn != null) {
			if (fontIn.created()) instance.currentFont = fontIn;
			else QoT.error("Font '" + fontIn.getFontFile() + "' failed to load!");
		}
	}
	
	//-----------------------------
	//FontRenderer Internal Methods
	//-----------------------------
	
	private double createString(String in, Number xIn, Number yIn, int colorIn) {
		if (in != null) {
			double sX = xIn.doubleValue();
			for (int i = 0; i < in.length(); i++) {
				char c = in.charAt(i);
				Box2<Integer, Integer> loc = currentFont.getCharImage(c);
				
				int w = currentFont.getWidth();
				int h = currentFont.getHeight();
				int xPos = loc.getA() * w;
				int yPos = loc.getB() * h;
				
				drawChar(sX, yIn.doubleValue(), xPos, yPos, colorIn);
				sX += (w * currentFont.getScaleSpace() / QoT.getGameScale());
			}
			return sX;
		}
		return 0;
	}
	
	private void drawChar(double posX, double posY, int tX, int tY, int color) {
		GL11.glPushMatrix();
		int w = currentFont.getWidth();
		int h = currentFont.getHeight();
		double sw = currentFont.getScaleW();
		double sh = currentFont.getScaleH();
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
