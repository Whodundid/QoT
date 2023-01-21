package game.entities.house;

import envision.gameEngine.gameObjects.entity.Entity;
import game.assets.textures.doodads.house.HouseTextures;
import game.entities.EntityList;

public class Crate extends Entity {

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
	public void onLivingUpdate() {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.CRATE.ID;
	}
	
}