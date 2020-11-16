package gameSystems.gameRenderer;

import main.Game;

public class GameScreen extends AbstractScreen implements IRendererProxy {
	
	GameRenderer renderer;
	int mX = 0;
	int mY = 0;
	
	public GameScreen() {
		renderer = Game.getGameRenderer();
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		mX = mXIn;
		mY = mYIn;
		drawScreen(mX, mY);
		super.drawObject(mXIn, mYIn);
	}
	
	protected void drawScreen(int mXIn, int mYIn) {}

	@Override public int getMX() { return mX; }
	@Override public int getMY() { return mY; }
	
}
