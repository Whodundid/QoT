package scripting.builder.events.region.baseTypes;

import scripting.builder.events.region.RegionEvent;
import storageUtil.EDimensionI;
import world.GameWorld;
import world.Region;

public class RegionResizedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionResizedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}
