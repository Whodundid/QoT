package envision.engine.loader.built.game;

import envision.engine.loader.dtos.IEngineResource;
import envision.engine.loader.dtos.game.ItemDTO;
import envision.game.GameObject;
import envision.game.items.ArmorSlotType;

public abstract class Item extends GameObject implements IEngineResource {
    
    //========
    // Fields
    //========
    
    private int id;
    protected String name;
    protected int basePrice;
    protected String description;
    protected double weight;
    protected boolean isUsable;
    protected boolean isDestroyedOnUse;
    protected int damageBonus;
    protected ArmorSlotType armorSlotType = ArmorSlotType.NA;
    
    //==============
    // Constructors
    //==============
    
    protected Item(String nameIn, int idIn) { this(nameIn, 0, "", idIn); }
    protected Item(String nameIn, int basePriceIn, int idIn) { this(nameIn, basePriceIn, "", idIn); }
    protected Item(String nameIn, int basePriceIn, String descriptionIn, int idIn) {
        name = nameIn;
        basePrice = basePriceIn;
        description = descriptionIn;
        id = idIn;
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public ItemDTO toDto() {
        return null;
    }
    
    //=========
    // Methods
    //=========
    
    public Item createNew() {
        return copy();
    }
    
    public abstract Item copy();
    
    /** Override in child classes to define behavior for items which have a use interaction. */
    public void onItemUse(Entity user) {}
    /** Override in child classes to define behavior for items which have an equip interaction. */
    public void onItemEquip(Entity user) {}
    /** Override in child classes to define behavior for items which have an unequip interaction. */
    public void onItemUnequip(Entity user) {}
    
    //=========
    // Getters
    //=========
    
    public String getName() { return name; }
    public int getBasePrice() { return basePrice; }
    public String getDescription() { return description; }
    public int getID() { return id; }
    public double getCarryWeight() { return weight; }
    public boolean isUsable() { return isUsable; }
    public boolean isDestroyedOnUse() { return isDestroyedOnUse; }
    public int getDamageBonus() { return damageBonus; }
    public ArmorSlotType getArmorSlotType() { return armorSlotType; }
    
    //=========
    // Setters
    //=========
    
    public Item setDescription(String in) { description = in; return this; }
    public Item setBasePrice(int in) { basePrice = in; return this; }
    public void setCarryWeight(double weightIn) { weight = weightIn; }
    public void setUsable(boolean val) { isUsable = val; }
    public void setIsDestroyedOnUse(boolean val) { isDestroyedOnUse = val; }
    public void setDamageBonus(int val) { damageBonus = val; }
    public void setArmorSlotType(ArmorSlotType typeIn) { armorSlotType = typeIn; }
    
}
