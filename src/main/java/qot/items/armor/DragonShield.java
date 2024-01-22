package qot.items.armor;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Item;
import qot.assets.textures.item.ItemTextures;
import qot.effects.DamageMitigationEffect;
import qot.items.ItemList;

public class DragonShield extends Item {

    public static final DamageMitigationEffect defenseEffect = new DamageMitigationEffect("Dragon Shield Defense", -0.20);
    
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
	    user.activeEffectsTracker.addEffect(defenseEffect);
	}
	
	@Override
	public void onItemUnequip(Entity user) {
	    user.activeEffectsTracker.removeEffect(defenseEffect);
	}
	
}
