package gameSystems.scriptingSystem.gameScripting.events.region;

import assets.entities.Entity;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;

public abstract class EntityRegionEvent extends RegionEvent {
	
	private Entity entity;
	
	public EntityRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity) {
		super(theWorld, theRegion);
		entity = theEntity;
	}
	
	public Entity getEntity() { return entity; }
	
}
