package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityExitedRegionEvent extends EntityRegionEvent {
	
	public EntityExitedRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
