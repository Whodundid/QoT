package qot.items.potions.healing;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Potion;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Healing_Major extends Potion {
	
	public Healing_Major() {
		super("Major Healing Potion", ItemList.HEALING_LESSER.ID);
		this.setUsable(true);
		this.setIsDestroyedOnUse(true);
		this.setSprite(new Sprite(ItemTextures.major_healing));
		this.setDescription("This potion heals 25 HP");
		this.setBasePrice(35);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishHealth(25);
	}
	
	@Override
	public Healing_Major copy() {
		return new Healing_Major();
	}

	@Override
	public int getInternalSaveID() {
		return ItemList.HEALING_MAJOR.ID;
	}
	
}
