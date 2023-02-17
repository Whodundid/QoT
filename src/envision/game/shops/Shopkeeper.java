package envision.game.shops;

import envision.game.objects.entities.Entity;

public abstract class Shopkeeper extends Entity {
	
	private Shop shop = new Shop();
	
	public Shopkeeper(String name) {
		super(name);
	}
	
	public Shop getShop() { return shop; }
	
}
