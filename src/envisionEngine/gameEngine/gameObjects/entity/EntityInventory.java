package envisionEngine.gameEngine.gameObjects.entity;

import envisionEngine.gameEngine.gameObjects.items.Item;
import eutil.datatypes.EArrayList;

public class EntityInventory {
	
	//--------
	// Fields
	//--------
	
	private EArrayList<Item> inventory = new EArrayList<>();
	private int maxCarryWeight = -1;
	private boolean usesCarryWeight = false;
	private Entity theEntity;

	//--------------
	// Constructors
	//--------------
	
	public EntityInventory(Entity theEntityIn) {
		theEntity = theEntityIn;
	}
	
	public void addItem(Item itemIn) {
		inventory.add(itemIn);
	}
	
	//---------
	// Getters
	//---------
	
	public Item getItemAtIndex(int index) {
		if (index >= 0 && index < inventory.size()) return inventory.get(index);
		return null;
	}
	
	public EArrayList<Item> getItems() {
		return new EArrayList<>(inventory);
	}
	
	public int getMaxCarryWeight() { return maxCarryWeight; }
	public int getWeight() { return -1; }
	
	//---------
	// Setters
	//---------
	
	/**
	 * Sets the item at the given index and returns the item that was at that
	 * index.
	 * 
	 * @param index
	 * @param item
	 * 
	 * @return
	 */
	public Item setItem(int index, Item item) {
		if (index >= 0 && index < inventory.size()) {
			Item r = inventory.get(index);
			inventory.set(index, item);
			return r;
		}
		return null;
	}
	
}
