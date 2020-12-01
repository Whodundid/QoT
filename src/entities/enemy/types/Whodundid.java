package entities.enemy.types;

import entities.enemy.Enemy;
import gameSystems.questSystem.RouteTracker;
import gameTextures.EntityTextures;
import util.mathUtil.NumUtil;

public class Whodundid extends Enemy {
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid", 1, 40, 40, 20, 20, 5, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.whodundid;
	}
	
	@Override
	public void drawEntity() {
		drawTexture();
	}
	
	
	@Override public RouteTracker getBackgroundStats() { return null; }
	
	@Override
	public void onLivingUpdate() {
		move(NumUtil.randomDir());
	}
	
}
