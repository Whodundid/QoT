package envision.game.scripts.scriptBuilder.events.region.entityTypes;

import envision.game.entity.Entity;
import envision.game.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityDiedInRegionEvent extends EntityRegionEvent {
	
	public EntityDiedInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
