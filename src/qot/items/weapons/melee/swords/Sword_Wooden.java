package qot.items.weapons.melee.swords;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameObjects.items.Weapon;
import envisionEngine.gameEngine.world.gameWorld.IGameWorld;
import envisionEngine.gameEngine.world.worldUtil.WorldCamera;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Sword_Wooden extends Weapon {
	// Wooden Sword
	// 1 Gold
	// 2 Damage
	
	public Sword_Wooden() {
		super("Wooden Sword", ItemList.SWORD_WOODEN.ID);
		this.setDescription("This is a sword carved out of an oak tree. It is light and cheap, but it gets the job done.");
		this.setBasePrice(1);
		this.setTexture(ItemTextures.iron_sword);
	}
	
	@Override
	public void onItemUse(Entity user) {
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
