package game.items.potions.healing;

import envision.game.entity.Entity;
import envision.game.items.Potion;
import game.items.ItemList;

public class Healing_Lesser extends Potion {
	
	public Healing_Lesser() {
		super("Lesser Healing Potion", ItemList.HEALING_LESSER.ID);
		this.setDescription("This potion heals 30 HP");
		this.setBasePrice(5);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishHealth(30);
	}
	
}
