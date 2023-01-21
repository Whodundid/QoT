package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.baseTypes;

import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.RegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;

public class RegionDeletedEvent extends RegionEvent {
	
	public RegionDeletedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}