package game.items.weapons.melee.swords;

import assets.textures.ItemTextures;
import game.entities.Entity;
import game.items.ItemList;
import game.items.weapons.Weapon;

public class Sword_Wooden extends Weapon {
	// Wooden Sword
	// 1 Gold
	// 2 Damage
	
	public Sword_Wooden() {
		super("Wooden Sword", ItemList.SWORD_WOODEN.ID);
		this.setDescription("This is a sword carved out of an oak tree. It is light and cheap, but it gets the job done.");
		this.setBasePrice(1);
		this.setTexture(ItemTextures.iron_sword);
	}
	
	@Override
	public void onItemUse(Entity user) {
	}
	
}
