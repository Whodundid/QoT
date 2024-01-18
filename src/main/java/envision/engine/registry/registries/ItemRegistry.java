package envision.engine.registry.registries;

import envision.game.items.Item;
import eutil.datatypes.util.EList;

public class ItemRegistry {
    
    private final EList<Item> items = EList.newList();
    
    public EList<Item> getRegisteredItems() { return items; }
    
}
