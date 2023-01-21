package qot.items.potions.mana;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameObjects.items.Potion;
import envisionEngine.gameEngine.world.gameWorld.IGameWorld;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import qot.items.ItemList;

public class Mana_Lesser extends Potion {
	
	public Mana_Lesser() {
		super("Lesser Mana Potion", ItemList.MANA_LESSER.ID);
		this.setDescription("This potion restores 30 MP");
		this.setBasePrice(100);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishMana(30);
	}

	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY,
		int distX, int distY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInternalSaveID() { // TODO Auto-generated method stub
	return 0; }
	
}
