package envision.engine.rendering.fontRenderer;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import envision.Envision;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.colors.EColors;
import eutil.datatypes.boxes.Box2;

public class FontRenderer {
    
    //========
    // Fields
    //========
    
    public static final double FONT_HEIGHT = 24;
    public static final double FH = FONT_HEIGHT;
    public static final double HALF_FH = FONT_HEIGHT / 2;
    public static final double FONT_MID_Y = FONT_HEIGHT * 0.4;
    
    public static final char COPYRIGHT = '\u00A9';
    public static final char ERROR_CHAR = '\u0000';
    
    public static GameFont defaultFont = GameFont.createFont("default", "font_map.txt", "font.png");
    public static GameFont newFont = GameFont.createFont("new", "control_map.txt", "font_test_fix.png");
    public static GameFont font8 = GameFont.createFont("8-bit", "font_map_8x8.txt", "font_8x8.png");
    public static GameFont courier = GameFont.createFont("courier", "courier_map.txt", "courier.png");
    
    public static GameFont smooth = GameFont.createFont("smooth", "control_map.txt", "font_test_fix.png", GL11.GL_LINEAR, GL11.GL_LINEAR);
    
    private static final Map<String, GameFont> REGISTERED_FONTS = new HashMap<>();
    
    static {
        REGISTERED_FONTS.put(defaultFont.getFontName().toLowerCase(), defaultFont);
        REGISTERED_FONTS.put(newFont.getFontName().toLowerCase(), newFont);
        REGISTERED_FONTS.put(font8.getFontName().toLowerCase(), font8);
        REGISTERED_FONTS.put(courier.getFontName().toLowerCase(), courier);
        REGISTERED_FONTS.put(smooth.getFontName().toLowerCase(), smooth);
    }
    
    public static Map<String, GameFont> getRegisteredFonts() { return REGISTERED_FONTS; }
    
    private GameFont currentFont;
    
    //=================
    // Static Instance
    //=================
    
    private static FontRenderer instance;
    
    public static FontRenderer getInstance() {
        if (instance == null) instance = new FontRenderer();
        return instance;
    }
    
    //==============
    // Constructors
    //==============
    
    private FontRenderer() {
        final var ts = TextureSystem.getInstance();
        ts.reg(defaultFont.getFontTexture());
        ts.reg(newFont.getFontTexture());
        ts.reg(font8.getFontTexture());
        ts.reg(courier.getFontTexture());
        ts.reg(smooth.getFontTexture());
        currentFont = newFont;
    }
    
    //=========
    // Methods
    //=========
    
    public static double drawString(Object in, double xIn, double yIn, double scaleX, double scaleY) {
        return drawString(in != null ? in.toString() : "null", xIn, yIn, 0xffffffff, scaleX, scaleY);
    }
    public static double drawString(String in, double xIn, double yIn, double scaleX, double scaleY) {
        return drawString(in, xIn, yIn, 0xffffffff, scaleX, scaleY);
    }
    
    public static double drawString(Object in, double xIn, double yIn, EColors colorIn, double scaleX, double scaleY) {
        return drawString(in, xIn, yIn, colorIn.intVal, scaleX, scaleY);
    }
    public static double drawString(Object in, double xIn, double yIn, int colorIn, double scaleX, double scaleY) {
        return drawString(in != null ? in.toString() : "null", xIn, yIn, colorIn, scaleX, scaleY);
    }
    
    public static double drawString(String in, double xIn, double yIn, EColors colorIn, double scaleX, double scaleY) {
        return drawString(in, xIn, yIn, colorIn.intVal, scaleX, scaleY);
    }
    public static double drawString(String in, double xIn, double yIn, int colorIn, double scaleX, double scaleY) {
        return instance.createString(in, xIn, yIn, colorIn, scaleX, scaleY);
    }
    
    //=========
    // Getters
    //=========
    
    public static GameFont getCurrentFont() { return getInstance().currentFont; }
    
    public static double strWidth(Object in) { return strWidth(String.valueOf(in)); }
    public static double strWidth(String in) {
        return EStringOutputFormatter.getStringWidth(in);
    }
    
    public static int getCharWidth() { return instance.currentFont.getWidth(); }
    public static double getScaleW() { return instance.currentFont.getScaleW(); }
    public static double getScaleH() { return instance.currentFont.getScaleH(); }
    public static double getScaleSpace() { return instance.currentFont.getScaleSpace(); }
    
    //=========
    // Setters
    //=========
    
    public static void setCurrentFont(GameFont fontIn) {
        if (fontIn == null) return;
        if (fontIn.created()) instance.currentFont = fontIn;
        else Envision.error("Font '" + fontIn.getFontFile() + "' failed to load!");
    }
    
    //==================
    // Internal Methods
    //==================
    
    private double createString(String in, double xIn, double yIn, int colorIn, double scaleX, double scaleY) {
        if (in == null || in.isEmpty()) return 0.0;
        
        double sX = xIn;
        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);
            Box2<Integer, Integer> loc = currentFont.getCharImage(c);
            
            int w = currentFont.getWidth();
            int h = currentFont.getHeight();
            int xPos = loc.getA() * w;
            int yPos = loc.getB() * h;
            
            drawChar(sX, yIn, xPos, yPos, colorIn, scaleX, scaleY);
            sX += (w * currentFont.getScaleSpace() / Envision.getGameScale()) * scaleX;
        }
        
        return sX;
    }
    
    private void drawChar(double posX, double posY, int tX, int tY, int color, double scaleX, double scaleY) {
        final double w = currentFont.getWidth();
        final double h = currentFont.getHeight();
        final double sw = currentFont.getScaleW() * scaleX;
        final double sh = currentFont.getScaleH() * scaleY;
        final double draw_w = w * sw;
        final double draw_h = h * sh;
        
        final var font = currentFont.getFontTexture();
        
        RenderingManager.drawTexture(font, posX, posY, draw_w, draw_h, tX, tY, w, h, color, false);
    }
    
    //================
    // Helper Methods
    //================
    
    public static String trimToWidth(String in, int width) { return trimToWidth(in, width, true); }
    public static String trimToWidth(String in, int width, boolean reverse) {
        String s = "";
        int curLen = 0;
        for (int i = (reverse) ? in.length() - 1 : 0; i < in.length() && curLen < width; i += (reverse) ? -1 : 1) {
            char c = in.charAt(i);
            int w = getCharWidth();
            if (reverse) s = c + s;
            else s += c;
            curLen += w;
        }
        return s;
    }
    
}
