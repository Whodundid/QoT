package envision.game.scripts.scriptBuilder.events.region.entityTypes;

import envision.game.entity.Entity;
import envision.game.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public class EntityEnteredRegionEvent extends EntityRegionEvent {
	
	public EntityEnteredRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
