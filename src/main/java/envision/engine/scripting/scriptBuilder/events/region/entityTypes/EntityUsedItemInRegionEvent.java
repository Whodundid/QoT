package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.entities.Entity;
import envision.game.items.Item;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityUsedItemInRegionEvent extends EntityRegionEvent {
	
	Item item;
	
	public EntityUsedItemInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Item theItem) {
		super(theWorld, theRegion, theEntity);
		item = theItem;
	}
	
	public Item getItem() { return item; }
	
}