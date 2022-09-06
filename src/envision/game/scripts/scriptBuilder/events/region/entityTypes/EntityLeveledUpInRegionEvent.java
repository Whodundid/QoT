package envision.game.scripts.scriptBuilder.events.region.entityTypes;

import envision.game.entity.Entity;
import envision.game.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

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
