package gameSystems.scriptingSystem.events.region.entityTypes;

import assets.entities.Entity;
import gameSystems.mapSystem.GameWorld;
import gameSystems.mapSystem.Region;
import gameSystems.scriptingSystem.events.region.EntityRegionEvent;

public class EntityWasHurtInRegionEvent extends EntityRegionEvent {
	
	private int damage = 0;
	
	public EntityWasHurtInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, int damageIn) {
		super(theWorld, theRegion, theEntity);
		damage = damageIn;
	}
	
	public int getDamage() { return damage; }
	
}