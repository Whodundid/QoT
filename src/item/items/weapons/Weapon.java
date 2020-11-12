package item.items.weapons;

import item.Item;

public abstract class Weapon extends Item {
	protected int damage;
	
	protected Weapon(int costIn, int weightIn, String nameIn) {
		super(costIn, weightIn, nameIn);
	}
	
	public int getDamage() {
		return damage;
	}
}
