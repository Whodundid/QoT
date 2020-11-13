package shop;

import entities.Entity;
import items.*;

public class Shopkeeper extends Entity {
	
	private Shop shop = new Shop();
	
	public Shopkeeper(String name, double gold) {
		super(name, 0, 58858588, 22233334, 123212321, 11331133, gold, 55);
	}
	
	public void displayShop() {
		System.out.println("-----------------------------------------------------");
		for (Item i : shop.getItems()) {
			System.out.println(i.getName() + " - " + i.getDescription() + " -  Costs: " + i.getPrice() + " Gold.");
		}
	}
	
	
	public Shop getShop() {
		return shop;
	}
	
}
