package gameSystems.scriptingSystem.events.region.entityTypes;

import assets.entities.Entity;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.events.region.EntityRegionEvent;

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
