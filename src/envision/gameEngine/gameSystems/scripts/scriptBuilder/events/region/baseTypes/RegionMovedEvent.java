package envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.baseTypes;

import envision.gameEngine.gameSystems.scripts.scriptBuilder.events.region.RegionEvent;
import envision.gameEngine.world.gameWorld.GameWorld;
import envision.gameEngine.world.worldUtil.Region;
import eutil.math.EDimensionI;

public class RegionMovedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionMovedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}