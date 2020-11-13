package openGL_Util;

import mainT.Game;
import org.lwjgl.opengl.GL11;
import util.renderUtil.EColors;

public class GLObject {
	
	protected double startX, endX;
	protected double startY, endY;
	protected double width, height;
	
	/** Draws a line from point a to b with a variable thickness. */
	public static void drawLine(double startX, double startY, double endX, double endY, EColors color) { drawLine(startX, startY, endX, endY, 2, color.intVal); }
	public static void drawLine(double startX, double startY, double endX, double endY, int color) { drawLine(startX, startY, endX, endY, 2, color); }
	public static void drawLine(double startX, double startY, double endX, double endY, int thickness, EColors color) { drawLine(startX, startY, endX, endY, thickness, color.intVal); }
	public static void drawLine(double startX, double startY, double endX, double endY, int thickness, int color) {
		GL11.glBegin(GL11.GL_LINES);
		GL11.glEnable(GL11.GL_BLEND);
		setGLColor(color);
		
	}
	
	/** Draws a horizontal line with a thickness of 1. */
	public static void drawHorizontalLine(double startX, double endX, double y, EColors color) { drawHorizontalLine(startX, endX, y, color.intVal); }
	public static void drawHorizontalLine(double startX, double endX, double y, int color) {
		if (endX < startX) {
			double i = startX;
			startX = endX;
			endX = i;
		}
		drawRect(startX, y, endX + 1, y + 1, color);
	}
	
	/** Draws a vertical line with a thickness of 1. */
	public static void drawVerticalLine(double x, double startY, double endY, EColors color) { drawVerticalLine(x, startY, endY, color.intVal); }
	public static void drawVerticalLine(double x, double startY, double endY, int color) {
		if (endY < startY) {
			double i = startY;
			startY = endY;
			endY = i;
		}
		drawRect(x, startY + 1, x + 1, endY, color);
	}
	
	/** Draws a hollow circle expanding out from the center. */
	public static void drawCircle(double posX, double posY, double radius, int detail, EColors color) { drawCircle(posX, posY, radius, detail, color.intVal); }
	public static void drawCircle(double posX, double posY, double radius, int detail, int color) { drawEllipse(posX, posY, radius, radius, detail, color); }
	
	/** Draws a solid circle expanding out from the center. */
	public static void drawFilledCircle(double posX, double posY, double radius, int detail, EColors color) { drawFilledCircle(posX, posY, radius, detail, color.intVal); }
	public static void drawFilledCircle(double posX, double posY, double radius, int detail, int color) { drawFilledEllipse(posX, posY, radius, radius, detail, color); }
	
	/** Draws a hollow ellipse expanding out from the center. */
	public static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	public static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		
	}
	
	/** Draws a solid ellipse expanding out from the center. */
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		
	}
	
	/** Draws a rectangle. */
	public static void drawRect(double left, double top, double right, double bottom, EColors colorIn) { drawRect(left, top, right, bottom, colorIn.intVal); }
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		
	}
	
	/** Draws a hollow rectangle. */
	public static void drawHRect(double left, double top, double right, double bottom, double borderWidth, EColors colorIn) { drawHRect(left, top, right, bottom, borderWidth, colorIn.intVal); }
	public static void drawHRect(double left, double top, double right, double bottom, double borderWidth, int color) {
		drawRect(left, top, left + borderWidth, bottom, color); //left
		drawRect(left, top, right, top + borderWidth, color); //top
		drawRect(right - borderWidth, top, right, bottom, color); //right
		drawRect(left, bottom - borderWidth, right, bottom, color); //bottom
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
	
	/** Breaks down an int representation of a color into the appropriate (r, g, b, a) OpenGL float values. */
	public static void setGLColor(int colorIn) {
		float f3 = (colorIn >> 24 & 255) / 255.0F;
        float f = (colorIn >> 16 & 255) / 255.0F;
        float f1 = (colorIn >> 8 & 255) / 255.0F;
        float f2 = (colorIn & 255) / 255.0F;
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
	
}
