package envision.engine.rendering;

import envision.Envision;
import envision.engine.inputHandlers.Mouse;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.renderingAPI.RendererContextType;
import envision.engine.rendering.renderingAPI.RenderingContext;
import envision.engine.rendering.renderingAPI.opengl.OpenGLContext;
import qot.assets.textures.general.GeneralTextures;

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
	
	public void draw(long partialTicks) {
		
		//-----------------------------------------
		
		// draw map and entities
		if (Envision.theWorld != null && Envision.theWorld.isLoaded() && !Envision.isWorldRenderPaused()) {
			BatchManager.startBatch();
			Envision.theWorld.getWorldRenderer().onRenderTick(partialTicks);
			BatchManager.endBatch();
		}
		
		//-----------------------------------------
		
		// draw current game screen
		BatchManager.startBatch();
		if (Envision.currentScreen != null) {
			Envision.currentScreen.drawObject_i(Mouse.getMx(), Mouse.getMy());
		}
		// if there wasn't a screen, draw the 'no screens' stuff
		else {
			RenderingManager.drawTexture(GeneralTextures.noscreens, 128, 128, 384, 384);
			RenderingManager.drawString("No Screens?", 256, 256);
		}
		BatchManager.endBatch();
		
		//-----------------------------------------
		
		// draw top overlay
		BatchManager.startBatch();
		Envision.developerDesktop.onRenderTick();
		BatchManager.endBatch();
		
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
	
	public Camera getCamera() { return camera; }
	
	/** Returns the actual rendering context in use. */
	public RenderingContext getRenderingContext() { return renderingContext; }
	
	public BatchManager getBatchManager() { return renderingContext.getBatchManager(); }
	
}
