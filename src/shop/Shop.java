package shop;

import items.Item;
import items.types.LesserHealing;
import java.util.ArrayList;

public class Shop {
	
	ArrayList<Item> shopInventory = new ArrayList();
	Item lesserHealingPotion = new LesserHealing();
	
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
	
	public ArrayList<Item> getItems() {
		return shopInventory;
	}
}
