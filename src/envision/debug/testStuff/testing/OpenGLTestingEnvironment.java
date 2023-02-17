package envision.debug.testStuff.testing;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GL46;

import envision.debug.testStuff.testing.renderingAPI.RenderingContext;
import envision.debug.testStuff.testing.renderingAPI.error.ErrorReportingLevel;
import envision.debug.testStuff.testing.renderingAPI.error.IRendererErrorReceiver;
import envision.debug.testStuff.testing.renderingAPI.error.RendererErrorReporter;
import envision.debug.testStuff.testing.renderingAPI.opengl.OpenGLContext;
import envision.debug.testStuff.testing.testgl.Camera2D;
import envision.engine.rendering.shaders.Shaders;
import eutil.math.dimensions.EDimension;
import eutil.math.vectors.Vec2f;
import qot.QoT;
import qot.launcher.LauncherLogger;

public class OpenGLTestingEnvironment implements IRendererErrorReceiver {
	
	private static RenderingContext context;
	private static long windowHandle;
	private static int quad1;
	
	private static Camera2D camera;
	//private static Matrix4f projection;
	private static EDimension windowSize;
	
	private static Vec2f position, scale;
	private static float rotation;
	
	//---------------------------------------------------------------------------------------------
	// OpenGL Back-end Setup
	//---------------------------------------------------------------------------------------------
	
	public static void runTestingEnvironment(long handle) {
		new OpenGLTestingEnvironment(handle);
	}
	
	private OpenGLTestingEnvironment(long handle) {
		windowHandle = handle;
		RendererErrorReporter.setReceiver(this);
		context = new OpenGLContext(handle);
		windowSize = QoT.getWindowDims();
		onAttach();
		Shaders.init();
		runTestLoop();
	}
	
	//------
	// Init
	//------
	
	public void onAttach() {
		context.init();
		
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		camera = new Camera2D(new Vec2f(windowSize.midX, windowSize.midY), 1.0f);
		// position (x, y, z) 		color (r, g, b, f) 		texture coord (x, y)
		
//		float[] vertices = new float[] {
//			-0.75f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.f,
//			-0.25f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		1.0f, 0.f,
//			-0.25f,  0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		1.0f, 1.f,
//			-0.75f,  0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 1.f,
//			
//			0.25f, -0.5f, 0.0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		0.0f, 0.f,
//			0.75f,  -0.5f, 0.0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		1.0f, 0.f,
//			0.75f,   0.5f, 0.0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		1.0f, 1.f,
//			0.25f,  0.5f, 0.0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		0.0f, 1.f,
//		};
		
		float[] vertices = new float[] {
			10f, 10f, 0f,		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.f,
			10f, 60f, 0f,		0.18f, 0.6f, 0.96f, 1.0f, 		1.0f, 0.f,
			60f, 60f, 0f,		0.18f, 0.6f, 0.96f, 1.0f, 		1.0f, 1.f,
			60f, 10f, 0f,		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 1.f,
			
			110f, 10f, 0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		0.0f, 0.f,
			110f, 60f, 0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		1.0f, 0.f,
			160f, 60f, 0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		1.0f, 1.f,
			160f, 10f, 0f, 		1.0f, 0.93f, 0.24f, 1.0f, 		0.0f, 1.f,
		};
		
		int quadVA = quad1 = GL45.glCreateVertexArrays();
		GL30.glBindVertexArray(quadVA);
		
		int quadVB = GL45.glCreateBuffers();
		GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, quadVB);
		GL45.glBufferData(GL45.GL_ARRAY_BUFFER, vertices, GL45.GL_DYNAMIC_DRAW);
		
		//we use 9 * Float.BYTES as our stride here so that we can fit all of the following bytes in one vertex buffer
		// position (x, y, z) 		color (r, g, b, f) 				texture coord (tx, ty)
		// -1.5f, -0.5f, 0.0f, 		0.18f, 0.6f, 0.96f, 1.0f, 		0.0f, 0.f,
		
		GL45.glEnableVertexArrayAttrib(quadVB, 0);
		GL45.glVertexAttribPointer(0, 3, GL45.GL_FLOAT, false, 9 * Float.BYTES, 0);
		//12 because we are looking at the index of the first color float (r) at index (3)
		
		GL45.glEnableVertexArrayAttrib(quadVB, 1);
		GL45.glVertexAttribPointer(1, 4, GL45.GL_FLOAT, false, 9 * Float.BYTES, 12);
		//12 because we are looking at the index of the first color float (r) at index (3)
		
		GL45.glEnableVertexArrayAttrib(quadVB, 2);
		GL45.glVertexAttribPointer(2, 2, GL45.GL_FLOAT, false, 9 * Float.BYTES, 28);
		//28 because we are looking at the index of the first texture float (x) at index (7)
		
		int[] indices = new int[12];
		int offset = 0;
		
		for (int i = 0; i < 12; i += 6) {
			indices[i + 0] = 0 + offset;
			indices[i + 1] = 1 + offset;
			indices[i + 2] = 2 + offset;
			
			indices[i + 3] = 2 + offset;
			indices[i + 4] = 3 + offset;
			indices[i + 5] = 0 + offset;
			
			offset += 4;
		}
		
//		int[] indices = new int[] {
//			0, 1, 2, 2, 3, 0,
//			4, 5, 6, 6, 7, 4
//		};
		
		int quadIB = GL45.glCreateBuffers();
		GL45.glBindBuffer(GL45.GL_ELEMENT_ARRAY_BUFFER, quadIB);
		GL45.glBufferData(GL45.GL_ELEMENT_ARRAY_BUFFER, indices, GL45.GL_DYNAMIC_DRAW);
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
	
	private void pre() {
		
	}
	
	private void post() {
		
	}
	
	private void render() {
		GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);
		
		Shaders.basic.enableAttribs();
		Shaders.bind(Shaders.basic);
		Shaders.basic.setUniform("u_projection", camera.getProjectionMatrix(windowSize.toFloat()));
		
		GLCall(() -> GL46.glBindVertexArray(quad1));
		GLCall(() -> GL46.glDrawElements(GL46.GL_TRIANGLES, 12, GL46.GL_UNSIGNED_INT, 0));
		
		GL46.glBindVertexArray(0);
		
		Shaders.unbind(Shaders.basic);
		Shaders.basic.disableAttribs();
		
//		//glTranslatef(0.0f, 0.0f, 0.0f);
//		glBegin(GL_QUADS);
//		glColor3f(1.0f, 1.0f, 0.0f);
//		glVertex2f(0.0f, 0.0f);
//		glVertex2f(0.5f, 0.0f);
//		glVertex2f(0.5f, 0.5f);
//		glVertex2f(0.0f, 0.5f);
//		glColor3f(1.0f, 0.0f, 0.0f);
//		glVertex2f(-0.5f, -0.5f);
//		glVertex2f(0.0f, -0.5f);
//		glVertex2f(0.0f, 0.0f);
//		glVertex2f(-0.5f, 0.0f);
//		glEnd();
//		
//		glMatrixMode(GL_PROJECTION);
//		glLoadIdentity();
	}
	
	private static void GLCall(Runnable r) {
		context.call(r);
	}
	
	public static void onMouseEvent(int action, int mXIn, int mYIn, int button, int change) {
		
	}
	
	public static void onKeyboardEvent(int action, char typedChar, int keyCode) {
		
	}
	
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
