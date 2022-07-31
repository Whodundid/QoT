package engine.topOverlay;

import engine.inputHandlers.Keyboard;
import engine.inputHandlers.Mouse;
import engine.renderEngine.GLObject;
import engine.renderEngine.GLSettings;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.terminal.window.ETerminal;
import engine.topOverlay.desktopOverlay.TaskBar;
import engine.windowLib.windowTypes.TopWindowParent;
import engine.windowLib.windowTypes.WindowParent;
import eutil.colors.EColors;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import main.QoT;
import main.settings.QoTSettings;

/** The renderer that is overlaid onto every other one. (Need a better name). */
public class GameTopRenderer<E> extends TopWindowParent<E> {
	
	public static GameTopRenderer<?> instance;
	private static boolean hasFocus = false;
	private static TaskBar<?> taskBar;
	
	public static GameTopRenderer<?> getInstance() {
		return instance = (instance != null) ? instance : new GameTopRenderer();
	}
	
	private GameTopRenderer() {
		res = QoT.getWindowSize();
		initChildren();
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
			
			drawString("Debug", borderWidth + 2, endY - borderWidth - FontRenderer.FONT_HEIGHT, EColors.dgray);
		}
		
		if (isVisible()) {
			//draw debug stuff
			if (QoT.isDebugMode()) drawDebugInfo();
			
			//draw all child objects
			for (var o : getChildren()) {
				//only draw if the object is actually visible
				if (!o.willBeDrawn() || o.isHidden()) continue;
				boolean draw = true;
				
				if (o instanceof WindowParent<?> wp) {
					draw = (wp.drawsWhileMinimized() || !wp.isMinimized());
				}
				
				if (!draw) continue;
				
				GLSettings.fullBright();
				GLSettings.clearDepth();
				GLSettings.disableScissor();
				
				//notify object on first draw
				if (!o.hasFirstDraw()) o.onFirstDraw();
				//actually draw the object
				o.drawObject(Mouse.getMx(), Mouse.getMy());
				
				//draw grayed out overlay over everything if a focus lock object is present
				if (focusLockObject != null && !o.equals(focusLockObject)) {
					if (o.isVisible()) {
						drawRect(o.getDimensions(), 0x77000000);
						EDimension d = o.getDimensions();
						GLObject.drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
					}
				}
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
		if (taskBar == null) removeChild(taskBar);
		addChild(taskBar = b);
	}
	
	public void addTaskBar(boolean fromScratch) {
		addChild(taskBar = new TaskBar(fromScratch));
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
	
}
