package game.entities.enemies;

import envision.game.entity.Enemy;
import eutil.random.ERandomUtil;
import game.assets.textures.entity.EntityTextures;

public class TrollBoar extends Enemy {

	public TrollBoar() { this(0, 0); }
	public TrollBoar(int posX, int posY) {
		super("Troll Boar");
		init(posX, posY, 64, 64);
		sprite = EntityTextures.trollboar;
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
