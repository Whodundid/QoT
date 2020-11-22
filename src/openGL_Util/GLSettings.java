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
	
	public static void enableBlend() { enable(GL_BLEND); }
	public static void enableAlpha() { enable(GL_ALPHA); }
	
	public static void disableBlend() { disable(GL_BLEND); }
	public static void disableAlpha() { disable(GL_ALPHA); }
	
	public static void pushMatrix() { glPushMatrix(); }
	public static void popMatrix() { glPopMatrix(); }
	
	public static void clearDepth() { clear(GL_DEPTH_BUFFER_BIT); }
	public static void enableScissor() { enable(GL_SCISSOR_BIT); }
	public static void disableScissor() { disable(GL_SCISSOR_TEST); }
	
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
		enable(GL_TEXTURE_2D);
		disable(GL_CULL_FACE);
		disable(GL_DEPTH_TEST);
	}
	
	public static void disableTexture() {
		disable(GL_TEXTURE_2D);
		enable(GL_CULL_FACE);
		enable(GL_DEPTH_TEST);
	}
	
	public static void clear(int mask) { glClear(mask); }
	public static void enable(int target) { glEnable(target); }
	public static void disable(int target) { glDisable(target); }
	
}