package entities.enemy.types;

import entities.enemy.Enemy;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import main.Game;

public class Thyrah extends Enemy {
	
	public Thyrah() { this(0, 0); }
	public Thyrah(int posX, int posY) {
		super("Thyrah, the dragon", 1, 4, 4, 0, 0, 1, 0);
		init(Game.getGameRenderer(), posX, posY, 150, 150);
	}
	
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawTexture(EntityTextures.thyrah);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override public RouteTracker getBackgroundStats() { return null; }

}
