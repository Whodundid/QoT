package engine.scriptingEngine.scriptBuilder.events.region.entityTypes;

import engine.scriptingEngine.scriptBuilder.events.region.EntityRegionEvent;
import game.entities.Entity;
import world.GameWorld;
import world.Region;

public class EntityEnteredRegionEvent extends EntityRegionEvent {
	
	public EntityEnteredRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion, theEntity);
	}
	
}
