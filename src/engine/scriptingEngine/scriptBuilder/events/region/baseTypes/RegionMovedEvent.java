package engine.scriptingEngine.scriptBuilder.events.region.baseTypes;

import engine.scriptingEngine.scriptBuilder.events.region.RegionEvent;
import eutil.math.EDimensionI;
import world.GameWorld;
import world.Region;

public class RegionMovedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionMovedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}