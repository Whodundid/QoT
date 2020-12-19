package main;

import assets.entities.player.Player;
import assets.textures.CursorTextures;
import assets.textures.EditorTextures;
import assets.textures.EntityTextures;
import assets.textures.WindowTextures;
import assets.textures.WorldTextures;
import envisionEngine.eWindow.windowTypes.TopWindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowParent;
import envisionEngine.input.Keyboard;
import envisionEngine.input.Mouse;
import envisionEngine.input.WindowResizeListener;
import envisionEngine.terminal.TerminalHandler;
import gameScreens.MainMenuScreen;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.mapSystem.GameWorld;
import gameSystems.screenSystem.GameScreen;
import gameSystems.screenSystem.GameTopRenderer;
import gameSystems.screenSystem.ScreenLevel;
import gameSystems.textureSystem.TextureSystem;
import gameSystems.worldRenderer.WorldRenderer;
import java.io.File;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.settings.MainConfigFile;
import main.settings.QotGameSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import util.miscUtil.OSType;
import util.miscUtil.SysUtil;
import util.miscUtil.TracingPrintStream;
import util.openGL_Util.GLSettings;
import util.openGL_Util.shader.Shaders;
import util.renderUtil.CenterType;
import util.renderUtil.WindowSize;
import util.storageUtil.EDimension;

public class Game {
	
	public static final Logger QoTLogger = Logger.getLogger("QoT");
	public static long handle = -1;
	private static Game instance = null;
	private static MainConfigFile mainConfig;
	public static final QotGameSettings settings = new QotGameSettings();
	
	private static Keyboard keyboard;
	private static Mouse mouse;
	private static WindowResizeListener resizeListener;
	private static FontRenderer fontRenderer;
	private static TextureSystem textureSystem;
	private static WorldRenderer worldRenderer;
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
	
	// Framerate stuff
	public long startTime = 0l;
	public long runningTime = 0l;
	private long frameTime = 0l;
	private int frames = 0;
	private int curFrameRate = 0;
	
	public static long updateCounter = 0;
	
	//-----------------------------------------------
	
	public static void main(String[] args) {
		// This guy VV allows us to see where everything is coming from in console
		TracingPrintStream.enableTrace();
		TracingPrintStream.setTracePrimitives(true);
		Game.getGame().runGame();
	}
	
	//-----------------------------------------------
	
	public static Game getGame() {
		return (instance != null) ? instance : new Game();
	}
	
	private Game() {
		instance = this;
		
		// setup local game directory
		if (!setupUserDir()) {
			throw new RuntimeException("Failed to create game local directory!");
		}
		
		mainConfig.tryLoad();
		
		// setup OpenGL
		if (!GLFW.glfwInit()) {
			System.err.println("GLFW Failed to initialize.");
			System.exit(1);
		}
		
		width = 1080;
		height = 720;
		
		handle = GLFW.glfwCreateWindow(width, height, "LWJGL Program", 0, 0);
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
		worldRenderer = WorldRenderer.getInstance();
		topRenderer = GameTopRenderer.getInstance();
		terminalHandler = TerminalHandler.getInstance();
		
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
		
		terminalHandler.initCommands();
		
		//testing things...
		
		//vbo = GL15.glGenBuffers();
		
		//IntBuffer buff = BufferUtils.createIntBuffer(1);
		//buff.put(0);
		
		//GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, 3 * Float.BYTES, buff);
		//GL20.glEnableVertexAttribArray(0);
	}
	
	private boolean setupUserDir() {
		// determine user OS and get their home directory
		OSType os = SysUtil.getOS();
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
	
	public void runGame() {
		if (!running) {
			running = true;
			
			displayScreen(new MainMenuScreen());
			
			while (running && !GLFW.glfwWindowShouldClose(handle)) {
				
				try {
					runningTime = System.currentTimeMillis() - startTime;
					runTick();
					
					if (GLFW.glfwWindowShouldClose(Game.getWindowHandle())) { running = false; }
					//if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) { running = false; }
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			stopGame();
		}
	}
	
	private void runTick() {
		//update inputs
		GLFW.glfwPollEvents();
		
		if (updateCounter == Long.MAX_VALUE) { updateCounter = 0; }
		else { updateCounter++; }
		
		//update window title
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		String mouseCoords = "(" + mX + ", " + mY + ")";
		GLFW.glfwSetWindowTitle(Game.getWindowHandle(), "QoT      FPS: " + curFrameRate + "              " + mouseCoords);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		renderTick();
		GLFW.glfwSwapBuffers(handle);
		GLSettings.disableAlpha();
		GLSettings.disableBlend();
	}
	
	/** Called from the main game loop to perform all rendering operations. */
	private void renderTick() {
		//update framerate counter
		updateFramerate();
		
		worldRenderer.onRenderTick();
		
		if (currentScreen != null) {
			//System.out.println(currentScreen.getObjectName());
			currentScreen.drawObject(Mouse.getMx(), Mouse.getMy());
		}
		
		topRenderer.onRenderTick();
	}
	
	public static void stopGame() {
		if (running) {
			running = false;
			
			textureSystem.destroyAllTextures();
			
			GLFW.glfwDestroyWindow(handle);
			GLFW.glfwTerminate();
		}
	}
	
	/** Simple framerate calculator. */
	private void updateFramerate() {
		frames++;
		if (System.currentTimeMillis() > frameTime + 1000) {
			curFrameRate = frames;
			frameTime = System.currentTimeMillis();
			frames = 0;
		}
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
		
		if (currentScreen != null) { currentScreen.onWindowResize(); }
		worldRenderer.onWindowResized();
		topRenderer.onWindowResized();
	}
	
	//-----------------------
	// Engine Input Handlers
	//-----------------------
	
	public static void keyboardEvent(int action, char typedChar, int keyCode) {
		if (getGLInit()) {
			topRenderer.handleKeyboardInput(action, typedChar, keyCode);
			//if (currentScreen != null && !topRenderer.hasFocus()) { currentScreen.handleKeyboardInput(action, typedChar, keyCode); }
			worldRenderer.handleKeyboardInput(action, typedChar, keyCode);
		}
	}
	
	public static void mouseEvent(int action, int mXIn, int mYIn, int button, int change) {
		if (getGLInit()) {
			topRenderer.handleMouseInput(action, mXIn, mYIn, button, change);
			//if (currentScreen != null && !topRenderer.hasFocus()) { currentScreen.handleMouseInput(action, mXIn, mYIn, button, change); }
			worldRenderer.handleMouseInput(action, mXIn, mYIn, button, change);
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
		if (theWorld != null) {
			theWorld.setLoaded(false);
		}
		
		theWorld = worldIn;
		worldRenderer.setWorld(theWorld);
		
		return worldIn;
	}
	
	//------------------------
	// Central Window Handles
	//------------------------
	
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn) { return displayWindow(level, windowIn, null, true, false, false, CenterType.screen); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, CenterType loc) { return displayWindow(level, windowIn, null, true, false, false, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, boolean transferFocus) { return displayWindow(level, windowIn, null, transferFocus, false, false, CenterType.screen); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, boolean transferFocus, CenterType loc) { return displayWindow(level, windowIn, null, transferFocus, false, false, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject) { return displayWindow(level, windowIn, oldObject, true, true, true, CenterType.object); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, CenterType loc) { return displayWindow(level, windowIn, oldObject, true, true, true, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus) { return displayWindow(level, windowIn, oldObject, transferFocus, true, true, CenterType.object); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, CenterType loc) { return displayWindow(level, windowIn, oldObject, transferFocus, true, true, loc); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld) { return displayWindow(level, windowIn, oldObject, transferFocus, closeOld, true, CenterType.object); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory) { return displayWindow(level, windowIn, oldObject, transferFocus, closeOld, transferHistory, CenterType.object); }
	public static IWindowParent displayWindow(ScreenLevel level, IWindowParent windowIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory, CenterType loc) {
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
	
	/** Returns true if the game is currently running in a debug state. */
	public static boolean isDebugMode() { return isDebug; }
	/** Returns the handle pointer that points to the game's window. */
	public static long getWindowHandle() { return handle; }
	/** Returns the game window's draw scale. 1 is default. */
	public static double getGameScale() { return gameScale; }
	
	/** Returns a WindowSize object containing values pertaining to the active game window. */
	public static WindowSize getWindowSize() { return new WindowSize(Game.getGame()); }
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
	
	/** Returns this game's central font rendering system. */
	public static FontRenderer getFontRenderer() { return fontRenderer; }
	/** Returns this game's central texture handling system. */
	public static TextureSystem getTextureSystem() { return textureSystem; }
	/** Returns this game's central object rendering system. */
	public static WorldRenderer getWorldRenderer() { return worldRenderer; }
	/** Returns this game's central top level rendering system. */
	public static GameTopRenderer<?> getTopRenderer() { return topRenderer; }
	/** Returns the actively rendererd screen. */
	public static GameScreen<?> getCurrentScreen() { return currentScreen; }
	/** Returns this game's central terminal command handler. */
	public static TerminalHandler getTerminalHandler() { return terminalHandler; }
	
	/** Returns this game's constant player object. */
	public static Player getPlayer() { return thePlayer; }
	
	public static MainConfigFile getMainConfig() { return mainConfig; }
	public static boolean saveConfig() { return mainConfig.trySave(); }
	
	//----------------
	// Static Setters
	//----------------
	
	/** Sets whether the game should run in a debug state. */
	public static void setDebugMode(boolean val) { isDebug = val; }
	
	public static Player setPlayer(Player p) { thePlayer = p; return thePlayer; }
	
	
}
