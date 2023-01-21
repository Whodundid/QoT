package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region;

import envisionEngine.gameEngine.gameObjects.entity.Entity;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public abstract class EntityRegionEvent extends RegionEvent {
	
	private Entity entity;
	
	public EntityRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion);
		entity = theEntity;
	}
	
	public Entity getEntity() { return entity; }
	
}
