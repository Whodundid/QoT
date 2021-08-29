package renderEngine;

import eutil.colors.EColors;
import eutil.misc.Rotation;
import main.QoT;
import main.WindowSize;
import org.lwjgl.opengl.GL11;
import renderEngine.fontRenderer.EStringBuilder;
import renderEngine.textureSystem.GameTexture;
import renderEngine.textureSystem.TextureSystem;

/** The base underlying object that all window objects are drawn from. */
public abstract class GLObject {
	
	protected WindowSize res = QoT.getWindowSize();
	protected float glZLevel;
	
	//--------------------------------------
	// Public Static String Drawing Methods
	//--------------------------------------
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y) { return drawString(toStr(o), x, y, EColors.white.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y, EColors colorIn) { return drawString(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredString(Object o, double x, double y, EColors colorIn) { return drawCenteredString(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawStringWithShadow(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(toStr(o), x, y, colorIn.intVal); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawString(Object o, double x, double y, int color) { return EStringBuilder.drawString(toStr(o), x, y, color, false, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredString(Object o, double x, double y, int color) { return EStringBuilder.drawString(toStr(o), x, y, color, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringWithShadow(Object o, double x, double y, int color) { return EStringBuilder.drawString(toStr(o), x, y, color, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawCenteredStringWithShadow(Object o, double x, double y, int color) { return EStringBuilder.drawString(toStr(o), x, y, color, true, true); }
	
	
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y) { return drawString(text, x, y, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, EColors colorIn) { return drawString(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(String text, double x, double y, EColors colorIn) { return drawCenteredString(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(String text, double x, double y, EColors colorIn) { return drawStringWithShadow(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(String text, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(text, x, y, colorIn.intVal); }
	
	
	/** Draws a String at the specified position. */
	public static double drawString(String text, double x, double y, int color) { return EStringBuilder.drawString(text, x, y, color, false, false); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(String text, double x, double y, int color) { return EStringBuilder.drawString(text, x, y, color, true, false); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(String text, double x, double y, int color) { return EStringBuilder.drawString(text, x, y, color, false, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(String text, double x, double y, int color) { return EStringBuilder.drawString(text, x, y, color, true, true); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y) { return drawStringC(toStr(o), x, y, EColors.white); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, EColors colorIn) { return drawStringC(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object o, double x, double y, EColors colorIn) { return drawStringS(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object o, double x, double y, EColors colorIn) { return drawStringCS(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object o, double x, double y, int color) { return drawCenteredString(toStr(o), x, y, color); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object o, double x, double y, int color) { return drawStringWithShadow(toStr(o), x, y, color); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object o, double x, double y, int color) { return drawCenteredStringWithShadow(toStr(o), x, y, color); }
	
	
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, EColors colorIn) { return drawStringC(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, EColors colorIn) { return drawStringS(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringCS(String text, double x, double y, EColors colorIn) { return drawStringCS(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	public static double drawStringC(String text, double x, double y, int color) { return drawCenteredString(text, x, y, color); }
	/** Draws a String at the specified position. */
	public static double drawStringS(String text, double x, double y, int color) { return drawStringWithShadow(text, x, y, color); }
	/** Draws a String at the specified position. */
	public static double drawStringCS(String text, double x, double y, int color) { return drawCenteredStringWithShadow(text, x, y, color); }
	
	/** Wrapper for EStringBuilder's getStringWidth. */
	public static int getStringWidth(String text) { return EStringBuilder.getStringWidth(text); }
	
	/** Returns a 'toString' representation of the given object. Accounts for null objets as well. */
	private static String toStr(Object in) { return (in != null) ? in.toString() : "null"; }
	
	//-------------------------------------
	// Public Static Shape Drawing Methods
	//-------------------------------------
	
	/** Draws a line from point a to b with a thickness of 2. */
	public static void drawLine(double startX, double startY, double endX, double endY, EColors color) { drawLine(startX, startY, endX, endY, 2, color.intVal); }
	/** Draws a line from point a to b with a thickness of 2. */
	public static void drawLine(double startX, double startY, double endX, double endY, int color) { drawLine(startX, startY, endX, endY, 2, color); }
	/** Draws a line from point a to b with a variable thickness. */
	public static void drawLine(double startX, double startY, double endX, double endY, int thickness, EColors color) { drawLine(startX, startY, endX, endY, thickness, color.intVal); }
	/** Draws a line from point a to b with a variable thickness. */
	public static void drawLine(double startX, double startY, double endX, double endY, int thickness, int color) {
		//set mode
		begin(GLModes.LINES);
		
		//set drawing settings
		blendOn();
		alphaOn();
		setGLColor(color);
		lineWidth(thickness);
		
		//define points
		vertex(tdx(startX), tdy(startY), 0);
		vertex(tdx(endX), tdy(endY), 0);
		
		//draw
		draw();
	}
	
	
	/** Draws a horizontal line with a thickness of 1. */
	public static void drawHorizontalLine(double startX, double endX, double y, EColors color) { drawHorizontalLine(startX, endX, y, color.intVal); }
	/** Draws a horizontal line with a thickness of 1. */
	public static void drawHorizontalLine(double startX, double endX, double y, int color) {
		//correct dimensions (if necessary)
		if (endX < startX) {
			double i = startX;
			startX = endX;
			endX = i;
		}
		
		//draw
		drawRect(startX, y, endX + 1, y + 1, color);
	}
	
	
	/** Draws a vertical line with a thickness of 1. */
	public static void drawVerticalLine(double x, double startY, double endY, EColors color) { drawVerticalLine(x, startY, endY, color.intVal); }
	/** Draws a vertical line with a thickness of 1. */
	public static void drawVerticalLine(double x, double startY, double endY, int color) {
		//correct dimensions (if necessary)
		if (endY < startY) {
			double i = startY;
			startY = endY;
			endY = i;
		}
		
		//draw
		drawRect(x, startY + 1, x + 1, endY, color);
	}
	
	
	/** Draws a hollow circle expanding out from the center. */
	public static void drawCircle(double posX, double posY, double radius, int detail, EColors color) { drawCircle(posX, posY, radius, detail, color.intVal); }
	/** Draws a hollow circle expanding out from the center. */
	public static void drawCircle(double posX, double posY, double radius, int detail, int color) { drawEllipse(posX, posY, radius, radius, detail, color); }
	
	
	/** Draws a solid circle expanding out from the center. */
	public static void drawFilledCircle(double posX, double posY, double radius, int detail, EColors color) { drawFilledCircle(posX, posY, radius, detail, color.intVal); }
	/** Draws a solid circle expanding out from the center. */
	public static void drawFilledCircle(double posX, double posY, double radius, int detail, int color) { drawFilledEllipse(posX, posY, radius, radius, detail, color); }
	
	
	/** Draws a hollow ellipse expanding out from the center. */
	public static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	/** Draws a hollow ellipse expanding out from the center. */
	public static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		//set mode
		begin(GLModes.LINE_LOOP);
		
		//set drawing settings
		blendOn();
		alphaOn();
		GLSettings.blendSeparate();
		setGLColor(color);
		
		//define points
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radiusX * Math.cos(theta);
			double y = radiusY * Math.sin(theta);
			vertex(tdx(x + posX), tdy(y + posY));
		}
		
		//draw
		draw();
	}
	
	
	/** Draws a solid ellipse expanding out from the center. */
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	/** Draws a solid ellipse expanding out from the center. */
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		//set mode
		begin(GLModes.TRIANGLE_FAN);
		
		//set drawing settings
		blendOn();
		alphaOn();
		GLSettings.blendSeparate();
		setGLColor(color);
		
		//define points
		double prevX = 0.0, prevY = 0.0;
		vertex(tdx(posX), tdy(posY));
		
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radiusX * Math.cos(theta);
			double y = radiusY * Math.sin(theta);
			vertex(tdx(x + posX), tdy(y + posY));
			vertex(tdx(posX + prevX), tdy(posY + prevY));
			prevX = x;
			prevY = y;
		}
		
		//draw
		draw();
	}
	
	
	/** Draws a filled rectangle. */
	public static void drawRect(double left, double top, double right, double bottom, EColors colorIn) { drawRect(left, top, right, bottom, colorIn.intVal); }
	/** Draws a filled rectangle. */
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		//correct dimensions (if necessary)
		if (left < right) {
			double i = left;
			left = right;
			right = i;
		}
		
		if (top < bottom) {
			double j = top;
			top = bottom;
			bottom = j;
		}
		
		//set mode
		begin(GLModes.QUADS);
		
		//set drawing settings
		blendOn();
		alphaOn();
		GLSettings.blendSeparate();
		setGLColor(color);
		
		//define points
		vertex(tdx(left), tdy(bottom));
		vertex(tdx(right), tdy(bottom));
		vertex(tdx(right), tdy(top));
		vertex(tdx(left), tdy(top));
		
		//draw
		draw();
	}
	
	
	/** Draws a hollow rectangle with variable line width. */
	public static void drawHRect(double left, double top, double right, double bottom, double borderWidth, EColors colorIn) { drawHRect(left, top, right, bottom, borderWidth, colorIn.intVal); }
	/** Draws a hollow rectangle with variable line width. */
	public static void drawHRect(double left, double top, double right, double bottom, double borderWidth, int color) {
		drawRect(left, top, left + borderWidth, bottom, color); //left
		drawRect(left, top, right, top + borderWidth, color); //top
		drawRect(right - borderWidth, top, right, bottom, color); //right
		drawRect(left, bottom - borderWidth, right, bottom, color); //bottom
	}
	
	/** Draws a texture with the given dimensions. */
	public static void drawTexture(double x, double y, double w, double h) { drawTexture(x, y, w, h, TextureSystem.getInstance().getBoundTexture(), false, Rotation.UP); }
	public static void drawTexture(double x, double y, double w, double h, GameTexture tex) { drawTexture(x, y, w, h, tex, false, Rotation.UP); }
	public static void drawTexture(double x, double y, double w, double h, GameTexture tex, boolean flip) { drawTexture(x, y, w, h, tex, false, Rotation.UP); }
	public static void drawTexture(double x, double y, double w, double h, GameTexture tex, boolean flip, Rotation rotation) {
		//Ensure the texture can actually be drawn
		if (tex != null && tex.hasBeenRegistered()) {
			
			alphaOn();
			blendOn();
			setGLColor(0xffffffff);
			GLSettings.enableTexture();
			bindTexture(tex);
			
			begin(GLModes.QUADS);
			tv(flip, rotation, x, y, x + w, y + h);
			
			draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public static void drawTexture(double x, double y, double w, double h, double tX, double tY, double tW, double tH) {
		drawTexture(x, y, w, h, tX, tY, tW, tH, TextureSystem.getInstance().getBoundTexture(), 0xffffffff);
	}
	public static void drawTexture(double x, double y, double w, double h, double tX, double tY, double tW, double tH, GameTexture imageIn) {
		drawTexture(x, y, w, h, tX, tY, tW, tH, imageIn, 0xffffffff);
	}
	public static void drawTexture(double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color) {
		drawTexture(x, y, w, h, tX, tY, tW, tH, TextureSystem.getInstance().getBoundTexture(), color.intVal);
	}
	public static void drawTexture(double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		drawTexture(x, y, w, h, tX, tY, tW, tH, TextureSystem.getInstance().getBoundTexture(), color);
	}
	public static void drawTexture(double x, double y, double w, double h, double tX, double tY, double tW, double tH, GameTexture imageIn, EColors color) {
		drawTexture(x, y, w, h, tX, tY, tW, tH, imageIn, color.intVal);
	}
	public static void drawTexture(double x, double y, double w, double h, double tX, double tY, double tW, double tH, GameTexture imageIn, int color) {
		if (imageIn != null) {
			
			double xVal = (double) tX / (double) imageIn.getWidth();
			double yVal = (double) tY / (double) imageIn.getHeight();
			double wVal = (double) tW / (double) imageIn.getWidth();
			double hVal = (double) tH / (double) imageIn.getHeight();
			
			//System.out.println(xVal + " " + yVal + " " + wVal + " " + hVal);
			
			alphaOn();
			blendOn();
			setGLColor(color);
			GLSettings.enableTexture();
			bindTexture(imageIn);
			
			begin(GLModes.QUADS);
			
			GL11.glTexCoord2d(xVal, yVal + hVal);
			vertex(tdx(x), tdy(y + h));
			GL11.glTexCoord2d(xVal + wVal, yVal + hVal);
			vertex(tdx(x + w), tdy(y + h));
			GL11.glTexCoord2d(xVal + wVal, yVal);
			vertex(tdx(x + w), tdy(y));
			GL11.glTexCoord2d(xVal, yVal);
			vertex(tdx(x), tdy(y));
			
			draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(int startX, int startY, int endX, int endY) { scissor((double) startX, (double) startY, (double) endX, (double) endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(double startX, double startY, double endX, double endY) { scissor(startX, startY, endX, endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(int startX, int startY, int endX, int endY, boolean useGlY) { scissor((double) startX, (double) startY, (double) endX, (double) endY, useGlY); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(double startX, double startY, double endX, double endY, boolean useGlY) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		double scale = QoT.getGameScale();
		int w = (int) ((endX - startX) * scale);
		int h = (int) ((endY - startY) * scale);
		int x = (int) (startX * scale);
		int y = useGlY ? (int) (startY * scale) : (int) (QoT.getWindowSize().getHeight() - (startY * scale) - h);
		if (w >= 0 && h >= 0) { GL11.glScissor(x, y, w, h); }
	}
	
	/** Stops scissoring an area. */
	public static void endScissor() { GL11.glDisable(GL11.GL_SCISSOR_TEST); }
	
	
	//----------------------------
	// Public GL Drawing Settings
	//----------------------------
	
	
	/** Breaks down an int representation of a color into the appropriate (r, g, b, a) OpenGL float values. */
	public static void setGLColor(int colorIn) {
		GL11.glClearColor(0, 0, 0, 0);
		float f = (colorIn >> 16 & 255) / 255.0F;
		float f1 = (colorIn >> 8 & 255) / 255.0F;
		float f2 = (colorIn & 255) / 255.0F;
		float f3 = (colorIn >> 24 & 255) / 255.0F;
		GLSettings.color(f, f1, f2, f3);
	}
	
	/** 'to double for x' converts a given value into a corresponding double value between -1.0f and 1.0f based on window x size. */
	public static float tdx(Number valIn) {
		float midX = (float) QoT.getWindowSize().getWidth() / 2;
		return (float) (((valIn.floatValue() * QoT.getGameScale()) - midX) / midX);
	}
	
	/** 'to double for y' converts a given value into a corresponding double value between -1.0f and 1.0f based on window y size. */
	public static float tdy(Number valIn) {
		float midY = (float) QoT.getWindowSize().getHeight() / 2;
		return (float) ((midY - (valIn.floatValue() * QoT.getGameScale())) / midY);
	}
	
	/** Binds a 2d texture. */
	public static void bindTexture(GameTexture in) {
		QoT.getTextureSystem().bind(in);
	}
	
	//-----------------------------
	// Private GL Drawing Settings
	//-----------------------------
	
	/** Starts drawing a shape with a given draw mode. */
	private static void begin(GLModes mode) {
		switch (mode) {
		case POINTS: GL11.glBegin(GL11.GL_POINTS); break;
		case LINES: GL11.glBegin(GL11.GL_LINES); break;
		case LINE_STRIP: GL11.glBegin(GL11.GL_LINE_STRIP); break;
		case LINE_LOOP: GL11.glBegin(GL11.GL_LINE_LOOP); break;
		case TRIANGLES: GL11.glBegin(GL11.GL_TRIANGLES); break;
		case TRIANGLE_STRIP: GL11.glBegin(GL11.GL_TRIANGLE_STRIP); break;
		case TRIANGLE_FAN: GL11.glBegin(GL11.GL_TRIANGLE_FAN); break;
		case QUADS: GL11.glBegin(GL11.GL_QUADS); break;
		case QUAD_STRIP: GL11.glBegin(GL11.GL_QUAD_STRIP); break;
		}
	}
	
	
	/** Enables GL color blending. Essentially allows transparent colors and textures to be blended with the color behind them. */
	private static void blendOn() { GLSettings.blendFunc(); GLSettings.enableBlend(); }
	/** Disables GL color blending. */
	private static void blendOff() { GLSettings.disableBlend(); }
	/** Enables GL alpha sampling. Enables colors and textures to have transparency when they are drawn. */
	private static void alphaOn() { GLSettings.enableAlpha(); }
	/** Disables GL alpha sampling. */
	private static void alphaOff() { GLSettings.disableAlpha(); }
	
	
	/** Declares a new vertex from 2 points. */
	private static void vertex(Number a, Number b) { vertex(a.doubleValue(), b.doubleValue(), 0); }
	/** Declares a new vertex from 3 points. */
	private static void vertex(Number a, Number b, Number c) { GL11.glVertex3d(a.doubleValue(), b.doubleValue(), c.doubleValue()); }
	
	private static void tv(boolean f, Rotation r, double sx, double sy, double ex, double ey) {
		switch (r) {
		case UP:
			GL11.glTexCoord2d(f ? 1 : 0, 0);
			vertex(tdx(sx), tdy(sy));
			GL11.glTexCoord2d(f ? 0 : 1, 0);
			vertex(tdx(ex), tdy(sy));
			GL11.glTexCoord2d(f ? 0 : 1, 1);
			vertex(tdx(ex), tdy(ey));
			GL11.glTexCoord2d(f ? 1 : 0, 1);
			vertex(tdx(sx), tdy(ey));
			break;
		case LEFT:
			GL11.glTexCoord2d(f ? 1 : 0, 1);
			vertex(tdx(sx), tdy(sy));
			GL11.glTexCoord2d(f ? 1 : 0, 0);
			vertex(tdx(ex), tdy(sy));
			GL11.glTexCoord2d(f ? 0 : 1, 0);
			vertex(tdx(ex), tdy(ey));
			GL11.glTexCoord2d(f ? 0 : 1, 1);
			vertex(tdx(sx), tdy(ey));
			break;
		case RIGHT:
			GL11.glTexCoord2d(f ? 0 : 1, 0);
			vertex(tdx(sx), tdy(sy));
			GL11.glTexCoord2d(f ? 0 : 1, 1);
			vertex(tdx(ex), tdy(sy));
			GL11.glTexCoord2d(f ? 1 : 0, 1);
			vertex(tdx(ex), tdy(ey));
			GL11.glTexCoord2d(f ? 1 : 0, 0);
			vertex(tdx(sx), tdy(ey));
			break;
		case DOWN:
			GL11.glTexCoord2d(f ? 0 : 1, 1);
			vertex(tdx(sx), tdy(sy));
			GL11.glTexCoord2d(f ? 1 : 0, 1);
			vertex(tdx(ex), tdy(sy));
			GL11.glTexCoord2d(f ? 1 : 0, 0);
			vertex(tdx(ex), tdy(ey));
			GL11.glTexCoord2d(f ? 0 : 1, 0);
			vertex(tdx(sx), tdy(ey));
			break;
		}
	}
	
	
	/** Sets the width of the a drawn line when drawing lines. */
	private static void lineWidth(int widthIn) { GL11.glLineWidth(widthIn); }
	
	
	/** Concludes the drawing. */
	private static void draw() { GL11.glEnd(); }
	
}
