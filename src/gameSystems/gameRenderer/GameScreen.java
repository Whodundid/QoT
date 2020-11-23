package gameSystems.gameRenderer;

import eWindow.windowTypes.interfaces.ITopParent;
import eWindow.windowTypes.interfaces.IWindowObject;
import main.Game;

public class GameScreen extends AbstractScreen {
	
	GameRenderer renderer;
	
	public GameScreen() {
		renderer = Game.getGameRenderer();
		init(renderer, 0, 0, Game.getWidth(), Game.getHeight());
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawScreen(mX, mY);
		super.drawObject(mXIn, mYIn);
	}
	
	protected void drawScreen(int mXIn, int mYIn) {}
	
	@Override public ITopParent getTopParent() { return this; }
	@Override public IWindowObject getParent() { return this; }
	
	@Override
	public void onWindowResize() {
		setDimensions(0, 0, Game.getWidth(), Game.getHeight());
		reInitObjects();
	}
	
}
