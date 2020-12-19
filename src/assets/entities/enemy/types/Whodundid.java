package assets.entities.enemy.types;

import assets.entities.enemy.Enemy;
import assets.textures.EntityTextures;
import util.mathUtil.NumUtil;

public class Whodundid extends Enemy {
	
	public Whodundid() { this(0, 0); }
	public Whodundid(int posX, int posY) {
		super("Whodundid", 1, 40, 40, 20, 20, 5, 0);
		init(posX, posY, 64, 64);
		sprite = EntityTextures.whodundid;
	}
	
	@Override
	public void onLivingUpdate() {
		move(NumUtil.randomDir());
	}
	
}
