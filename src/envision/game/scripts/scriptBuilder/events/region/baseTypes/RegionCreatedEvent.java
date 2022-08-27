package envision.game.scripts.scriptBuilder.events.region.baseTypes;

import envision.game.scripts.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class RegionCreatedEvent extends RegionEvent {
	
	public RegionCreatedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}
