package envision.game.objects.entities;

import envision.game.objects.items.Item;
import eutil.datatypes.EArrayList;
import eutil.datatypes.util.EList;

public class EntityInventory {
	
	//--------
	// Fields
	//--------
	
	private EList<Item> inventory = new EArrayList<>();
	private int inventorySize = 3; // 3 by default
	private int maxCarryWeight = -1;
	private boolean usesCarryWeight = false;
	private Entity theEntity;

	//--------------
	// Constructors
	//--------------
	
	public EntityInventory(Entity theEntityIn) {
		theEntity = theEntityIn;
		inventorySize = theEntityIn.baseInventorySize;
		setSize(inventorySize);
	}
	
	public EntityInventory(Entity theEntityIn, int inventorySizeIn) {
		theEntity = theEntityIn;
		inventorySize = inventorySizeIn;
		setSize(inventorySize);
	}
	
	//---------
	// Getters
	//---------
	
	public Item getItemAtIndex(int index) {
		synchronized (inventory) {
			if (index >= 0 && index < inventorySize) {
				return inventory.get(index);
			}
		}
		return null;
	}
	
	public EList<Item> getItems() {
		return inventory;
	}
	
	/**
	 * @return True if there are no free inventory slots.
	 */
	public boolean isFull() {
		for (int i = 0; i < inventorySize && i < inventory.size(); i++) {
			if (inventory.get(i) == null) return false;
		}
		return true;
	}
	
	/**
	 * @return True if there are no items in this inventory.
	 */
	public boolean isEmpty() {
		for (int i = 0; i < inventorySize && i < inventory.size(); i++) {
			if (inventory.get(i) != null) return false;
		}
		return true;
	}
	
	public int size() { return inventorySize; }
	
	public int getMaxCarryWeight() { return maxCarryWeight; }
	public int getWeight() { return -1; }
	
	//---------
	// Setters
	//---------
	
	public void setSize(int sizeIn) {
		inventorySize = sizeIn;
		
		// if the inventory's internal size is less then the new size, grow to meet it
		if (inventory.size() < inventorySize) {
			for (int i = inventory.size(); i < inventorySize; i++) {
				inventory.add((Item) null);
			}
		}
	}
	
	/**
	 * Sets the item at the given index and returns the item that was at that
	 * index.
	 * 
	 * @param index The slot to add the new item to
	 * @param item The item to add
	 * 
	 * @return The old item at that index
	 */
	public Item setItem(int index, Item item) {
		if (index >= 0 && index < inventorySize) {
			Item r = inventory.get(index);
			inventory.set(index, item);
			return r;
		}
		return null;
	}
	
	public void swapItems(int indexA, int indexB) {
		inventory.swap(indexA, indexB);
	}
	
	public boolean addItem(Item item) {
		// search for first open inventory slot
		for (int i = 0; i < inventorySize; i++) {
			Item r = inventory.get(i);
			if (r == null) {
				inventory.set(i, item);
				return true;
			}
		}
		return false;
	}
	
}
