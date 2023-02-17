package envision.engine.scripting.scriptBuilder.events.region.entityTypes;

import envision.engine.scripting.scriptBuilder.events.region.EntityRegionEvent;
import envision.game.objects.entities.Entity;
import envision.game.world.GameWorld;
import envision.game.world.Region;

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
