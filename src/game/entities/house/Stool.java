package game.entities.house;

import envision.game.entity.Entity;
import game.assets.textures.doodads.house.HouseTextures;
import game.entities.EntityList;

public class Stool extends Entity {

	public Stool() { this(0, 0); }
	public Stool(int x, int y) {
		super("Stool");
		init(x, y, 32, 32);
		setTexture(HouseTextures.stool);
		invincible = true;
		
		setMaxHealth(10);
		setHealth(10);
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
	@Override
	public int getObjectID() {
		return EntityList.STOOL.ID;
	}
	
}
