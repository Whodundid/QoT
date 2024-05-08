package qot.items.armor;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Item;
import eutil.random.ERandomUtil;
import qot.assets.textures.item.ItemTextures;
import qot.effects.DamageMitigationEffect;
import qot.items.ItemList;

public class DragonShield extends Item {

    public static final DamageMitigationEffect defenseEffect = new DamageMitigationEffect("Dragon Shield Defense") {
        @Override
        public boolean isProcessable() {
            return true;
        }
        
        @Override
        public Integer processEvent(Object... arguments) {
            if (!checkArgs(arguments)) return null;
            
            int incomingDamage = getInt(arguments);
            int outDamage = 0;
            
            if (ERandomUtil.roll(0, 0, 5)) {
                outDamage = -(incomingDamage / 2);
            }
            
            return outDamage;
        };
    };
    
	public DragonShield() {
		super("Dragon Shield", ItemList.DRAGON_SHIELD.ID);
		this.setUsable(false);
        this.setSprite(new Sprite(ItemTextures.dragon_shield));
        this.setDescription("Bestows its weilder with a 20% chance to take 50% less incoming damage when attacked");
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
