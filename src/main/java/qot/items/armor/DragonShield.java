package qot.items.armor;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Item;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class DragonShield extends Item {

	public DragonShield() {
		super("Dragon Shield", ItemList.DRAGON_SHIELD.ID);
		this.setUsable(false);
        this.setSprite(new Sprite(ItemTextures.dragon_shield));
        this.setDescription("Bestows its weilder with a defense bonus to negate 20% of incoming damage");
        this.setBasePrice(70);
	}

	@Override
	public Item copy() {
		return new DragonShield();
	}

	@Override
	public int getInternalSaveID() { return ItemList.DRAGON_SHIELD.ID; }
	
	@Override
	public void onItemEquip(Entity user) {
		if (!user.activeEffectsTracker.containsKey("DEFENSE_MODIFIER")) {
			user.activeEffectsTracker.put("DEFENSE_MODIFIER", -0.20);
		}
	}
	
	@Override
	public void onItemUnequip(Entity user) {
		if (!user.getInventory().containsItemType(this)) {
			user.activeEffectsTracker.remove("DEFENSE_MODIFIER");
		}
	}
	
}
