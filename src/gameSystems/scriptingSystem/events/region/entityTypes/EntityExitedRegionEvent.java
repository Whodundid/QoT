package gameSystems.scriptingSystem.events.region.entityTypes;

import assets.entities.Entity;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.events.region.EntityRegionEvent;

public class EntityExitedRegionEvent extends EntityRegionEvent {
	
	public EntityExitedRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
