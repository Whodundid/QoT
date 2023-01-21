package qot.items.potions.healing;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameObjects.items.Potion;
import envisionEngine.gameEngine.world.gameWorld.IGameWorld;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import qot.items.ItemList;

public class Healing_Lesser extends Potion {
	
	public Healing_Lesser() {
		super("Lesser Healing Potion", ItemList.HEALING_LESSER.ID);
		this.setDescription("This potion heals 30 HP");
		this.setBasePrice(5);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishHealth(30);
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
