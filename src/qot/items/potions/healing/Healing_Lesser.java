package qot.items.potions.healing;

import envision.game.objects.entities.Entity;
import envision.game.objects.items.Potion;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Healing_Lesser extends Potion {
	
	public Healing_Lesser() {
		super("Lesser Healing Potion", ItemList.HEALING_LESSER.ID);
		this.setUsable(true);
		this.setDiesOnUse(true);
		this.setTexture(ItemTextures.lesser_healing);
		this.setDescription("This potion heals 30 HP");
		this.setBasePrice(5);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishHealth(10);
	}
	
	@Override
	public Healing_Lesser copy() {
		return new Healing_Lesser();
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
