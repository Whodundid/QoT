package game.doodads;

import envision.game.entity.Entity;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import game.assets.textures.doodads.bushes.BushTextures;
import game.entities.EntityList;

public class Bush0 extends Entity {
	
	public Bush0() { this(0, 0); }
	public Bush0(int posX, int posY) {
		super("bush0");
		init(posX, posY, 32, 32);
		sprite = BushTextures.bush_0;
		facing = (RandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		setCollisionBox(midX - 10, midY - 5, midX + 10, midY + 10);
	}
	
	@Override
	public int getObjectID() {
		return EntityList.BUSH0.ID;
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
}
