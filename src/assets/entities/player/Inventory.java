package assets.entities.player;

import assets.items.Item;
import storageUtil.EArrayList;

public class Inventory {
	
	EArrayList<Item> content = new EArrayList();
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
			if (i.getName().equals(nameIn)) {
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
	
	//-------------------
	// Inventory Getters
	//-------------------
	
	public EArrayList<Item> getItems() { return content; }
	public Player getPlayer() { return thePlayer; }
	
}
