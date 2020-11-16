package entities.player;

import items.Item;
import java.util.ArrayList;

public class Inventory {
	
	ArrayList<Item> content = new ArrayList();
	Player thePlayer;
	
	public Inventory(Player playerIn) {
		thePlayer = playerIn;
	}
	
	// Inventory Methods
	
	public void addItem(Item in) {
		if (in != null) {
			content.add(in);
		}
	}
	
	// WORK ON THIS
	//public void useItem(Player player, Item item) {
	//	player.setHealth(30);
	//	player.inventory.removeItem(potion.getName());
	//}
	
	//... means array
	public void storeItems(Item... in) {
		for (Item i : in) {
			addItem(i);
		}
	}
	
	public Item getItem(String nameIn) {
		for (Item i : content) {
			if (i.getName().contentEquals(nameIn)) {
				return i;
			}
		}
		return null;
	}
	
	public Item removeItem(String nameIn) {
		Item theItem = null;
		
		for (Item i : content) {
			if (i.getName().contentEquals(nameIn)) {
				theItem = i;
				break;
			}
		}
		content.remove(theItem);
		
		return theItem;
	}
	
	
	public ArrayList<Item> getItems() {
		return content;
	}
	
	
	
	// Inventory Getters
	
	public Player getPlayer() {
		return thePlayer;
	}
	
}
