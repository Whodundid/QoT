package qot.entities.house;

import envision.game.objects.entities.Entity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;

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
	public void onLivingUpdate(float dt) {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.STOOL.ID;
	}
	
}
