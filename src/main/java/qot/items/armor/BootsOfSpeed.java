package qot.items.armor;

import envision.game.entities.Entity;
import envision.game.items.Item;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class BootsOfSpeed extends Item {

	public BootsOfSpeed() {
		super("Boots of Speed", 1000, "These boots bestow its wearer with unmatched swiftness", ItemList.BOOTS_OF_SPEED.ID);
		this.setTexture(ItemTextures.boots_of_speed);
	}

	@Override
	public Item copy() {
		return new BootsOfSpeed();
	}

	@Override
	public int getInternalSaveID() { return 0; }
	
	@Override
	public void onItemEquip(Entity user) {
		if (!user.activeEffectsTracker.containsKey("SPEED_MODIFIER")) {
			user.activeEffectsTracker.put("SPEED_MODIFIER", 750.0);
		}
	}
	
	@Override
	public void onItemUnequip(Entity user) {
		if (!user.getInventory().containsItemType(this)) {
			user.activeEffectsTracker.remove("SPEED_MODIFIER");
		}
	}
	
}
