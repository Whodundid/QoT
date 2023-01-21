package envisionEngine.topOverlay;

import envisionEngine.inputHandlers.Keyboard;
import envisionEngine.inputHandlers.Mouse;
import envisionEngine.renderEngine.GLObject;
import envisionEngine.renderEngine.GLSettings;
import envisionEngine.renderEngine.fontRenderer.FontRenderer;
import envisionEngine.terminal.window.ETerminal;
import envisionEngine.topOverlay.desktopOverlay.DesktopRCM;
import envisionEngine.topOverlay.desktopOverlay.OverlayShortcut;
import envisionEngine.topOverlay.desktopOverlay.TaskBar;
import envisionEngine.windowLib.bundledWindows.CalculatorWindow;
import envisionEngine.windowLib.bundledWindows.fileExplorer.FileExplorerWindow;
import envisionEngine.windowLib.windowTypes.TopWindowParent;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowObject;
import envisionEngine.windowLib.windowTypes.interfaces.IWindowParent;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import qot.QoT;
import qot.assets.textures.taskbar.TaskBarTextures;
import qot.assets.textures.window.WindowTextures;
import qot.settings.QoTSettings;

/** The renderer that is overlaid onto every other one. (Need a better name). */
public class GameTopScreen<E> extends TopWindowParent<E> {
	
	public static GameTopScreen<?> instance;
	private static boolean hasFocus = false;
	private static TaskBar<?> taskBar;
	private final EArrayList<IWindowParent> highlightedWindows = new EArrayList<>();
	
	public static GameTopScreen<?> getInstance() {
		return instance = (instance != null) ? instance : new GameTopScreen();
	}
	
	private GameTopScreen() {
		res = QoT.getWindowSize();
		initChildren();
	}
	
	@Override
	public void initChildren() {
		OverlayShortcut explorer = new OverlayShortcut(200, QoT.getHeight() - 200);
		OverlayShortcut calculator = new OverlayShortcut(400, QoT.getHeight() - 200);
		OverlayShortcut terminal = new OverlayShortcut(600, QoT.getHeight() - 200);
		
		explorer.setWindowTarget(FileExplorerWindow.class);
		calculator.setWindowTarget(CalculatorWindow.class);
		terminal.setWindowTarget(ETerminal.class);
		
		explorer.setIcon(WindowTextures.file_folder);
		calculator.setIcon(TaskBarTextures.calculator);
		terminal.setIcon(TaskBarTextures.terminal);
		
		explorer.setDescription("File Explorer");
		calculator.setDescription("Calculator");
		terminal.setDescription("Terminal");
		
		IWindowObject.setHidden(true, explorer, calculator, terminal);
		
		addObject(explorer, calculator, terminal);
	}
	
	public void onRenderTick() {
		updateBeforeNextDraw(Mouse.getMx(), Mouse.getMy());
		if (getChildren().isEmpty()) hasFocus = false;
		
		//prime renderer
		GLSettings.pushMatrix();
		GLSettings.enableBlend();
		GLSettings.clearDepth();
		
		if (hasFocus) {
			drawRect(EColors.dsteel.opacity(140));
			
			int borderColor = 0xaaff0000;
			int borderWidth = 4;
			var b = getTaskBar();
			if (b != null && !b.isHidden()) {
				double ds = TaskBar.drawSize();
				drawRect(0, ds, borderWidth, height, borderColor); //left
				drawRect(borderWidth, ds, width - borderWidth, ds + borderWidth, borderColor); //top
				drawRect(width - borderWidth, ds, width, height, borderColor); //right
				drawRect(borderWidth, height - borderWidth, width - borderWidth, height, borderColor); //bottom
			}
			else {
				drawRect(0, 0, borderWidth, height, borderColor); //left
				drawRect(borderWidth, 0, width - borderWidth, borderWidth, borderColor); //top
				drawRect(width - borderWidth, 0, width, height, borderColor); //right
				drawRect(borderWidth, height - borderWidth, width - borderWidth, height, borderColor); //bottom
			}
			
			//drawString("Debug", borderWidth + 2, endY - borderWidth - FontRenderer.FONT_HEIGHT, EColors.dgray);
		}
		
		if (isVisible()) {
			int mX = Mouse.getMx();
			int mY = Mouse.getMy();
			
			//reset highlighted
			highlightedWindows.clear();
			
			if (!hasFirstDraw()) onFirstDraw_i();
			
			//draw this object first
			drawObject(mX, mY);
			
			//draw debug stuff
			if (QoT.isDebugMode()) drawDebugInfo();
			
			//now draw all child objects on top of parent
			for (var o : getChildren()) {
				//only draw if the object is actually visible
				if (!o.willBeDrawn() || o.isHidden()) continue;
				boolean draw = true;
				
				if (o instanceof IWindowParent<?> wp) {
					draw = (wp.drawsWhileMinimized() || !wp.isMinimized());
					if (wp.isHighlighted()) highlightedWindows.add(wp);
				}
				
				if (!draw) continue;
				
				GLSettings.fullBright();
				GLSettings.clearDepth();
				GLSettings.disableScissor();
				
				//notify object on first draw
				if (!o.hasFirstDraw()) o.onFirstDraw_i();
				//actually draw the child object
				o.drawObject_i(mX, mY);
				
				//draw grayed out overlay over everything if a focus lock object is present
				if (focusLockObject != null && !o.equals(focusLockObject)) {
					if (o.isVisible()) {
						drawRect(o.getDimensions(), 0x77000000);
						EDimension d = o.getDimensions();
						GLObject.drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
					}
				}
			}
			
			//draw highlighted window borders
			for (IWindowParent<?> p : highlightedWindows) {
				if (p == null) continue;
				p.drawHighlightBorder();
			}
			
			//notify hover object
			var hoveringObject = getHoveringObject();
			if (hoveringObject != null) hoveringObject.onMouseHover(Mouse.getMx(), Mouse.getMy());
		}
		
		//draw game fps
		if (QoTSettings.drawFPS.get()) {
			String s = "FPS: " + QoT.getFPS();
			int s_width = FontRenderer.getStringWidth(s);
			drawString(s, QoT.getWidth() - 10 - s_width, 10);
		}
		
		GLSettings.popMatrix();
	}
	
	@Override
	public void handleMouseInput(int action, int mXIn, int mYIn, int button, int change) {
		if (hasFocus) {
			super.handleMouseInput(action, mXIn, mYIn, button, change);
			//check if desktop rcm should open
			DesktopRCM.checkOpen(action, button);
		}
		else if (QoT.currentScreen != null) {
			QoT.currentScreen.handleMouseInput(action, mXIn, mYIn, button, change);
		}
	}
	
	@Override
	public void handleKeyboardInput(int action, char typedChar, int keyCode) {
		//debug terminal
		if (action == 1 && Keyboard.isAltDown() && keyCode == Keyboard.KEY_TILDE) {
			if (QoT.currentScreen != null) {
				if (!isTerminalOpen()) {
					displayWindow(new ETerminal());
					setFocused(true);
				}
				else if (!hasFocus()) {
					setFocused(true);
					ETerminal term = getTerminalInstance();
					if (term != null) term.requestFocus();
				}
			}
			else {
				var term = getTerminalInstance();
				if (term != null) term.requestFocus();
				else displayWindow(new ETerminal());
				setFocused(true);
			}
		}
		//display framerate
		else if (action == 1 && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
			QoTSettings.drawFPS.set(!QoTSettings.drawFPS.get());
		}
		else if (hasFocus) {
			super.handleKeyboardInput(action, typedChar, keyCode);
		}
		else if (QoT.currentScreen != null) {
			QoT.currentScreen.handleKeyboardInput(action, typedChar, keyCode);
		}
	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESC) && getEscapeStopper() == null) {
			hasFocus = false;
			hideUnpinnedObjects();
		}
		else super.keyPressed(typedChar, keyCode);
	}
	
	public void clear() {
		removeAllObjects();
	}
	
	public void addTaskBar(TaskBar b) {
		if (taskBar == null) removeObject(taskBar);
		addObject(taskBar = b);
	}
	
	public void addTaskBar(boolean fromScratch) {
		addObject(taskBar = new TaskBar(fromScratch));
	}
	
	public TaskBar<?> getTaskBar() {
		var objects = new EArrayList(getChildren());
		objects.removeAll(getRemovingChildren());
		objects.addAll(getAddingChildren());
		
		if (taskBar != null) return taskBar;
		else {
			for (int i = 0; i < objects.size(); i++) {
				if (objects.get(i) instanceof TaskBar<?> b) {
					return taskBar = b;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean hasFocus() {
		return hasFocus;
	}
	
	public void setFocused(boolean val) {
		hasFocus = val;
		if (hasFocus) {
			revealHiddenObjects();
			if (getTaskBar() == null) addTaskBar(true);
		}
		else hideUnpinnedObjects();
	}
	
	public static boolean isTopFocused() {
		return hasFocus;
	}
	
	@Override
	public void onScreenResized() {
		super.onScreenResized();
		if (taskBar != null) taskBar.onScreenResized();
	}
	
}
