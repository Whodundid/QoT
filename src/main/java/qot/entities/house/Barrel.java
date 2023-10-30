package qot.entities.house;

import envision.engine.resourceLoaders.Sprite;
import envision.game.component.types.death.DropItemOnDeathComponent;
import envision.game.entities.BasicRenderedEntity;
import qot.assets.textures.doodads.house.HouseTextures;
import qot.entities.EntityList;
import qot.items.Items;

public class Barrel extends BasicRenderedEntity {

	public Barrel() { this(0, 0); }
	public Barrel(int x, int y) {
		super("Barrel");
		init(x, y, 32, 32);
		sprite = new Sprite(HouseTextures.barrel);
		setMaxHealth(5);
		setHealth(5);
		setExperienceRewardedOnKill(1);
		
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
		return EntityList.BARREL.ID;
	}
	
}