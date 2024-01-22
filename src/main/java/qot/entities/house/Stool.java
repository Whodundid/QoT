package qot.entities.house;

import envision.engine.registry.types.Sprite;
import envision.game.entities.BasicRenderedEntity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;

public class Stool extends BasicRenderedEntity {

	public Stool() { this(0, 0); }
	public Stool(int x, int y) {
		super("Stool");
		init(x, y, 32, 32);
		sprite = new Sprite(HouseTextures.stool);
		invincible = true;
		
		setCollisionBox(startX + 6, startY + 14, endX - 6, endY - 3);
		
		setMaxHealth(10);
		setHealth(10);
		
        this.canBeMoved = true;
        this.canBeCarried = true;
        this.canMoveEntities = true;
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.STOOL.ID;
	}
	
}
