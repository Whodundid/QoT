package gameSystems.scriptingSystem.gameScripting.events.region.entityTypes;

import assets.entities.Entity;
import assets.items.Item;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.gameScripting.events.region.EntityRegionEvent;

public class EntityUsedItemInRegionEvent extends EntityRegionEvent {
	
	Item item;
	
	public EntityUsedItemInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Item theItem) {
		super(theWorld, theRegion, theEntity);
		item = theItem;
	}
	
	public Item getItem() { return item; }
	
}