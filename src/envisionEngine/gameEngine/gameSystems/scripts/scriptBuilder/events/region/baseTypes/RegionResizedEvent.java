package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.baseTypes;

import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region.RegionEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;
import eutil.math.EDimensionI;

public class RegionResizedEvent extends RegionEvent {
	
	private EDimensionI old;
	
	public RegionResizedEvent(GameWorld theWorld, Region theRegion, EDimensionI oldDims) {
		super(theWorld, theRegion);
		old = oldDims;
	}
	
	public EDimensionI getOldDims() { return old; }
	
}
