package envision.game.scripts.scriptBuilder.events.region;

import envision.game.entity.Entity;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public abstract class EntityRegionEvent extends RegionEvent {
	
	private Entity entity;
	
	public EntityRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion);
		entity = theEntity;
	}
	
	public Entity getEntity() { return entity; }
	
}
