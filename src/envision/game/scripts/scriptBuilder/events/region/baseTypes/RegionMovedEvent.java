package envision.game.scripts.scriptBuilder.events.region.baseTypes;

import envision.game.scripts.scriptBuilder.events.region.RegionEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;
import eutil.math.EDimensionI;

public class RegionMovedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionMovedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}