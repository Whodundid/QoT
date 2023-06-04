package qot.doodads;

import envision.game.entities.BasicRenderedEntity;
import eutil.misc.Rotation;
import eutil.random.ERandomUtil;
import qot.assets.textures.doodads.bushes.BushTextures;
import qot.entities.EntityList;

public class Bush0 extends BasicRenderedEntity {
	
	public Bush0() { this(0, 0); }
	public Bush0(int posX, int posY) {
		super("bush0");
		init(posX, posY, 32, 32);
		tex = BushTextures.bush_0;
		facing = (ERandomUtil.randomBool()) ? Rotation.LEFT : Rotation.RIGHT;
		setCollisionBox(midX - 10, midY - 5, midX + 10, midY + 10);
		invincible = true;
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.BUSH0.ID;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
}
