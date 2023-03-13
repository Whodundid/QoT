package qot.doodads;

import envision.game.objects.entities.Entity;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import qot.entities.EntityList;

public class SticksGroundClutter extends Entity {
	
	public SticksGroundClutter() { this(0, 0); }
	public SticksGroundClutter(int posX, int posY) {
		super("sticks_clutter");
		init(posX, posY, 64, 32);
		tex = GroundClutterTextures.sticks.getRandVariant();
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		//setCollisionBox(0, 0, 0, 0);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.STICKS.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}
