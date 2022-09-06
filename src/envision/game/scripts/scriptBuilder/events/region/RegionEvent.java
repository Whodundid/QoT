package envision.game.scripts.scriptBuilder.events.region;

import envision.game.scripts.scriptBuilder.ScriptEvent;
import envision.game.world.gameWorld.GameWorld;
import envision.game.world.util.Region;

public abstract class RegionEvent extends ScriptEvent {
	
	private Region region;
	
	protected RegionEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld);
		region = theRegion;
	}
	
	public Region getRegion() { return region; }
	
}
