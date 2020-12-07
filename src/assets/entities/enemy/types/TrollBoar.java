package assets.entities.enemy.types;

import assets.entities.enemy.Enemy;
import assets.textures.EntityTextures;
import gameSystems.questSystem.RouteTracker;
import util.mathUtil.NumUtil;

public class TrollBoar extends Enemy {

	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar", 1, 1, 1, 22222, 22222, 16, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.trollboar;
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
