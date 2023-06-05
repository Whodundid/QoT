package qot.items.potions.mana;

import envision.game.entities.Entity;
import envision.game.items.Potion;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Mana_Major extends Potion {
	
	public Mana_Major() {
		super("Major Mana Potion", ItemList.MANA_MAJOR.ID);
		this.setUsable(true);
		this.setDiesOnUse(true);
		this.setTexture(ItemTextures.major_mana);
		this.setDescription("This potion restores 25 MP");
		this.setBasePrice(35);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishMana(25);
	}
	
	@Override
	public Mana_Major copy() {
		return new Mana_Major();
	}

	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
