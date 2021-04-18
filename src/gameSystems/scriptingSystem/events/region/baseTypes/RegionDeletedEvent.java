package gameSystems.scriptingSystem.events.region.baseTypes;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.events.region.RegionEvent;

public class RegionDeletedEvent extends RegionEvent {
	
	public RegionDeletedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}