package gameSystems.gameRenderer;

import eWindow.windowTypes.TopWindowParent;
import eWindow.windowTypes.interfaces.ITopParent;

/** The base class for window screens. */
public abstract class AbstractScreen extends TopWindowParent implements ITopParent {
	
	/** Initializer method that is called before a screen is built. */
	public void initScreen() {}
	
	/** Called whenever this screen is about to be closed. */
	public void onScreenClosed() {}
	
	/** Called whenever the game window is resized. */
	public void onWindowResize() {
		setDimensions(game.getWidth(), game.getHeight());
	}
	
}
