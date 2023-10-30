package qot.doodads;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Doodad;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import qot.entities.EntityList;

public class WeedsGroundClutter extends Doodad {
	
	public WeedsGroundClutter() { this(0, 0); }
	public WeedsGroundClutter(int posX, int posY) {
		super("weeds_clutter");
		init(posX, posY, 32, 32);
		sprite = new Sprite(GroundClutterTextures.weeds.getRandVariant());
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		setCollisionBox(0, 0, 0, 0);
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
