package game.doodads;

import envision.game.entity.Entity;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import game.assets.textures.doodads.trees.TreeTextures;
import game.entities.EntityList;

public class BirchTree extends Entity {

	public BirchTree() { this(0, 0); }
	public BirchTree(int posX, int posY) {
		super("birch");
		init(posX, posY, 160, 160);
		sprite = TreeTextures.birch_0;
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		double sx = (facing == Rotation.RIGHT) ? 15 : 5;
		double ex = (facing == Rotation.RIGHT) ? 5 : 15;
		
		setCollisionBox(midX - sx, endY - 15, midX + ex, endY);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.BIRCH.ID;
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
}
