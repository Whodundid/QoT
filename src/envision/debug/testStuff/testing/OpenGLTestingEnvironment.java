package envision.debug.testStuff.testing;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GL46;

import envision.engine.rendering.GLSettings;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.renderingAPI.RenderingContext;
import envision.engine.rendering.renderingAPI.error.ErrorReportingLevel;
import envision.engine.rendering.renderingAPI.error.IRendererErrorReceiver;
import envision.engine.rendering.renderingAPI.error.RendererErrorReporter;
import envision.engine.rendering.renderingAPI.opengl.OpenGLContext;
import envision.engine.rendering.shaders.Shaders;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import eutil.colors.EColors;
import eutil.datatypes.util.EList;
import eutil.math.dimensions.EDimension;
import qot.QoT;
import qot.assets.textures.GameTextures;
import qot.assets.textures.entity.EntityTextures;
import qot.launcher.LauncherLogger;
import qot.launcher.LauncherSettings;

public class OpenGLTestingEnvironment implements IRendererErrorReceiver {
	
	private static RenderingContext context;
	private static long windowHandle;
	private static int quadVAO, quadVBO, quadIBO;
	
	private static FontRenderer fontRenderer;
	private static TextureSystem textureSystem;
	
	private static EDimension windowSize;
	
	private static ByteBuffer byteBuffer;
	private static int nextVertOffset = 0;
	private static int totalElements = 0;
	private static int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
	private static EList<GameTexture> textures;
	
	//---------------------------------------------------------------------------------------------
	// OpenGL Back-end Setup
	//---------------------------------------------------------------------------------------------
	
	public static void runTestingEnvironment(LauncherSettings settings, long handle) {
		//Envision.createGame(new TestGame());
		//Envision.startGame(settings);
		new OpenGLTestingEnvironment(handle);
	}
	
	private OpenGLTestingEnvironment(long handle) {
		windowHandle = handle;
		
		RendererErrorReporter.setReceiver(this);
		context = new OpenGLContext(handle);
		
		// this is 2MB in bytes
		byteBuffer = ByteBuffer.allocateDirect(2097152).order(ByteOrder.nativeOrder());
		textures = EList.newList();

		onAttach();
		Shaders.init();
		textureSystem = TextureSystem.getInstance();
		fontRenderer = FontRenderer.getInstance();
		GameTextures.instance().onRegister(textureSystem);
		runTestLoop();
	}
	
	//------
	// Init
	//------
	
	public void onAttach() {
		context.init();
		
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		quadVAO = GL45.glCreateVertexArrays();
	}
	
	public void runTestLoop() {
		while (!glfwWindowShouldClose(windowHandle)) {
			glfwPollEvents();
			
			pre();
			render();
			post();
			
			context.swapBuffers();
		}
		
		glfwDestroyWindow(windowHandle);
		glfwTerminate();
	}
	
	private void pre() {}
	private void post() {}
	
	//=======================================================================================
	
	/** 'to double for x' converts a given value into a corresponding double value between -1.0f and 1.0f based on window x size. */
	public static float tdx(float valIn) {
		float midX = QoT.getWidth() * 0.5f;
		return (valIn * (float) QoT.getGameScale() - midX) / midX;
	}
	
	/** 'to double for y' converts a given value into a corresponding double value between -1.0f and 1.0f based on window y size. */
	public static float tdy(float valIn) {
		float midY = QoT.getHeight() * 0.5f;
		return (midY - (valIn * (float) QoT.getGameScale())) / midY;
	}
	
	//=======================================================================================
	
	private static void drawRect(double sx, double sy, double ex, double ey, EColors color) {
		drawRect(sx, sy, ex, ey, color.intVal);
	}
	private static void drawRect(double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		float sx = tdx((float) sxIn);
		float sy = tdy((float) syIn);
		float ex = tdx((float) exIn);
		float ey = tdy((float) eyIn);
		
		float r = (colorIn >> 16 & 255) / 255.0F;
		float g = (colorIn >> 8 & 255) / 255.0F;
		float b = (colorIn & 255) / 255.0F;
		float f = (colorIn >> 24 & 255) / 255.0F;
		
		vert(sx, sy, r, g, b, f);
		vert(sx, ey, r, g, b, f);
		vert(ex, ey, r, g, b, f);
		vert(ex, sy, r, g, b, f);
//		addIndices();
		totalElements++;
	}
	
	public static void drawTexture(GameTexture texture, double sxIn, double syIn, double exIn, double eyIn, int colorIn) {
		if (texture != null && textures.notContains(texture)) {
			textures.add(texture);
		}
		
		int texID = 0;
		if (texture != null) {
			for (int i = 0; i < textures.size(); i++) {
				if (textures.get(i) == texture) {
					texID = i + 1;
					break;
				}
			}
		}
		
		float sx = tdx((float) sxIn);
		float sy = tdy((float) syIn);
		float ex = tdx((float) exIn);
		float ey = tdy((float) eyIn);
		
		float r = (colorIn >> 16 & 255) / 255.0F;
		float g = (colorIn >> 8 & 255) / 255.0F;
		float b = (colorIn & 255) / 255.0F;
		float f = (colorIn >> 24 & 255) / 255.0F;
		
		vert(sx, sy, 0f, r, g, b, f, 0f, 0f, (float) texID);
		vert(sx, ey, 0f, r, g, b, f, 0f, 1f, (float) texID);
		vert(ex, ey, 0f, r, g, b, f, 1f, 1f, (float) texID);
		vert(ex, sy, 0f, r, g, b, f, 1f, 0f, (float) texID);
		
		totalElements++;
	}
	
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color) {
		drawTexture(texture, x, y, w, h, tX, tY, tW, tH, color, false);
	}
	public static void drawTexture(GameTexture texture, double x, double y, double w, double h, double tX, double tY, double tW, double tH, int color, boolean flip) {
		if (texture != null && textures.notContains(texture)) {
			textures.add(texture);
		}
		
		int texID = 0;
		if (texture != null) {
			for (int i = 0; i < textures.size(); i++) {
				if (textures.get(i) == texture) {
					texID = i + 1;
					break;
				}
			}
		}
		
		float xVal = (float) (tX / (float) texture.getWidth());
		float yVal = (float) (tY / (float) texture.getHeight());
		float wVal = (float) (tW / (float) texture.getWidth());
		float hVal = (float) (tH / (float) texture.getHeight());
		
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		float f = (color >> 24 & 255) / 255.0F;
		
		//garbage duct-tape font bottom fix
		//hVal -= 0.005f;
		
		float sx = tdx((float) x);
		float sy = tdy((float) y);
		float ex = tdx((float) (x + w));
		float ey = tdy((float) (y + h));
		
		float tsx = xVal;
		float tex = xVal + wVal;
		float tsy = yVal;
		float tey = yVal + hVal;
		
		float draw_tsx = (flip) ? tex : tsx;
		float draw_tsy = (flip) ? tsy : tsy;
		float draw_tex = (flip) ? tsx : tex;
		float draw_tey = (flip) ? tey : tey;
		
		vert(sx, sy, 0f, r, g, b, f, draw_tsx, draw_tsy, (float) texID);
		vert(sx, ey, 0f, r, g, b, f, draw_tsx, draw_tey, (float) texID);
		vert(ex, ey, 0f, r, g, b, f, draw_tex, draw_tey, (float) texID);
		vert(ex, sy, 0f, r, g, b, f, draw_tex, draw_tsy, (float) texID);
		
		totalElements++;
	}
	
	//=======================================================================================
	
	private static final int posOffset = 0;
	private static final int colorOffset = 12;
	private static final int texCoordOffset = 28;
	private static final int texIdOffset = 36;
	
	private static void vert(float x, float y, float r, float g, float b, float f) {
		vert(x, y, 0.0f, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	private static void vert(float x, float y, float z, float r, float g, float b, float f) {
		vert(x, y, z, r, g, b, f, 0.0f, 0.0f, 0.0f);
	}
	private static void vert(float x, float y, float r, float g, float b, float f, float tx, float ty, float tid) {
		vert(x, y, 0.0f, r, g, b, f, tx, ty, tid);
	}
	private static void vert(float x, float y, float z, float r, float g, float b, float f, float tx, float ty, float tid) {
		int v = nextVertOffset;
		
		byteBuffer.limit(v + 40);
		
		// position
		byteBuffer.putFloat(v + posOffset + 0, x);
		byteBuffer.putFloat(v + posOffset + 4, y);
		byteBuffer.putFloat(v + posOffset + 8, z);
		// color
		byteBuffer.putFloat(v + colorOffset + 0, r);
		byteBuffer.putFloat(v + colorOffset + 4, g);
		byteBuffer.putFloat(v + colorOffset + 8, b);
		byteBuffer.putFloat(v + colorOffset + 12, f);
		// texture coords
		byteBuffer.putFloat(v + texCoordOffset + 0, tx);
		byteBuffer.putFloat(v + texCoordOffset + 4, ty);
		// texture id
		byteBuffer.putFloat(v + texIdOffset + 0, tid);
		
		nextVertOffset += 40;
	}
	
//	private void addIndices() {
//		int i = nextIndiceOffset;
//		int offset = nextIndiceDataOffset;
//		
//		indicesBuffer.limit((totalElements + 1) * 6);
//		
//		// indices
//		indicesBuffer.put(i + 0, 0 + offset);
//		indicesBuffer.put(i + 1, 1 + offset);
//		indicesBuffer.put(i + 2, 2 + offset);
//		
//		indicesBuffer.put(i + 3, 2 + offset);
//		indicesBuffer.put(i + 4, 3 + offset);
//		indicesBuffer.put(i + 5, 0 + offset);
//		
//		nextIndiceOffset += 6;
//		nextIndiceDataOffset += 4;
//	}
	
	private void updateVBO() {
		GL30.glBindVertexArray(quadVAO);

		GL15.glDeleteBuffers(quadVBO);
		GL15.glDeleteBuffers(quadIBO);
		
		quadVBO = GL45.glCreateBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, quadVBO);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, byteBuffer, GL15.GL_STATIC_DRAW);
		
		//we use 10 * Float.BYTES as our stride here so that we can fit all of the following bytes in one vertex buffer
		// position (x, y, z) 		color (r, g, b, a) 				texture coord (tx, ty)		texture ID (tid)
		// -1.5f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.f,					0f
		
		GL45.glEnableVertexArrayAttrib(quadVBO, 0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 10 * Float.BYTES, 0);
		//12 because we are looking at the index of the first position float (x) at index (0)
		
		GL45.glEnableVertexArrayAttrib(quadVBO, 1);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, 10 * Float.BYTES, 12);
		//12 because we are looking at the index of the first color float (r) at index (3)
		
		GL45.glEnableVertexArrayAttrib(quadVBO, 2);
		GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, 10 * Float.BYTES, 28);
		//28 because we are looking at the index of the first texture float (tx) at index (7)
		
		GL45.glEnableVertexArrayAttrib(quadVBO, 3);
		GL20.glVertexAttribPointer(3, 1, GL11.GL_FLOAT, false, 10 * Float.BYTES, 36);
		//36 because we are looking at the index of the starting point of (tid) at index (9)
		
		int[] indices = new int[totalElements * 6];
		int offset = 0;
		
		for (int i = 0; i < totalElements * 6; i += 6) {
			indices[i + 0] = 0 + offset;
			indices[i + 1] = 1 + offset;
			indices[i + 2] = 2 + offset;
			
			indices[i + 3] = 2 + offset;
			indices[i + 4] = 3 + offset;
			indices[i + 5] = 0 + offset;
			
			offset += 4;
		}
		
		quadIBO = GL45.glCreateBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadIBO);
		GLCall(() -> GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW));
	}
	
	private void resetBuffer() {
		byteBuffer.rewind();
//		indicesBuffer.rewind();
		byteBuffer.limit(0);
//		indicesBuffer.limit(0);
		totalElements = 0;
		nextVertOffset = 0;
//		nextIndiceOffset = 0;
//		nextIndiceDataOffset = 0;
	}
	
	private void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
		
		Shaders.basic.enableAttribs();
		Shaders.bind(Shaders.basic);
		drawRect(100, 100, 400, 300, EColors.turquoise);
		drawRect(160, 500, 800, 700, EColors.yellow);
		drawTexture(EntityTextures.whodundid, 400, 400, 700, 700, EColors.white.intVal);
		drawTexture(EntityTextures.goblin, 800, 400, 1000, 700, EColors.lgray.intVal);
		drawTexture(FontRenderer.smooth.getFontTexture(), 500, 100, 700, 300, EColors.white.intVal);
		//GLObject.drawString("HELLO THERE!", 200, 200, EColors.rainbow());
		GLSettings.enableAlpha();
		GLSettings.enableBlend();
		GLSettings.blendFunc();
		
		for (int i = 0; i < textures.size(); i++) {
			GL46.glActiveTexture(GL13.GL_TEXTURE0 + i);
			TextureSystem.getInstance().bind(textures.get(i));
		}
		Shaders.basic.setUniform("texSamplers", texSlots);
		
		updateVBO();
		
		GLCall(() -> GL46.glBindVertexArray(quadVAO));
		GLCall(() -> GL46.glDrawElements(GL46.GL_TRIANGLES, totalElements * 6, GL46.GL_UNSIGNED_INT, 0));
		
		for (int i = 0; i < textures.size(); i++) {
			GL46.glActiveTexture(GL13.GL_TEXTURE0 + i);
			TextureSystem.getInstance().unbind(textures.get(i));
		}
		
		GLSettings.disableBlend();
		GL46.glBindVertexArray(0);
		resetBuffer();
		
		Shaders.unbind(Shaders.basic);
		Shaders.basic.disableAttribs();
	}
	
	private static void GLCall(Runnable r) {
		context.call(r);
	}
	
	public static void onMouseEvent(int action, int mXIn, int mYIn, int button, int change) {}
	public static void onKeyboardEvent(int action, char typedChar, int keyCode) {}
	
	public static void onWindowResized() {
		context.onWindowResized();
		windowSize = QoT.getWindowDims();
		//int width = WindowResizeListener.getWidth();
		//int height = WindowResizeListener.getHeight();
		//projection = MatrixBuilder.projection(70.0f, (float) width / (float) height, 0.1f, 1000.0f);
	}
	
	//-----------
	// Overrides
	//-----------
	
	public void onRenderErrorReporterMessage(String msg, ErrorReportingLevel reportingLevel) {
		if (reportingLevel == ErrorReportingLevel.HIGH) {
			LauncherLogger.logError(msg);
			System.err.println(msg);
		}
		else {
			LauncherLogger.log(msg);
		}
	}
	
	//---------------------------------------------------------------------------------------------
	//---------------------------------------------------------------------------------------------
	
}
