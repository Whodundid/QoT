package scripting.builder.events.region.baseTypes;

import eutil.storage.EDimsI;
import scripting.builder.events.region.RegionEvent;
import world.GameWorld;
import world.Region;

public class RegionResizedEvent extends RegionEvent {
	
	private EDimsI old;
	
	public RegionResizedEvent(GameWorld theWorld, Region theRegion, EDimsI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimsI getOldDims() { return old; }
	
}
