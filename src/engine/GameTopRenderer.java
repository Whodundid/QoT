package engine;

import engine.input.Keyboard;
import engine.input.Mouse;
import engine.renderEngine.GLObject;
import engine.renderEngine.GLSettings;
import engine.renderEngine.fontRenderer.FontRenderer;
import engine.terminal.window.ETerminal;
import engine.windowLib.desktopOverlay.TaskBar;
import engine.windowLib.windowTypes.TopWindowParent;
import engine.windowLib.windowTypes.WindowParent;
import engine.windowLib.windowTypes.interfaces.IWindowObject;
import eutil.datatypes.EArrayList;
import eutil.math.EDimension;
import main.QoT;
import main.settings.QoT_Settings;

/** The renderer that is overlaid onto every other one. (Need a better name). */
public class GameTopRenderer<E> extends TopWindowParent<E> {
	
	public static GameTopRenderer<?> instance;
	private static boolean hasFocus = false;
	
	public static GameTopRenderer<?> getInstance() {
		return instance = (instance != null) ? instance : new GameTopRenderer();
	}
	
	private GameTopRenderer() {
		res = QoT.getWindowSize();
		initObjects();
	}
	
	public void onRenderTick() {
		updateBeforeNextDraw(Mouse.getMx(), Mouse.getMy());
		if (getObjects().isEmpty()) hasFocus = false;
		
		//prime renderer
		GLSettings.pushMatrix();
		GLSettings.enableBlend();
		GLSettings.clearDepth();
		
		if (hasFocus) {
			int borderColor = 0xaaff0000;
			int borderWidth = 4;
			/*
			TaskBar b = getTaskBar();
			if (b != null && !b.isHidden()) {
				double ds = TaskBar.drawSize();
				drawRect(0, ds, borderWidth, height, borderColor); //left
				drawRect(borderWidth, ds, width - 1, ds + 1, borderColor); //top
				drawRect(width - borderWidth, ds, width, height, borderColor); //right
				drawRect(borderWidth, height - borderWidth, width - 1, height, borderColor); //bottom
			}
			else {*/
				drawRect(0, 0, borderWidth, height, borderColor); //left
				drawRect(borderWidth, 0, width - borderWidth, borderWidth, borderColor); //top
				drawRect(width - borderWidth, 0, width, height, borderColor); //right
				drawRect(borderWidth, height - borderWidth, width - borderWidth, height, borderColor); //bottom
			//}
		}
		
		if (visible) {
			//draw debug stuff
			if (QoT.isDebugMode()) drawDebugInfo();
			
			//draw all child objects
			for (IWindowObject<?> o : windowObjects) {
				//only draw if the object is actually visibile
				if (!o.willBeDrawn() || o.isHidden()) continue;
				boolean draw = true;
				
				if (o instanceof WindowParent<?> wp) {
					draw = (wp.drawsWhileMinimized() || !wp.isMinimized());
				}
				
				if (draw) {
					GLSettings.fullBright();
					GLSettings.clearDepth();
					GLSettings.disableScissor();
					
					//notify object on first draw
					if (!o.hasFirstDraw()) o.onFirstDraw();
					//actually draw the object
					o.drawObject(mX, mY);
					
					//draw greyed out overlay over everything if a focus lock object is present
					if (focusLockObject != null && !o.equals(focusLockObject)) {
						if (o.isVisible()) {
							drawRect(o.getDimensions(), 0x77000000);
							EDimension d = o.getDimensions();
							GLObject.drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
						}
					}
				}
			}
			
			//notify hover object
			IWindowObject<?> hoveringObject = getHoveringObject();
			if (hoveringObject != null) hoveringObject.onMouseHover(mX, mY);
		}
		
		//draw game fps
		if (QoT_Settings.drawFPS.get()) {
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
				ETerminal term = (ETerminal) getWindowInstance(ETerminal.class);
				if (term != null) term.requestFocus();
				else displayWindow(new ETerminal());
				setFocused(true);
			}
		}
		//display framerate
		else if (action == 1 && Keyboard.isKeyDown(Keyboard.KEY_F3)) {
			QoT_Settings.drawFPS.set(!QoT_Settings.drawFPS.get());
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
	
	public void addTaskBar(boolean fromScratch) {
		addObject(new TaskBar(fromScratch));
	}
	
	public TaskBar getTaskBar() {
		EArrayList<IWindowObject> objects = new EArrayList(windowObjects);
		objects.removeAll(objsToBeRemoved);
		objects.addAll(objsToBeAdded);
		
		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) instanceof TaskBar b) return b;
		
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
