package engine.scriptingEngine.scriptBuilder.events.region;

import engine.scriptingEngine.scriptBuilder.ScriptEvent;
import world.GameWorld;
import world.Region;

public abstract class RegionEvent extends ScriptEvent {
	
	private Region region;
	
	protected RegionEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld);
		region = theRegion;
	}
	
	public Region getRegion() { return region; }
	
}
