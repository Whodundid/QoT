package game.items.types;

import game.entities.Entity;
import game.items.ItemList;
import game.items.Weapon;

public class Sword_Wooden extends Weapon {
	// Wooden Sword
	// 1 Gold
	// 2 Damage
	
	public Sword_Wooden() {
		super("Wooden Sword", ItemList.SWORD_WOODEN.ID);
		this.setDescription("This is a sword carved out of an oak tree. It is light and cheap, but it gets the job done.");
		this.setBasePrice(1);
	}
	
	@Override
	public void onItemUse(Entity user) {
	}
	
}
