package engine.scripting.builder.events.region.entityTypes;

import engine.scripting.builder.events.region.EntityRegionEvent;
import game.entities.Entity;
import world.GameWorld;
import world.Region;

public class EntityLeveledUpInRegionEvent extends EntityRegionEvent {
	
	int oldLevel = -1;
	int newLevel = -1;
	
	public EntityLeveledUpInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, int oldLevelIn, int newLevelIn) {
		super(theWorld, theRegion, theEntity);
		oldLevel = oldLevelIn;
		newLevel = newLevelIn;
	}
	
	public int getOldLevel() { return oldLevel; }
	public int getNewLevel() { return newLevel; }
	
}
