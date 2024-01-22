package qot.items.armor;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Item;
import qot.assets.textures.item.ItemTextures;
import qot.effects.SpeedEffect;
import qot.items.ItemList;

public class BootsOfSpeed extends Item {

    public final SpeedEffect speedEffect = new SpeedEffect("BootsOfSpeed Effect", 750.0);
    
	public BootsOfSpeed() {
		super("Boots of Speed", ItemList.BOOTS_OF_SPEED.ID);
		this.setUsable(false);
        this.setIsDestroyedOnUse(true);
        this.setSprite(new Sprite(ItemTextures.boots_of_speed));
        this.setDescription("These boots bestow its wearer with unmatched swiftness");
        this.setBasePrice(100);
	}

	@Override
	public Item copy() {
		return new BootsOfSpeed();
	}

	@Override
	public int getInternalSaveID() { return ItemList.BOOTS_OF_SPEED.ID; }
	
	@Override
	public void onItemEquip(Entity user) {
	    user.activeEffectsTracker.addEffect(speedEffect);
	}
	
	@Override
	public void onItemUnequip(Entity user) {
	    user.activeEffectsTracker.removeEffect(speedEffect);
	}
	
}
