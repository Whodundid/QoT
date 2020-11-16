package gameSystems.gameRenderer;

import eWindow.windowTypes.WindowObject;

/** The base class for window screens. */
public abstract class AbstractScreen extends WindowObject {
	
	/** Initializer method that is called before a screen is built. */
	public void initScreen() {}
	
	/** Called after the screen has been initialized. */
	public void postInit() {}
	
	/** Called whenever this screen is about to be closed. */
	public void onClosed() {}
	
	/** Called whenever the game window is resized. */
	public void onWindowResize() {}
	
	//------------------------
	// WindowObject Overrides
	//------------------------
	
	@Override
	public void close() {
		//not sure how to handle yet..
	}
	
	@Override
	public void close(boolean recursive) {
		//not sure how to handle yet..
	}
	
}
