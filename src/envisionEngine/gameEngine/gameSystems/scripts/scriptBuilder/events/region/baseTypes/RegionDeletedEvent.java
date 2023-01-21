package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.baseTypes;

import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.RegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public class RegionDeletedEvent extends RegionEvent {
	
	public RegionDeletedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}