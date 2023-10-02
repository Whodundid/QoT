package envision.game.shops;

import static envision.game.shops.ShopTransactionResult.*;

import envision.game.entities.Entity;
import envision.game.entities.inventory.EntityInventory;
import envision.game.items.Item;
import eutil.datatypes.util.EList;

public class Shop {
	
    private String shopName;
	private int inventorySize = 10;
	private EntityInventory shopInventory;
	private EList<Shopkeeper> shopkeeper;
	
	//==============
	// Constructors
	//==============
	
	public Shop() { this("Shop"); }
	public Shop(String shopNameIn) {
	    shopName = shopNameIn;
	}
	
	//==============
	// Shop Methods
	//==============
	
	public ShopTransactionResult buyItemAtSlot(Shopkeeper seller, Entity buyer, int slot) {
		if (buyer == null) return fail_buy("The entity trying to buy is null!");
		if (!checkSlot(slot)) return fail_buy("The given item slot '" + slot + "' is out of range!");
		
		return fail_buy("No item selected!");
	}
	
	public ShopTransactionResult sellItem(Item toSell) {
		return fail_sell("Generic failure reason!");
	}
	
	public void addItem(Item in) {
		if (in != null) {
			shopInventory.addItem(in);
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
		for (Item i : shopInventory.getItems()) {
			if (i.getName().contentEquals(nameIn)) {
				return i;
			}
		}
		return null;
	}
	
	public EntityInventory getInventory() {
	    return shopInventory;
	}
	
	public EList<Item> getItems() {
		return shopInventory.getItems();
	}
	
}
