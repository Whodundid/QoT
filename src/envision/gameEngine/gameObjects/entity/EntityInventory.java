package envision.gameEngine.gameObjects.entity;

import envision.gameEngine.gameObjects.items.Item;
import eutil.datatypes.EArrayList;

public class EntityInventory {
	
	private EArrayList<Item> inventory = new EArrayList<>();
	private int maxCarryWeight = -1;
	private boolean usesCarryWeight = false;
	private Entity theEntity;
	
	public EntityInventory(Entity theEntityIn) {
		theEntity = theEntityIn;
	}
	
	public void addItem(Item itemIn) {
		inventory.add(itemIn);
	}
	
}
