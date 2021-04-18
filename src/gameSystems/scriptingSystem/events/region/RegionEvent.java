package gameSystems.scriptingSystem.events.region;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.ScriptEvent;

public abstract class RegionEvent extends ScriptEvent {
	
	private Region region;
	
	protected RegionEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld);
		region = theRegion;
	}
	
	public Region getRegion() { return region; }
	
}
