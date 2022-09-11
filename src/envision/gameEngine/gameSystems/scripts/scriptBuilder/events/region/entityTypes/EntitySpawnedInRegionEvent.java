package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;

public class EntitySpawnedInRegionEvent extends EntityRegionEvent {
	
	public EntitySpawnedInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
