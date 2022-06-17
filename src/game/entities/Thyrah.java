package game.entities;

import assets.textures.EntityTextures;
import eutil.random.RandomUtil;

public class Thyrah extends Enemy {
	
	public Thyrah() { this(0, 0); }
	public Thyrah(int posX, int posY) {
		super("Thyra, the dragon");
		init(posX, posY, 128, 128);
		texture = EntityTextures.thyrah;
		
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
		
		randShort = 200l;
		randLong = 200l;
	}

	@Override
	public void onLivingUpdate() {
		if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
			waitTime = RandomUtil.getRoll(randShort, randLong);
			//moveTime = RandomUtil.getRoll(randShort, 800l);
			//waitDelay = RandomUtil.getRoll(randShort, randLong);
			lastMove = System.currentTimeMillis();
			lastDir = RandomUtil.randomDir(true);
		}
		
		if (System.currentTimeMillis() - lastMove >= moveTime) {
			move(lastDir);
		}
	}
	
}