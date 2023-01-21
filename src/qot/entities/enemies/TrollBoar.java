package qot.entities.enemies;

import envisionEngine.gameEngine.gameObjects.entity.Enemy;
import eutil.random.ERandomUtil;
import qot.assets.textures.entity.EntityTextures;

public class TrollBoar extends Enemy {

	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar");
		init(posX, posY, 64, 64);
		tex = EntityTextures.trollboar;
		setMaxHealth(20);
		setHealth(20);
		setExperienceRewardedOnKill(25);
	}
	
	@Override
	public void onLivingUpdate() {
		move(ERandomUtil.randomDir());
	}
	
	@Override
	public int getInternalSaveID() {
		return 3;
	}
	
}
