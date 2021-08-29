package assets.entities;

import assets.textures.EntityTextures;
import eutil.random.RandomUtil;

public class Thyrah extends Enemy {
	
	public Thyrah() { this(0, 0); }
	public Thyrah(int posX, int posY) {
		super("Thyrah, the dragon", 1, 4, 4, 0, 0, 1, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.thyrah;
		
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
	}

	@Override
	public void onLivingUpdate() {
		move(RandomUtil.randomDir());
	}
	
}
