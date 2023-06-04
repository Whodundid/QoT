package envision.engine.scripting.scriptBuilder.events.region.baseTypes;

import envision.engine.scripting.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;
import eutil.math.dimensions.Dimension_i;

public class RegionResizedEvent extends RegionEvent {
	
	private Dimension_i old;
	
	public RegionResizedEvent(GameWorld theWorld, Region theRegion, Dimension_i oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public Dimension_i getOldDims() { return old; }
	
}
