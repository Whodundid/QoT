package scripting.builder.events.region.baseTypes;

import scripting.builder.events.region.RegionEvent;
import world.GameWorld;
import world.Region;

public class RegionDeletedEvent extends RegionEvent {
	
	public RegionDeletedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}