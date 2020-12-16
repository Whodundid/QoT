package gameSystems.screenSystem;

import envisionEngine.eWindow.windowTypes.TopWindowParent;
import envisionEngine.eWindow.windowTypes.WindowParent;
import envisionEngine.eWindow.windowTypes.interfaces.IWindowObject;
import envisionEngine.input.Keyboard;
import envisionEngine.input.Mouse;
import main.Game;
import util.openGL_Util.GLSettings;
import util.storageUtil.EDimension;

/** The renderer that is overlaid onto every other one. (Need a better name). */
public class GameTopRenderer<E> extends TopWindowParent<E> {
	
	public static GameTopRenderer<?> instance;
	private static boolean hasFocus = false;
	
	public static GameTopRenderer<?> getInstance() {
		return instance = (instance != null) ? instance : new GameTopRenderer();
	}
	
	private GameTopRenderer() {
		res = Game.getWindowSize();
		initObjects();
	}
	
	public void onRenderTick() {
		updateBeforeNextDraw(Mouse.getMx(), Mouse.getMy());
		
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
			if (Game.isDebugMode()) { drawDebugInfo(); }
			
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
								EDimension d = o.getDimensions();
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
	public void keyPressed(char typedChar, int keyCode) {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESC) && getEscapeStopper() == null) {
			hasFocus = false;
			hideUnpinnedObjects();
		}
		else { super.keyPressed(typedChar, keyCode); }
	}
	
	public void clear() { removeAllObjects(); }
	
	public boolean hasFocus() { return hasFocus; }
	
	public void setFocused(boolean val) {
		hasFocus = val;
		if (hasFocus) { revealHiddenObjects(); }
		else { hideUnpinnedObjects(); }
	}
	
}
