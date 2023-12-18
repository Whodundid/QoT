package envision.engine.rendering;

import java.util.Objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import envision.Envision;
import envision.engine.rendering.fontRenderer.EStringOutputFormatter;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.colors.EColors;
import eutil.math.dimensions.Dimension_d;
import eutil.math.dimensions.Dimension_l;
import eutil.misc.Rotation;

/** The base underlying object that all window objects are drawn from. */
public interface GLObject {
	
	//--------------------------------------
	// Public Static String Drawing Methods
	//--------------------------------------
	
	/** Draws the toString representation of an object at the specified position. */
	static double drawString(Object o, double x, double y) { return drawString(toStr(o), x, y, EColors.white.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawString(Object o, double x, double y, EColors colorIn) { return drawString(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawCenteredString(Object o, double x, double y, EColors colorIn) { return drawCenteredString(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawStringWithShadow(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawCenteredStringWithShadow(Object o, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(toStr(o), x, y, colorIn.intVal); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	static double drawString(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, false, false); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawCenteredString(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringWithShadow(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawCenteredStringWithShadow(Object o, double x, double y, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, 1.0, 1.0, color, true, true); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	static double drawString(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, false, false); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawCenteredString(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, true, false); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringWithShadow(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, false, true); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawCenteredStringWithShadow(Object o, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(toStr(o), x, y, scaleX, scaleY, color, true, true); }
	
	
	/** Draws a String at the specified position. */
	static double drawString(String text, double x, double y) { return drawString(text, x, y, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	static double drawString(String text, double x, double y, EColors colorIn) { return drawString(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawCenteredString(String text, double x, double y, EColors colorIn) { return drawCenteredString(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringWithShadow(String text, double x, double y, EColors colorIn) { return drawStringWithShadow(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawCenteredStringWithShadow(String text, double x, double y, EColors colorIn) { return drawCenteredStringWithShadow(text, x, y, colorIn.intVal); }
	
	
	/** Draws a String at the specified position. */
	static double drawString(String text, double x, double y, double scaleX, double scaleY) { return drawString(text, x, y, scaleX, scaleY, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	static double drawString(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawString(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawCenteredString(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawCenteredString(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringWithShadow(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringWithShadow(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawCenteredStringWithShadow(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawCenteredStringWithShadow(text, x, y, scaleX, scaleY, colorIn.intVal); }
	
	
	/** Draws a String at the specified position. */
	static double drawString(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, false, false); }
	/** Draws a String at the specified position. */
	static double drawCenteredString(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, true, false); }
	/** Draws a String at the specified position. */
	static double drawStringWithShadow(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, false, true); }
	/** Draws a String at the specified position. */
	static double drawCenteredStringWithShadow(String text, double x, double y, int color) { return EStringOutputFormatter.drawString(text, x, y, 1.0, 1.0, color, true, true); }
	
	
	/** Draws a String at the specified position. */
	static double drawString(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, false, false); }
	/** Draws a String at the specified position. */
	static double drawCenteredString(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, true, false); }
	/** Draws a String at the specified position. */
	static double drawStringWithShadow(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, false, true); }
	/** Draws a String at the specified position. */
	static double drawCenteredStringWithShadow(String text, double x, double y, double scaleX, double scaleY, int color) { return EStringOutputFormatter.drawString(text, x, y, scaleX, scaleY, color, true, true); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringC(Object o, double x, double y) { return drawStringC(toStr(o), x, y, EColors.white); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringC(Object o, double x, double y, EColors colorIn) { return drawStringC(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringS(Object o, double x, double y, EColors colorIn) { return drawStringS(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringCS(Object o, double x, double y, EColors colorIn) { return drawStringCS(toStr(o), x, y, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringC(Object o, double x, double y, int color) { return drawCenteredString(toStr(o), x, y, color); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringS(Object o, double x, double y, int color) { return drawStringWithShadow(toStr(o), x, y, color); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringCS(Object o, double x, double y, int color) { return drawCenteredStringWithShadow(toStr(o), x, y, color); }
	
	
	/** Draws a String at the specified position. */
	static double drawStringC(String text, double x, double y, EColors colorIn) { return drawStringC(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringS(String text, double x, double y, EColors colorIn) { return drawStringS(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringCS(String text, double x, double y, EColors colorIn) { return drawStringCS(text, x, y, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringC(String text, double x, double y, int color) { return drawCenteredString(text, x, y, color); }
	/** Draws a String at the specified position. */
	static double drawStringS(String text, double x, double y, int color) { return drawStringWithShadow(text, x, y, color); }
	/** Draws a String at the specified position. */
	static double drawStringCS(String text, double x, double y, int color) { return drawCenteredStringWithShadow(text, x, y, color); }
	
	
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringC(Object o, double x, double y, double scaleX, double scaleY) { return drawStringC(toStr(o), x, y, scaleX, scaleY, EColors.white); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringC(Object o, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringC(toStr(o), x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringS(Object o, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringS(toStr(o), x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringCS(Object o, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringCS(toStr(o), x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringC(Object o, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredString(toStr(o), x, y, scaleX, scaleY, color); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringS(Object o, double x, double y, double scaleX, double scaleY, int color) { return drawStringWithShadow(toStr(o), x, y, scaleX, scaleY, color); }
	/** Draws the toString representation of an object at the specified position. */
	static double drawStringCS(Object o, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredStringWithShadow(toStr(o), x, y, scaleX, scaleY, color); }
	
	
	/** Draws a String at the specified position. */
	static double drawStringC(String text, double x, double y, double scaleX, double scaleY) { return drawStringC(text, x, y, scaleX, scaleY, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringS(String text, double x, double y, double scaleX, double scaleY) { return drawStringS(text, x, y, scaleX, scaleY, EColors.white.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringC(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringC(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringS(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringS(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringCS(String text, double x, double y, double scaleX, double scaleY, EColors colorIn) { return drawStringCS(text, x, y, scaleX, scaleY, colorIn.intVal); }
	/** Draws a String at the specified position. */
	static double drawStringC(String text, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredString(text, x, y, scaleX, scaleY, color); }
	/** Draws a String at the specified position. */
	static double drawStringS(String text, double x, double y, double scaleX, double scaleY, int color) { return drawStringWithShadow(text, x, y, scaleX, scaleY, color); }
	/** Draws a String at the specified position. */
	static double drawStringCS(String text, double x, double y, double scaleX, double scaleY, int color) { return drawCenteredStringWithShadow(text, x, y, scaleX, scaleY, color); }
	
	/** Wrapper for EStringBuilder's getStringWidth. */
	static double getStringWidth(String text) { return EStringOutputFormatter.getStringWidth(text); }
	
	/** Returns a 'toString' representation of the given object. Accounts for null objets as well. */
	private static String toStr(Object in) { return (in != null) ? in.toString() : "null"; }
	
	//-------------------------------------
	// Public Static Shape Drawing Methods
	//-------------------------------------
	
	/** Draws a line from point a to b with a thickness of 2. */
	static void drawLine(double startX, double startY, double endX, double endY, EColors color) { drawLine(startX, startY, endX, endY, 2, color.intVal); }
	/** Draws a line from point a to b with a thickness of 2. */
	static void drawLine(double startX, double startY, double endX, double endY, int color) { drawLine(startX, startY, endX, endY, 2, color); }
	/** Draws a line from point a to b with a variable thickness. */
	static void drawLine(double startX, double startY, double endX, double endY, int thickness, EColors color) { drawLine(startX, startY, endX, endY, thickness, color.intVal); }
	/** Draws a line from point a to b with a variable thickness. */
	static void drawLine(double startX, double startY, double endX, double endY, int thickness, int color) {
		//set drawing settings
		//blendOn();
		//alphaOn();
		//GLSettings.blendSeparate();
		setGLColor(color);
		GL11.glLineWidth(thickness);
		
		//set mode
		begin(GLModes.LINES);
		
		//define points
		vertex(tdx(startX), tdy(startY), 0);
		vertex(tdx(endX), tdy(endY), 0);
		
		//draw
		draw();
	}
	
	
	/** Draws a horizontal line with a thickness of 1. */
	static void drawHorizontalLine(double startX, double endX, double y, int thickness, EColors color) { drawHorizontalLine(startX, endX, y, thickness, color.intVal); }
	/** Draws a horizontal line with a thickness of 1. */
	static void drawHorizontalLine(double startX, double endX, double y, int thickness, int color) {
		//correct dimensions (if necessary)
		if (endX < startX) {
			double i = startX;
			startX = endX;
			endX = i;
		}
		
		//draw
		drawRect(startX, y, endX + thickness, y + thickness, color);
	}
	
	
	/** Draws a vertical line with a thickness of 1. */
	static void drawVerticalLine(double x, double startY, double endY, int thickness, EColors color) { drawVerticalLine(x, startY, endY, thickness, color.intVal); }
	/** Draws a vertical line with a thickness of 1. */
	static void drawVerticalLine(double x, double startY, double endY, int thickness, int color) {
		//correct dimensions (if necessary)
		if (endY < startY) {
			double i = startY;
			startY = endY;
			endY = i;
		}
		
		//draw
		drawRect(x, startY + thickness, x + thickness, endY, color);
	}
	
	
	/** Draws a hollow circle expanding out from the center. */
	static void drawCircle(double posX, double posY, double radius, int detail, EColors color) { drawCircle(posX, posY, radius, detail, color.intVal); }
	/** Draws a hollow circle expanding out from the center. */
	static void drawCircle(double posX, double posY, double radius, int detail, int color) { drawEllipse(posX, posY, radius, radius, detail, color); }
	
	
	/** Draws a solid circle expanding out from the center. */
	static void drawFilledCircle(double posX, double posY, double radius, int detail, EColors color) { drawFilledCircle(posX, posY, radius, detail, color.intVal); }
	/** Draws a solid circle expanding out from the center. */
	static void drawFilledCircle(double posX, double posY, double radius, int detail, int color) { drawFilledEllipse(posX, posY, radius, radius, detail, color); }
	
	
	/** Draws a hollow ellipse expanding out from the center. */
	static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	/** Draws a hollow ellipse expanding out from the center. */
	static void drawEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		
		//set drawing settings
		blendOn();
		alphaOn();
		GLSettings.blendSeparate();
		
		//set mode
		begin(GLModes.LINE_LOOP);

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
	static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, EColors color) { drawFilledEllipse(posX, posY, radiusX, radiusY, detail, color.intVal); }
	/** Draws a solid ellipse expanding out from the center. */
	static void drawFilledEllipse(double posX, double posY, double radiusX, double radiusY, int detail, int color) {
		//set drawing settings
		blendOn();
		alphaOn();
		GLSettings.blendSeparate();
		setGLColor(color);
		
		//set mode
		begin(GLModes.TRIANGLE_FAN);

		//define points
		vertex(tdx(posX), tdy(posY));
		
		for (int i = 0; i < detail + 1; i++) {
			double theta = 2.0f * Math.PI * i / detail;
			double x = radiusX * Math.cos(theta);
			double y = radiusY * Math.sin(theta);
			vertex(tdx(x + posX), tdy(y + posY));
		}
		
		//draw
		draw();
	}
	
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_l dims, EColors color) { drawRect(dims, color.intVal, 0); }
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_l dims, int color) { drawRect(dims, color, 0); }
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_l dims, EColors color, int offset) { drawRect(dims, color.intVal, offset); }
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_l dims, int color, int offset) {
		drawRect((double) dims.startX + offset,
				 (double) dims.startY + offset,
				 (double) dims.endX - offset,
				 (double) dims.endY - offset,
				 color);
	}
	
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_d dims, EColors color) { drawRect(dims, color.intVal, 0); }
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_d dims, int color) { drawRect(dims, color, 0); }
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_d dims, EColors color, int offset) { drawRect(dims, color.intVal, offset); }
	/** Draws a filled rectangle within the given dimension bounds. */
	static void drawRect(Dimension_d dims, int color, int offset) {
		drawRect(dims.startX + offset,
				 dims.startY + offset,
				 dims.endX - offset,
				 dims.endY - offset,
				 color);
	}
	
	/** Draws a filled rectangle. */
	static void drawRect(double left, double top, double right, double bottom, EColors colorIn) {
		drawRect(left, top, right, bottom, colorIn.intVal);
	}
	/** Draws a filled rectangle. */
	static void drawRect(double left, double top, double right, double bottom, int color) {
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
		
		//set drawing settings
		blendOn();
		alphaOn();
		GLSettings.blendSeparate();
		setGLColor(color);
		
		//Shaders.fixed.enableAttribs();
		//Shaders.fixed.bind();
		
		//set mode
		begin(GLModes.QUADS);
		
		double sx = tdx(left);
		double sy = tdy(top);
		double ex = tdx(right);
		double ey = tdy(bottom);
		
		//define points
		vertex(sx, ey);
		vertex(ex, ey);
		vertex(ex, sy);
		vertex(sx, sy);
		
		//draw
		draw();
		
		//Shaders.fixed.unbind();
		//Shaders.fixed.disableAttribs();
	}
	
	
	/** Draws a hollow rectangle with variable line width. */
	static void drawHRect(double left, double top, double right, double bottom, double borderWidth, EColors colorIn) { drawHRect(left, top, right, bottom, borderWidth, colorIn.intVal); }
	/** Draws a hollow rectangle with variable line width. */
	static void drawHRect(double left, double top, double right, double bottom, double borderWidth, int color) {
		drawRect(left, top, left + borderWidth, bottom, color); //left
		drawRect(left, top, right, top + borderWidth, color); //top
		drawRect(right - borderWidth, top, right, bottom, color); //right
		drawRect(left, bottom - borderWidth, right, bottom, color); //bottom
	}
	
	/** Draws a texture with the given dimensions. */
	//static void drawTexture(double x, double y, double w, double h) { drawTexture(TextureSystem.getInstance().getBoundTexture(), x, y, w, h, false, Rotation.UP, 0xffffffff); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h) { drawTexture(tex, x, y, w, h, false, Rotation.UP, 0xffffffff); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h, int color) { drawTexture(tex, x, y, w, h, false, Rotation.UP, color); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h, Rotation rotation) { drawTexture(tex, x, y, w, h, false, rotation, 0xffffffff); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip) { drawTexture(tex, x, y, w, h, flip, Rotation.UP, 0xffffffff); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, int color) { drawTexture(tex, x, y, w, h, flip, Rotation.UP, color); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation) { drawTexture(tex, x, y, w, h, flip, rotation, 0xffffffff); }
	static void drawTexture(GameTexture tex, double x, double y, double w, double h, boolean flip, Rotation rotation, int color) {
		//Ensure the texture can actually be drawn
		if (tex != null && tex.hasBeenRegistered()) {
			
			alphaOn();
			blendOn();
			setGLColor(color);
			GLSettings.enableTexture();
			bindTexture(tex);
			
			begin(GLModes.QUADS);
			
			//as far as I am concerned, this value might as well have come from the ether..
			//However, it's necessary in order to prevent weird texture artifacts from being
			//drawn at the bottom of the texture itself
			//
			//modified by adding 0.1 to original value
			//final double adjustment = 0.1019836425781818436357012429;
			final double adjustment = +0.05f;
			
			double sx = tdx(x);
			double sy = tdy(y);
			double ex = tdx(x + w);
			double ey = tdy(y + h + adjustment);
	
			tv(flip, rotation, sx, sy, ex, ey);
			
			draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	static void drawTexture(GameTexture imageIn, double x, double y, double w, double h, double tX, double tY, double tW, double tH) {
		drawTexture(imageIn, x, y, w, h, tX, tY, tW, tH, 0xffffffff);
	}
	static void drawTexture(GameTexture imageIn, double x, double y, double w, double h, double tX, double tY, double tW, double tH, EColors color) {
		drawTexture(imageIn, x, y, w, h, tX, tY, tW, tH, color.intVal);
	}
	static void drawTexture(GameTexture imageIn, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		if (imageIn != null) {
			
			double xVal = tX / (double) imageIn.getWidth();
			double yVal = tY / (double) imageIn.getHeight();
			double wVal = tW / (double) imageIn.getWidth();
			double hVal = tH / (double) imageIn.getHeight();
			
			//System.out.println(xVal + " " + yVal + " " + wVal + " " + hVal);
			
			//garbage duct-tape font bottom fix
			hVal -= 0.005;
			
			alphaOn();
			blendOn();
			setGLColor(color);
			GLSettings.enableTexture();
			bindTexture(imageIn);
			
			begin(GLModes.QUADS);
			
			double sx = tdx(x);
			double sy = tdy(y);
			double ex = tdx(x + w);
			double ey = tdy(y + h);
			
			texCoord(sx, ey, 	xVal, 			yVal + hVal);
			texCoord(ex, ey, 	xVal + wVal, 	yVal + hVal);
			texCoord(ex, sy, 	xVal + wVal, 	yVal);
			texCoord(sx, sy, 	xVal, 			yVal);
			
			draw();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
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
	
	static PolyTexture drawTexturePoly(GameTexture t) {
		Objects.requireNonNull(t);
		return new PolyTexture(t);
	}
	
	static void drawTexturePoly(GameTexture t, double[] points, boolean flipped, Rotation rot, EColors color) {
		if (t == null || points.length != 8) return;
		drawTexturePoly(t, points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7], flipped, rot, color.intVal);
	}
	
	static void drawTexturePoly(GameTexture t, double[] points, boolean flipped, Rotation rot, int color) {
		if (t == null || points.length != 8) return;
		drawTexturePoly(t, points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7], flipped, rot, color);
	}
	
	static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, false, Rotation.UP, 0xffffffff);
	}
	
	static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, EColors color) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, false, Rotation.UP, color.intVal);
	}
	
	static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, int color) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, false, Rotation.UP, color);
	}
	
	static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, boolean flipped, Rotation rot, EColors color) {
		drawTexturePoly(t, x1, y1, x2, y2, x3, y3, x4, y4, flipped, rot, color.intVal);
	}
	
	/** Draws a texture but with individually specified vertices, so not necessarily as a rect. */
	static void drawTexturePoly(GameTexture t, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4, boolean flipped, Rotation rot, int color) {
		if (t == null) return;
		
		alphaOn();
		blendOn();
		setGLColor(color);
		GLSettings.enableTexture();
		bindTexture(t);
		
		begin(GLModes.QUADS);
		
		x1 = tdx(x1);	y1 = tdy(y1);
		x2 = tdx(x2);	y2 = tdy(y2);
		x3 = tdx(x3);	y3 = tdy(y3);
		x4 = tdx(x4);	y4 = tdy(y3);
		
		switch (rot) {
		case UP:
			texCoord(x1, y1, flipped ? 1 : 0, 1);
			texCoord(x2, y2, flipped ? 0 : 1, 1);
			texCoord(x3, y3, flipped ? 0 : 1, 0);
			texCoord(x4, y4, flipped ? 1 : 0, 0);
			break;
		case LEFT:
			texCoord(x1, y1, flipped ? 1 : 0, 0);
			texCoord(x2, y2, flipped ? 1 : 0, 1);
			texCoord(x3, y3, flipped ? 0 : 1, 1);
			texCoord(x4, y4, flipped ? 0 : 1, 0);
			break;
		case RIGHT:
			texCoord(x1, y1, flipped ? 0 : 1, 1);
			texCoord(x2, y2, flipped ? 0 : 1, 0);
			texCoord(x3, y3, flipped ? 1 : 0, 0);
			texCoord(x4, y4, flipped ? 1 : 0, 1);
			break;
		case DOWN:
			texCoord(x1, y1, flipped ? 1 : 0, 0);
			texCoord(x2, y2, flipped ? 0 : 1, 0);
			texCoord(x3, y3, flipped ? 0 : 1, 1);
			texCoord(x4, y4, flipped ? 1 : 0, 1);
			break;
		}
		
		draw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
	
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	static void scissor(int startX, int startY, int endX, int endY) { scissor((double) startX, (double) startY, (double) endX, (double) endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	static void scissor(double startX, double startY, double endX, double endY) { scissor(startX, startY, endX, endY, false); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	static void scissor(int startX, int startY, int endX, int endY, boolean useGlY) { scissor((double) startX, (double) startY, (double) endX, (double) endY, useGlY); }
	/** Performs a scissoring on the specified region. IMPORTANT: ALWAYS CALL 'endScissor' OR gl11.disable(gl_scissor) AFTER THIS TO PREVENT RENDERING ERRORS!*/
	static void scissor(double startX, double startY, double endX, double endY, boolean useGlY) {
		GLSettings.enableScissor();
		//Envision.renderEngine.getBatchManager().pushLayerIndex();
		double scale = Envision.getGameScale();
		int w = (int) ((endX - startX) * scale);
		int h = (int) ((endY - startY) * scale);
		int x = (int) (startX * scale);
		int y = useGlY ? (int) (startY * scale) : (int) (Envision.getHeight() - (startY * scale) - h);
		if (w >= 0 && h >= 0) GL11.glScissor(x, y, w, h);
		GLSettings.setScissorBounds(x, y, w, h);
	}
	
	/** Stops scissoring an area. */
	static void endScissor() {
		//Envision.renderEngine.getBatchManager().popLayerIndex();
		GLSettings.disableScissor();
	}
	
	
	//----------------------------
	// Public GL Drawing Settings
	//----------------------------
	
	
	/** Breaks down an int representation of a color into the appropriate (r, g, b, a) OpenGL float values. */
	static void setGLColor(int colorIn) {
		float f = (colorIn >> 16 & 255) / 255.0F;
		float f1 = (colorIn >> 8 & 255) / 255.0F;
		float f2 = (colorIn & 255) / 255.0F;
		float f3 = (colorIn >> 24 & 255) / 255.0F;
		GLSettings.color(f, f1, f2, f3);
	}
	
	/** 'to double for x' converts a given value into a corresponding double value between -1.0f and 1.0f based on window x size. */
	static double tdx(double valIn) {
		double midX = Envision.getWidth() * 0.5;
		return (valIn * Envision.getGameScale() - midX) / midX;
	}
	
	/** 'to double for y' converts a given value into a corresponding double value between -1.0f and 1.0f based on window y size. */
	static double tdy(double valIn) {
		double midY = Envision.getHeight() * 0.5;
		return (midY - (valIn * Envision.getGameScale())) / midY;
	}
	
	/** Binds a 2d texture. */
	private static void bindTexture(GameTexture in) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		TextureSystem.getInstance().bind(in);
	}
	
	//-----------------------------
	// Private GL Drawing Settings
	//-----------------------------
	
	/** Starts drawing a shape with a given draw mode. */
	private static void begin(GLModes mode) {
		switch (mode) {
		case POINTS: 			GL11.glBegin(GL11.GL_POINTS); 			break;
		case LINES: 			GL11.glBegin(GL11.GL_LINES); 			break;
		case LINE_STRIP: 		GL11.glBegin(GL11.GL_LINE_STRIP);		break;
		case LINE_LOOP: 		GL11.glBegin(GL11.GL_LINE_LOOP); 		break;
		case TRIANGLES: 		GL11.glBegin(GL11.GL_TRIANGLES); 		break;
		case TRIANGLE_STRIP:	GL11.glBegin(GL11.GL_TRIANGLE_STRIP); 	break;
		case TRIANGLE_FAN: 		GL11.glBegin(GL11.GL_TRIANGLE_FAN); 	break;
		case QUADS: 			GL11.glBegin(GL11.GL_QUADS); 			break;
		case QUAD_STRIP: 		GL11.glBegin(GL11.GL_QUAD_STRIP); 		break;
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
	private static void vertex(double a, double b) { vertex(a, b, 0); }
	/** Declares a new vertex from 3 points. */
	private static void vertex(double a, double b, double c) { GL11.glVertex3d(a, b, c); }
	
	private static void tv(boolean f, Rotation r, double sx, double sy, double ex, double ey) {
		switch (r) {
		case UP:
			texCoord(sx, sy, f ? 1 : 0, 0);
			texCoord(ex, sy, f ? 0 : 1, 0);
			texCoord(ex, ey, f ? 0 : 1, 1);
			texCoord(sx, ey, f ? 1 : 0, 1);
			break;
		case LEFT:
			texCoord(sx, sy, f ? 0 : 1, 0);
			texCoord(ex, sy, f ? 0 : 1, 1);
			texCoord(ex, ey, f ? 1 : 0, 1);
			texCoord(sx, ey, f ? 1 : 0, 0);
			break;
		case RIGHT:
			texCoord(sx, sy, f ? 0 : 1, 1);
			texCoord(ex, sy, f ? 0 : 1, 0);
			texCoord(ex, ey, f ? 1 : 0, 0);
			texCoord(sx, ey, f ? 1 : 0, 1);
			break;
		case DOWN:
			texCoord(sx, sy, f ? 0 : 1, 1);
			texCoord(ex, sy, f ? 1 : 0, 1);
			texCoord(ex, ey, f ? 1 : 0, 0);
			texCoord(sx, ey, f ? 0 : 1, 0);
			break;
		}
	}
	
	private static void texCoord(double x, double y, double tX, double tY) {
		GL11.glTexCoord2d(tX, tY);
		vertex(x, y);
	}
	
	/** Sets the width of the a drawn line when drawing lines. */
	private static void lineWidth(int widthIn) { GL11.glLineWidth(widthIn); }
	
	/** Concludes the drawing. */
	private static void draw() {
		GL11.glEnd();
	}
	
}
