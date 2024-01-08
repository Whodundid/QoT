package qot.doodads;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Doodad;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.trees.TreeTextures;
import qot.entities.EntityList;

public class FallBirch extends Doodad {

	public FallBirch() { this(0, 0); }
	public FallBirch(int posX, int posY) {
		super("birch");
		init(posX, posY, 32 * 4, 32 * 6);
		sprite = new Sprite(TreeTextures.fall_birch);
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		double sx = (facing == Rotation.RIGHT) ? 15 : 5;
		double ex = (facing == Rotation.RIGHT) ? 5 : 15;
		setCollisionBox(midX - sx, endY - 15, midX + ex, endY);
		
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.FALL_BIRCH.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}
