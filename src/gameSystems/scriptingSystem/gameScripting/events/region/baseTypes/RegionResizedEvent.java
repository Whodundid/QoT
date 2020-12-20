package gameSystems.scriptingSystem.gameScripting.events.region.baseTypes;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.gameScripting.events.region.RegionEvent;
import util.storageUtil.EDimensionI;

public class RegionResizedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionResizedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}
