package envision.engine.scripting.scriptBuilder.events.region.baseTypes;

import envision.engine.scripting.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import eutil.math.dimensions.Dimension_l;

public class RegionMovedEvent extends RegionEvent {
	
	private Dimension_l old;
	
	public RegionMovedEvent(GameWorld theWorld, Region theRegion, Dimension_l oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public Dimension_l getOldDims() { return old; }
	
}