package qot.entities.house;

import envision.engine.registry.types.Sprite;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.BasicRenderedEntity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;
import qot.items.Items;

public class Crate extends BasicRenderedEntity {

	public Crate() { this(0, 0); }
	public Crate(int x, int y) {
		super("Crate");
		init(x, y, 32, 32);
		sprite = new Sprite(HouseTextures.crate);
		
		setMaxHealth(5);
		setHealth(5);
		
		setCollisionBox(startX + 2, startY + 14, endX - 2, endY - 1);
		setExperienceRewardedOnKill(2);
		
        // item on death
        
        var itemOnDeath = DropItemOnDeathComponent.setItem(this, Items.random());
        itemOnDeath.setChance(10);
        
        addComponent(itemOnDeath);
	}
	
	@Override
	public void onLivingUpdate(float dt) {
		
	}
	
	@Override
	public int getInternalSaveID() {
		return EntityList.CRATE.ID;
	}
	
}