package envision.game.entities;

import envision.game.items.ArmorSlotType;
import envision.game.items.Item;
import eutil.datatypes.util.EList;

public class EntityArmor {
    
    //========
    // Fields
    //========
    
    private final Entity theEntity;
    
    public Item headSlot;
    public Item chestSlot;
    public Item necklaceSlot;
    
    //public Item leftHandSlot;
    //public Item rightHandSlot;
    //public Item leftRingSlot;
    //public Item rightRingSlot;
    public Item handSlot;
    public Item ringSlot;
    
    public Item beltSlot;
    public Item legSlot;
    public Item bootsSlot;
    
    //==============
    // Constructors
    //==============
    
    public EntityArmor(Entity entityIn) {
        theEntity = entityIn;
    }
    
    //=========
    // Methods
    //=========
    
    public Item equipItem(Item in) {
        if (in == null) return null;
        if (in.getArmorSlotType() == null || in.getArmorSlotType() == ArmorSlotType.NA) return null;
        
        final var type = in.getArmorSlotType();
        Item old = null;
        
        switch (type) {
        case HEAD: old = headSlot; headSlot = in; break;
        case NECK: old = necklaceSlot; necklaceSlot = in; break;
        case CHEST: old = chestSlot; chestSlot = in; break;
        case FEET: old = bootsSlot; bootsSlot = in; break;
        case HAND: old = handSlot; handSlot = in; break;
        case LEGS: old = legSlot; legSlot = in; break;
        case RING: old = ringSlot; ringSlot = in; break;
        default: break;
        }
        
        return old;
    }
    
    public Item equipHat(Item in) {
        if (!checkItemType(in, ArmorSlotType.HEAD)) return null;
        Item item = headSlot;
        headSlot = in;
        return item;
    }
    
    public Item equipNecklace(Item in) {
        if (!checkItemType(in, ArmorSlotType.NECK)) return null;
        Item item = necklaceSlot;
        necklaceSlot = in;
        return item;
    }
    
    public Item equipChestpiece(Item in) {
        if (!checkItemType(in, ArmorSlotType.CHEST)) return null;
        Item item = chestSlot;
        chestSlot = in;
        return item;
    }
    
    public Item equipGloves(Item in) {
        if (!checkItemType(in, ArmorSlotType.HAND)) return null;
        Item item = handSlot;
        handSlot = in;
        return item;
    }
    
    public Item equipRing(Item in) {
        if (!checkItemType(in, ArmorSlotType.RING)) return null;
        Item item = ringSlot;
        ringSlot = in;
        return item;
    }
    
    public Item equipLegs(Item in) {
        if (!checkItemType(in, ArmorSlotType.LEGS)) return null;
        Item item = legSlot;
        legSlot = in;
        return item;
    }
    
    public Item equipBoots(Item in) {
        if (!checkItemType(in, ArmorSlotType.FEET)) return null;
        Item item = bootsSlot;
        bootsSlot = in;
        return item;
    }
    
    public Item unequipItem(Item in) {
        if (in == null) return null;
        if (in.getArmorSlotType() == null || in.getArmorSlotType() == ArmorSlotType.NA) return null;
        if (!isItemEquiped(in)) return null;
        
        final var type = in.getArmorSlotType();
        
        switch (type) {
        case HEAD: headSlot = null; break;
        case NECK: necklaceSlot = null; break;
        case CHEST: chestSlot = null; break;
        case FEET: bootsSlot = null; break;
        case HAND: handSlot = null; break;
        case LEGS: legSlot = null; break;
        case RING: ringSlot = null; break;
        default: break;
        }
        
        return in;
    }
    
    public Item unequipHat() {
        Item item = headSlot;
        headSlot = null;
        return item;
    }
    
    public Item unequipNecklace() {
        Item item = necklaceSlot;
        necklaceSlot = null;
        return item;
    }
    
    public Item unequipChestpiece() {
        Item item = chestSlot;
        chestSlot = null;
        return item;
    }
    
    public Item unequipGloves() {
        Item item = handSlot;
        handSlot = null;
        return item;
    }
    
    public Item unequipRing() {
        Item item = ringSlot;
        ringSlot = null;
        return item;
    }
    
    public Item unequipLegs() {
        Item item = legSlot;
        legSlot = null;
        return item;
    }
    
    public Item unequipBoots() {
        Item item = bootsSlot;
        bootsSlot = null;
        return item;
    }
    
    public EList<Item> unequipAll() {
        EList<Item> items = EList.newList();
        items.addIfNotNull(unequipHat());
        items.addIfNotNull(unequipNecklace());
        items.addIfNotNull(unequipChestpiece());
        items.addIfNotNull(unequipGloves());
        items.addIfNotNull(unequipRing());
        items.addIfNotNull(unequipLegs());
        items.addIfNotNull(unequipBoots());
        return items;
    }
    
    public boolean isItemEquiped(Item in) {
        if (in == null) return false;
        if (in.getArmorSlotType() == null || in.getArmorSlotType() == ArmorSlotType.NA) return false;
        
        final var type = in.getArmorSlotType();
        Item item = null;
        
        switch (type) {
        case HEAD: item = headSlot; break;
        case NECK: item = necklaceSlot; break;
        case CHEST: item = chestSlot; break;
        case HAND: item = handSlot; break;
        case RING: item = ringSlot; break;
        case LEGS: item = legSlot; break;
        case FEET: item = bootsSlot; break;
        default: break;
        }
        
        return item != null && item == in;
    }
    
    //=========================
    // Internal Helper Methods
    //=========================
    
    protected boolean checkItemType(Item in, ArmorSlotType type) {
        if (in == null) return false;
        ArmorSlotType t = in.getArmorSlotType();
        if (t == null || t == ArmorSlotType.NA) return false;
        return t == type;
    }
    
}
