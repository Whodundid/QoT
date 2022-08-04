package game.entities.house;

import assets.textures.doodads.house.HouseTextures;
import game.entities.Entity;
import game.entities.EntityList;

public class Stool extends Entity {

	public Stool() { this(0, 0); }
	public Stool(int x, int y) {
		super("Stool");
		init(x, y, 32, 32);
		setTexture(HouseTextures.stool);
		invincible = true;
	}
	
	@Override
	public void onLivingUpdate() {
		
	}
	
	@Override
	public int getObjectID() {
		return EntityList.STOOL.ID;
	}
	
}
