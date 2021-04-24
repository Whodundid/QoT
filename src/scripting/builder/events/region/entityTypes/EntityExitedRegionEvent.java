package scripting.builder.events.region.entityTypes;

import assets.entities.Entity;
import scripting.builder.events.region.EntityRegionEvent;
import world.GameWorld;
import world.Region;

public class EntityExitedRegionEvent extends EntityRegionEvent {
	
	public EntityExitedRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
