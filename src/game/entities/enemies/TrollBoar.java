package game.entities.enemies;

import assets.textures.entity.EntityTextures;
import eutil.random.RandomUtil;

public class TrollBoar extends Enemy {

	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar");
		init(posX, posY, 64, 64);
		sprite = EntityTextures.trollboar;
	}
	
	@Override
	public void onLivingUpdate() {
		move(RandomUtil.randomDir());
	}
	
	@Override
	public int getObjectID() {
		return 3;
	}
	
}
