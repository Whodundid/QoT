package engine.scriptingEngine.scriptBuilder.events.region.entityTypes;

import engine.scriptingEngine.scriptBuilder.events.region.EntityRegionEvent;
import game.entities.Entity;
import world.GameWorld;
import world.Region;

public class EntityWasHurtInRegionEvent extends EntityRegionEvent {
	
	private int damage = 0;
	
	public EntityWasHurtInRegionEvent(GameWorld theWorld, Region theRegion, Entity theEntity, int damageIn) {
		super(theWorld, theRegion, theEntity);
		damage = damageIn;
	}
	
	public int getDamage() { return damage; }
	
}