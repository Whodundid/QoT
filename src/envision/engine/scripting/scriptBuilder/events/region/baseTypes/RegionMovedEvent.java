package envision.engine.scripting.scriptBuilder.events.region.baseTypes;

import envision.engine.scripting.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import eutil.math.dimensions.EDimensionI;

public class RegionMovedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionMovedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}