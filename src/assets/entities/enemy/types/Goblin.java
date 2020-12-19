package assets.entities.enemy.types;

import assets.entities.enemy.Enemy;
import assets.textures.EntityTextures;
import util.mathUtil.NumUtil;
import util.miscUtil.Direction;

public class Goblin extends Enemy {
	
	public int treasure;
	public double loot;
	
	public Goblin() { this(0, 0); }
	public Goblin(int posX, int posY) {
		super("Goblin", 1, 20, 20, 0, 0, 2, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.goblin;
	}
	
	// This gets the loot and returns it
	public void getLoot() {
		treasure = (int) Math.random() * 30;
		loot = (double) treasure;
	}
	
	@Override
	public void onLivingUpdate() {
		boolean shouldMove = NumUtil.roll(10, 0, 10);
		if (shouldMove) {
			Direction dir = NumUtil.randomDir();
			move(dir);
		}
	}
	
}
