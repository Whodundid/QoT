package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public class EntityEnteredRegionEvent extends EntityRegionEvent {
	
	public EntityEnteredRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
