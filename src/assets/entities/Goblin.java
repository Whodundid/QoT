package assets.entities;

import assets.textures.EntityTextures;
import eutil.misc.Direction;
import eutil.random.RandomUtil;

public class Goblin extends Enemy {
	
	public int treasure;
	public double loot;
	
	public Goblin() { this(0, 0); }
	public Goblin(int posX, int posY) {
		super("Goblin", 1, 20, 20, 0, 0, 2, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.goblin;
		
		setCollisionBox(startX + 16, endY - 15, endX - 16, endY);
	}
	
	// This gets the loot and returns it
	public void getLoot() {
		treasure = (int) Math.random() * 30;
		loot = (double) treasure;
	}
	
	@Override
	public void onLivingUpdate() {
		boolean shouldMove = RandomUtil.roll(10, 0, 10);
		if (shouldMove) {
			Direction dir = RandomUtil.randomDir();
			move(dir);
		}
	}
	
}
