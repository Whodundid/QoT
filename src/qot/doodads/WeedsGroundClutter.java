package qot.doodads;

import envision.game.objects.entities.Entity;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import qot.entities.EntityList;

public class WeedsGroundClutter extends Entity {
	
	public WeedsGroundClutter() { this(0, 0); }
	public WeedsGroundClutter(int posX, int posY) {
		super("weeds_clutter");
		init(posX, posY, 32, 32);
		tex = GroundClutterTextures.weeds.getRandVariant();
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		//setCollisionBox(0, 0, 0, 0);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.WEEDS.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}
