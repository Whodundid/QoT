package envision.game.scripts.scriptBuilder.events.region.baseTypes;

import envision.game.scripts.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class RegionDeletedEvent extends RegionEvent {
	
	public RegionDeletedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}