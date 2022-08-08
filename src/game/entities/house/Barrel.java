package game.entities.house;

import assets.textures.doodads.house.HouseTextures;
import game.entities.Entity;
import game.entities.EntityList;

public class Barrel extends Entity {

	public Barrel() { this(0, 0); }
	public Barrel(int x, int y) {
		super("Barrel");
		init(x, y, 32, 32);
		setTexture(HouseTextures.barrel);
		setMaxHealth(5);
		setHealth(5);
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
	@Override
	public int getObjectID() {
		return EntityList.BARREL.ID;
	}
	
}