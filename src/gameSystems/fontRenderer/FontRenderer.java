package gameSystems.fontRenderer;

import main.Game;
import openGL_Util.GLObject;
import org.lwjgl.opengl.GL11;
import renderUtil.EColors;
import storageUtil.StorageBox;

public class FontRenderer {
	
	public static final double FONT_HEIGHT = 24;
	public static GameFont defaultFont = GameFont.createFont("bin/font/font_map.txt", "bin/font/font.png");
	public static GameFont newFont = GameFont.createFont("bin/font/control_map.txt", "bin/font/font_test_fix.png");
	public static GameFont font8 = GameFont.createFont("bin/font/control_map.txt", "bin/font/font_8x8.png");
	
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
		Game.getTextureSystem().registerTexture(defaultFont.getFontTexture());
		Game.getTextureSystem().registerTexture(newFont.getFontTexture());
		Game.getTextureSystem().registerTexture(font8.getFontTexture());
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
	
	public static int getStringWidth(String in) { return (int) (in.length() * (16 / Game.getGameScale())); }
	public static int getCharWidth(char c) { return getStringWidth("" + c); }
	
	//--------------------
	//FontRenderer Setters
	//--------------------	
	
	public FontRenderer setCurrentFont(GameFont fontIn) { if (fontIn != null) { currentFont = fontIn; } return this; }
	
	//-----------------------------
	//FontRenderer Internal Methods
	//-----------------------------
	
	private double createString(String in, Number xIn, Number yIn, int colorIn) {
		if (in != null) {
			double sX = xIn.doubleValue();
			for (int i = 0; i < in.length(); i++) {
				char c = in.charAt(i);
				StorageBox<Integer, Integer> loc = currentFont.getCharImage(c);
				
				int xPos = loc.getA() * 16;
				int yPos = loc.getB() * 32;
				
				//System.out.println(xPos + " : " + yPos);
				//System.out.println(currentFont.getFontTexture().getWidth() + " : " + currentFont.getFontTexture().getHeight());
				
				drawChar(sX, yIn.doubleValue(), xPos, yPos, colorIn);
				sX += (16 / Game.getGameScale());
			}
			return sX;
		}
		return 0;
	}
	
	private void drawChar(double posX, double posY, int tX, int tY, int color) {
		GL11.glPushMatrix();
		GL11.glScaled(1, 1, 1);
		GLObject.drawTexture(posX, posY, 16, 32, tX, tY, 16, 31.75, currentFont.getFontTexture(), color);
		GL11.glPopMatrix();
	}
	
	public String trimToWidth(String in, int width) { return trimToWidth(in, width, true); }
	public String trimToWidth(String in, int width, boolean reverse) {
		String s = "";
		int curLen = 0;
		for (int i = (reverse) ? in.length() - 1 : 0; i < in.length() && curLen < width; i += (reverse) ? -1 : 1) {
			char c = in.charAt(i);
			int w = getCharWidth(c);
			if (reverse) { s = c + s; }
			else { s += c; }
			curLen += w;
		}
		return s;
	}
	
}
