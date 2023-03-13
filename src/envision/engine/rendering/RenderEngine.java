package envision.engine.rendering;

import envision.Envision;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.renderingAPI.RendererContextType;
import envision.engine.rendering.renderingAPI.RenderingContext;
import envision.engine.rendering.renderingAPI.opengl.OpenGLContext;

public class RenderEngine {
	
	public Camera camera;
	
	//==================
	// Static Singleton
	//==================
	
	private static final RenderEngine instance = new RenderEngine();
	public static RenderEngine getInstance() { return instance; }
	//private constructor
	private RenderEngine() {}
	
	//========
	// Fields
	//========
	
	/** True if render engine is initialized. */
	private boolean init = false;
	/** True if the game window is currently in fullscreen. */
	private boolean fullscreen = false;
	
	/** The active rendering context (back-end) that is rendering the game. */
	private RenderingContext renderingContext;
	
	//==============
	// Init Methods
	//==============
	
	/** Initializes with OpenGL by default. */
	public void init(long windowHandle) { init(RendererContextType.OPENGL, windowHandle); }
	public void init(RendererContextType typeIn, long windowHandle) {
		Envision.debugf("Initializing rendering engine with context: '{}'", typeIn);
		
		switch (typeIn) {
		case OPENGL:
			renderingContext = new OpenGLContext(windowHandle);
			break;
		case VULKAN:
			throw new RuntimeException("Vulkan not supported!");
		default:
			throw new IllegalStateException("No rendering context set!");
		}
		
		renderingContext.init();
		
		var width = Envision.getWidth();
		var height = Envision.getHeight();
		camera = new Camera();
		camera.updateProjection(width, height);
		
		init = true;
	}
	
	
	//================
	// Internal Ticks
	//================
	
	public void drawFrame() {
		renderingContext.drawFrame();
		renderingContext.swapBuffers();
	}
	
	//=========
	// Getters
	//=========
	
	public boolean isInit() { return init; }
	public boolean isContextInit() { return (renderingContext != null) ? renderingContext.isInit() : false; }
	
	public Camera getCamera() { return camera; }
	
	/** Returns the actual rendering context in use. */
	public RenderingContext getRenderingContext() { return renderingContext; }
	
	public BatchManager getBatchManager() { return renderingContext.getBatchManager(); }
	
}
