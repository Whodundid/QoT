package gameScreens.screenUtil;

import debug.terminal.window.ETerminal;
import eutil.storage.EDims;
import input.Keyboard;
import input.Mouse;
import main.QoT;
import renderEngine.GLSettings;
import windowLib.windowTypes.TopWindowParent;
import windowLib.windowTypes.WindowParent;
import windowLib.windowTypes.interfaces.IWindowObject;

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
		if (getObjects().isEmpty()) { hasFocus = false; }
		
		//prime renderer
		GLSettings.pushMatrix();
		GLSettings.enableBlend();
		GLSettings.clearDepth();
		
		if (hasFocus) {
			int borderColor = 0xaaff0000;
			int borderWidth = 4;
			drawRect(0, 0, borderWidth, height, borderColor); //left
			drawRect(borderWidth, 0, width - borderWidth, borderWidth, borderColor); //top
			drawRect(width - borderWidth, 0, width, height, borderColor); //right
			drawRect(borderWidth, height - borderWidth, width - borderWidth, height, borderColor); //bottom
		}
		
		if (visible) {
			
			//draw debug stuff
			if (QoT.isDebugMode()) { drawDebugInfo(); }
			
			//draw all child objects
			for (IWindowObject<?> o : windowObjects) {
				if (o.checkDraw() && !o.isHidden()) {
					boolean draw = true;
					
					if (o instanceof WindowParent<?>) {
						WindowParent<?> wp = (WindowParent<?>) o;
						if (!wp.isMinimized() || wp.drawsWhileMinimized()) { draw = true; }
						else { draw = false; }
					}
					
					if (draw) {
						GLSettings.colorA(1.0f);
						GLSettings.clearDepth();
						GLSettings.disableScissor();
						
						if (!o.hasFirstDraw()) { o.onFirstDraw(); }
						o.drawObject(mX, mY);
						
						//draw greyed out overlay over everything if a focus lock object is present
						if (focusLockObject != null && !o.equals(focusLockObject)) {
							if (o.isVisible()) {
								EDims d = o.getDimensions();
								drawRect(d.startX, d.startY, d.endX, d.endY, 0x77000000);
							}
						}
					}
					
				}
			}
			
			//notify hover object
			if (getHoveringObject() != null) { getHoveringObject().onMouseHover(mX, mY); }
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
		if (Keyboard.isAltDown() && keyCode == Keyboard.KEY_TILDE) {
			if (QoT.currentScreen != null) {
				if (!isWindowOpen(ETerminal.class)) {
					displayWindow(new ETerminal());
					setFocused(true);
				}
				else if (!hasFocus()) {
					setFocused(true);
					ETerminal term = (ETerminal) getWindowInstance(ETerminal.class);
					if (term != null) { term.requestFocus(); }
				}
			}
			else {
				ETerminal term = (ETerminal) getWindowInstance(ETerminal.class);
				if (term != null) { term.requestFocus(); }
			}
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
		else { super.keyPressed(typedChar, keyCode); }
	}
	
	public void clear() { removeAllObjects(); }
	
	@Override
	public boolean hasFocus() { return hasFocus; }
	
	public void setFocused(boolean val) {
		hasFocus = val;
		if (hasFocus) { revealHiddenObjects(); }
		else { hideUnpinnedObjects(); }
	}
	
}
