package game.shops;

import eutil.datatypes.EArrayList;
import eutil.datatypes.EList;
import game.items.Item;
import game.items.potions.healing.Healing_Lesser;

public class Shop {
	
	EList<Item> shopInventory = new EArrayList<>();
	Item lesserHealingPotion = new Healing_Lesser();
	
	public Shop() {
		
	}
	
	public void buyItem() {
		
	}
	
	public void sellItem() {
		
	}
	
	// Shop Methods
	
	public void addItem(Item in) {
		if (in != null) {
			shopInventory.add(in);
		}
	}
	
	//... means array
	public void storeItems(Item... in) {
		for (Item i : in) {
			addItem(i);
		}
	}
	
	public Item getItem(String nameIn) {
		for (Item i : shopInventory) {
			if (i.getName().contentEquals(nameIn)) {
				return i;
			}
		}
		return null;
	}
	
	public Item removeItem(String nameIn) {
		Item theItem = null;
		
		for (Item i : shopInventory) {
			if (i.getName().contentEquals(nameIn)) {
				theItem = i;
				break;
			}
		}
		shopInventory.remove(theItem);
		
		return theItem;
	}
	
	public EList<Item> getItems() {
		return shopInventory;
	}
}
