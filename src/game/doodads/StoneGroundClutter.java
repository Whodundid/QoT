package game.doodads;

import envision.game.entity.Entity;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import game.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import game.entities.EntityList;

public class StoneGroundClutter extends Entity {
	
	public StoneGroundClutter() { this(0, 0); }
	public StoneGroundClutter(int posX, int posY) {
		super("stone_ground_clutter");
		init(posX, posY, 32, 32);
		sprite = GroundClutterTextures.stones.getRandVariant();
		facing = (RandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		setCollisionBox(0, 0, 0, 0);
	}
	
	@Override
	public int getObjectID() {
		return EntityList.STONE_GROUND_CLUTTER.ID;
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
}
