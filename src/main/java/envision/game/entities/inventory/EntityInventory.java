package envision.game.entities.inventory;

import envision.game.entities.Entity;
import envision.game.items.Item;
import eutil.datatypes.util.EList;

public class EntityInventory {
	
	//========
    // Fields
    //========
	
	private EList<Item> inventory = EList.newList();
	private int inventorySize = 3; // 3 by default
	private int maxCarryWeight = -1;
	private boolean usesCarryWeight = false;
	private Entity theEntity;

	//==============
    // Constructors
    //==============
	
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
	
	//=========
	// Methods
	//=========
	
	public boolean containsItemType(Item itemIn) {
		if (itemIn == null) return false;
		if (inventorySize == 0 || inventory.isEmpty()) return false;
		
		for (int i = 0; i < inventorySize; i++) {
			final var item = inventory.get(i);
			if (itemIn.getClass().isInstance(item)) return true;
		}
		
		return false;
	}
	
	//=========
    // Getters
    //=========
	
	public Item getItemAtIndex(int index) {
		if (index >= 0 && index < inventorySize) {
			synchronized (inventory) {
				return inventory.get(index);
			}
		}
		return null;
	}
	
	public Item removeItemAtIndex(int index) {
	    if (index >= 0 && index < inventorySize) {
            synchronized (inventory) {
                var item = inventory.get(index);
                if (item != null) {
                    inventory.set(index, null);
                    item.onItemUnequip(theEntity);                    
                }
                return item;
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
	
	//=========
    // Setters
    //=========
	
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
	
	/**
	 * Attempts to put this item into this inventory at the first available
	 * slot (if there is one).
	 * 
	 * @param item The item to add
	 * @return true if actually added
	 */
	public boolean addItem(Item item) {
		if (item == null) return false;
		
		// search for first open inventory slot
		for (int i = 0; i < inventorySize; i++) {
			Item r = inventory.get(i);
			if (r == null) {
				inventory.set(i, item);
				item.onItemEquip(theEntity);
				return true;
			}
		}
		return false;
	}
	
}
