package engine.screenEngine;

import java.util.Stack;

import engine.inputHandlers.Keyboard;
import engine.windowLib.windowTypes.TopWindowParent;
import engine.windowLib.windowTypes.interfaces.ITopParent;
import eutil.datatypes.EArrayList;
import game.screens.main.MainMenuScreen;
import main.QoT;

public abstract class GameScreen<E> extends TopWindowParent<E> implements ITopParent<E> {
	
	protected Stack<GameScreen<?>> screenHistory = new Stack();
	protected EArrayList<String> aliases = new EArrayList();
	
	/** The point at which the screen will start fading. */
	protected long screenFadeStart;
	/** The amount of time it takes for the screen to fade in/out. (measured in milliseconds) */
	protected long screenFadeDuration = 1500;
	/** True if the screen is actively fading. */
	protected boolean isFading = false;
	/** True if fading in. */
	protected boolean isFadingIn = false;
	/** True if screen fading should happen on load/close. */
	protected boolean performFade = true;
	
	//--------------
	// Constructors
	//--------------
	
	public GameScreen() {
		setDefaultDims();
	}
	
	//-----------
	// Overrides
	//-----------
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawScreen(mXIn, mYIn);
		super.drawObject(mXIn, mYIn);
		

	}
	
	@Override
	public void keyPressed(char typedChar, int keyCode) {
		if (keyCode == Keyboard.KEY_ESC && !screenHistory.isEmpty()) closeScreen();
		super.keyPressed(typedChar, keyCode);
	}
	
	@Override
	public void onFirstDraw() {
		super.onFirstDraw();
		//fadeIn();
	}
	
	//---------
	// Methods
	//---------
	
	protected void setDefaultDims() {
		setDimensions(0, 0, QoT.getWidth(), QoT.getHeight());
	}
	
	/** Initializer method that is called before a screen is built. */
	public void initScreen() {}
	/** Called everytime this screen is about to be drawn. */
	public void drawScreen(int mXIn, int mYIn) {}
	/** Called everytime a new game tick occurs. */
	public void onGameTick(long ticks) {}
	/** Called whenever this screen is about to be closed. */
	public void onScreenClosed() {}
	
	public void enableScreenFade(boolean val) {
		performFade = val;
	}
	
	public void onWindowResized() {
		setDimensions(0, 0, QoT.getWidth(), QoT.getHeight());
		reInitChildren();
	}
	
	public Stack<GameScreen<?>> getScreenHistory() { return screenHistory; }
	public GameScreen<E> setScreenHistory(Stack<GameScreen<?>> historyIn) {
		screenHistory = historyIn;
		return this;
	}
	
	/**
	 * Closes this screen and displays the previous screen in history.
	 */
	public void closeScreen() {
		closeScreen(false);
	}
	
	/**
	 * Set the boolean argument to true if you want the next screen to
	 * remember this screen in history. Generally you will want this value
	 * to be false!
	 * 
	 * @param hist
	 */
	public void closeScreen(boolean hist) {
		if (!screenHistory.isEmpty() && screenHistory.peek() != null) {
			var screen = screenHistory.pop();
			screen.setScreenHistory(screenHistory);
			
			QoT.displayScreen(screen, (hist) ? this : new MainMenuScreen());
		}
		else {
			QoT.displayScreen(new MainMenuScreen());
		}
	}
	
	public EArrayList<String> getAliases() {
		return aliases;
	}
	
	public GameScreen<?> getPreviousScreen() {
		return (!screenHistory.isEmpty()) ? screenHistory.peek() : null;
	}
	
	public GameScreen<E> setWindowSize() {
		setDimensions(0, 0, QoT.getWidth(), QoT.getHeight());
		return this;
	}
	
	//---------------------
	// Screen Fade Methods
	//---------------------
	
	public void fadeIn() {
		if (!performFade) return;
		screenFadeStart = System.currentTimeMillis();
		isFading = true;
		isFadingIn = true;
	}
	
	public void fadeOutAndClose() {
		if (!performFade) return;
		screenFadeStart = System.currentTimeMillis();
		isFading = true;
		isFadingIn = false;
	}
	
}
