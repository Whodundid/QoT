package gameSystems.scriptingSystem.gameScripting.events.region;

import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.gameScripting.ScriptEvent;

public abstract class RegionEvent extends ScriptEvent {
	
	private Region region;
	
	protected RegionEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld);
		region = theRegion;
	}
	
	public Region getRegion() { return region; }
	
}
