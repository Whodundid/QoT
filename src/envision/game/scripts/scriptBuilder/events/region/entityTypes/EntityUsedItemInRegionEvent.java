package envision.game.scripts.scriptBuilder.events.region.entityTypes;

import envision.game.entity.Entity;
import envision.game.items.Item;
import envision.game.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public class EntityUsedItemInRegionEvent extends EntityRegionEvent {
	
	Item item;
	
	public EntityUsedItemInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Item theItem) {
		super(theWorld, theRegion, theEntity);
		item = theItem;
	}
	
	public Item getItem() { return item; }
	
}