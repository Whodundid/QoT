package envision.game.scripts.scriptBuilder.events.region.baseTypes;

import envision.game.scripts.scriptBuilder.events.region.RegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public class RegionCreatedEvent extends RegionEvent {
	
	public RegionCreatedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}
