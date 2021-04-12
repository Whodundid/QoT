package assets.entities.enemy.types;

import assets.entities.enemy.Enemy;
import assets.textures.EntityTextures;
import randomUtil.RandomUtil;

public class TrollBoar extends Enemy {

	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar", 1, 1, 1, 22222, 22222, 16, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.trollboar;
	}
	
	@Override
	public void onLivingUpdate() {
		move(RandomUtil.randomDir());
	}
	
}
