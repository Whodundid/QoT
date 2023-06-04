package qot.doodads;

import envision.game.entities.BasicRenderedEntity;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import qot.entities.EntityList;

public class LeavesGroundClutter extends BasicRenderedEntity {
	
	public LeavesGroundClutter() { this(0, 0); }
	public LeavesGroundClutter(int posX, int posY) {
		super("leaves_clutter");
		init(posX, posY, 32, 32);
		tex = GroundClutterTextures.leaves.getRandVariant();
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		setCollisionBox(0, 0, 0, 0);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.LEAVES.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}
