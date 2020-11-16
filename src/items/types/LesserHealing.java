package items.types;

import entities.Entity;
import entities.player.Player;
import items.Item;
import items.Potion;

public class LesserHealing extends Potion {
	
	public LesserHealing() {
		super("Lesser Healing Potion", 100, "This potion heals 30 HP", 0.2, 30, 0, 30);
	}
	
	@Override
	public void heal(Entity entity, double healthIn) {
		System.out.println("Healed 30 HP! :D");
		entity.setHealth(30);
	}
	
}
