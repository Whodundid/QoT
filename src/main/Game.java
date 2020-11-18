package main;

import eWindow.windowTypes.OverlayWindow;
import eWindow.windowTypes.WindowParent;
import eWindow.windowTypes.interfaces.IWindowObject;
import eWindow.windowTypes.interfaces.IWindowParent;
import gameScreens.TestScreen;
import gameSystems.fontRenderer.FontRenderer;
import gameSystems.gameRenderer.AbstractScreen;
import gameSystems.gameRenderer.GameRenderer;
import gameSystems.gameRenderer.IRendererProxy;
import gameSystems.gameRenderer.GameScreen;
import gameSystems.textureSystem.TextureSystem;
import gameTextures.EntityTextures;
import gameTextures.Textures;
import input.Keyboard;
import input.Mouse;
import input.WindowResizeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import openGL_Util.shader.Shaders;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import terminal.TerminalHandler;
import terminal.window.ETerminal;
import util.renderUtil.CenterType;
import util.renderUtil.WindowSize;
import util.storageUtil.EArrayList;
import util.storageUtil.EDimension;

public class Game {
	
	public static final Logger QoTLogger = Logger.getLogger("QoT");
	public static long handle = -1;
	private static Game instance = null;
	
	private static Keyboard keyboard;
	private static Mouse mouse;
	private static WindowResizeListener resizeListener;
	private static FontRenderer fontRenderer;
	private static TextureSystem textureSystem;
	private static GameRenderer gameRenderer;
	private static TerminalHandler terminalHandler;
	
	private static double gameScale = 1;
	private static int width;
	private static int height;
	
	/** The screen currently being displayed. */
	public static AbstractScreen currentScreen;
	
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
	
	private int vbo;
	
	//-----------------------------------------------
	
	public static Game getGame() {
		return (instance != null) ? instance : new Game();
	}
	
	private Game() {
		instance = this;
		
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
		gameRenderer = GameRenderer.getInstance();
		terminalHandler = TerminalHandler.getInstance();
		
		GLFW.glfwSetKeyCallback(handle, keyboard);
		GLFW.glfwSetMouseButtonCallback(handle, mouse);
		GLFW.glfwSetCursorPosCallback(handle, mouse.getCursorPosCallBack());
		GLFW.glfwSetScrollCallback(handle, mouse.getScrollCallBack());
		GLFW.glfwSetWindowSizeCallback(handle, resizeListener);
		
		GLFW.glfwShowWindow(handle);
		
		Textures.registerTextures(textureSystem);
		EntityTextures.registerTextures(textureSystem);
		
		terminalHandler.initCommands();
		
		//testing things...
		
		//vbo = GL15.glGenBuffers();
		
		//IntBuffer buff = BufferUtils.createIntBuffer(1);
		//buff.put(0);
		
		//GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, 3 * Float.BYTES, buff);
		//GL20.glEnableVertexAttribArray(0);
	}
	
	public void runGame() {
		if (!running) {
			//Thread t = new Thread(Main.m);
			//t.start();
			
			running = true;
			
			displayScreen(new TestScreen(null));
			
			while (running && !GLFW.glfwWindowShouldClose(handle)) {
				
				try {
					runningTime = System.currentTimeMillis() - startTime;
					runTick();
					
					if (GLFW.glfwWindowShouldClose(Game.getWindowHandle())) { running = false; }
					if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) { running = false; }
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
		
		//debug terminal
		if (Keyboard.isKeyDown(GLFW.GLFW_KEY_V) && !isEGuiOpen(ETerminal.class)) {
			displayWindow(new ETerminal());
		}
		
		//update window title
		int mX = Mouse.getMx();
		int mY = Mouse.getMy();
		String mouseCoords = "(" + mX + ", " + mY + ")";
		GLFW.glfwSetWindowTitle(Game.getWindowHandle(), "QoT      FPS: " + curFrameRate + "              " + mouseCoords);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		renderTick();
		GLFW.glfwSwapBuffers(handle);
	}
	
	/** Called from the main game loop to perform all rendering operations. */
	private void renderTick() {
		//update framerate counter
		updateFramerate();
		
		if (currentScreen != null) {
			currentScreen.drawObject(Mouse.getMx(), Mouse.getMy());
		}
		
		//GLObject.drawRect(50, 50, 150, 150, EColors.red);
		
		//GLObject.drawFilledEllipse(300, 300, 50, 50, 30, EColors.seafoam);
		
		//GLObject.drawTexture(400, 200, 128 * 2, 64 * 2, Textures.debug);
		
		gameRenderer.onRenderTick();
	}
	
	public static void stopGame() {
		if (running) {
			running = false;
			
			textureSystem.destroyAllTextures();
			//GL.setCapabilities(null);
			
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
		gameRenderer.onWindowResized();
	}
	
	//--------------------------------
	// Public Static Engine Functions
	//--------------------------------
	
	/** Attempts to display a new AbstractScreen in game. */
	public static void displayScreen(AbstractScreen screenIn) {
		if (screenIn != null) {
			AbstractScreen old = currentScreen;
			
			if (old != null) {
				gameRenderer.removeObject(currentScreen);
				currentScreen.removeAllObjects();
				old.onScreenClosed();
			}
			
			currentScreen = screenIn;
			gameRenderer.addObject(currentScreen);
			currentScreen.initScreen();
			currentScreen.postInit();
		}
	}
	
	/** Returns true if the specified window parent is open. */
	public static <T extends WindowParent> boolean isEGuiOpen(Class<T> windowIn) {
		return windowIn != null ? getGameRenderer().getAllChildren().stream().anyMatch(o -> o.getClass() == windowIn) : false;
	}
	
	/** Returns a list of all actively drawn window parents. */
	public static EArrayList<WindowParent> getAllActiveWindows() {
		EArrayList<WindowParent> windows = new EArrayList();
		try {
			getGameRenderer().getAllChildren().stream().filter(o -> WindowParent.class.isInstance(o)).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((WindowParent) w));
		}
		catch (Exception e) { e.printStackTrace(); }
		return windows;
	}
	
	/** Returns the first active instance of a specified type of window parent. If none are active, null is returned instead. */
	public static <T extends WindowParent> WindowParent getWindowInstance(Class<T> windowIn) {
		return (windowIn != null) ? (WindowParent) (getGameRenderer().getAllChildren().filter(o -> o.getClass() == windowIn).getFirst()) : null;
	}
	
	/** Returns a list of all actively drawn window parents of a given type. */
	public static <T extends WindowParent> EArrayList<T> getAllWindowInstances(Class<T> windowIn) {
		EArrayList<T> windows = new EArrayList();
		try {
			getGameRenderer().getAllChildren().stream().filter(o -> o.getClass() == windowIn).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((T) w));
		}
		catch (Exception e) { e.printStackTrace(); }
		return windows;
	}
	
	/** Reloads all actively drawn windows. */
	public static void reloadAllWindows() { getAllActiveWindows().forEach(w -> w.sendArgs("Reload")); }
	/** Reloads all actively drawn windows and sends a set of arguments to each. */
	public static void reloadAllWindows(Object... args) { getAllActiveWindows().forEach(w -> w.sendArgs("Reload", args)); }
	/** Reloads all actively drawn windows of a specific type. */
	public static <T extends WindowParent> void reloadAllWindowInstances(Class<T> windowIn) { getAllWindowInstances(windowIn).forEach(w -> w.sendArgs("Reload")); }
	/** Reloads all actively drawn windows of a specific type and sends a set of arguments to each. */
	public static <T extends WindowParent> void reloadAllWindowInstances(Class<T> windowIn, Object... args) { getAllWindowInstances(windowIn).forEach(w -> w.sendArgs("Reload", args)); }
	
	/** Displays the specified window parent. */
	public static IWindowParent displayWindow(IWindowParent guiIn) { return displayWindow(guiIn, null, true, false, false, CenterType.screen); }
	/** Displays the specified window parent around a specific location on the screen. */
	public static IWindowParent displayWindow(IWindowParent guiIn, CenterType loc) { return displayWindow(guiIn, null, true, false, false, loc); }
	/** Displays the specified window parent and specifies whether focus should be transfered to it. */
	public static IWindowParent displayWindow(IWindowParent guiIn, boolean transferFocus) { return displayWindow(guiIn, null, transferFocus, false, false, CenterType.screen); }
	/** Displays the specified window parent where focus transfer properties can be set along with where it is drawn. */
	public static IWindowParent displayWindow(IWindowParent guiIn, boolean transferFocus, CenterType loc) { return displayWindow(guiIn, null, transferFocus, false, false, loc); }
	/** Displays the specified window parent and passes a previous window for history traversal means. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject) { return displayWindow(guiIn, oldObject, true, true, true, CenterType.object); }
	/** Displays the specified window parent, passes a previous window, and sets where this window will be relatively positioned. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject, CenterType loc) { return displayWindow(guiIn, oldObject, true, true, true, loc); }
	/** Displays the specified window parent with variable arguments. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject, boolean transferFocus) { return displayWindow(guiIn, oldObject, transferFocus, true, true, CenterType.object); }
	/** Displays the specified window parent with variable arguments. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject, boolean transferFocus, CenterType loc) { return displayWindow(guiIn, oldObject, transferFocus, true, true, loc); }
	/** Displays the specified window parent with variable arguments. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld) { return displayWindow(guiIn, oldObject, transferFocus, closeOld, true, CenterType.object); }
	/** Displays the specified window parent with variable arguments. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory) { return displayWindow(guiIn, oldObject, transferFocus, closeOld, transferHistory, CenterType.object); }
	/** Displays the specified window parent with variable arguments. */
	public static IWindowParent displayWindow(IWindowParent guiIn, IWindowParent oldObject, boolean transferFocus, boolean closeOld, boolean transferHistory, CenterType loc) {
		if (guiIn == null) { displayScreen(null); }
		if (Game.currentScreen == null || !(Game.currentScreen instanceof IRendererProxy)) { displayScreen(new GameScreen()); }
		if (guiIn != null) {
			
			gameRenderer.addObject(guiIn);
			if (oldObject instanceof AbstractScreen) { displayScreen(null); }
			else if (oldObject instanceof IWindowParent && closeOld) { ((IWindowParent) oldObject).close(); }
			
			if (transferHistory && oldObject != null) {
				IWindowParent old = (IWindowParent) oldObject;
				old.getWindowHistory().add(old);
				guiIn.setWindowHistory(old.getWindowHistory());
				guiIn.setPinned(old.isPinned());
			}
			
			setPos(guiIn, oldObject instanceof IWindowObject ? (IWindowObject) oldObject : null, loc);
			guiIn.bringToFront();
			if (transferFocus) { guiIn.requestFocus(); }
		}
		return guiIn;
	}
	
	/** Helper method used in conjunction wth displayWindow that actually positions the newley created window on the screen. */
	private static void setPos(IWindowParent windowIn, IWindowObject objectIn, CenterType typeIn) {
		WindowSize res = Game.getWindowSize();
		EDimension gDim = windowIn.getDimensions();
		double headerHeight = windowIn.hasHeader() ? windowIn.getHeader().height : 0;
		
		int sX = 0;
		int sY = 0;
		
		switch (typeIn) {
		case screen:
			sX = (int) ((res.getWidth() / 2) - (gDim.width / 2));
			sY = (int) ((res.getHeight() / 2) - (gDim.height / 2));
			break;
		case botLeftScreen:
			sX = 1;
			sY = (int) (res.getHeight() - 2 - gDim.height);
			break;
		case topLeftScreen:
			sX = 1;
			sY = 2;
			break;
		case botRightScreen:
			sX = (int) (res.getWidth() - 1 - gDim.width);
			sY = (int) (res.getHeight() - 2 - gDim.height);
			break;
		case topRightScreen:
			sX = (int) (res.getWidth() - 1 - gDim.width);
			sY = 2;
			break;
		case cursor:
			sX = (int) (Mouse.getMx() - (gDim.width / 2));
			sY = (int) (Mouse.getMy() - (gDim.height - headerHeight) / 2 + (gDim.height / 7));
			break;
		case cursorCorner:
			sX = Mouse.getMx();
			sY = Mouse.getMy();
			break;
		case object:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = (int) (objDim.midX - (gDim.width / 2));
				sY = (int) (objDim.midY - (gDim.height / 2));
			}
			break;
		case objectCorner:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = (int) objDim.startX;
				sY = (int) objDim.startY;
			}
			break;
		case objectIndent:
			if (objectIn != null) {
				EDimension objDim = objectIn.getDimensions();
				sX = (int) (objDim.startX + 25);
				sY = (int) (objDim.startY + 25);
			}
			break;
		case existingObjectIndent:
			EArrayList<WindowParent> windows = new EArrayList();
			gameRenderer.getAllChildren().stream().filter(o -> windowIn.getClass().isInstance(o)).filter(o -> !o.isBeingRemoved()).forEach(w -> windows.add((WindowParent) w));
			
			if (windows.isNotEmpty()) {
				if (windows.get(0) != null) {
					EDimension objDim = windows.get(0).getDimensions();
					sX = (int) (objDim.startX + 25);
					sY = (int) (objDim.startY + 25);
				}
			}
			
			break;
		default: break;
		}
		
		if (!(windowIn instanceof OverlayWindow)) {
			sX = sX < 0 ? 4 : sX;
			sY = (int) ((sY - headerHeight) < 2 ? 4 + headerHeight : sY);
			sX = (int) (sX + gDim.width > res.getWidth() ? -4 + sX - (sX + gDim.width - res.getWidth()) : sX);
			sY = (int) (sY + gDim.height > res.getHeight() ? -4 + sY - (sY + gDim.height - res.getHeight()) : sY);
		}
		
		windowIn.setPosition(sX, sY);
	}
	
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
	
	/** Returns this game's central font rendering system. */
	public static FontRenderer getFontRenderer() { return fontRenderer; }
	/** Returns this game's central texture handling system. */
	public static TextureSystem getTextureSystem() { return textureSystem; }
	/** Returns this game's central object rendering system. */
	public static GameRenderer getGameRenderer() { return gameRenderer; }
	/** Returns this game's central terminal command handler. */
	public static TerminalHandler getTerminalHandler() { return terminalHandler; }
	
	//----------------
	// Static Setters
	//----------------
	
	/** Sets whether the game should run in a debug state. */
	public static void setDebugMode(boolean val) { isDebug = val; }

	
	
}
