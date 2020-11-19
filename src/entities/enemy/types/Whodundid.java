package entities.enemy.types;

import entities.enemy.Enemy;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import main.Game;

public class Whodundid extends Enemy {
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid", 1, 40, 40, 20, 20, 5, 0);
		init(Game.getGameRenderer(), posX, posY, 150, 150);
	}
	
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawTexture(EntityTextures.whodundid);
		super.drawObject(mXIn, mYIn);
	}
	
	@Override public RouteTracker getBackgroundStats() { return null; }
	
}
