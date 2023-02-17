package envision.engine.rendering;

import org.lwjgl.glfw.GLFW;

import envision.Envision;
import envision.debug.testStuff.testing.renderingAPI.RendererContextType;
import envision.debug.testStuff.testing.renderingAPI.RenderingContext;
import envision.debug.testStuff.testing.renderingAPI.opengl.OpenGLContext;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.textureSystem.TextureSystem;

public class RenderEngine {
	
	//------------------
	// Static Singleton
	//------------------
	
	private static final RenderEngine instance = new RenderEngine();
	public static RenderEngine getInstance() { return instance; }
	//private constructor
	private RenderEngine() {}
	
	//--------
	// Fields
	//--------
	
	/** True if render engine is initialized. */
	private boolean init = false;
	/** True if GLFW is initialized. */
	private boolean glfwInit = false;
	/** True if the game window is currently in fullscreen. */
	private boolean fullscreen = false;
	
	/** The game window's handle. */
	private long windowHandle;
	/** The width and height of the game window. */
	private int width, height;
	
	/** The active rendering context (back-end) that is rendering the game. */
	private RenderingContext renderingContext;
	
	/** The engine's primary font renderer. */
	private static FontRenderer fontRenderer;
	/** The engine's underlying texture system. */
	private static TextureSystem textureSystem;

	
	//--------------
	// Init Methods
	//--------------
	
	/** Initializes with OpenGL by default. */
	public void init() { init(RendererContextType.OPENGL); }
	public void init(RendererContextType typeIn) {
		Envision.debugf("Initializing rendering engine with context: '{}'", typeIn);
		
		//init glfw and window context
		initGLFW();
		
		switch (typeIn) {
		case OPENGL: renderingContext = new OpenGLContext(windowHandle); break;
		case VULKAN: throw new RuntimeException("Vulkan not yet supported!");
		default: throw new IllegalStateException("No rendering context set!");
		}
		
		init = true;
	}
	
	private void initGLFW() {
		if (!GLFW.glfwInit()) throw new IllegalStateException("Failed to initialize GLFW!");
		
		windowHandle = GLFW.glfwCreateWindow(width, height, "", 0, 0);
	}
	
	public void shutdown() {
		textureSystem.destroyAllTextures();
		
		GLFW.glfwDestroyWindow(windowHandle);
		GLFW.glfwTerminate();
	}
	
	//----------------
	// Internal Ticks
	//----------------
	
	public void onRenderTick() {
		renderingContext.swapBuffers();
	}
	
	//---------
	// Getters
	//---------
	
	public boolean isInit() { return init; }
	public boolean isGlfwInit() { return glfwInit; }
	public boolean isContextInit() { return (renderingContext != null) ? renderingContext.isInit() : false; }
	
	/** Returns this game's central font rendering system. */
	public static FontRenderer getFontRenderer() { return fontRenderer; }
	/** Returns this game's central texture handling system. */
	public static TextureSystem getTextureSystem() { return textureSystem; }
	
	public static boolean shouldClose() {
		if (instance == null) return false;
		return GLFW.glfwWindowShouldClose(instance.windowHandle);
	}
	
}
