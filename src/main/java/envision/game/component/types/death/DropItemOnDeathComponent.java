package envision.game.component.types.death;

import java.util.Collection;

import envision.game.component.ComponentBasedObject;
import envision.game.entities.Entity;
import envision.game.items.Item;
import envision.game.items.ItemOnGround;
import envision.game.world.IGameWorld;
import eutil.datatypes.util.EList;
import eutil.random.ERandomUtil;

public class DropItemOnDeathComponent extends OnDeathComponent {

    private final EList<Item> itemsToDrop = EList.newList();
    
    private boolean random = false;
    private boolean hasChance = false;
    private int chance;
    
    //==============
    // Constructors
    //==============
    
    protected DropItemOnDeathComponent(ComponentBasedObject theEntityWhoWillDie) {
        super(theEntityWhoWillDie);
    }
    
    //===========
    // Overrides
    //===========
    
    @Override
    public void onDeath(Entity killingEntity) {
        super.onDeath(killingEntity);
        
        boolean roll = ERandomUtil.roll(1, 1, chance);
        if (hasChance && !roll) return;
        
        EList<Item> toDrop = EList.newList();
        
        if (random) toDrop.add(itemsToDrop.getRandom());
        else toDrop.addAll(itemsToDrop);
        
        if (theObject == null) return;
        if (theObject.world == null) return;
        
        IGameWorld world = theObject.world;
        for (Item item : toDrop) {
            var dims = theObject.getCollisionDims();
            int spawnX = (int) dims.midX / world.getTileWidth();
            int spawnY = (int) dims.midY / world.getTileHeight();
            world.dropItemOnGround(item, spawnX, spawnY);
        }
    }
    
    /**
     * The chance of (1 in X chances).
     * 
     * if input is 10, then it's a 1 in 10 chance.
     * if input is 1, then it's a 100% chance.
     * 
     * @param oneInXChances 
     */
    public DropItemOnDeathComponent setChance(int oneInXChances) {
        hasChance = true;
        chance = oneInXChances;
        return this;
    }
    
    //=================
    // Static Builders
    //=================
    
    public static DropItemOnDeathComponent setItem(ComponentBasedObject entity, Item item) {
        var comp = new DropItemOnDeathComponent(entity);
        comp.itemsToDrop.add(item);
        return comp;
    }
    
    public static DropItemOnDeathComponent setItems(ComponentBasedObject entity, Item... items) {
        return setItems(entity, EList.of(items));
    }
    
    public static DropItemOnDeathComponent setItems(ComponentBasedObject entity, Collection<Item> items) {
        var comp = new DropItemOnDeathComponent(entity);
        comp.itemsToDrop.addAll(items);
        return comp;
    }
    
    public static DropItemOnDeathComponent randomItem(ComponentBasedObject entity, Item... items) {
        return randomItem(entity, EList.of(items));
    }
    
    public static DropItemOnDeathComponent randomItem(ComponentBasedObject entity, Collection<Item> items) {
        var comp = new DropItemOnDeathComponent(entity);
        comp.random = true;
        comp.itemsToDrop.addAll(items);
        return comp;
    }
    
}
