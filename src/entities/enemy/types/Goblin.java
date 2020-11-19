package entities.enemy.types;

import entities.enemy.Enemy;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import main.Game;

public class Goblin extends Enemy {
	
	public int treasure;
	public double loot;
	
	public Goblin() { this(0, 0); }
	public Goblin(int posX, int posY) {
		super("Goblin", 1, 20, 20, 0, 0, 2, 0);
		init(Game.getGameRenderer(), posX, posY, 150, 150);
	}
	
	@Override
	public void drawObject(int mXIn, int mYIn) {
		drawTexture(EntityTextures.goblin);
		super.drawObject(mXIn, mYIn);
	}
	
	// This gets the loot and returns it
	public void getLoot() {
		treasure = (int) Math.random() * 30;
		loot = (double) treasure;
	}
	
	@Override public RouteTracker getBackgroundStats() { return null; }
	
}
