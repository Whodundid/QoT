package envision.game.shops;

import envision.game.entities.Entity;
import envision.game.items.Item;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class Shop {
	
	private int inventorySize = 6;
	private EList<Item> shopInventory = new EArrayList<>();
	private Shopkeeper shopkeeper;
	
	//==============
	// Shop Methods
	//==============
	
	public ShopTransactionResult buyItemAtSlot(Entity buyingEntity, int slot) {
		//if (buyingEntity == null) return fail_buy("The entity trying to buy is null!");
		//if (!checkSlot(slot)) return fail_buy("The given item slot is out of range!");
		
		//return fail_buy("No item selected!");
		return null;
	}
	
	public ShopTransactionResult sellItem(Item toSell) {
		//return fail_sell(toSell, "Generic failure reason!");
		return null;
	}
	
	public void addItem(Item in) {
		if (in != null) {
			shopInventory.add(in);
		}
	}
	
	public void storeItems(Item... in) {
		for (Item i : in) {
			addItem(i);
		}
	}
	
	/**
	 * Internal check to see if the given item slot is actually in this shop
	 * inventory's range.
	 */
	private boolean checkSlot(int slot) {
		return slot >= 0 && slot < inventorySize;
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
	
	//fail_buy(Entity buyer) { return ShopTransactionResult.base(buyer, this)
	
}
