package qot.doodads;

import envision.engine.rendering.textureSystem.Sprite;
import envision.game.entities.Doodad;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import qot.entities.EntityList;

public class SticksGroundClutter extends Doodad {
	
	public SticksGroundClutter() { this(0, 0); }
	public SticksGroundClutter(int posX, int posY) {
		super("sticks_clutter");
		init(posX, posY, 64, 32);
		sprite = new Sprite(GroundClutterTextures.sticks.getRandVariant());
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		setCollisionBox(0, 0, 0, 0);
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
