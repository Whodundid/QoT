package engine.scriptingEngine.scriptBuilder.events.region.baseTypes;

import engine.scriptingEngine.scriptBuilder.events.region.RegionEvent;
import world.GameWorld;
import world.Region;

public class RegionCreatedEvent extends RegionEvent {
	
	public RegionCreatedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}
