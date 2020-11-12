package item.items.weapons;

import item.Item;

public class Sword extends Item {

	public Sword() {
		super(150, 10, "A Sword");
	}

	@Override
	public String getDescription() {
		return "This Sword is made of iron.";
	}
}
