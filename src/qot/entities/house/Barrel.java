package qot.entities.house;

import envision.game.entities.BasicRenderedEntity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;

public class Barrel extends BasicRenderedEntity {

	public Barrel() { this(0, 0); }
	public Barrel(int x, int y) {
		super("Barrel");
		init(x, y, 32, 32);
		setTexture(HouseTextures.barrel);
		setMaxHealth(5);
		setHealth(5);
		setExperienceRewardedOnKill(1);
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.BARREL.ID;
	}
	
}