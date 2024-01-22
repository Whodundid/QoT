package qot.items.weapons.magic;

import envision.engine.registry.types.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Item;
import envision.game.items.Weapon;
import qot.assets.textures.item.ItemTextures;
import qot.effects.MagicDamageModifierEffect;
import qot.items.ItemList;

public class WoodenStick extends Weapon {
    
    public static final MagicDamageModifierEffect magicStickEffect = new MagicDamageModifierEffect("Magic Stick Bonus", 0.2);
    
    public WoodenStick() {
        super("Wooden Stick", ItemList.WOODEN_STICK.ID);
        this.setUsable(false);
        this.setSprite(new Sprite(ItemTextures.wooden_stick));
        this.setDescription("A slightly magical stick");
        this.setBasePrice(50);
    }

    @Override
    public Item copy() {
        return new WoodenStick();
    }

    @Override
    public int getInternalSaveID() { return ItemList.WOODEN_STICK.ID; }
    
    @Override
    public void onItemEquip(Entity user) {
        user.activeEffectsTracker.addEffect(magicStickEffect);
    }
    
    @Override
    public void onItemUnequip(Entity user) {
       user.activeEffectsTracker.removeEffect(magicStickEffect);
    }
    
}
