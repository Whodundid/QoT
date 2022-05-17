package game.entities;

import assets.textures.EntityTextures;
import eutil.random.RandomUtil;

public class Whodundid extends Enemy {
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid");
		init(posX, posY, 64, 64);
		texture = EntityTextures.whodundid;
		lastDir = RandomUtil.randomDir();
		
		setCollisionBox(startX + 6, endY - height / 2, endX - 6, endY);
		
		randShort = 400l;
		randLong = 800l;
	}
	
	@Override
	public void onLivingUpdate() {
		if (System.currentTimeMillis() - lastMove >= waitTime + waitDelay) {
			waitTime = RandomUtil.getRoll(randShort, randLong);
			moveTime = RandomUtil.getRoll(randShort, randLong);
			waitDelay = RandomUtil.getRoll(randShort, randLong);
			lastMove = System.currentTimeMillis();
			lastDir = RandomUtil.randomDir();
		}
		
		if (System.currentTimeMillis() - lastMove >= moveTime) {
			move(lastDir);
		}
	}
	
}
