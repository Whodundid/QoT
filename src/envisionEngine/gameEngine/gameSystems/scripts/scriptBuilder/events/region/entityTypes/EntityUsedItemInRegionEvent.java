package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameObjects.items.Item;
import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public class EntityUsedItemInRegionEvent extends EntityRegionEvent {
	
	Item item;
	
	public EntityUsedItemInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, Item theItem) {
		super(theWorld, theRegion, theEntity);
		item = theItem;
	}
	
	public Item getItem() { return item; }
	
}