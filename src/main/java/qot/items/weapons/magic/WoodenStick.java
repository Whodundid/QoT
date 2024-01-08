package qot.items.weapons.magic;

import envision.engine.resourceLoaders.Sprite;
import envision.game.entities.Entity;
import envision.game.items.Item;
import envision.game.items.Weapon;
import qot.assets.textures.item.ItemTextures;
import qot.items.ItemList;

public class WoodenStick extends Weapon {
    
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
        if (!user.activeEffectsTracker.containsKey("MAGIC_MODIFIER")) {
            user.activeEffectsTracker.put("MAGIC_MODIFIER", 2.0);
        }
    }
    
    @Override
    public void onItemUnequip(Entity user) {
        if (!user.getInventory().containsItemType(this)) {
            user.activeEffectsTracker.remove("MAGIC_MODIFIER");
        }
    }
    
}
