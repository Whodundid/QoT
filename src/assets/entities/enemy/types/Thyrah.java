package assets.entities.enemy.types;

import assets.entities.enemy.Enemy;
import assets.textures.EntityTextures;
import gameSystems.questSystem.RouteTracker;
import util.mathUtil.NumUtil;

public class Thyrah extends Enemy {
	
	public Thyrah() { this(0, 0); }
	public Thyrah(int posX, int posY) {
		super("Thyrah, the dragon", 1, 4, 4, 0, 0, 1, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.thyrah;
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
