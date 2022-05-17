package engine.scripting.builder.events.region.entityTypes;

import engine.scripting.builder.events.region.EntityRegionEvent;
import game.entities.Entity;
import world.GameWorld;
import world.Region;

public class EntitySpawnedInRegionEvent extends EntityRegionEvent {
	
	public EntitySpawnedInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
