package qot.items.potions.healing;

import envision.game.entities.Entity;
import envision.game.items.Potion;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Healing_Lesser extends Potion {
	
	public Healing_Lesser() {
		super("Lesser Healing Potion", ItemList.HEALING_LESSER.ID);
		this.setUsable(true);
		this.setIsDestroyedOnUse(true);
		this.setTexture(ItemTextures.lesser_healing);
		this.setDescription("This potion heals 30 HP");
		this.setBasePrice(5);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishHealth(10);
	}
	
	@Override
	public Healing_Lesser copy() {
		return new Healing_Lesser();
	}

	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
