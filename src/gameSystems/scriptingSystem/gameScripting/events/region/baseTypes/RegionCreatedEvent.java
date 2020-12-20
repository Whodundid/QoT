package gameSystems.scriptingSystem.gameScripting.events.region.baseTypes;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.gameScripting.events.region.RegionEvent;

public class RegionCreatedEvent extends RegionEvent {
	
	public RegionCreatedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}
