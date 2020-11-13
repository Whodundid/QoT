package openGL_Util;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

//final to prevent extension
public final class GLSettings {
	
	//-----------------------
	//GLSettings Constructors
	//-----------------------
	
	//private to prevent instantiation
	private GLSettings() {}
	
	//-------------------------
	//GLSettings Static Methods
	//-------------------------
	
	public static void enableBlend() { glEnable(GL_BLEND); }
	public static void enableAlpha() { glEnable(GL_ALPHA); }
	
	public static void disableBlend() { glDisable(GL_BLEND); }
	public static void disableAlpha() { glDisable(GL_ALPHA); }
	
	public static void pushMatrix() { glPushMatrix(); }
	public static void popMatrix() { glPopMatrix(); }
	
	public static void color(float val) { color(val, val, val); }
	public static void colorA(float val) { color(val, val, val, val); }
	public static void color(float r, float g, float b) { glColor3f(r, g, b); }
	public static void color(float r, float g, float b, float a) { glColor4f(r, g, b, a); }
	
	public static void blendFunc() { blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); }
	public static void blendFunc(int source, int destination) { glBlendFunc(source, destination); }
	public static void blendSeparate() { blendSeparate(770, 771, 1, 0); }
	public static void blendSeparate(int srcRGB, int destRGB, int srcA, int destA) { glBlendFuncSeparate(srcRGB, destRGB, srcA, destA); }
	
	public static void shade(int mode) { glShadeModel(mode); }

	public static void fullBright() { colorA(1.0f); }
	public static void setLight(float val) { colorA(val); }
	
	public static void enableTexture() {
		blendFunc();
		glDisable(GL_CULL_FACE);
		glDisable(GL_DEPTH_TEST);
	}
	
	public static void disableTexture() {
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
	}
	
}