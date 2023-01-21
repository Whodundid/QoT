package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameObjects.items.Item;
import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;

public class EntityUsedItemInRegionEvent extends EntityRegionEvent {
	
	Item item;
	
	public EntityUsedItemInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Item theItem) {
		super(theWorld, theRegion, theEntity);
		item = theItem;
	}
	
	public Item getItem() { return item; }
	
}