package main;

import assets.entities.Player;
import assets.items.Items;
import assets.textures.CursorTextures;
import assets.textures.EditorTextures;
import assets.textures.EntityTextures;
import assets.textures.GeneralTextures;
import assets.textures.WindowTextures;
import assets.textures.WorldTextures;
import debug.terminal.TerminalHandler;
import eutil.storage.EDims;
import eutil.sys.ESystemInfo;
import eutil.sys.OSType;
import eutil.sys.TracingPrintStream;
import gameScreens.MainMenuScreen;
import gameScreens.screenUtil.GameScreen;
import gameScreens.screenUtil.GameTopRenderer;
import gameScreens.screenUtil.ScreenLevel;
import input.Keyboard;
import input.Mouse;
import input.WindowResizeListener;
import java.io.File;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.settings.MainConfigFile;
import main.settings.QotGameSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import renderEngine.fontRenderer.FontRenderer;
import renderEngine.shaders.Shaders;
import renderEngine.textureSystem.TextureSystem;
import windowLib.windowTypes.TopWindowParent;
import windowLib.windowTypes.interfaces.IWindowParent;
import windowLib.windowUtil.ObjectPosition;
import world.GameWorld;

public class QoT {
	
	public static final Logger QoTLogger = Logger.getLogger("QoT");
	public static long handle = -1;
	private static QoT instance = null;
	private static MainConfigFile mainConfig;
	public static final QotGameSettings settings = new QotGameSettings();
	
	private static Keyboard keyboard;
	private static Mouse mouse;
	private static WindowResizeListener resizeListener;
	private static FontRenderer fontRenderer;
	private static TextureSystem textureSystem;
	
	private static GameTopRenderer<?> topRenderer;
	private static TerminalHandler terminalHandler;
	
	private static double gameScale = 1;
	private static int width;
	private static int height;
	
	public static Player thePlayer;
	public static GameWorld theWorld;
	
	/** The screen currently being displayed. */
	public static GameScreen<?> currentScreen;
	
	/** Enables debug mode if true. */
	private static boolean isDebug = false;
	/** Indicates whether the game is actively running or not. */
	private static boolean running = false;
	
	// Game tick stuff
	private long UPS = 60;
	double timeU = 1000000000 / UPS;
	private int curNumTicks = 0;
	private int ticks = 0;
	
	// Framerate stuff
	private long FPS = 144;
	double timeF = 1000000000 / FPS;
	public long startTime = 0l;
	public long runningTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	public static long updateCounter = 0;
	
	//-----------------------------------------------
	
	public static void main(String[] args) {
		// This guy VV allows us to see where everything is coming from in console
		TracingPrintStream.enableTrace();
		TracingPrintStream.setTracePrimitives(true);
		getGame().runGameLoop();
	}
	
	//-----------------------------------------------
	
	public static QoT getGame() {
		return (instance != null) ? instance : new QoT();
	}
	
	private QoT() {
		instance = this;
		
		// setup local game directory
		if (!setupUserDir()) {
			throw new RuntimeException("Failed to create game local directory!");
		}
		
		mainConfig.tryLoad();
		setTargetFPSi(settings.targetFPS.get());
		setTargetUPSi(settings.targetUPS.get());
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		// setup OpenGL
		if (!GLFW.glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
		}
		
		width = 1080;
		height = 720;
		
		handle = GLFW.glfwCreateWindow(width, height, "LWJGL Program", 0, 0);
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			
			GLFW.glfwGetWindowSize(handle, w, h);
			GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			
			int x = (vidmode.width() - w.get(0)) / 2;
			int y = (vidmode.height() - h.get(0)) / 2;
			GLFW.glfwSetWindowPos(handle, x, y);
		}
		
		GLFW.glfwMakeContextCurrent(handle);
		
		GL.createCapabilities();
		GLFW.glfwSwapInterval(0);
		
		//initialize shaders
		Shaders.init();
		
		//initialize other things
		keyboard = Keyboard.getInstance();
		mouse = Mouse.getInstance();
		resizeListener = WindowResizeListener.getInstance();
		textureSystem = TextureSystem.getInstance();
		fontRenderer = FontRenderer.getInstance();
		topRenderer = GameTopRenderer.getInstance();
		terminalHandler = TerminalHandler.getInstance();
		//tickManager = new TickManager();
		
		GLFW.glfwSetKeyCallback(handle, keyboard);
		GLFW.glfwSetMouseButtonCallback(handle, mouse);
		GLFW.glfwSetCursorPosCallback(handle, mouse.getCursorPosCallBack());
		GLFW.glfwSetScrollCallback(handle, mouse.getScrollCallBack());
		GLFW.glfwSetWindowSizeCallback(handle, resizeListener);
		
		GLFW.glfwShowWindow(handle);
		
		WorldTextures.registerTextures(textureSystem);
		EntityTextures.registerTextures(textureSystem);
		EditorTextures.registerTextures(textureSystem);
		WindowTextures.registerTextures(textureSystem);
		CursorTextures.registerTextures(textureSystem);
		GeneralTextures.registerTextures(textureSystem);
		
		terminalHandler.initCommands();
		
		Items.lesserHealing.setTexture(EntityTextures.thyrah);
		Items.woodSword.setTexture(WorldTextures.stone);
	}
	
	private boolean setupUserDir() {
		// determine user OS and get their home directory
		OSType os = ESystemInfo.getOS();
		String homeDir = System.getProperty("user.home");
		
		File dir = null;
		
		switch (os) {
		case WINDOWS: dir = new File(homeDir + "\\AppData\\Roaming\\QoT");
		case MAC:
		case LINUX:
		case SOLARIS: // no idea how to handle yet
		default: break;
		}
		
		// initialize game directories
		settings.initDirectories(dir);
		
		// establish game main config
		mainConfig = new MainConfigFile(new File(dir, "MainConfig"));
		
		// setup successful
		return true;
	}
	
	public void runGameLoop() {
		if (!running) {
			running = true;
			startTime = System.currentTimeMillis();
			
			/*
			try {
				Path dir = Files.createTempDirectory("script");
				File script = new File(dir.toFile(), "main.nvis");
				FileWriter w = new FileWriter(script);
				w.write(WorldInit.worldInit);
				w.close();
				script.deleteOnExit();
				
				Envision env = Envision.buildProgram(script);
				File map = new File(QoT.settings.getEditorWorldsDir(), QoT.settings.lastMap.get());
				GameWorld world = new GameWorld(map);
				QoTWorldClass worldClass = new QoTWorldClass(world);
				EnvisionInterpreter interpreter = env.getWorkingDirectory().getMain().getInterpreter();
				Scope scope = interpreter.scope();
				scope.define("world", worldClass.build(interpreter));
				
				env.runProgram();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			*/
			
			displayScreen(new MainMenuScreen());
			
			long initialTime = System.nanoTime();
			double deltaU = 0, deltaF = 0;
			long timer = System.currentTimeMillis();
			
			while (running && !GLFW.glfwWindowShouldClose(handle)) {
				try {
					long currentTime = System.nanoTime();
					deltaU += (currentTime - initialTime) / timeU;
					deltaF += (currentTime - initialTime) / timeF;
					initialTime = currentTime;
					
					//synchronize game inputs to 60 ticks per second
					if (deltaU >= 1) {
						//update inputs
						GLFW.glfwPollEvents();
						if (GLFW.glfwWindowShouldClose(handle)) { running = false; }
						runGameTick();
						curNumTicks++;
						deltaU--;
					}
					
					if (deltaF >= 1) {
						onRenderTick(1);
						frames++;
						deltaF--;
					}
					
					if (System.currentTimeMillis() - timer > 1000) {
						curFrameRate = frames;
						frames = 0;
						ticks = 0;
						timer += 1000;
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
	}
	
	private void runGameTick() {
		curNumTicks++;
		
		//update window title
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		String mouseCoords = "(" + mX + ", " + mY + ")";
		GLFW.glfwSetWindowTitle(QoT.getWindowHandle(), "QoT      FPS: " + curFrameRate + "              " + mouseCoords);
	}
	
	/** Called from the main game loop to perform all rendering operations. */
	private void onRenderTick(long partialTicks) {
		//updateFramerate();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		if (theWorld != null && theWorld.isLoaded()) {
			theWorld.getWorldRenderer().onRenderTick();
		}
		
		if (currentScreen != null) {
			//System.out.println(currentScreen.getObjectName());
			currentScreen.drawObject(Mouse.getMx(), Mouse.getMy());
		}
		
		topRenderer.onRenderTick();
		
		GLFW.glfwSwapBuffers(handle);
		//GLSettings.disableAlpha();
		//GLSettings.disableBlend();
	}
	
	public static void stopGame() {
		if (running) { running = false; }
	}
	
	//----------------
	// Public Methods
	//----------------
	
	/** Called whenever the game window itself is resized. Do not manually call! */
	public void onWindowResize() {
		WindowResizeListener w = WindowResizeListener.getInstance();
		width = w.getWidth();
		height = w.getHeight();
		GL11.glViewport(0, 0, width, height);
		
		if (theWorld != null && theWorld.isLoaded()) { theWorld.getWorldRenderer().onWindowResized(); }
		if (currentScreen != null) { currentScreen.onWindowResize(); }
		topRenderer.onWindowResized();
	}
	
	private long getTargetFPSi() { return FPS; }
	private long getTargetUPSi() { return UPS; }
	
	private void setTargetUPSi(int upsIn) {
		UPS = upsIn;
		timeU = 1000000000 / UPS;
	}
	
	private void setTargetFPSi(int fpsIn) {
		FPS = fpsIn;
		timeF = 1000000000 / FPS;
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
	
	public static GameScreen displayScreen(GameScreen screenIn) { return displayScreen(screenIn, null, true); }
	public static GameScreen displayScreen(GameScreen screenIn, boolean init) { return displayScreen(screenIn, true); }
	public static GameScreen displayScreen(GameScreen screenIn, GameScreen previous) { return displayScreen(screenIn, previous, true); }
	/** Attempts to display a new GameScreen in game. */
	public static GameScreen displayScreen(GameScreen screenIn, GameScreen previous, boolean init) {
		if (screenIn != null) {
			GameScreen old = currentScreen;
			currentScreen = screenIn;
			
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
					currentScreen.initObjects();
				}
				catch (Exception e) {
					e.printStackTrace();
					currentScreen = old;
					
					old.setWindowSize();
					currentScreen.initScreen();
					currentScreen.initObjects();
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
	
	public static GameWorld loadWorld(GameWorld worldIn) {
		//unload the last world (if there was one)
		if (theWorld != null) {
			theWorld.setLoaded(false);
		}
		
		theWorld = worldIn;
		
		if (theWorld != null) {
			//load the world
			theWorld.setLoaded(true);
			
			//check if loaded
			if (!theWorld.isLoaded()) {
				System.out.println("Failed to load world: ");
			}
		}
		
		return worldIn;
	}
	
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
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory, ObjectPosition loc) {
		switch (level) {
		case TOP: topRenderer.displayWindow(windowIn); break;
		case SCREEN: if (currentScreen != null) { currentScreen.displayWindow(windowIn); } break;
		}
		return windowIn;
	}
	
	public static TopWindowParent<?> getActiveTopParent() { return (currentScreen != null) ? currentScreen : topRenderer; }
	
	//--------------
	// Game Loggers
	//--------------
	
	public static void log(Level levelIn, String msg) { QoTLogger.log(levelIn, msg); }
	public static void info(String msg) { QoTLogger.log(Level.INFO, msg); }
	public static void error(String msg) { QoTLogger.log(Level.SEVERE, msg); }
	public static void error(String msg, Throwable throwableIn) { QoTLogger.log(Level.SEVERE, msg, throwableIn); }
	
	//----------------
	// Static Getters
	//----------------
	
	public static boolean isRunning() { return running; }
	public static boolean getGLInit() { return GLFW.glfwInit(); }
	
	public static int getFPS() { return instance.curFrameRate; }
	public static int getTargetFPS() { return (int) instance.getTargetFPSi(); }
	public static int getTargetUPS() { return (int) instance.getTargetUPSi(); }
	
	/** Returns true if the game is currently running in a debug state. */
	public static boolean isDebugMode() { return isDebug; }
	/** Returns the handle pointer that points to the game's window. */
	public static long getWindowHandle() { return handle; }
	/** Returns the game window's draw scale. 1 is default. */
	public static double getGameScale() { return gameScale; }
	
	/** Returns a WindowSize object containing values pertaining to the active game window. */
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
	
	public static EDims getWindowDims() {
		IntBuffer xBuff = BufferUtils.createIntBuffer(1);
		IntBuffer yBuff = BufferUtils.createIntBuffer(1);
		GLFW.glfwGetWindowPos(handle, xBuff, yBuff);
		int x = xBuff.get();
		int y = yBuff.get();
		return new EDims(x, y, x + width, y + height);
	}
	
	/** Returns this game's central font rendering system. */
	public static FontRenderer getFontRenderer() { return fontRenderer; }
	/** Returns this game's central texture handling system. */
	public static TextureSystem getTextureSystem() { return textureSystem; }
	/** Returns this game's central top level rendering system. */
	public static GameTopRenderer<?> getTopRenderer() { return topRenderer; }
	/** Returns the actively rendererd screen. */
	public static GameScreen<?> getCurrentScreen() { return currentScreen; }
	/** Returns this game's central terminal command handler. */
	public static TerminalHandler getTerminalHandler() { return terminalHandler; }
	
	/** Returns the game's current tickrate. */
	public static long getTickrate() { return getGame().curFrameRate; }
	
	/** Returns this game's constant player object. */
	public static Player getPlayer() { return thePlayer; }
	/** Returns this game's active world. */
	public static GameWorld getWorld() { return theWorld; }
	
	public static MainConfigFile getMainConfig() { return mainConfig; }
	public static boolean saveConfig() { return mainConfig.trySave(); }
	
	//----------------
	// Static Setters
	//----------------
	
	/** Sets whether the game should run in a debug state. */
	public static void setDebugMode(boolean val) { isDebug = val; }
	
	public static Player setPlayer(Player p) { thePlayer = p; return thePlayer; }
	public static void setTargetFPS(int fpsIn) { instance.setTargetFPSi(fpsIn); }
	public static void setTargetUPS(int upsIn) { instance.setTargetUPSi(upsIn); }
	
}
