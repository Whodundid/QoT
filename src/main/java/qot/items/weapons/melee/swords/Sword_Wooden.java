package qot.items.weapons.melee.swords;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Weapon;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Sword_Wooden extends Weapon {
	// Wooden Sword
	// 1 Gold
	// 2 Damage
	
	public Sword_Wooden() {
		super("Wooden Sword", ItemList.SWORD_WOODEN.ID);
		this.setDescription("This is a sword carved out of an oak tree. It is light and cheap, but it gets the job done.");
		this.setBasePrice(1);
		this.setSprite(new Sprite(ItemTextures.iron_sword));
		this.setDamageBonus(1);
	}
	
	@Override
	public void onItemUse(Entity user) {
	}
	
	@Override
	public Sword_Wooden copy() {
		return new Sword_Wooden();
	}

	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
