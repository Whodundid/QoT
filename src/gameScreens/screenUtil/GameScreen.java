package gameScreens.screenUtil;

import input.Keyboard;
import java.util.Stack;
import main.QoT;
import windowLib.windowTypes.TopWindowParent;
import windowLib.windowTypes.interfaces.ITopParent;

public abstract class GameScreen<E> extends TopWindowParent<E> implements ITopParent<E> {
	
	protected Stack<GameScreen<?>> screenHistory = new Stack();
	
	public GameScreen() {
		setDimensions(0, 0, QoT.getWidth(), QoT.getHeight());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawScreen(mX, mY);
		super.drawObject(mXIn, mYIn);
	}
	
	/** Initializer method that is called before a screen is built. */
	public abstract void initScreen();
	
	public abstract void drawScreen(int mXIn, int mYIn);
	
	/** Called whenever this screen is about to be closed. */
	public abstract void onScreenClosed();
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESC && !screenHistory.isEmpty()) { closeScreen(false); }
		super.keyPressed(typedChar, keyCode);
	}
	
	public void onWindowResize() {
		setDimensions(0, 0, QoT.getWidth(), QoT.getHeight());
		reInitObjects();
	}
	
	public Stack<GameScreen<?>> getScreenHistory() { return screenHistory; }
	public GameScreen<E> setScreenHistory(Stack<GameScreen<?>> historyIn) {
		screenHistory = historyIn;
		return this;
	}
	
	/** Closes this screen and displays the previous screen in history. */
	public void closeScreen() { closeScreen(false); }
	/** Set the boolean argument to true if you want the next screen to remember this screen in history.
	 *  Generally you will want this value to be false! */
	public void closeScreen(boolean hist) {
		if (!screenHistory.isEmpty() && screenHistory.peek() != null) {
			
			//System.out.println("pre: " + screenHistory);
			GameScreen<?> screen = screenHistory.pop();
			screen.setScreenHistory(screenHistory);
			//System.out.println("post: " + screenHistory);
			
			QoT.displayScreen(screen, (hist) ? this : null);
		}
		else {
			QoT.displayScreen(null);
		}
	}
	
	public GameScreen<?> getPreviousScreen() {
		return (!screenHistory.isEmpty()) ? screenHistory.peek() : null;
	}
	
	public GameScreen<E> setWindowSize() { setDimensions(0, 0, QoT.getWidth(), QoT.getHeight()); return this; }
	
}
