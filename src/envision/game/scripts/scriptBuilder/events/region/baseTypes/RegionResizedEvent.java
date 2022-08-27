package envision.game.scripts.scriptBuilder.events.region.baseTypes;

import envision.game.scripts.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import eutil.math.EDimensionI;

public class RegionResizedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionResizedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}
