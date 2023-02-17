package qot.items.weapons.melee.swords;

import envision.game.objects.entities.Entity;
import envision.game.objects.items.Weapon;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
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
		this.setDamageBonus(1);
	}
	
	@Override
	public void onItemUse(Entity user) {
	}
	
	@Override
	public Sword_Wooden copy() {
		return new Sword_Wooden();
	}

	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY,
		int distX, int distY) {
	}

	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
