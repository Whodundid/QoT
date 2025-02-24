package envision.engine.internal.rendering;

import envision.Envision;
import envision.debug.Profiler;
import envision.engine.internal.assets.EngineTextures;
import envision.engine.internal.inputHandlers.Mouse;
import envision.engine.internal.rendering.batching.BatchManager;
import envision.engine.internal.rendering.renderingAPI.RendererContextType;
import envision.engine.internal.rendering.renderingAPI.RenderingContext;
import envision.engine.internal.rendering.renderingAPI.opengl.OpenGLContext;

public class RenderEngine {
    
    public GLCamera orthoCamera;
    public GLCamera perspectiveCamera;
    
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
        orthoCamera = new GLCamera();
        orthoCamera.setupForOrtho(width, height);
        perspectiveCamera = new GLCamera();
        perspectiveCamera.setupForPerspective(width, height);
        
        init = true;
    }
    
//    protected void setupBatches() {
//        BatchManager batchMan = getBatchManager();
//        
//        batchMan.pushLayerIndex();
//    }
    
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

        var p = Profiler.getProfiler("FRAME_TICK");
        
        //-----------------------------------------
        
        // draw map and entities
        p.startSection("world");
        BatchManager.startLayer(0);
        //System.out.println("drawing: " + BatchManager.getCurrentLayer().getLayerNum());
        if (Envision.theWorld != null && Envision.theWorld.isLoaded() && !Envision.isWorldRenderPaused()) {
            Envision.theWorld.getWorldRenderer().onRenderTick(dt);
        }
        BatchManager.endLayer(0);
        p.endSection("world");
        
        //-----------------------------------------
        
        // draw current game screen
        p.startSection("screen");
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
        p.endSection("screen");
        
        //-----------------------------------------
        
        // draw top overlay
        p.startSection("desktop");
        BatchManager.startLayer(2);
        //System.out.println("drawing: " + BatchManager.getCurrentLayer().getLayerNum());
        Envision.developerDesktop.onRenderTick(dt);
        //RenderingManager.drawString("delta g: " + Envision.deltaGameTick / 100000f, 10, 80);
        //RenderingManager.drawString("delta f: " + Envision.deltaFrameTick / 100000f, 10, 100);
        BatchManager.endLayer(2);
        p.endSection("desktop");
        
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
    
    public GLCamera getPerspectiveCamera() { return perspectiveCamera; }
    public GLCamera getOrthoCamrea() { return orthoCamera; }
    
    /** Returns the actual rendering context in use. */
    public RenderingContext getRenderingContext() { return renderingContext; }
    
    public BatchManager getBatchManager() { return renderingContext.getBatchManager(); }
    
}
