package game.doodads.nature;

import assets.textures.doodads.trees.TreeTextures;
import eutil.misc.Rotation;
import eutil.random.RandomUtil;
import game.entities.Entity;
import game.entities.EntityList;

public class BirchTree extends Entity {

	public BirchTree() { this(0, 0); }
	public BirchTree(int posX, int posY) {
		super("birch");
		init(posX, posY, 160, 160);
		sprite = TreeTextures.birch_0;
		facing = (RandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		setCollisionBox(startX + 26, endY - 25, endX - 26, endY);
	}
	
	@Override
	public int getObjectID() {
		return EntityList.BIRCH.ID;
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
}
