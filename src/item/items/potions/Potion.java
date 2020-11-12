package item.items.potions;

import Entities.Entity;
import item.Item;

public abstract class Potion extends Item {
	
	protected PotionType type;
	
	protected Potion(int costIn, int weightIn, String nameIn, PotionType typeIn) {
		super(costIn, weightIn, nameIn);
		type = typeIn;
	}
	
	public abstract void consume(Entity characterIn);
	
	public PotionType getType() {
		return type;
	}
	
}
