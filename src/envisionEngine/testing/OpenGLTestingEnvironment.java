package envision.testing;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL45.*;

import envision.inputHandlers.WindowResizeListener;
import envision.renderEngine.shaders.Shaders;
import envision.testing.renderingAPI.RenderingContext;
import envision.testing.renderingAPI.camera.Camera;
import envision.testing.renderingAPI.error.ErrorReportingLevel;
import envision.testing.renderingAPI.error.IRendererErrorReceiver;
import envision.testing.renderingAPI.error.RendererErrorReporter;
import envision.testing.renderingAPI.math.Matrix4f;
import envision.testing.renderingAPI.math.MatrixBuilder;
import envision.testing.renderingAPI.opengl.OpenGLContext;
import eutil.math.Vec3f;
import game.launcher.LauncherLogger;

public class OpenGLTestingEnvironment implements IRendererErrorReceiver {
	
	private static RenderingContext context;
	private static long windowHandle;
	private static int vaoID, vboID, iboID, cboID;
	private static int m_QuadVA, m_QuadVB, m_QuadIB;
	
	private static Camera camera;
	private static Matrix4f projection;
	
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
		onAttach();
		Shaders.init();
		runTestLoop();
	}
	
	//------
	// Init
	//------
	
	public void onAttach() {
		context.init();
		
		camera = new Camera();
		int width = WindowResizeListener.getWidth();
		int height = WindowResizeListener.getHeight();
		projection = MatrixBuilder.projection(70.0f, (float) width / (float) height, 0.1f, 1000.0f);
		
		glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		float[] vertices = new float[] {
			-1.5f, -0.5f, 0.0f, 0.18f, 0.6f, 0.96f, 1.0f,
			-0.5f, -0.5f, 0.0f, 0.18f, 0.6f, 0.96f, 1.0f,
			-0.5f,  0.5f, 0.0f, 0.18f, 0.6f, 0.96f, 1.0f,
			-1.5f,  0.5f, 0.0f, 0.18f, 0.6f, 0.96f, 1.0f,
			
			 0.5f, -0.5f, 0.0f, 1.0f, 0.93f, 0.24f, 1.0f,
			 1.5f, -0.5f, 0.0f, 1.0f, 0.93f, 0.24f, 1.0f,
			 1.5f,  0.5f, 0.0f, 1.0f, 0.93f, 0.24f, 1.0f,
			 0.5f,  0.5f, 0.0f, 1.0f, 0.93f, 0.24f, 1.0f,
		};
		
		m_QuadVA = glCreateVertexArrays();
		glBindVertexArray(m_QuadVA);
		
		m_QuadVB = glCreateBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, m_QuadVB);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glEnableVertexArrayAttrib(m_QuadVB, 0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 7, 0);
		
		glEnableVertexArrayAttrib(m_QuadVB, 1);
		glVertexAttribPointer(1, 4, GL_FLOAT, false, Float.BYTES * 7, Float.BYTES * 3);
		
		int[] indices = new int[] {
			0, 1, 2, 2, 3, 0,
			4, 5, 6, 6, 7, 4
		};
		
		m_QuadIB = glCreateBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_QuadIB);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
//		vaoID = glGenVertexArrays();
//		glBindVertexArray(vaoID);
//		
//		vboID = glGenBuffers();
//		glBindBuffer(GL_ARRAY_BUFFER, vboID);
//		
//		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
//		
//		glEnableVertexAttribArray(0);
//		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
//		//glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES * 3, 0l);
//		
//		iboID = glGenBuffers();
//		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboID);
//		
//		int[] indices = new int[] {0, 1, 2};
//		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
//		
//		//cboID = glGenBuffers();
//		//glBindBuffer(GL46.GL_ELEMENT_ARRAY_BUFFER, cboID);
//		
//		
	}
	
	public void runTestLoop() {
		while (!glfwWindowShouldClose(windowHandle)) {
			glEnable(GL_DEPTH_TEST);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glfwPollEvents();
			camera.update();
			
			GLCall(() -> glBindVertexArray(m_QuadVA));
			
			Shaders.basic.enableAttribs();
			GLCall(() -> Shaders.basic.bind());
			
			Matrix4f transform = MatrixBuilder.transform(new Vec3f(0, 0, 0), new Vec3f(), new Vec3f(1, 1, 1));
			Matrix4f view = MatrixBuilder.view(camera);
			boolean isTexture = false;
			
			GLCall(() -> Shaders.basic.loadTransform(transform));
			GLCall(() -> Shaders.basic.loadView(view));
			GLCall(() -> Shaders.basic.loadProjection(projection));
			GLCall(() -> Shaders.basic.loadIsTexture(isTexture));
			
			GLCall(() -> glActiveTexture(GL_TEXTURE0));
			GLCall(() -> glDrawElements(GL_TRIANGLES, 12, GL_UNSIGNED_INT, 0));
			
			GLCall(() -> Shaders.basic.unbind());
			Shaders.basic.disableAttribs();
			GLCall(() -> glBindVertexArray(0));
			
			context.swapBuffers();
		}
		
		glfwDestroyWindow(windowHandle);
		glfwTerminate();
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
		int width = WindowResizeListener.getWidth();
		int height = WindowResizeListener.getHeight();
		projection = MatrixBuilder.projection(70.0f, (float) width / (float) height, 0.1f, 1000.0f);
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
