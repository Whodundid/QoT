package envision;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision.events.EventHandler;
import envision.game.entity.Player;
import envision.game.screens.GameScreen;
import envision.game.sounds.SoundEngine;
import envision.game.world.gameWorld.GameWorld;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.inputHandlers.WindowResizeListener;
import envision.layers.LayerHandler;
import envision.renderEngine.RenderEngine;
import envision.terminal.TerminalHandler;
import envision.testing.renderingAPI.error.ErrorReportingLevel;
import envision.testing.renderingAPI.error.IRendererErrorReceiver;
import envision.testing.renderingAPI.error.RendererErrorReporter;
import envision.topOverlay.GameTopScreen;
import envision_lang.EnvisionLang;
import game.launcher.LauncherLogger;
import game.settings.QoTSettings;

public final class Envision implements IRendererErrorReceiver {
	
	//--------
	// Logger
	//--------
	
	private static final Logger envisionLogger = LoggerFactory.getLogger(Envision.class);
	
	//--------
	// Fields
	//--------
	
	private static boolean gameCreated = false;
	
	private static EnvisionGame gameObject;
	private static String gameName;
	private static GameScreen startScreen;
	
	//---------------
	// Engine Fields
	//---------------
	
	/** True if the engine's main components have been created and successfully initialized. */
	private static boolean init = false;
	/** True if running in debug mode. */
	private static boolean debug = false;
	/** True if the renderer is actively running. */
	private static boolean running = false;
	/** True if the game is currently paused. */
	private static boolean pause = false;
	/** True if the game world will be rendered. */
	private static boolean renderWorld = true;

	private static RenderEngine renderEngine;
	private static SoundEngine soundEngine;

	private static Keyboard keyboard;
	private static Mouse mouse;
	private static WindowResizeListener resizeListener;
	
	private static EventHandler eventHandler;
	private static TerminalHandler terminalHandler;
	private static LayerHandler layerHandler;
	private static EnvisionLang envisionLang;
	
	/** The top most rendered screen. */
	private static GameTopScreen<?> topScreen;
	/** The screen currently being displayed. */
	public static GameScreen<?> currentScreen;
	/** The game's world. */
	public static GameWorld theWorld;
	/** The game's player object. */
	public static Player thePlayer;
	
	// Game tick stuff
	private long UPS = 60;
	private double timeU = 1000000000 / UPS;
	private double deltaU = 0;
	private long lagCheck = 0;
	private long curNumTicks = 0;
	private long ticks = 0;
	
	// Framerate stuff
	private long FPS = 60; //60 fps by default -- user can modify
	private double timeF = 1000000000 / FPS;
	private double deltaF = 0;
	private long startTime = 0l;
	private long runningTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	//-----------
	// Overrides
	//-----------
	
	public void onRenderErrorReporterMessage(String msg, ErrorReportingLevel reportingLevel) {
		if (reportingLevel == ErrorReportingLevel.HIGH) {
			LauncherLogger.logError(msg);
			System.out.println(msg);
		}
		else {
			LauncherLogger.log(msg);
		}
	}
	
	//-------------------
	// Instance Creation
	//-------------------
	
	public static void createGame(EnvisionGame gameObject) { createGame(gameObject, "Envision Game Engine"); }
	public static void createGame(EnvisionGame gameObject, String gameName) { createInstance(gameObject, gameName); }
	
	/** The singleton engine instance. */
	private static Envision instance = null;
	/** Returns the singleton instance of the EnvisionEngine. Note: could be null if not already built. */
	public static Envision getInstance() { return instance; }
	
	private static final void createInstance(EnvisionGame game, String gameName) {
		if (instance != null || gameCreated)
			throw new IllegalStateException("Engine already created! Only one instance of the Envision Engine may be created per JVM!");
		instance = new Envision(game, gameName);
	}
	
	//--------------
	// Constructors
	//--------------
	
	private Envision(EnvisionGame gameClassIn, String gameNameIn) {
		gameCreated = true;
		gameObject = gameClassIn;
		gameName = gameNameIn;
		
		//set error reporter for renderer
		RendererErrorReporter.setReceiver(this);
		
		//init engine
		renderEngine = RenderEngine.getInstance();
		soundEngine = SoundEngine.getInstance();
		renderEngine.init();
		
		keyboard = Keyboard.getInstance();
		mouse = Mouse.getInstance();
		resizeListener = WindowResizeListener.getInstance();
		
		eventHandler = EventHandler.getInstance();
		terminalHandler = TerminalHandler.getInstance();
		layerHandler = LayerHandler.getInstance();
		envisionLang = new EnvisionLang();
		
		init = true;
		gameObject.onEngineLoad();
		
		//init game
		gameObject.onGameSetup();
	}
	
	//----------
	// Shutdown
	//----------
	
	public static void shutdown() {
		if (instance == null)
			throw new IllegalStateException("No Envision Engine instance exists -- cannot shutdown!");
		instance.shutdownEngine();
	}
	
	private void shutdownEngine() {
		theWorld = null;
		thePlayer = null;
		currentScreen = null;
		
		renderEngine.shutdown();
	}
	
	//-------------------
	// Primary Game Loop
	//-------------------
	
	private void runGameLoop() {
		//ignore if already running
		if (running) return;
		running = true;
		
		//prepare timers
		startTime = System.currentTimeMillis();
		long initialTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		
		while (running && !RenderEngine.shouldClose()) {
			try {
				long currentTime = System.nanoTime();
				deltaU += (currentTime - initialTime) / timeU;
				deltaF += (currentTime - initialTime) / timeF;
				initialTime = currentTime;
				
				//synchronize game inputs to 60 ticks per second by default
				if (deltaU >= 1) {
					//update inputs
					GLFW.glfwPollEvents();
					if (RenderEngine.shouldClose()) { running = false; }
					runGameTick();
					if (ticks == Integer.MAX_VALUE) ticks = 0;
					else ticks++;
					deltaU--;
				}
				
				if (deltaF >= 1) {
					runRenderTick(1);
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
		
		shutdownEngine();
	}
	
	//-------------------------
	// Internal Event Managers
	//-------------------------
	
	private void runGameTick() {
		//process game events
		eventHandler.onGameTick();
		
		//update current screen (if there is one)
		if (currentScreen != null) currentScreen.onGameTick(-1);
		
		//update current world (if there is one, and it's loaded, and the engine is not paused)
		if (theWorld != null && theWorld.isLoaded() && !pause) {
			theWorld.onUpdate();
			theWorld.updateEntities();
		}
	}
	
	private void runRenderTick(long partialTicks) {
		renderEngine.onRenderTick();
	}
	
	public static void keyboardEvent(int action, char typedChar, int keyCode) {
		if (!renderEngine.isContextInit()) return;
	}
	
	public static void mouseEvent(int action, int mXIn, int mYIn, int button, int change) {
		if (!renderEngine.isContextInit()) return;
	}
	
	public static void onWindowResized() {
//		width = WindowResizeListener.getWidth();
//		height = WindowResizeListener.getHeight();
//		GL11.glViewport(0, 0, width, height);
//		
//		if (theWorld != null && theWorld.isLoaded()) theWorld.getWorldRenderer().onWindowResized();
//		if (currentScreen != null) currentScreen.onScreenResized();
//		topScreen.onScreenResized();
	}
	
	//-----------------------
	// Game Loop FPS and UPS
	//-----------------------
	
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
	
	//---------------
	// Debug Logging
	//---------------
	
	public static void info(String msg) { envisionLogger.info(msg); }
	public static void infof(String msg, Object... args) { envisionLogger.info(msg, args); }
	public static void debug(String msg) { envisionLogger.debug(msg); }
	public static void debugf(String msg, Object... args) { envisionLogger.debug(msg, args); }
	public static void warn(String msg) { envisionLogger.warn(msg); }
	public static void warnf(String msg, Object... args) { envisionLogger.warn(msg, args); }
	public static void error(String err) { envisionLogger.error(err); }
	public static void error(String err, Throwable throwable) { envisionLogger.error(err, throwable); }
	public static void errorf(String err, Object... args) { envisionLogger.error(err, args); }
	
	//---------
	// Methods
	//---------
	
	//--------------------------------
	// Public Static Engine Functions
	//--------------------------------
	
	public static GameScreen displayScreen(GameScreen screenIn) { return displayScreen(screenIn, null, true, true); }
	public static GameScreen displayScreen(GameScreen screenIn, boolean init) { return displayScreen(screenIn, null, init, true); }
	public static GameScreen displayScreen(GameScreen screenIn, GameScreen previous) { return displayScreen(screenIn, previous, true, true); }
	/** Attempts to display a new GameScreen in game. */
	public static GameScreen displayScreen(GameScreen screenIn, GameScreen previous, boolean init, boolean fade) {
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
			QoTSettings.lastMap.set(theWorld.getWorldName());
			
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
	
	//---------
	// Getters
	//---------
	
	public static String getGameName() { return gameName; }
	public static EnvisionGame getGame() { return gameObject; }
	
	public static boolean isInit() { return init; }
	public static boolean isRunning() { return running; }
	
	public static int getFPS() { return instance.curFrameRate; }
	public static int getTargetFPS() { return (int) instance.getTargetFPSi(); }
	public static int getTargetUPS() { return (int) instance.getTargetUPSi(); }
	
	/** Returns true if the game is currently running in a debug state. */
	public static boolean isDebugMode() { return debug; }
	
	/** Returns the engine's rendering engine. */
	public static RenderEngine getRenderEngine() { return renderEngine; }
	/** Returns this engine's top level rendering system. */
	public static GameTopScreen<?> getTopScreen() { return topScreen; }
	/** Returns the actively rendered screen. */
	public static GameScreen<?> getCurrentScreen() { return currentScreen; }
	/** Returns this engine's terminal command handler. */
	public static TerminalHandler getTerminalHandler() { return terminalHandler; }
	
	/** Returns this game's constant player object. */
	public static Player getPlayer() { return thePlayer; }
	/** Returns this game's active world. */
	public static GameWorld getWorld() { return theWorld; }
	
	public static EnvisionLang getEnvision() { return envisionLang; }
	
	//---------
	// Setters
	//---------
	
	public static Player setPlayer(Player p) {
		thePlayer = p;
		return thePlayer;
	}
	
	/** Sets whether the game should run in a debug state. */
	public static void setDebugMode(boolean val) { debug = val; }
	
	public static void setTargetFPS(int fpsIn) { instance.setTargetFPSi(fpsIn); }
	public static void setTargetUPS(int upsIn) { instance.setTargetUPSi(upsIn); }
	
}
