package scripting.builder.events.region.entityTypes;

import assets.entities.Entity;
import scripting.builder.events.region.EntityRegionEvent;
import world.GameWorld;
import world.Region;

public class EntityDiedInRegionEvent extends EntityRegionEvent {
	
	public EntityDiedInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
