package game;

import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import envision.EnvisionGame;
import envision.events.EventHandler;
import envision.game.screens.GameScreen;
import envision.game.screens.ScreenLevel;
import envision.game.scripts.envisionMappings.Envision_QoT_ErrorCallback;
import envision.game.scripts.envisionMappings.qot_package.Envision_QoT_Package;
import envision.game.world.GameWorld;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.inputHandlers.WindowResizeListener;
import envision.renderEngine.GLObject;
import envision.renderEngine.fontRenderer.FontRenderer;
import envision.renderEngine.shaders.Shaders;
import envision.renderEngine.textureSystem.TextureSystem;
import envision.terminal.TerminalHandler;
import envision.testing.OpenGLTestingEnvironment;
import envision.topOverlay.GameTopRenderer;
import envision.windowLib.WindowSize;
import envision.windowLib.windowTypes.TopWindowParent;
import envision.windowLib.windowTypes.interfaces.IWindowParent;
import envision.windowLib.windowUtil.ObjectPosition;
import envision_lang.EnvisionLang;
import eutil.file.FileOpener;
import eutil.math.EDimension;
import game.assets.textures.GameTextures;
import game.assets.textures.general.GeneralTextures;
import game.entities.player.QoT_Player;
import game.launcher.LauncherLogger;
import game.launcher.LauncherSettings;
import game.screens.main.MainMenuScreen;
import game.settings.QoTSettings;

public class QoT implements EnvisionGame {
	
	public static final String version = "Aug 7, 2022";
	
	//-----------------------------------------------------------------------
	//-----------------------------------------------------------------------
	//-----------------------------------------------------------------------
	
	/**
	 * Does not run QoT, instead runs an OpenGL testing environment instead.
	 */
	public static final boolean RUN_OPEN_GL_TESTING_ENVIRONMENT = false;
	
	//-----------------------------------------------------------------------
	//-----------------------------------------------------------------------
	//-----------------------------------------------------------------------
	
	/** The primary logging instance for QoT. */
	public static final Logger QoTLogger = Logger.getLogger("QoT");
	/** The OpenGL window handle for QoT's display. */
	public static long handle = -1;
	/** The singular QoT game instance. */
	private static QoT instance = null;
	/** The active game settings for this QoT instance. */
	public static final QoTSettings settings = new QoTSettings();
	/** The Envision Scripting Language instance. */
	public static final EnvisionLang envision = new EnvisionLang().addBuildPackage(new Envision_QoT_Package());
	
	private static Keyboard keyboard;
	private static Mouse mouse;
	private static WindowResizeListener resizeListener;
	private static FontRenderer fontRenderer;
	private static TextureSystem textureSystem;
	private static EventHandler eventSystem;
	
	private static GameTopRenderer<?> topRenderer;
	private static TerminalHandler terminalHandler;
	
	private static int width;
	private static int height;
	private static int[] xPos = new int[1], yPos = new int[1];
	
	public static QoT_Player thePlayer;
	public static GameWorld theWorld;
	private static boolean pause = false;
	private static boolean renderWorld = true;
	
	/** The screen currently being displayed. */
	public static GameScreen<?> currentScreen;
	
	private static boolean init = false;
	/** Enables debug mode if true. */
	private static boolean isDebug = false;
	/** Indicates whether the game is actively running or not. */
	private static boolean running = false;
	/** True if window rendered in full screen. */
	private static boolean fullscreen = false;
	
	// Game tick stuff
	private long UPS = 60;
	double timeU = 1000000000 / UPS;
	double deltaU = 0;
	long lagCheck = 0;
	private long curNumTicks = 0;
	private long ticks = 0;
	
	// Framerate stuff
	private long FPS = 60; //60 fps by default -- user can modify
	double timeF = 1000000000 / FPS;
	double deltaF = 0;
	public long startTime = 0l;
	public long runningTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	public static long updateCounter = 0;
	
	//-----------------------------------------------
	
	public static QoT getGame() { return (instance != null) ? instance : (instance = new QoT()); }
	
	public static void startGame(LauncherSettings settings) {
		try {
			LauncherLogger.log("---------------------------\n");
			LauncherLogger.log("Running game with settings: " + settings);
			QoTSettings.init(settings.INSTALL_DIR, settings.USE_INTERNAL_RESOURCES_PATH);
			setupGLFW();
			if (RUN_OPEN_GL_TESTING_ENVIRONMENT) OpenGLTestingEnvironment.runTestingEnvironment(handle);
			else getGame().runGameLoop();
		}
		catch (Exception e) {
			LauncherLogger.logError(e);
			FileOpener.openFile(LauncherLogger.getLogFile());
		}
	}
	
	public static void stopGame() {
		if (running) {
			running = false;
			info("Stopping Game!");
		}
		else {
			info("Attempted to stop game when not running!");
		}
	}
	
	//--------------
	// Constructors
	//--------------
	
	private QoT() {}
	
	//-------------------
	// Pre-Setup Methods
	//-------------------
	
	private static void setupGLFW() {
		width = 1600;
		height = 900;
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		// setup OpenGL
		if (!GLFW.glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
		}
		
		handle = GLFW.glfwCreateWindow(width, height, "QoT", 0, 0);
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			
			GLFW.glfwGetWindowSize(handle, w, h);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			
			xPos[0] = ((vidmode.width() - w.get(0)) / 2);
			yPos[0] = (vidmode.height() - h.get(0)) / 2;
			GLFW.glfwSetWindowPos(handle, xPos[0], yPos[0]);
		}
		
		//initialize other things
		keyboard = Keyboard.getInstance();
		mouse = Mouse.getInstance();
		resizeListener = WindowResizeListener.getInstance();
		
		GLFW.glfwSetKeyCallback(handle, keyboard);
		GLFW.glfwSetMouseButtonCallback(handle, mouse);
		GLFW.glfwSetCursorPosCallback(handle, mouse.getCursorPosCallBack());
		GLFW.glfwSetScrollCallback(handle, mouse.getScrollCallBack());
		GLFW.glfwSetWindowSizeCallback(handle, resizeListener);
		
		GLFW.glfwShowWindow(handle);
	}
	
	private static void setupOpenGL() {
		GLFW.glfwMakeContextCurrent(handle);
		GL.createCapabilities();
		
		int interval = (QoTSettings.vsync.get()) ? 1 : 0;
		GLFW.glfwSwapInterval(interval);
		
		//initialize shaders
		Shaders.init();
		
		textureSystem = TextureSystem.getInstance();
		fontRenderer = FontRenderer.getInstance();
	}
	
	private static void setupQoT() {
		envision.setErrorCallback(new Envision_QoT_ErrorCallback());
		
		topRenderer = GameTopRenderer.getInstance();
		terminalHandler = TerminalHandler.getInstance();
		eventSystem = EventHandler.getInstance();
		//tickManager = new TickManager();
		
		GameTextures.instance().onRegister(textureSystem);
		
		terminalHandler.initCommands();
	}
	
	//-------------------
	// Primary Game Loop
	//-------------------
	
	private void runGameLoop() {
		//ignore if already running
		if (running) return;
		running = true;
		
		//initialize OpenGL back-end before continuing
		setupOpenGL();
		
		//initialize QoT back-end before continuing
		setupQoT();
		
		//set fps/ups targets
		setTargetFPS(QoTSettings.targetFPS.get());
		setTargetUPS(QoTSettings.targetUPS.get());
		
		//load main menu
		displayScreen(new MainMenuScreen());
		
		//prepare timers
		startTime = System.currentTimeMillis();
		long initialTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		while (running && !GLFW.glfwWindowShouldClose(handle)) {
			try {
				long currentTime = System.nanoTime();
				deltaU += (currentTime - initialTime) / timeU;
				deltaF += (currentTime - initialTime) / timeF;
				initialTime = currentTime;
				
				//synchronize game inputs to 60 ticks per second by default
				if (deltaU >= 1) {
					//update inputs
					GLFW.glfwPollEvents();
					if (GLFW.glfwWindowShouldClose(handle)) { running = false; }
					runGameTick();
					if (ticks == Integer.MAX_VALUE) ticks = 0;
					else ticks++;
					deltaU--;
				}
				
				if (deltaF >= 1) {
					onRenderTick(1);
					frames++;
					deltaF--;
				}
				
				if (System.currentTimeMillis() - timer > 1000) {
					curFrameRate = frames;
					curNumTicks = ticks;
					frames = 0;
					ticks = 0;
					timer += 1000;
				}
				
				if (deltaU > 3 || deltaF > 5) {
					deltaU = 0;
					deltaF = 0;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Dying
		
		textureSystem.destroyAllTextures();
		
		GLFW.glfwDestroyWindow(handle);
		GLFW.glfwTerminate();
	}
	
	private void runGameTick() {
		curNumTicks++;
		
		//process game events
		eventSystem.onGameTick();
		
		if (currentScreen != null) currentScreen.onGameTick(curNumTicks);
		
		//only update if world actually exists, is loaded, and is not paused
		if (theWorld != null && theWorld.isLoaded() && !pause) {
			theWorld.onUpdate();
			theWorld.updateEntities();
		}
	}
	
	/**
	 * Called from the main game loop to perform all rendering operations.
	 */
	private void onRenderTick(long partialTicks) {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		if (theWorld != null && theWorld.isLoaded() && renderWorld) {
			theWorld.getWorldRenderer().onRenderTick();
		}
		
		if (currentScreen != null) {
			currentScreen.drawObject(Mouse.getMx(), Mouse.getMy());
			//currentScreen.drawString("deltaF: " + deltaF, 0, currentScreen.endY - currentScreen.midY / 2, EColors.aquamarine);
			//currentScreen.drawString("deltaU: " + deltaU, 0, currentScreen.endY - currentScreen.midY / 2 + 25, EColors.aquamarine);
		}
		else {
			GLObject.drawTexture(GeneralTextures.noscreens, 128, 128, 384, 384);
			GLObject.drawString("No Screens?", 256, 256);
		}
		
		topRenderer.onRenderTick();
		
		GLFW.glfwSwapBuffers(handle);
	}
	
	//----------------
	// Public Methods
	//----------------
	
	/**
	 * Called whenever the game window itself is resized. Do not manually
	 * call!
	 */
	public void onWindowResize() {
		width = WindowResizeListener.getWidth();
		height = WindowResizeListener.getHeight();
		GL11.glViewport(0, 0, width, height);
		
		if (theWorld != null && theWorld.isLoaded()) theWorld.getWorldRenderer().onWindowResized();
		if (currentScreen != null) currentScreen.onScreenResized();
		topRenderer.onScreenResized();
	}
	
	private long getTargetFPSi() { return FPS; }
	private long getTargetUPSi() { return UPS; }
	
	private void setTargetUPSi(int upsIn) {
		UPS = upsIn;
		timeU = 1000000000 / UPS;
		deltaU = 0;
		deltaF = 0;
		ticks = 0;
	}
	
	private void setTargetFPSi(int fpsIn) {
		FPS = fpsIn;
		timeF = 1000000000 / FPS;
		deltaU = 0;
		deltaF = 0;
		frames = 0;
	}
	
	//-----------------------
	// Engine Input Handlers
	//-----------------------
	
	public static void keyboardEvent(int action, char typedChar, int keyCode) {
		if (getGLInit()) {
			topRenderer.handleKeyboardInput(action, typedChar, keyCode);
			if (theWorld != null) {
				if (action == 0) { theWorld.getWorldRenderer().keyReleased(typedChar, keyCode); }
				if (action == 1 || action == 2) { theWorld.getWorldRenderer().keyPressed(typedChar, keyCode); }
			}
			//if (currentScreen != null && !topRenderer.hasFocus()) { currentScreen.handleKeyboardInput(action, typedChar, keyCode); }
			//worldRenderer.handleKeyboardInput(action, typedChar, keyCode);
		}
	}
	
	public static void mouseEvent(int action, int mXIn, int mYIn, int button, int change) {
		if (getGLInit()) {
			topRenderer.handleMouseInput(action, mXIn, mYIn, button, change);
			//if (currentScreen != null && !topRenderer.hasFocus()) { currentScreen.handleMouseInput(action, mXIn, mYIn, button, change); }
			//worldRenderer.handleMouseInput(action, mXIn, mYIn, button, change);
		}
	}
	
	//--------------------------------
	// Public Static Engine Functions
	//--------------------------------
	
	public static GameScreen displayScreen(GameScreen screenIn) { return displayScreen(screenIn, null, true, true); }
	public static GameScreen displayScreen(GameScreen screenIn, boolean init) { return displayScreen(screenIn, null, init, true); }
	public static GameScreen displayScreen(GameScreen screenIn, GameScreen previous) { return displayScreen(screenIn, previous, true, true); }
	/** Attempts to display a new GameScreen in game. */
	public static GameScreen displayScreen(GameScreen screenIn, GameScreen previous, boolean init, boolean fade) {
		instance.deltaF = 0;
		instance.deltaU = 0;
		
		if (screenIn != null) {
			GameScreen old = currentScreen;
			currentScreen = screenIn;
			if (fade) currentScreen.fadeIn();
			
			if (old != null) {
				old.close();
				old.onClosed();
				old.onScreenClosed();
			}
			
			if (previous != null) {
				old.getScreenHistory().push(previous);
				currentScreen.setScreenHistory(old.getScreenHistory());
			}
			
			if (init) {
				try {
					currentScreen.setWindowSize();
					currentScreen.initScreen();
					currentScreen.initChildren();
				}
				catch (Exception e) {
					e.printStackTrace();
					currentScreen = old;
					
					old.setWindowSize();
					currentScreen.initScreen();
					currentScreen.initChildren();
				}
			}
		}
		else {
			GameScreen old = currentScreen;
			currentScreen = screenIn;
			
			if (old != null) {
				old.close();
				old.onClosed();
				old.onScreenClosed();
			}
		}
		
		return currentScreen;
	}
	
	/**
	 * Unloads the current world and loads the given one. If the given
	 * world is null, the current world is unloaded regardless.
	 * 
	 * @param worldIn The game world to load
	 * @return The game world to be loaded
	 */
	public static GameWorld loadWorld(GameWorld worldIn) {
		//unload the last world (if there was one)
		if (theWorld != null) { theWorld.setLoaded(false); }
		
		theWorld = worldIn;
		
		if (theWorld != null) {
			//assign as last world loaded
			QoTSettings.lastMap.set(theWorld.getName());
			
			//load the world
			theWorld.setLoaded(true);
			pause = false;
			renderWorld = true;
			
			//check if loaded
			if (!theWorld.isLoaded()) { warn("Failed to load world: "); }
		}
		
		return worldIn;
	}
	
	public static boolean isPaused() { return pause; }
	public static boolean isWorldRenderPaused() { return renderWorld; }
	
	public static void pause() { pause = true; }
	public static void unpause() { pause = false; }
	public static void pauseWorldRender() { renderWorld = false; }
	public static void unpauseWorldRender() { renderWorld = true; }
	
	//------------------------
	// Central Window Handles
	//------------------------
	
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn) { return displayWindow(level, windowIn, null, true, false, false, ObjectPosition.SCREEN_CENTER); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, ObjectPosition loc) { return displayWindow(level, windowIn, null, true, false, false, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, boolean transferFocus) { return displayWindow(level, windowIn, null, transferFocus, false, false, ObjectPosition.SCREEN_CENTER); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, boolean transferFocus, ObjectPosition loc) { return displayWindow(level, windowIn, null, transferFocus, false, false, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject) { return displayWindow(level, windowIn, oldObject, true, true, true, ObjectPosition.OBJECT_CENTER); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, ObjectPosition loc) { return displayWindow(level, windowIn, oldObject, true, true, true, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus) { return displayWindow(level, windowIn, oldObject, transferFocus, true, true, ObjectPosition.OBJECT_CENTER); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, ObjectPosition loc) { return displayWindow(level, windowIn, oldObject, transferFocus, true, true, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld) { return displayWindow(level, windowIn, oldObject, transferFocus, closeOld, true, ObjectPosition.OBJECT_CENTER); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory) { return displayWindow(level, windowIn, oldObject, transferFocus, closeOld, transferHistory, ObjectPosition.OBJECT_CENTER); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject,
		boolean transferFocus, boolean closeOld, boolean transferHistory, ObjectPosition loc) {
		switch (level) {
		case TOP:
			topRenderer.displayWindow(windowIn);
			break;
		case SCREEN:
			if (currentScreen != null) currentScreen.displayWindow(windowIn);
			break;
		}
		return windowIn;
	}
	
	public static TopWindowParent<?> getActiveTopParent() {
		return (GameTopRenderer.isTopFocused()) ? topRenderer : currentScreen;
	}
	
	//--------------
	// Game Loggers
	//--------------
	
	public static Logger getLogger() { return QoTLogger; }
	
	public static void log(Level levelIn, String msg) { QoTLogger.log(levelIn, msg); }
	public static void logf(Level levelIn, String msg, String... args) { QoTLogger.log(levelIn, msg, args); }
	public static void info(String msg) { QoTLogger.log(Level.INFO, msg); }
	public static void infof(String msg, String... args) { QoTLogger.log(Level.INFO, msg, args); }
	public static void warn(String msg) { QoTLogger.log(Level.WARNING, msg); }
	public static void warn(String msg, String... args) { QoTLogger.log(Level.WARNING, msg, args); }
	public static void error(String msg) { QoTLogger.log(Level.SEVERE, msg); }
	public static RuntimeException error(String msg, Throwable throwableIn) {
		QoTLogger.log(Level.SEVERE, msg, throwableIn);
		return new RuntimeException(throwableIn);
	}
	
	//----------------
	// Static Getters
	//----------------
	
	public static boolean isRunning() { return running; }
	public static boolean getGLInit() { return GLFW.glfwInit(); }
	public static boolean isInit() { return init; }
	
	public static int getFPS() { return instance.curFrameRate; }
	/** Returns the game's current amount of ticks run. */
	public static long getRunningTicks() { return getGame().ticks; }
	public static int getTargetFPS() { return (int) instance.getTargetFPSi(); }
	public static int getTargetUPS() { return (int) instance.getTargetUPSi(); }
	
	/** Returns true if the game is currently running in a debug state. */
	public static boolean isDebugMode() { return isDebug; }
	/** Returns the handle pointer that points to the game's window. */
	public static long getWindowHandle() { return handle; }
	/** Returns the game window's draw scale. 1 is default. */
	public static double getGameScale() { return QoTSettings.resolutionScale.get(); }
	
	/**
	 * Returns a WindowSize object containing values pertaining to the
	 * active game window.
	 */
	public static WindowSize getWindowSize() { return new WindowSize(QoT.getGame()); }
	/** Returns the game window's width in pixels. */
	public static int getWidth() { return width; }
	/** Returns the game window's height in pixels. */
	public static int getHeight() { return height; }
	
	public static int getX() {
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(handle, x, y);
		return x.get();
	}
	
	public static int getY() {
		IntBuffer x = BufferUtils.createIntBuffer(1);
		IntBuffer y = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(handle, x, y);
		return y.get();
	}
	
	public static EDimension getWindowDims() {
		IntBuffer xBuff = BufferUtils.createIntBuffer(1);
		IntBuffer yBuff = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(handle, xBuff, yBuff);
		int x = xBuff.get();
		int y = yBuff.get();
		return new EDimension(x, y, x + width, y + height);
	}
	
	public static void setFullScreen(boolean val) {
		if (!QoT.init) return;
		QoTSettings.fullscreen.set(val);
		
		if (QoTSettings.fullscreen.get()) {
			GLFW.glfwGetWindowPos(handle, xPos, yPos);
			GLFW.glfwSetWindowMonitor(handle, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, 0);
		}
		else {
			GLFW.glfwSetWindowMonitor(handle, 0, xPos[0], yPos[0], width, height, 0);
		}
		
		instance.onWindowResize();
	}
	
	public static void setVSync(boolean val) {
		if (!QoT.init) return;
		QoTSettings.vsync.set(val);
		GLFW.glfwSwapInterval((QoTSettings.vsync.get()) ? 1 : 0);
	}
	
	/** Returns this game's central font rendering system. */
	public static FontRenderer getFontRenderer() { return fontRenderer; }
	/** Returns this game's central texture handling system. */
	public static TextureSystem getTextureSystem() { return textureSystem; }
	/** Returns this game's central top level rendering system. */
	public static GameTopRenderer<?> getTopRenderer() { return topRenderer; }
	/** Returns the actively rendered screen. */
	public static GameScreen<?> getCurrentScreen() { return currentScreen; }
	/** Returns this game's central terminal command handler. */
	public static TerminalHandler getTerminalHandler() { return terminalHandler; }
	
	/** Returns this game's constant player object. */
	public static QoT_Player getPlayer() { return thePlayer; }
	/** Returns this game's active world. */
	public static GameWorld getWorld() { return theWorld; }
	
	public static EnvisionLang getEnvision() { return envision; }
	
	//----------------
	// Static Setters
	//----------------
	
	public static QoT_Player setPlayer(QoT_Player p) {
		thePlayer = p;
		return thePlayer;
	}
	/** Sets whether the game should run in a debug state. */
	public static void setDebugMode(boolean val) { isDebug = val; }
	
	public static void setTargetFPS(int fpsIn) { instance.setTargetFPSi(fpsIn); }
	public static void setTargetUPS(int upsIn) { instance.setTargetUPSi(upsIn); }
	
}
