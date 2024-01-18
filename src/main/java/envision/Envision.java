package envision;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.ZonedDateTime;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import envision.engine.EngineConfig;
import envision.engine.EngineSettings;
import envision.engine.assets.EngineTextures;
import envision.engine.events.EventHandler;
import envision.engine.events.GameEvent;
import envision.engine.inputHandlers.IEnvisionInputReceiver;
import envision.engine.inputHandlers.Keyboard;
import envision.engine.inputHandlers.Mouse;
import envision.engine.inputHandlers.WindowResizeListener;
import envision.engine.loader.GameSettings;
import envision.engine.loader.AbstractWorldCreator;
import envision.engine.loader.EnvisionGame;
import envision.engine.loader.GameLoader;
import envision.engine.loader.LoadedGameDirectory;
import envision.engine.notifications.NotificationHandler;
import envision.engine.notifications.util.NotificationType;
import envision.engine.rendering.GameWindow;
import envision.engine.rendering.RenderEngine;
import envision.engine.rendering.RenderingManager;
import envision.engine.rendering.batching.BatchManager;
import envision.engine.rendering.fontRenderer.FontRenderer;
import envision.engine.rendering.renderingAPI.error.ErrorReportingLevel;
import envision.engine.rendering.renderingAPI.error.IRendererErrorReceiver;
import envision.engine.rendering.renderingAPI.error.RendererErrorReporter;
import envision.engine.rendering.textureSystem.GameTexture;
import envision.engine.rendering.textureSystem.TextureSystem;
import envision.engine.resourceLoaders.textures.TextureLoader;
import envision.engine.screens.GameScreen;
import envision.engine.screens.ScreenLevel;
import envision.engine.settings.UserProfile;
import envision.engine.settings.UserProfileRegistry;
import envision.engine.settings.config.ConfigSetting;
import envision.engine.settings.config.EnvisionConfigFile;
import envision.engine.terminal.TerminalCommandHandler;
import envision.engine.windows.developerDesktop.DeveloperDesktop;
import envision.engine.windows.windowTypes.TopWindowParent;
import envision.engine.windows.windowTypes.interfaces.IWindowParent;
import envision.engine.windows.windowUtil.ObjectPosition;
import envision.game.effects.sounds.SoundEngine;
import envision.game.entities.player.Player;
import envision.game.manager.LevelManager;
import envision.game.world.GameWorld;
import envision.game.world.IGameWorld;
import envision.game.world.layerSystem.LayerSystem;
import envision.launcher.LauncherLogger;
import envision.launcher.LauncherSettings;
import envision_lang.EnvisionLang;
import envision_lang.lang.java.annotations.EClass;
import eutil.datatypes.points.Point2i;
import eutil.datatypes.util.EList;
import eutil.file.FileOpener;
import eutil.math.ENumUtil;
import eutil.math.dimensions.Dimension_i;

@EClass
public final class Envision implements IRendererErrorReceiver, IEnvisionInputReceiver {
	
	//========
	// Logger
	//========
	
	private static final Logger envisionLogger = LoggerFactory.getLogger(Envision.class);
	
	//========
	// Fields
	//========
	
	/** The auto-set date timestamp of when this version was built. */
	public static final String VERSION_DATE = "Dec 24, 2023 - 21:01:32";
	/** The auto-set build number of the engine. */
	public static final String VERSION_BUILD = "83";
	
	public static long updateCounter = 0;
	
	public static EnvisionGame currentGameInstance;
	public static GameWindow gameWindow;
	public static String gameName;
	private static GameScreen startScreen;
	
	//===============
	// Engine Fields
	//===============
	
	/** True if the engine's main components have been created and successfully initialized. */
	private static boolean init = false;
	/** True if running in debug mode. */
	private static boolean debug = false;
	/** True if the renderer is actively running. */
	private static boolean running = false;
	/** True if the game is currently paused. */
	private static boolean pause = false;
	/** True if window rendered in full screen. */
	private static boolean fullscreen = false;
	/** True if the game world will be rendered. */
	private static boolean renderWorld = true;
	
	public static RenderEngine renderEngine;
	private static SoundEngine soundEngine;

	private static Keyboard keyboard;
	private static Mouse mouse;
	private static WindowResizeListener resizeListener;
	private static FontRenderer fontRenderer;
	private static TextureSystem textureSystem;
	
	private static EventHandler eventHandler;
	private static TerminalCommandHandler terminalHandler;
	private static LayerSystem layerHandler;
	private static EnvisionLang envisionLang;
	private static final NotificationHandler notificationHandler = NotificationHandler.getInstance();
	public static final NotificationType envisionNotifaction = new NotificationType("envision", "General", "Envision", "Notifications received on general Envision events.");
	
	private static UserProfileRegistry profileRegistry = new UserProfileRegistry();
	
	private static TextureLoader textureLoader = new TextureLoader("");
	
	/** The top most rendered screen. */
	public static DeveloperDesktop developerDesktop;
	/** The screen currently being displayed. */
	public static GameScreen currentScreen;
	
	/** The engine's active game manager which is in charge of active worlds. */
	public static LevelManager levelManager;
	/** The game's active world. */
	public static IGameWorld theWorld;
	/** The game's active player object. */
	public static Player thePlayer;
	
	// Game tick stuff
	private long TPS = 60;
	private double timeT = 1000.0 / TPS;
	private double deltaT = 0;
	private int curNumTicks = 0;
	private int ticks = 0;
	private long totalRunningTicks = 0L;
	
	// Framerate stuff
	private long FPS = 60; //60 fps by default -- user can modify
	private double timeF = 1000.0 / FPS;
	private double deltaF = 0;
	private long startTime = 0l;
	/** The time of the current update. */
	private long curTime = 0l;
	/** The time of the last update. */
	private long oldTime = 0l;
	/** The change in time between updates. */
	private float dt = 0.0f;
	private long runningTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	//===========
	// Overrides
	//===========
	
	public void onRenderErrorReporterMessage(String msg, ErrorReportingLevel reportingLevel) {
		if (reportingLevel == ErrorReportingLevel.HIGH) {
			LauncherLogger.logError(msg);
			System.out.println(msg);
		}
		else {
			LauncherLogger.log(msg);
		}
	}
	
    //===================
    // Instance Creation
    //===================
    
    /** The singleton engine instance. */
    private static Envision instance = null;
    
    /**
     * Returns the singleton instance of the EnvisionEngine. Note: could be
     * null if not already built.
     */
    public static Envision getInstance() {
        return instance;
    }
	
	//===================
	// Instance Creation
	//===================
	
    public static void initialize() {
        if (init) throw new IllegalStateException("Engine already initialized!");
        instance = new Envision();
    }
    
    //=======
    // Start
    //=======
    
    public static void startEngine() {
        createEngineInstance();
        instance.runGameLoop();
    }
    
    public static void startGame() {
        try {
            instance.runGameLoop();
        }
        catch (Exception e) {
            LauncherLogger.logError(e);
            FileOpener.openFile(LauncherLogger.getLogFile());
            throw e;
        }
    }
    
    private static final void createEngineInstance() {
        if (instance != null) {
            if (currentGameInstance != null) currentGameInstance.onPostEngineLoad();
            return;
        }
        
        if (instance != null) {
            throw new IllegalStateException("Engine already created! Only one instance of the Envision Engine may be created per JVM!");
        }
        
        // init game
        if (currentGameInstance != null) currentGameInstance.onPreEngineLoad();
        instance = new Envision();
        if (currentGameInstance != null) currentGameInstance.onPostEngineLoad();
    }
    
    public static void loadGame(String gameJsonPath) throws Exception {
        File path = new File(gameJsonPath);
        
        if (!path.exists()) throw new FileNotFoundException(gameJsonPath);
        
        LoadedGameDirectory dir = GameLoader.loadGameFromDirectory(path);
        
        if (dir == null) throw new IllegalStateException("Loaded game directory returned null!");
        
        EnvisionGame game = new EnvisionGame(dir);
        currentGameInstance = game;
        gameName = currentGameInstance.getGameName();
        
        if (instance == null) createEngineInstance();
        
        LauncherLogger.log("========================");
        LauncherLogger.log(" Loading game from JSON");
        LauncherLogger.log("========================");
        
        startGame();
    }
    
    public static void loadGame(LauncherSettings settings) {
        currentGameInstance = settings.getGame();
        gameName = currentGameInstance.getGameName();
        
        var gameSettings = currentGameInstance.getGameSettings();
        gameSettings.initializeSettings(settings.INSTALL_DIR, settings.USE_INTERNAL_RESOURCES_PATH);
        
        if (instance == null) createEngineInstance();
        
        LauncherLogger.log("=============================");
        LauncherLogger.log(" Loading game with settings:\n" + settings);
        LauncherLogger.log("=============================\n");
    }
    
//    public static void switchToGame(String gameName) {
//        
//    }
    
    public static void setWindowIcon(GameTexture textureIn) {
        if (!init) return;
        
        GLFWImage image = GLFWImage.malloc();
        GLFWImage.Buffer imagebf = GLFWImage.malloc(1);
        image.set(textureIn.getWidth(), textureIn.getHeight(), textureIn.getImageBytes());
        imagebf.put(0, image);
        
        GLFW.glfwSetWindowIcon(gameWindow.getWindowHandle(), imagebf);
        image.free();
        imagebf.free();
    }
	
	//==============
	// Constructors
	//==============
	
    private Envision() {
        var user = new UserProfile("user");
        profileRegistry.registerProfile(user);
        profileRegistry.registerProfile(new UserProfile("dev", true));
        profileRegistry.setCurrentUser(user);
        
        setupGLFW();
        setupRenderingContext();
        setupEngine();
        
        init = true;
    }
	
	private void setupGLFW() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		// setup GLFW
		if (!GLFW.glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
		}
		
		gameWindow = new GameWindow();
		long handle = gameWindow.getWindowHandle();
		
		keyboard = Keyboard.create(handle, this);
		mouse = Mouse.create(handle, this);
		resizeListener = WindowResizeListener.create(handle, this);
		
		GLFW.glfwShowWindow(gameWindow.getWindowHandle());
	}
	
	private void setupRenderingContext() {
		//set error reporter for renderer
		RendererErrorReporter.setReceiver(Envision.instance);
		
		renderEngine = RenderEngine.getInstance();
		renderEngine.init(gameWindow.getWindowHandle());
		
		if (EngineSettings.batchRendering.getBoolean()) BatchManager.enable();
		else BatchManager.disable();
		
		GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		int interval = (EngineSettings.vsync.getBoolean()) ? 1 : 0;
		GLFW.glfwSwapInterval(interval);
		
		textureSystem = TextureSystem.getInstance();
		fontRenderer = FontRenderer.getInstance();
		
        // register internal engine textures first
        EngineTextures.instance().onRegister(textureSystem);
        // then register the game instance's textures
        if (currentGameInstance != null) currentGameInstance.onRegisterInternalTextures(textureSystem);
	}
	
	private void setupEngine() {
		soundEngine = SoundEngine.getInstance();
		developerDesktop = DeveloperDesktop.getInstance();
		eventHandler = EventHandler.getInstance();
		layerHandler = new LayerSystem();
		envisionLang = EnvisionLang.getInstance();
		
		terminalHandler = TerminalCommandHandler.getInstance();
		terminalHandler.initCommands();
		DeveloperDesktop.buildDesktopFromConfig();
	}
	
	//==========
	// Shutdown
	//==========
	
	public static void shutdown() {
		if (instance == null) {
			throw new IllegalStateException("No Envision Engine instance exists -- cannot shutdown!");
		}
		instance.shutdownEngine();
	}
	
	private void shutdownEngine() {
		info("Shutdown initiated at: " + ZonedDateTime.now());
		running = false;
	}
	
	private void onShutdown() {
		info("Shutting down engine!");
		if (currentGameInstance != null) {
			currentGameInstance.onPreGameUnload();
		}
		
		running = false;
		theWorld = null;
		thePlayer = null;
		currentScreen = null;
		
		if (currentGameInstance != null) {
			currentGameInstance.onPostGameUnload();
		}
		
		TextureSystem.getInstance().destroyAllTextures();
		Keyboard.destroy();
		Mouse.destroy();
		WindowResizeListener.destroy();
		RenderEngine.getInstance().destroy();
		gameWindow.destroy();
		
		System.exit(0);
	}
	
	//===================
	// Primary Game Loop
	//===================
	
	private void runGameLoop() {
		//ignore if already running
		if (running) return;
		running = true;
		
		//prepare timers
		startTime = System.currentTimeMillis();
		oldTime = startTime;
		long timer = startTime;
		long initialTime = System.currentTimeMillis();
		
		GLFW.glfwMakeContextCurrent(getGameWindow().getWindowHandle());
		
		while (running && !GLFW.glfwWindowShouldClose(gameWindow.getWindowHandle())) {
			try {
				long currentTime = System.currentTimeMillis();
				deltaT += (currentTime - initialTime) / timeT;
				deltaF += (currentTime - initialTime) / timeF;
				initialTime = currentTime;
				
				if (deltaT >= 1) {
					oldTime = curTime;
					curTime = System.currentTimeMillis();
					
					// 'dt' is ms :)
					dt = curTime - oldTime;
					
					if (dt > 15.0f) dt = 15.0f;
					
					//update inputs
					GLFW.glfwPollEvents();
					if (GLFW.glfwWindowShouldClose(gameWindow.getWindowHandle())) {
						running = false;
						break;
					}
					
					runGameTick(dt);
					
					if (totalRunningTicks == Long.MAX_VALUE) totalRunningTicks = 0;
					else totalRunningTicks++;
					
					deltaT--;
				}
				
				if (deltaF >= 1) {
					runRenderTick(currentTime - oldTime);
					frames++;
					deltaF--;
				}
				
				// measures fps
				if (System.currentTimeMillis() - timer > 1000) {
					curFrameRate = frames;
					curNumTicks = ticks;
					frames = 0;
					ticks = 0;
					timer += 1000;
				}
				
				if (deltaT > 3 || deltaF > 5) {
					deltaT = 0;
					deltaF = 0;
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		onShutdown();
	}
	
	//=========================
	// Internal Event Managers
	//=========================
	
	private void runGameTick(float dt) {
		if (updateCounter >= 100000000) updateCounter = -1;
		updateCounter++;
		//if (updateCounter % 500 == 0 && BatchManager.isEnabled()) System.out.println(getFPS());
		
		//process game events
		eventHandler.onGameTick();
		
		//update current screen (if there is one)
		if (currentScreen != null) currentScreen.onGameTick(dt);
		developerDesktop.onTickUpdate_i(dt);
		
		//update current world (if there is one, and it's loaded, and the engine is not paused)
		if (levelManager != null && !pause) {
		    levelManager.onGameTick(dt);
		}
		
//		if (theWorld != null && theWorld.isLoaded() && !pause) {
//			theWorld.onGameTick(dt);
//		}
	}
	
	// TODO: TEMP: REMOVE THIS!
	private int mX_old, mY_old;
	
	private void runRenderTick(long dt) {
		if (!running) return;
		if (!BatchManager.isEnabled()) {
			if (theWorld != null && theWorld.isLoaded() && renderWorld) {
				theWorld.onRenderTick(dt);
			}
			
			if (currentScreen != null) {
				currentScreen.drawObject_i(dt, Mouse.getMx(), Mouse.getMy());
				//currentScreen.drawString("deltaF: " + deltaF, 0, currentScreen.endY - currentScreen.midY / 2, EColors.aquamarine);
				//currentScreen.drawString("deltaU: " + deltaU, 0, currentScreen.endY - currentScreen.midY / 2 + 25, EColors.aquamarine);
			}
			else {
				RenderingManager.drawTexture(EngineTextures.noscreens, 128, 128, 384, 384);
				RenderingManager.drawString("No Screens?", 256, 256);
			}
			
			developerDesktop.onRenderTick(dt);
			//renderEngine.getRenderingContext().swapBuffers();
			renderEngine.endFrame();
		}
		else {
		    if (!DeveloperDesktop.isOpen()) {
	            final var pos = RenderEngine.getInstance().perspectiveCamera.position;
	            final var rot = RenderEngine.getInstance().perspectiveCamera.rotation;
	            final float speed = 6.0f;
	            
	            // press
	            if (Keyboard.isKeyDown(Keyboard.KEY_W)) pos.y -= speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_D)) pos.x += speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_S)) pos.y += speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_A)) pos.x -= speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) pos.y -= speed;
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) pos.x += speed;
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) pos.y += speed;
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) pos.x -= speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) pos.z += speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) pos.z -= speed;
	            if (Keyboard.isKeyDown(Keyboard.KEY_PERIOD)) rot.x += 0.0025;
	            if (Keyboard.isKeyDown(Keyboard.KEY_COMMA)) rot.x -= 0.0025;
	            if (Keyboard.isKeyDown(Keyboard.KEY_K)) rot.y += 0.005;
	            if (Keyboard.isKeyDown(Keyboard.KEY_L)) rot.y -= 0.005;
	            if (Keyboard.isKeyDown(Keyboard.KEY_E)) rot.z += 0.005;
	            if (Keyboard.isKeyDown(Keyboard.KEY_Q)) rot.z -= 0.005;
	            if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
	                rot.set(0, 2, 0);
	                pos.z = 0;
	            }
	            
	            RenderingManager.drawString(pos, 50, 150);
	            
	            int mX = Mouse.getMx();
	            int mY = Mouse.getMy();
	            
	            float dX = (float) (mX - mX_old);
	            float dY = (float) (mY - mY_old);
	            
	            mX_old = mX;
	            mY_old = mY;
	            
	            rot.add(new Vector3f(dY * 0.001f, dX * 0.001f, 0), rot);
	            rot.set(ENumUtil.clamp(rot.x, -90, 90), rot.y, 0);
		    }
		    
			renderEngine.draw(dt);
			renderEngine.endFrame();
		}
	}
	
	@Override
	public void onKeyboardInput(int action, char typedChar, int keyCode) {
		if (!renderEngine.isContextInit()) return;
		developerDesktop.handleKeyboardInput(action, typedChar, keyCode);
		
		if (theWorld != null) {
			if (action == 0) { theWorld.getWorldRenderer().keyReleased(typedChar, keyCode); }
			if (action == 1 || action == 2) { theWorld.getWorldRenderer().keyPressed(typedChar, keyCode); }
		}
	}
	
	@Override
	public void onMouseInput(int action, int mXIn, int mYIn, int button, int change) {
		if (!renderEngine.isContextInit()) return;
		developerDesktop.handleMouseInput(action, mXIn, mYIn, button, change);
	}
	
    @Override
    public void onMouseEnteredWindow(int mXIn, int mYIn) {
        
    }
    
    @Override
    public void onMouseExitedWindow(int mXIn, int mYIn) {
        
    }
	
	@Override
	public void onDroppedFiles(EList<String> droppedFileNames) {
	    DeveloperDesktop.getInstance().onSystemDragAndDrop(droppedFileNames);
	}
	
	@Override
	public void onWindowResized(long window, int newWidth, int newHeight) {
		renderEngine.getRenderingContext().onWindowResized();
		gameWindow.onWindowResized(newWidth, newHeight);
		if (theWorld != null && theWorld.isLoaded()) theWorld.getWorldRenderer().onWindowResized();
		if (currentScreen != null) currentScreen.onScreenResized();
		developerDesktop.onScreenResized();
	}
	
	//=======================
	// Game Loop FPS and UPS
	//=======================
	
	private long getTargetFPSi() { return FPS; }
	private long getTargetUPSi() { return TPS; }
	
	private void setTargetUPSi(int upsIn) {
		TPS = upsIn;
		timeT = 1000.0 / TPS;
		deltaT = 0;
		deltaF = 0;
		ticks = 0;
	}
	
	private void setTargetFPSi(int fpsIn) {
		FPS = fpsIn;
		timeF = 1000.0 / FPS;
		deltaT = 0;
		deltaF = 0;
		frames = 0;
	}
	
	//===============
	// Debug Logging
	//===============
	
	public static void trace(String msg) { envisionLogger.trace(msg); }
	public static void trace(String msg, Throwable throwable) { envisionLogger.trace(msg, throwable); }
	public static void tracef(String msg, Object... args) { envisionLogger.trace(msg, args); }
	public static void info(String msg) { envisionLogger.info(msg); }
	public static void info(String msg, Throwable throwable) { envisionLogger.info(msg, throwable); }
	public static void infof(String msg, Object... args) { envisionLogger.info(msg, args); }
	public static void debug(String msg) { envisionLogger.debug(msg); }
	public static void debug(String msg, Throwable throwable) { envisionLogger.debug(msg, throwable); }
	public static void debugf(String msg, Object... args) { envisionLogger.debug(msg, args); }
	public static void warn(String msg) { envisionLogger.warn(msg); }
	public static void warn(String msg, Throwable throwable) { envisionLogger.warn(msg, throwable); }
	public static void warnf(String msg, Object... args) { envisionLogger.warn(msg, args); }
	public static void error(String err) { envisionLogger.error(err); }
	public static void error(String err, Throwable throwable) { envisionLogger.error(err, throwable); }
	public static void errorf(String err, Object... args) { envisionLogger.error(err, args); }
	
	//=========================
	// Static Engine Functions
	//=========================
	
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
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory, ObjectPosition loc) {
		switch (level) {
		case TOP:
			developerDesktop.displayWindow(windowIn);
			break;
		case SCREEN:
			if (currentScreen != null) currentScreen.displayWindow(windowIn);
			break;
		}
		return windowIn;
	}
	
	public static TopWindowParent getActiveTopParent() {
		return (DeveloperDesktop.isOpen()) ? developerDesktop : currentScreen;
	}
	
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
				old.onClosed_i();
				old.onScreenClosed();
			}
			
			if (previous != null) {
			    currentScreen.setScreenHistory(old.getScreenHistory());
				//System.out.println("PRE: " + screenIn + " : " + currentScreen.getScreenHistory());
			    currentScreen.getScreenHistory().push(old);
				//System.out.println("POST: " + screenIn + " : " + currentScreen.getScreenHistory());
			}
			
			if (init) {
				try {
				    currentScreen.onPreInit();
					currentScreen.setWindowSize();
					currentScreen.initScreen();
					currentScreen.reInitChildren();
					currentScreen.onPostInit();
				}
				catch (Exception e) {
					e.printStackTrace();
					currentScreen = old;
					
					currentScreen.onPreInit();
                    currentScreen.setWindowSize();
                    currentScreen.initScreen();
                    currentScreen.reInitChildren();
                    currentScreen.onPostInit();
				}
			}
		}
		else {
			GameScreen old = currentScreen;
			currentScreen = screenIn;
			
			if (old != null) {
				old.close();
				old.onClosed_i();
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
	public static GameWorld loadLevel(GameWorld worldIn) {
	    return loadLevel(worldIn, true);
	}
    
    public static GameWorld loadLevel(GameWorld worldIn, boolean isNewLevel) {
//        if (theWorld != null) {
//            QoTSettings.lastMap.set(theWorld.getWorldName().replace(".twld", ""));            
//        }
        
        if (isNewLevel || levelManager == null) {
            levelManager = new LevelManager(worldIn);
            levelManager.loadDefaultRules();
            levelManager.loadStartingWorld();
        }
        else {
            levelManager.loadWorld(worldIn);
        }
        
        pause = false;
        renderWorld = true;
        if (currentScreen != null) currentScreen.onWorldLoaded();
        
        // check if loaded
        if (theWorld != null && !theWorld.isLoaded()) warn("Failed to load world: ");
        
        return worldIn;
    }
    
    public static void loadWorld(File worldFile) {
        if (levelManager == null) {
            levelManager = new LevelManager();
            levelManager.loadDefaultRules();
        }
        levelManager.tryLoadWorld(worldFile);
    }
	
	public static boolean isPaused() { return pause; }
	public static boolean isWorldRenderPaused() { return !renderWorld; }
	
	public static void pause() { pause = true; }
	public static void unpause() { pause = false; }
	public static void pauseWorldRender() { renderWorld = false; }
	public static void unpauseWorldRender() { renderWorld = true; }
	
	//=========
	// Getters
	//=========
	
	/** Returns the name of the game object that the engine is running. */
	public static String getGameName() { return gameName; }
	/** Returns the game object that the engine is running. */
	public static EnvisionGame getGame() { return currentGameInstance; }
	
	/** Returns true if the Envision Engine has been fully initialized. */
	public static boolean isInit() { return init; }
	/** Returns true if the engine is actively running a game. */
	public static boolean isRunning() { return running; }
	/** Returns true if the game is currently running in a debug state. */
	public static boolean isDebugMode() { return debug; }
	
	/** Returns the FPS (frames per second) of the active game's game window. */
	public static int getFPS() { return instance.curFrameRate; }
	/** Returns the TPS (ticks per second) of the engine. */
	public static int getTPS() { return instance.curNumTicks; }
	/** Returns the engine's current total number of game ticks run. */
	public static long getRunningTicks() { return instance.totalRunningTicks; }
	/** Returns the FPS target that the window is trying to run at. */
	public static int getTargetFPS() { return (int) instance.getTargetFPSi(); }
	/** Returns the UPS (updates per second) that the game is trying to run at. */
	public static int getTargetTPS() { return (int) instance.getTargetUPSi(); }
	/** Returns the change in time between ticks. */
	public static float getDeltaTime() { return instance.dt; }
	
    public static EngineConfig getEngineConfig() { return EngineSettings.ENGINE_CONFIG; }
    public static boolean reloadEngineConfig() { return EngineSettings.ENGINE_CONFIG.loadConfig(); }
    public static boolean saveEngineConfig() { return EngineSettings.ENGINE_CONFIG.saveConfig(); }
	
	/** Returns Envision's underlying EventHandler. */
	public static EventHandler getEventHandler() { return eventHandler; }
	/** Posts a game event to the engine's event handler. */
	public static EventHandler postEvent(GameEvent event) {
		eventHandler.postEvent(event);
		return eventHandler;
	}
	
	/** Returns Envision's notification handler instance. */
	public static NotificationHandler getNotificationHandler() { return notificationHandler; }
	
	/** Returns the engine's rendering engine. */
	public static RenderEngine getRenderEngine() { return renderEngine; }
	/** Returns this engine's top level rendering system. */
	public static DeveloperDesktop getDeveloperDesktop() { return developerDesktop; }
	/** Returns the actively rendered screen. */
	public static GameScreen getCurrentScreen() { return currentScreen; }
	/** Returns this engine's terminal command handler. */
	public static TerminalCommandHandler getTerminalHandler() { return terminalHandler; }
	/** Returns this game's constant player object. */
	public static Player getPlayer() { return thePlayer; }
	/** Returns this game's active world. */
	public static IGameWorld getWorld() { return theWorld; }
	/** Returns the game's window. */
	public static GameWindow getGameWindow() { return gameWindow; }
	/** Returns this game's central font rendering system. */
	public static FontRenderer getFontRenderer() { return fontRenderer; }
	/** Returns this game's central texture handling system. */
	public static TextureSystem getTextureSystem() { return textureSystem; }
	/** Returns the primary Envision engine instance. */
	public static EnvisionLang getEnvisionLang() { return envisionLang; }
	
    public static void enableVSync(boolean val) { gameWindow.enableVSync(val); }
    public static void setFullscreen(boolean val) { gameWindow.setFullscreen(val); }
    
	/** Returns the current user. */
	public static UserProfile getCurrentUser() { return profileRegistry.getCurrentUser(); }
	/** Returns the user profile registry. */
	public static UserProfileRegistry getProfileRegistry() { return profileRegistry; }
	
	//================================
	// Game Screen Dimension Wrappers
	//================================
	
	public static Dimension_i getWindowDims() { return gameWindow.getWindowDims(); }
	public static Point2i getWindowPosition() { return gameWindow.getWindowPosition(); }
	public static int getWidth() { return gameWindow.getWidth(); }
	public static int getHeight() { return gameWindow.getHeight(); }
	/** Returns the game window's draw scale. 1 is default. */
	public static double getGameScale() { return EngineSettings.resolutionScale.get(); }
	public static long getWindowHandle() { return gameWindow.getWindowHandle(); }
	
	//=========
	// Setters
	//=========
	
	public static Player setPlayer(Player p) {
		thePlayer = p;
		return thePlayer;
	}
	
	/** Sets whether the game should run in a debug state. */
	public static void setDebugMode(boolean val) { debug = val; }
	
	/** Used to specify the FPS (frames per second) that the game window will try to run at. */
	public static void setTargetFPS(int fpsIn) { instance.setTargetFPSi(fpsIn); }
	/** Used to specify the UPS (updates per second) that the game will try to run at. */
	public static void setTargetUPS(int upsIn) { instance.setTargetUPSi(upsIn); }
	
    //===============================
    // Static Game Instance Wrappers
    //===============================
    
    public static GameScreen getStartScreen() { return currentGameInstance.getStartScreen(); }
    public static GameScreen getMainMenuScreen() { return currentGameInstance.getMainMenuScreen(); }
    public static GameScreen getNewGameScreen() { return currentGameInstance.getNewGameScreen(); }
    public static GameSettings getGameSettings() { return currentGameInstance.getGameSettings(); }
    public static EList<ConfigSetting<?>> getGameConfigSettings() { return getGameSettings().getConfigSettings(); }
    public static EnvisionConfigFile getGameConfig() { return currentGameInstance.getGameConfig(); }
    public static boolean reloadGameConfig() { var c = getGameConfig(); return (c != null) && c.loadConfig(); }
    public static boolean saveGameConfig() { var c = getGameConfig(); return (c != null) && c.saveConfig(); }
    public static File getGameResourcesDirectory() { return currentGameInstance.getResourcesDirectory(); }
    public static File getGameInstallationDirectory() { return currentGameInstance.getInstallationDirectory(); }
    public static File getGameSavedGamesDirectory() { return currentGameInstance.getSavedGamesDirectory(); }
    public static AbstractWorldCreator getGameWorldCreator() { return currentGameInstance.getWorldCreator(); }
	
}
