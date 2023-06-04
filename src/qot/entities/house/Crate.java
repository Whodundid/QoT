package qot.entities.house;

import envision.game.entities.BasicRenderedEntity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;

public class Crate extends BasicRenderedEntity {

	public Crate() { this(0, 0); }
	public Crate(int x, int y) {
		super("Crate");
		init(x, y, 32, 32);
		setTexture(HouseTextures.crate);
		
		setMaxHealth(5);
		setHealth(5);
		
		setCollisionBox(startX + 2, startY + 14, endX - 2, endY - 1);
		setExperienceRewardedOnKill(2);
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.CRATE.ID;
	}
	
}