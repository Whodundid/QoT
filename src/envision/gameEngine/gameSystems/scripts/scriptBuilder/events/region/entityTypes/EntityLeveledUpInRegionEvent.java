package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.entityTypes;

import envision.gameEngine.gameObjects.entity.Entity;
import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.EntityRegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;

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
