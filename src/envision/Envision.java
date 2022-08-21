package envision;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision.events.EventHandler;
import envision.game.entity.Player;
import envision.game.screens.GameScreen;
import envision.game.sounds.SoundEngine;
import envision.game.world.GameWorld;
import envision.inputHandlers.Keyboard;
import envision.inputHandlers.Mouse;
import envision.inputHandlers.WindowResizeListener;
import envision.layers.LayerHandler;
import envision.renderEngine.RenderEngine;
import envision.terminal.TerminalHandler;
import envision_lang.EnvisionLang;

public final class Envision {
	
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
	
	/** The screen currently being displayed. */
	public static GameScreen<?> currentScreen;
	/** The game's world. */
	public static GameWorld theWorld;
	/** The game's player object. */
	public static Player thePlayer;
	
	//--------------
	// Constructors
	//--------------
	
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
	}
	
	//-------------------------
	// Internal Event Managers
	//-------------------------
	
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
//		topRenderer.onScreenResized();
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
	// Getters
	//---------
	
	public String getGameName() { return gameName; }
	public EnvisionGame getGame() { return gameObject; }
	
}
