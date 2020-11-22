package entities.enemy.types;

import entities.enemy.Enemy;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import main.Game;

public class TollBoar extends Enemy {

	public TollBoar() { this(0, 0); }
	public TollBoar(int posX, int posY) {
		super("A Singular Toll Boar With A Third Eye", 1, 1, 1, 22222, 22222, 16, 0);
		init(Game.getGameRenderer(), posX, posY, 150, 150);
	}
	
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawTexture(EntityTextures.tollboar);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override public RouteTracker getBackgroundStats() { return null; }
	
}
