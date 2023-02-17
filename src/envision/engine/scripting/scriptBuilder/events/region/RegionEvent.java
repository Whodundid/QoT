package envision.engine.scripting.scriptBuilder.events.region;

import envision.engine.scripting.scriptBuilder.ScriptEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public abstract class RegionEvent extends ScriptEvent {
	
	private Region region;
	
	protected RegionEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld);
		region = theRegion;
	}
	
	public Region getRegion() { return region; }
	
}
