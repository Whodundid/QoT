package assets.shops;

import assets.entities.Entity;
import assets.items.Item;

public abstract class Shopkeeper extends Entity {
	
	private Shop shop = new Shop();
	
	public Shopkeeper(String name, double maxHealth, double health, double maxMana, double mana, double damage, double gold) {
		super(name, 0, maxHealth, health, maxMana, mana, damage, gold);
	}
	
	public void displayShop() {
		System.out.println("-----------------------------------------------------");
		for (Item i : shop.getItems()) {
			System.out.println(i.getName() + " - " + i.getDescription() + " -  Costs: " + i.getPrice() + " Gold.");
		}
	}
	
	public Shop getShop() { return shop; }
	
}
