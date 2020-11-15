package openGL_Util;

import main.Game;
import org.lwjgl.opengl.GL11;
import util.renderUtil.EColors;

/** The base underlying object that all window objects are drawn from. */
public abstract class GLObject {
	
	//------------------
	// Protected Fields
	//------------------
	
	
	// Drawing position
	
		/** The z layer for which this object is drawn at. By default it is 0. */
		protected double zLayer = 0;
		
	
	// Object Position Coordinates
	
		/** The left most position of this object. */
		protected double startX;
		/** The right most position of this object. */
		protected double endX;
		/** The top most position of this object. */
		protected double startY;
		/** The bottom most position of this object. */
		protected double endY;
		
	
	// Object Dimensions
	
		/** A measurement of the right most point minus the left most point. */
		protected double width;
		/** A measurement of the top most point minus the bottom most point. */
		protected double height;
		
		
	//------------------------
	// Protected Constructors
	//------------------------
	
	protected GLObject(int sX, int sY) {
		startX = sX;
		startY = sY;
	}
	
	protected GLObject(int sX, int sY, int eX, int eY) {
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;
		width = endX - startX;
		height = endY - startY;
	}
	
	//----------------
	// Public Methods
	//----------------
	
	public GLObject drawRect() { return drawRect(0, 0xffffffff); }
	public GLObject drawRect(EColors color) { return drawRect(0, color.intVal); }
	public GLObject drawRect(int offset) { return drawRect(offset, 0xffffffff); }
	public GLObject drawRect(int offset, EColors color) { return drawRect(offset, color.intVal); }
	public GLObject drawRect(int offset, int color) { drawRect(startX + offset, startY + offset, endX - offset, endY - offset, color); return this; }
	
	public GLObject drawHRect() { return drawHRect(0, 0xffffffff); }
	public GLObject drawHRect(EColors color) { return drawHRect(0, 1, color.intVal); }
	public GLObject drawHRect(EColors color, int thickness) { return drawHRect(0, thickness, color.intVal); }
	public GLObject drawHRect(int offset) { return drawHRect(offset, 0xffffffff); }
	public GLObject drawHRect(int offset, int thickness) { return drawHRect(offset, 0xffffffff); }
	public GLObject drawHRect(int offset, EColors color) { return drawHRect(offset, color.intVal); }
	public GLObject drawHRect(int offset, int thickness, EColors color) { drawHRect(offset, thickness, color.intVal); return this; }
	public GLObject drawHRect(int offset, int thickness, int color) { drawHRect(startX + offset, startY + offset, endX - offset, endY - offset, thickness, color); return this; }
	
	//-------------------------------
	// Public Static Drawing Methods
	//-------------------------------
	
	
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
		
		//post draw
		blendOff();
		alphaOff();
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
			vertex(tdx(x + posX), tdy(y + posY), 0.0d);
		}
		
		//draw
		draw();
		
		//post draw
		blendOff();
		alphaOff();
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
		vertex(posX, posY, 0.0d);
		
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radiusX * Math.cos(theta);
			double y = radiusY * Math.sin(theta);
			vertex(tdx(x + posX), tdy(y + posY), 0.0d);
			vertex(tdx(posX + prevX), tdy(posY + prevY), 0.0d);
			prevX = x;
			prevY = y;
		}
		
		//draw
		draw();
		
		//post draw
		blendOff();
		alphaOff();
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
		
		//post draw
		blendOff();
		alphaOff();
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
	
	/** DO NOT USE -- NOT FINISHED! */
	public static void drawTexture(double x, double y, double w, double h, GameTexture tex) {
		
		//Ensure the texture can actually be drawn
		if (tex != null && tex.hasBeenRegistered()) {
			
			float xVal = (float) x / (float) tex.getWidth();
			float yVal = (float) y / (float) tex.getHeight();
			float wVal = (float) w / (float) tex.getWidth();
			float hVal = (float) h / (float) tex.getHeight();
			
			alphaOn();
			blendOn();
			bindTexture(tex);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
		}
	}
	
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(int startX, int startY, int endX, int endY) { scissor(startX, startY, endX, endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(double startX, double startY, double endX, double endY) { scissor(startX, startY, endX, endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(int startX, int startY, int endX, int endY, boolean useGlY) { scissor(startX, startY, endX, endY, useGlY); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(double startX, double startY, double endX, double endY, boolean useGlY) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int w = ((int) endX - (int) startX);
		int h = ((int) endY - (int) startY);
		int x = (int) startX;
		int y = useGlY ? (int) startY : (Game.getInstance().getWindowSize().getHeight() - ((int) startY) - h);
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
		float midX = Game.getWindowSize().getWidth() / 2;
		return (valIn.floatValue() - midX) / midX;
	}
	
	/** 'to double for y' converts a given value into a corresponding double value between -1.0f and 1.0f based on window y size. */
	public static float tdy(Number valIn) {
		float midY = Game.getWindowSize().getHeight() / 2;
		return (midY - valIn.floatValue()) / midY;
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
	
	
	/** Sets the width of the a drawn line when drawing lines. */
	private static void lineWidth(int widthIn) { GL11.glLineWidth(widthIn); }
	
	
	/** Concludes the drawing. */
	private static void draw() { GL11.glEnd(); }
	
	
	/** Binds a 2d texture. */
	private static void bindTexture(GameTexture in) { GL11.glBindTexture(GL11.GL_TEXTURE_2D, in.getTextureID()); }
	
}
