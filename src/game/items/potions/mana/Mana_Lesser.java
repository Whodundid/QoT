package game.items.potions.mana;

import envision.game.entity.Entity;
import envision.game.items.Potion;
import game.items.ItemList;

public class Mana_Lesser extends Potion {
	
	public Mana_Lesser() {
		super("Lesser Mana Potion", ItemList.MANA_LESSER.ID);
		this.setDescription("This potion restores 30 MP");
		this.setBasePrice(100);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishMana(30);
	}
	
}
