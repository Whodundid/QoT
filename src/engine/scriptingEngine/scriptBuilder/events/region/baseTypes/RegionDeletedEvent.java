package engine.scriptingEngine.scriptBuilder.events.region.baseTypes;

import engine.scriptingEngine.scriptBuilder.events.region.RegionEvent;
import world.GameWorld;
import world.Region;

public class RegionDeletedEvent extends RegionEvent {
	
	public RegionDeletedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}