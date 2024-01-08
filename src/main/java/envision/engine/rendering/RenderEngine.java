package envision.engine.rendering;

import envision.Envision;
import envision.engine.assets.EngineTextures;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.renderingAPI.RendererContextType;
import envision.engine.rendering.renderingAPI.RenderingContext;
import envision.engine.rendering.renderingAPI.opengl.OpenGLContext;

public class RenderEngine {
	
	public Camera orthoCamera;
	public Camera perspectiveCamera;
	
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
		
		int width = Envision.getWidth();
		int height = Envision.getHeight();
		orthoCamera = new Camera();
		orthoCamera.setupForOrtho(width, height);
		perspectiveCamera = new Camera();
		perspectiveCamera.setupForPerspective(width, height);
		
		init = true;
	}
	
//	protected void setupBatches() {
//		BatchManager batchMan = getBatchManager();
//		
//		batchMan.pushLayerIndex();
//	}
	
	public void destroy() {
		if (renderingContext != null) {
			renderingContext.destroy();
			renderingContext = null;
		}
	}
	
	//================
	// Internal Ticks
	//================
	
	public void draw(long dt) {
		
		//-----------------------------------------
		
		// draw map and entities
	    BatchManager.startLayer(0);
	    //System.out.println("drawing: " + BatchManager.getCurrentLayer().getLayerNum());
		if (Envision.theWorld != null && Envision.theWorld.isLoaded() && !Envision.isWorldRenderPaused()) {
			Envision.theWorld.getWorldRenderer().onRenderTick(dt);
		}
	    BatchManager.endLayer(0);
		
		//-----------------------------------------
		
		// draw current game screen
	    BatchManager.startLayer(1);
		//System.out.println("drawing: " + BatchManager.getCurrentLayer().getLayerNum());
		if (Envision.currentScreen != null) {
			Envision.currentScreen.drawObject_i(dt, Mouse.getMx(), Mouse.getMy());
		}
		// if there wasn't a screen, draw the 'no screens' stuff
		else {
            RenderingManager.drawTexture(EngineTextures.noscreens, 128, 128, 384, 384);
            RenderingManager.drawString("No Screens?", 256, 256);
		}
		BatchManager.endLayer(1);
		
		//-----------------------------------------
		
		// draw top overlay
		BatchManager.startLayer(2);
		//System.out.println("drawing: " + BatchManager.getCurrentLayer().getLayerNum());
		Envision.developerDesktop.onRenderTick(dt);
		BatchManager.endLayer(2);
		
		//-----------------------------------------
		
	}
	
	public void endFrame() {
		renderingContext.drawFrame();
		renderingContext.swapBuffers();
	}
	
	//=========
	// Getters
	//=========
	
	public boolean isInit() { return init; }
	public boolean isContextInit() { return (renderingContext != null) ? renderingContext.isInit() : false; }
	
	public Camera getPerspectiveCamera() { return perspectiveCamera; }
	public Camera getOrthoCamrea() { return orthoCamera; }
	
	/** Returns the actual rendering context in use. */
	public RenderingContext getRenderingContext() { return renderingContext; }
	
	public BatchManager getBatchManager() { return renderingContext.getBatchManager(); }
	
}
