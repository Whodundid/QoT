package game.entities.house;

import assets.textures.doodads.house.HouseTextures;
import game.entities.Entity;
import game.entities.EntityList;

public class Chair extends Entity {

	public Chair() { this(0, 0); }
	public Chair(int x, int y) {
		super("Chair");
		init(x, y, 32, 32);
		setTexture(HouseTextures.chair);
		invincible = true;
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
	@Override
	public int getObjectID() {
		return EntityList.CHAIR.ID;
	}
	
}
