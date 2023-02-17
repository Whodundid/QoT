package envision.engine.scripting.scriptBuilder.events.region;

import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public abstract class EntityRegionEvent extends RegionEvent {
	
	private Entity entity;
	
	public EntityRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion);
		entity = theEntity;
	}
	
	public Entity getEntity() { return entity; }
	
}
