package gameSystems.scriptingSystem.events.region.baseTypes;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.events.region.RegionEvent;
import storageUtil.EDimensionI;

public class RegionMovedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionMovedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}