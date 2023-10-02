package envision.engine.rendering;

import java.util.Objects;

import org.lwjgl.opengl.GL11;

import envision.Envision;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.rendering.textureSystem.GameTexture;
import eutil.colors.EColors;
import eutil.math.dimensions.IDimension;
import eutil.misc.Rotation;

public class RenderingManager {
	
	//========================
	// String Drawing Methods
	//========================
	
	/** Draws a String at the specified position. */
	public static double drawString(Object text, double x, double y) { return drawString_i(text, x, y, 1.0, 1.0, 0xffffffff, false, false); }
	/** Draws a String at the specified position. */
	public static double drawString(Object text, double x, double y, EColors color) { return drawString_i(text, x, y, 1.0, 1.0, color.intVal, false, false); }
	/** Draws a String at the specified position. */
	public static double drawString(Object text, double x, double y, int color) { return drawString_i(text, x, y, 1.0, 1.0, color, false, false); }
	/** Draws a String at the specified position. */
	public static double drawString(Object text, double x, double y, double scaleX, double scaleY) { return drawString_i(text, x, y, scaleX, scaleY, 0xffffffff, false, false); }
	/** Draws a String at the specified position. */
	public static double drawString(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, false, false); }
	/** Draws a String at the specified position. */
	public static double drawString(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, false, false); }
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object text, double x, double y) { return drawString_i(text, x, y, 1.0, 1.0, 0xffffffff, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object text, double x, double y, EColors color) { return drawString_i(text, x, y, 1.0, 1.0, color.intVal, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object text, double x, double y, int color) { return drawString_i(text, x, y, 1.0, 1.0, color, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object text, double x, double y, double scaleX, double scaleY) { return drawString_i(text, x, y, scaleX, scaleY, 0xffffffff, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringC(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, true, false); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, true, false); }
	/** Draws a String at the specified position. */
	public static double drawCenteredString(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, true, false); }
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object text, double x, double y, EColors color) { return drawString_i(text, x, y, 1.0, 1.0, color.intVal, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringS(Object text, double x, double y, int color) { return drawString_i(text, x, y, 1.0, 1.0, color, false, true); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, false, true); }
	/** Draws a String at the specified position. */
	public static double drawStringWithShadow(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, false, true); }
	
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object text, double x, double y) { return drawString_i(text, x, y, 1.0, 1.0, 0xffffffff, true, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object text, double x, double y, EColors color) { return drawString_i(text, x, y, 1.0, 1.0, color.intVal, true, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object text, double x, double y, int color) { return drawString_i(text, x, y, 1.0, 1.0, color, true, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, true, true); }
	/** Draws the toString representation of an object at the specified position. */
	public static double drawStringCS(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, true, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(Object text, double x, double y, double scaleX, double scaleY, EColors color) { return drawString_i(text, x, y, scaleX, scaleY, color.intVal, true, true); }
	/** Draws a String at the specified position. */
	public static double drawCenteredStringWithShadow(Object text, double x, double y, double scaleX, double scaleY, int color) { return drawString_i(text, x, y, scaleX, scaleY, color, true, true); }
	
	
	private static double drawString_i(Object text, double x, double y, double scaleX, double scaleY, int color, boolean centered, boolean shadowed) {
		return EStringOutputFormatter.drawString(String.valueOf(text), x, y, scaleX, scaleY, color, centered, shadowed);
	}
	
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
		if (BatchManager.isEnabled()) BatchManager.drawLine(startX, startY, endX, endY, thickness, color);
		else GLObject.drawLine(startX, startY, endX, endY, thickness, color);
	}
	
	
	/** Draws a horizontal line with a thickness of 1. */
	public static void drawHorizontalLine(double startX, double endX, double y, int thickness, EColors color) { drawHorizontalLine(startX, endX, y, thickness, color.intVal); }
	/** Draws a horizontal line with a thickness of 1. */
	public static void drawHorizontalLine(double startX, double endX, double y, int thickness, int color) {
		//correct dimensions (if necessary)
		if (endX < startX) {
			double i = startX;
			startX = endX;
			endX = i;
		}
		
		if (BatchManager.isEnabled()) BatchManager.drawRect(startX, y, endX, y + thickness, color);
		else GLObject.drawHorizontalLine(startX, endX, y, thickness, color);
	}
	
	
	/** Draws a vertical line with a thickness of 1. */
	public static void drawVerticalLine(double x, double startY, double endY, int thickness, EColors color) { drawVerticalLine(x, startY, endY, thickness, color.intVal); }
	/** Draws a vertical line with a thickness of 1. */
	public static void drawVerticalLine(double x, double startY, double endY, int thickness, int color) {
		//correct dimensions (if necessary)
		if (endY < startY) {
			double i = startY;
			startY = endY;
			endY = i;
		}
		
		if (BatchManager.isEnabled()) BatchManager.drawRect(x, startY + thickness, x + thickness, endY, color);
		else GLObject.drawVerticalLine(x, startY, endY, thickness, color);
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
		if (BatchManager.isEnabled()) {}
		else GLObject.drawEllipse(posX, posY, radiusX, radiusY, detail, color);
	}
	
	
	/** Draws a solid ellipse expanding out from the center. */
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	/** Draws a solid ellipse expanding out from the center. */
	public static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		if (BatchManager.isEnabled()) {
			//BatchManager.drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color);
		}
		else GLObject.drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color);
	}
	
	/** Draws a filled rectangle within the given dimension bounds. */
	public static void drawRect(IDimension<?> dims, EColors color) { drawRect(dims, color.intVal, 0); }
	/** Draws a filled rectangle within the given dimension bounds. */
	public static void drawRect(IDimension<?> dims, int color) { drawRect(dims, color, 0); }
	/** Draws a filled rectangle within the given dimension bounds. */
	public static void drawRect(IDimension<?> dims, EColors color, int offset) { drawRect(dims, color.intVal, offset); }
	/** Draws a filled rectangle within the given dimension bounds. */
	public static void drawRect(IDimension<?> dims, int color, int offset) {
		drawRect(dims.startX().doubleValue() + offset,
				 dims.startY().doubleValue() + offset,
				 dims.endX().doubleValue() - offset,
				 dims.endY().doubleValue() - offset,
				 color);
	}
	
	/** Draws a filled rectangle. */
	public static void drawRect(double left, double top, double right, double bottom, EColors colorIn) {
		drawRect(left, top, right, bottom, colorIn.intVal);
	}
	/** Draws a filled rectangle. */
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		if (BatchManager.isEnabled()) BatchManager.drawRect(left, top, right, bottom, color);
		else GLObject.drawRect(left, top, right, bottom, color);
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
	
	/** Draws a filled rectangle within the given dimension bounds. */
    public static void drawHRect(IDimension<?> dims, EColors color) { drawHRect(dims, color.intVal, 1); }
    /** Draws a filled rectangle within the given dimension bounds. */
    public static void drawHRect(IDimension<?> dims, int color) { drawHRect(dims, color, 1); }
    /** Draws a filled rectangle within the given dimension bounds. */
    public static void drawHRect(IDimension<?> dims, EColors color, int borderWidth) { drawHRect(dims, color.intVal, borderWidth); }
    /** Draws a filled rectangle within the given dimension bounds. */
    public static void drawHRect(IDimension<?> dims, int color, int borderWidth) {
        drawHRect(dims.startX().doubleValue(),
        		dims.startY().doubleValue(),
        		dims.endX().doubleValue(),
        		dims.endY().doubleValue(),
        		borderWidth,
        		color);
    }
	
	/** Draws a texture with the given dimensions. */
	//static void drawTexture(double x, double y, double w, double h) { drawTexture(TextureSystem.getInstance().getBoundTexture(), x, y, w, h, false, Rotation.UP, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h) { drawTexture(tex, x, y, w, h, false, Rotation.UP, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, int color) { drawTexture(tex, x, y, w, h, false, Rotation.UP, color); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, Rotation rotation) { drawTexture(tex, x, y, w, h, false, rotation, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip) { drawTexture(tex, x, y, w, h, flip, Rotation.UP, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, int color) { drawTexture(tex, x, y, w, h, flip, Rotation.UP, color); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation) { drawTexture(tex, x, y, w, h, flip, rotation, 0xffffffff); }
	public static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation, int color) {
		//Ensure the texture can actually be drawn
		if (tex == null || !tex.hasBeenRegistered()) return;
		
		if (BatchManager.isEnabled()) BatchManager.drawTexture(tex, x, y, w, h, flip, rotation, color);
		else GLObject.drawTexture(tex, x, y, w, h, flip, rotation, color);
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH) {
		drawTexture(texture, x, y, w, h, tX, tY, tW, tH, 0xffffffff, false);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, boolean flip) {
		drawTexture(texture, x, y, w, h, tX, tY, tW, tH, 0xffffffff, flip);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color) {
		drawTexture(texture, x, y, w, h, tX, tY, tW, tH, color.intVal, false);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color, boolean flip) {
		drawTexture(texture, x, y, w, h, tX, tY, tW, tH, color.intVal, false);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		if (texture == null) return;
		
		if (BatchManager.isEnabled()) BatchManager.drawTexture(texture, x, y, w, h, tX, tY, tW, tH, color, flip);
		else GLObject.drawTexture(texture, x, y, w, h, tX, tY, tW, tH, color);
	}
	
	public static class PolyTexture {
		private final GameTexture tex;
		private final double[] points = new double[8];
		private boolean flipped = false;
		private Rotation rotation = Rotation.UP;
		private int color = 0xffffffff;
		private int setNum = 0;
		
		PolyTexture(GameTexture texIn) {
			tex = texIn;
		}
		
		PolyTexture point(double x, double y) {
			if (setNum >= 7) return this;
			points[setNum++] = x;
			points[setNum++] = y;
			return this;
		}
		
		PolyTexture flipped(boolean val) { flipped = val; return this; }
		PolyTexture rotation(Rotation rot) { rotation = rot; return this; }
		PolyTexture color(EColors colorIn) { color = colorIn.intVal; return this; }
		PolyTexture color(int colorIn) { color = colorIn; return this; }
		
		void draw() {
			drawTexturePoly(tex, points, flipped, rotation, color);
		}
	}
	
	public static PolyTexture drawTexturePoly(GameTexture t) {
		Objects.requireNonNull(t);
		return new PolyTexture(t);
	}
	
	public static void drawTexturePoly(GameTexture t, double[] points, boolean flipped, Rotation rot, EColors color) {
		if (t == null || points.length != 8) return;
		drawTexturePoly(t, points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7], flipped, rot, color.intVal);
	}
	
	public static void drawTexturePoly(GameTexture t, double[] points, boolean flipped, Rotation rot, int color) {
		if (t == null || points.length != 8) return;
		drawTexturePoly(t, points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7], flipped, rot, color);
	}
	
	public static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, false, Rotation.UP, 0xffffffff);
	}
	
	public static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, EColors color) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, false, Rotation.UP, color.intVal);
	}
	
	public static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, int color) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, false, Rotation.UP, color);
	}
	
	public static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, boolean flipped, Rotation rot, EColors color) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, flipped, rot, color.intVal);
	}
	
	/** Draws a texture but with individually specified vertices, so not necessarily as a rect. */
	public static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, boolean flipped, Rotation rot, int color) {
		if (t == null) return;
		
		
	}
	
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(int startX, int startY, int endX, int endY) { scissor((double) startX, (double) startY, (double) endX, (double) endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(double startX, double startY, double endX, double endY) { scissor(startX, startY, endX, endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(int startX, int startY, int endX, int endY, boolean useGlY) { scissor((double) startX, (double) startY, (double) endX, (double) endY, useGlY); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	public static void scissor(double startX, double startY, double endX, double endY, boolean useGlY) {
		GLSettings.enableScissor();
		double scale = Envision.getGameScale();
		int w = (int) ((endX - startX) * scale);
		int h = (int) ((endY - startY) * scale);
		int x = (int) (startX * scale);
		int y = useGlY ? (int) (startY * scale) : (int) (Envision.getHeight() - (startY * scale) - h);
		if (w >= 0 && h >= 0) GL11.glScissor(x, y, w, h);
		GLSettings.setScissorBounds(x, y, w, h);
	}
	
	/** Stops scissoring an area. */
	public static void endScissor() {
		GLSettings.disableScissor();
		if (BatchManager.isEnabled()) BatchManager.draw();
	}
	
}
