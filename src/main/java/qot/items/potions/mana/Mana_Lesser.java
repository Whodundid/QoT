package qot.items.potions.mana;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Potion;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Mana_Lesser extends Potion {
	
	public Mana_Lesser() {
		super("Lesser Mana Potion", ItemList.MANA_LESSER.ID);
		this.setUsable(true);
		this.setIsDestroyedOnUse(true);
		this.setSprite(new Sprite(ItemTextures.lesser_mana));
		this.setDescription("This potion restores 30 MP");
		this.setBasePrice(100);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishMana(10);
	}
	
	@Override
	public Mana_Lesser copy() {
		return new Mana_Lesser();
	}

	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
