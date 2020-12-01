package gameSystems.gameRenderer;

import eWindow.windowTypes.TopWindowParent;
import eWindow.windowTypes.interfaces.ITopParent;
import java.util.Stack;
import main.Game;

public abstract class GameScreen extends TopWindowParent implements ITopParent {
	
	protected Stack<GameScreen> screenHistory = new Stack();
	
	public GameScreen() {
		init(Game.getGameRenderer(), 0, 0, Game.getWidth(), Game.getHeight());
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
	
	public void onWindowResize() {
		setDimensions(0, 0, Game.getWidth(), Game.getHeight());
		reInitObjects();
	}
	
	public Stack<GameScreen> getScreenHistory() { return screenHistory; }
	public GameScreen setScreenHistory(Stack<GameScreen> historyIn) {
		screenHistory = historyIn;
		return this;
	}
	
	public void closeScreen() { closeScreen(false); }
	public void closeScreen(boolean hist) {
		if (!screenHistory.isEmpty() && screenHistory.peek() != null) {
			
			//System.out.println("pre: " + screenHistory);
			GameScreen screen = screenHistory.pop();
			screen.setScreenHistory(screenHistory);
			//System.out.println("post: " + screenHistory);
			
			Game.displayScreen(screen, (hist) ? this : null);
		}
		else {
			Game.displayScreen(null);
		}
	}
	
	public GameScreen getPreviousScreen() {
		return (!screenHistory.isEmpty()) ? screenHistory.peek() : null;
	}
	
	public GameScreen setWindowSize() { setDimensions(0, 0, Game.getWidth(), Game.getHeight()); return this; }
	
}
