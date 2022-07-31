package engine.scriptingEngine.scriptBuilder.events.region.entityTypes;

import engine.scriptingEngine.scriptBuilder.events.region.EntityRegionEvent;
import game.entities.Entity;
import game.items.Item;
import world.GameWorld;
import world.Region;

public class EntityUsedItemInRegionEvent extends EntityRegionEvent {
	
	Item item;
	
	public EntityUsedItemInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Item theItem) {
		super(theWorld, theRegion, theEntity);
		item = theItem;
	}
	
	public Item getItem() { return item; }
	
}