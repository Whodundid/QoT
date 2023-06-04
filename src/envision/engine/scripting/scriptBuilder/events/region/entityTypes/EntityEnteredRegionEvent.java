package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.entities.Entity;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class EntityEnteredRegionEvent extends EntityRegionEvent {
	
	public EntityEnteredRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
