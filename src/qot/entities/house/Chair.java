package qot.entities.house;

import envision.game.objects.entities.Entity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;

public class Chair extends Entity {

	public Chair() { this(0, 0); }
	public Chair(int x, int y) {
		super("Chair");
		init(x, y, 32, 32);
		setTexture(HouseTextures.chair);
		invincible = true;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.CHAIR.ID;
	}
	
}
