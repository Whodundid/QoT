package gameSystems.gameRenderer;

import eWindow.windowTypes.TopWindowParent;
import input.Mouse;
import main.Game;

//Author: Hunter Bragg

public class GameRenderer extends TopWindowParent {
	
	public static GameRenderer instance;
	
	public static GameRenderer getInstance() {
		return instance == null ? instance = new GameRenderer() : instance;
	}
	
	private GameRenderer() {
		res = Game.getWindowSize();
		initObjects();
	}
	
	public void onRenderTick() {
		drawObject(Mouse.getMx(), Mouse.getMy());
	}

	@Override public void close() { System.out.println("FOOL! Dagoth Ur cannot be closed, I am a god!"); }
	
}
