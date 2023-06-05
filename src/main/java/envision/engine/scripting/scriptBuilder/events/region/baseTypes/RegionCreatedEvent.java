package envision.engine.scripting.scriptBuilder.events.region.baseTypes;

import envision.engine.scripting.scriptBuilder.events.region.RegionEvent;
import envision.game.world.GameWorld;
import envision.game.world.Region;

public class RegionCreatedEvent extends RegionEvent {
	
	public RegionCreatedEvent(GameWorld theWorld, Region theRegion) {
		super(theWorld, theRegion);
	}
	
}
