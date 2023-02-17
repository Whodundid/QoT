package qot.items.potions.mana;

import envision.game.objects.entities.Entity;
import envision.game.objects.items.Potion;
import envision.game.world.IGameWorld;
import envision.game.world.WorldCamera;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class Mana_Lesser extends Potion {
	
	public Mana_Lesser() {
		super("Lesser Mana Potion", ItemList.MANA_LESSER.ID);
		this.setUsable(true);
		this.setDiesOnUse(true);
		this.setTexture(ItemTextures.lesser_mana);
		this.setDescription("This potion restores 30 MP");
		this.setBasePrice(100);
	}
	
	@Override
	public void onItemUse(Entity user) {
		user.replenishMana(10);
	}
	
	@Override
	public Mana_Lesser copy() {
		return new Mana_Lesser();
	}

	@Override
	public void draw(IGameWorld world, WorldCamera camera, int midDrawX, int midDrawY, double midX, double midY,
		int distX, int distY)
	{
		
	}

	@Override
	public int getInternalSaveID() {
		return 0;
	}
	
}
