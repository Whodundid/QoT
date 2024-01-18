package qot.doodads;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Doodad;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.ground_clutter.GroundClutterTextures;
import qot.entities.EntityList;

public class StoneGroundClutter extends Doodad {
	
	public StoneGroundClutter() { this(0, 0); }
	public StoneGroundClutter(int posX, int posY) {
		super("stone_ground_clutter");
		init(posX, posY, 32, 32);
		sprite = new Sprite(GroundClutterTextures.stones.getRandVariant());
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		
		setCollisionBox(0, 0, 0, 0);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.STONE_GROUND_CLUTTER.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}
