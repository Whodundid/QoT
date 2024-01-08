package envision.engine.registry.types;

import envision.game.items.Item;
import eutil.datatypes.util.EList;

public class ItemRegistry {
    
    private final EList<Item> items = EList.newList();
    
    public EList<Item> getRegisteredItems() { return items; }
    
}
