package envision.gameEngine.gameSystems.shops;

import envision.gameEngine.gameObjects.entity.Entity;

public abstract class Shopkeeper extends Entity {
	
	private Shop shop = new Shop();
	
	public Shopkeeper(String name) {
		super(name);
	}
	
	public Shop getShop() { return shop; }
	
}
