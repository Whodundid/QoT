package envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.events.region;

import envisionEngine.gameEngine.gameSystems.scripts.scriptBuilder.ScriptEvent;
import envisionEngine.gameEngine.world.gameWorld.GameWorld;
import envisionEngine.gameEngine.world.worldUtil.Region;

public abstract class RegionEvent extends ScriptEvent {
	
	private Region region;
	
	protected RegionEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld);
		region = theRegion;
	}
	
	public Region getRegion() { return region; }
	
}
